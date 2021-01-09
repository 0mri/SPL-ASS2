package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcats;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

/**
 * C3POMicroservices is in charge of the handling {@link AttackEvents}. This
 * class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class. You MAY change
 * constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {

    public C3POMicroservice() {
        super("C3PO");
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, att_e -> {
            Ewoks.getInstance().acquire(att_e.getSerials());
            try {
                Thread.sleep(att_e.getDuration());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Ewoks.getInstance().release(att_e.getSerials());
            Diary.getInstance().incrementTotalAttacks();
            Diary.getInstance().setC3POFinish(System.currentTimeMillis());
            complete(att_e, true);
        });
        subscribeBroadcast(TerminateBroadcats.class, c -> {
            Diary.getInstance().setC3POTerminate(System.currentTimeMillis());
            terminate();
        });
    }
}