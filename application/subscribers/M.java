package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
    private MessageBroker m = MessageBrokerImpl.getInstance();
    private Diary diary;
    private long Qtime;
    private int id;
    private int MoneypennyID;
    private long tick;
    private Map<Message, Future> mapOfFuture;
    private List<String> agentNames;


    public M(int id) {
        super("M" + id);
        this.id = id;
        diary = Diary.getInstance();
    }

    @Override
    protected void initialize() {
        this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
            this.tick = b.getTick();
        });
        this.subscribeBroadcast(killAll.class, (killAll b) -> {
            this.terminate();
        });
        this.subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEvent e) ->
        {
            try {
                diary.incrementTotal();
                Future<Pair<Boolean, Integer>> fut = m.sendEvent(new AgentsAvailableEvent((e.getAgentsSerialNumbers())));
                Pair<Boolean, Integer> pairAgentsAvailableEvent = fut.get(100 * (e.getTimeExpired() - tick), MILLISECONDS);
                if (pairAgentsAvailableEvent != null) {
                    MoneypennyID = (int) pairAgentsAvailableEvent.getSecond();
                    if ((boolean) (pairAgentsAvailableEvent.getFirst())) {
                        Future<Pair<Boolean, Long>> fut2 = m.sendEvent(new GadgetAvailableEvent(e.getGadgetName()));
                        Pair<Boolean, Long> pairGadgetAvailableEvent = fut2.get(100 * (e.getTimeExpired() - tick), MILLISECONDS);
                        if (pairGadgetAvailableEvent != null) {
                            if ((boolean) pairGadgetAvailableEvent.getFirst()) {
                                Qtime = (long) pairGadgetAvailableEvent.getSecond();
                                if (e.getTimeExpired() >= tick) {
                                    Future<Pair<List<String>, Integer>> fut3 = m.sendEvent(new AgentsSendEvent(e.getAgentsSerialNumbers(), e.getDuration() * 100));
                                    Pair<List<String>, Integer> pairAgentsSendEvent = fut3.get();
                                    if (pairAgentsSendEvent != null) {
                                        this.agentNames = (List<String>) fut3.get().getFirst();
                                        Report report = createReport(e);
                                        diary.addReport(report);
                                    }
                                }
                            }
                        }
                    } else {
                        Future<Boolean> fut4 = m.sendEvent(new AgentsReleaseEvent(e.getAgentsSerialNumbers()));
                        Boolean sendEvent = fut4.get(100 * (e.getTimeExpired() - tick), MILLISECONDS);
                    }
                }
            } catch (InterruptedException w) {
                w.printStackTrace();
            }
        });
    }


    public Report createReport(MissionReceivedEvent e) {
        Report r = new Report();
        r.setMissionName(e.getMissionName());
        r.setM(id);
        r.setMoneypenny(MoneypennyID);
        r.setAgentsSerialNumbersNumber(e.getAgentsSerialNumbers());
        r.setAgentsNames(agentNames);
        r.setGadgetName(e.getGadgetName());
        r.setTimeIssued(e.getTimeIssued());
        r.setTimeCreated((int) tick);
        r.setQTime((int) Qtime);
        return r;
    }
}




