package bgu.spl.mics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus
 * interface. Write your implementation here! Only private fields and methods
 * can be added to this class.
 * 
 */
public class MessageBusImpl implements MessageBus {
	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> mcs_msg_map;
	private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedDeque<MicroService>> msg_hndlr;
	private ConcurrentHashMap<Event<?>, Future> future_map;

	// private ConcurrentHashMap<>
	private static class MessageBusHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl() {
		mcs_msg_map = new ConcurrentHashMap<>();
		msg_hndlr = new ConcurrentHashMap<>();
		future_map = new ConcurrentHashMap<>();
	}

	public static MessageBusImpl getInstance() {
		return MessageBusHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		msg_hndlr.putIfAbsent(type, new ConcurrentLinkedDeque<MicroService>());
		msg_hndlr.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		msg_hndlr.putIfAbsent(type, new ConcurrentLinkedDeque<MicroService>());
		msg_hndlr.get(type).add(m);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		Future<T> f = future_map.get(e);
		f.resolve(result);
		future_map.remove(e);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		for (MicroService m : msg_hndlr.get(b.getClass())) {
			mcs_msg_map.get(m).add(b);
		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		MicroService mcs = msg_hndlr.get(e.getClass()).poll();
		mcs_msg_map.get(mcs).add(e);
		Future<T> ftr = new Future<>();
		msg_hndlr.get(e.getClass()).add(mcs);
		future_map.putIfAbsent(e, ftr);
		return ftr;
	}

	@Override
	public void register(MicroService m) {
		this.mcs_msg_map.putIfAbsent(m, new LinkedBlockingQueue<Message>());
	}

	@Override
	public void unregister(MicroService m) {
		this.mcs_msg_map.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		return mcs_msg_map.get(m).take();
	}
}
