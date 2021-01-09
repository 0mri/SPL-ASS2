package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

public class DummyEvent implements Event<Integer> {
    private int num;

    public DummyEvent(int n) {
        this.num = n;
    }
}
