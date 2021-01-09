package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyEvent;
import bgu.spl.mics.application.messages.TerminateBroadcats;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice You can add private fields and public methods to this
 * class. You MAY change constructor signatures and even add new public
 * constructors.
 */
public class LandoMicroservice extends MicroService {
    private long duration;

    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(BombDestroyEvent.class, c -> {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendBroadcast(new TerminateBroadcats());
        });
        subscribeBroadcast(TerminateBroadcats.class, c -> {
            Diary.getInstance().setLandoTerminate(System.currentTimeMillis());
            terminate();
        });

    }
}
