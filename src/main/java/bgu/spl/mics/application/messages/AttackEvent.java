package bgu.spl.mics.application.messages;

import java.util.List;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

public class AttackEvent implements Event<Boolean> {
    private Attack attack;

    public AttackEvent(Attack attack) {
        this.attack = attack;
    }

    public Attack getAttack() {
        return this.attack;
    }

    public List<Integer> getSerials() {
        return this.attack.getSerials();
    }

    public int getDuration() {
        return this.attack.getDuration();
    }

}
