package bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.BombDestroyEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcats;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as
 * {@link AttackEvents}. This class may not hold references for objects which it
 * is not responsible for: {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class. You MAY change
 * constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private Attack[] attacks;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        this.attacks = attacks;
    }

    @Override
    protected void initialize() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        subscribeBroadcast(TerminateBroadcats.class, c -> {
            Diary.getInstance().setLeiaTerminate(System.currentTimeMillis());
            terminate();
        });

        List<Future<Boolean>> attacks_future = new ArrayList<>();
        for (Attack attack : this.attacks)
            attacks_future.add(sendEvent(new AttackEvent(attack)));

        for (Future<Boolean> atck_ftr : attacks_future)
            try {
                atck_ftr.get();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

        Future<Boolean> deactivate_future = sendEvent(new DeactivationEvent());
        try {
            deactivate_future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sendEvent(new BombDestroyEvent());
    }

}
