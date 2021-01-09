package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class DummyBroadcast implements Broadcast {
    private int num;

    public DummyBroadcast(int n) {
        this.num = n;
    }
}
