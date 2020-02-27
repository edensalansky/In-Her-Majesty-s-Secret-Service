package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private long tick;

    public TickBroadcast(long tick){
        this.tick= tick;
    }

    public long getTick() {
        return tick;
    }
}