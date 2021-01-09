package bgu.spl.mics;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bgu.spl.mics.application.messages.BombDestroyEvent;
import bgu.spl.mics.application.messages.DummyBroadcast;
import bgu.spl.mics.application.messages.DummyEvent;
import bgu.spl.mics.application.services.DummyMicroService;

public class MessageBusImplTest {
    private MessageBus msg_bus;

    @BeforeEach
    public void setUp() {
        msg_bus = MessageBusImpl.getInstance();
    }

    @Test
    public void testSendBroadcast() {
        int n = 5;
        Broadcast bc = new DummyBroadcast(n);
        MicroService m1 = new DummyMicroService();
        MicroService m2 = new DummyMicroService();
        msg_bus.register(m1);
        msg_bus.register(m2);
        m2.subscribeBroadcast(bc.getClass(), null);
        m1.sendBroadcast(bc);
        Broadcast bc2 = null;
        try {
            bc2 = (Broadcast) msg_bus.awaitMessage(m2);
        } catch (InterruptedException e) {
        }
        assertTrue(bc2.equals(bc));
    }

    @Test
    public void testSendEvent() {
        int n = 5;
        Event<Integer> e1 = new DummyEvent(n);

        MicroService m1 = new DummyMicroService();
        MicroService m2 = new DummyMicroService();
        msg_bus.register(m1);
        msg_bus.register(m2);
        m2.subscribeEvent(DummyEvent.class, null);

        Future<Integer> fut1 = m1.sendEvent(e1);
        fut1.resolve(n);
        Event<Integer> e2 = null;
        try {
            e2 = (DummyEvent) msg_bus.awaitMessage(m2);
        } catch (InterruptedException e) {
        }
        assertTrue(e1.equals(e2));
    }

    public void testAwaitMessage() {
        int n = 5;
        Event<Integer> e1 = new DummyEvent(n);
        MicroService m = new DummyMicroService();
        msg_bus.register(m);
        m.sendEvent(e1);
        Event<Integer> e2 = null;
        try {
            e2 = (DummyEvent) msg_bus.awaitMessage(m);
        } catch (InterruptedException e) {
        }
        assertTrue(e1.equals(e2));
    }

    @Test
    public void testComplete() throws InterruptedException {
        MicroService m = new DummyMicroService();
        msg_bus.register(m);
        BombDestroyEvent e = new BombDestroyEvent();
        m.subscribeEvent(BombDestroyEvent.class, null);
        Future<Boolean> fut = m.sendEvent(e);
        msg_bus.complete(e, true);
        assertTrue(fut.get());

    }
}
