package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.killAll;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
    private List<MissionInfo> missions = new LinkedList<MissionInfo>();
    private MessageBroker m = MessageBrokerImpl.getInstance();
    private long tick;

    public Intelligence(List<MissionInfo> missions) {
        super("Intelligence");
        this.missions = missions;
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
            this.tick = b.getTick();
            if (!missions.isEmpty()) {

                while (!missions.isEmpty() && missions.get(0).getTimeIssued() == tick) {
                    MissionInfo mr = missions.get(0);
                    int time = mr.getTimeIssued();
                    if (tick == time) {
                        Future<Boolean> fut = m.sendEvent(new MissionReceivedEvent(missions.get(0)));
                        missions.remove(0);
                    }

                }
            }
        });
        this.subscribeBroadcast(killAll.class, (killAll b) ->
        {
            this.terminate();
        });

    }
}
