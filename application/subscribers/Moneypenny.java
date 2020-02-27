package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.LinkedList;
import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private Squad agents = Squad.getInstance();
    private long tick;
    private int id;
    private List<String> listOfAgents = new LinkedList<>();

    public Moneypenny(int id) {
        super("Moneypenny" + id);
        this.id = id;
    }

    @Override
    protected void initialize() {
        if (id % 2 != 0) {
            this.subscribeBroadcast(killAll.class, (killAll b) -> {
                agents.releaseAgents(listOfAgents);
                agents.getAgentsNames(null);
                this.terminate();
            });
            this.subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent e) -> {
                this.complete(e, new Pair(agents.getAgents(e.getAgents()), id));
                addTolistOfAgents(e.getAgents());
            });
            this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
                this.tick = b.getTick();
            });
        } else {
            this.subscribeBroadcast(killAll.class, (killAll b) -> {
                agents.releaseAgents(listOfAgents);
                agents.getAgentsNames(null);
                this.terminate();
            });
            this.subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
                this.tick = b.getTick();
            });
            this.subscribeEvent(AgentsSendEvent.class, (AgentsSendEvent e) -> {
                agents.sendAgents(e.getAgents(), e.getMissionTime());
                Pair<List<String>, Integer> pair = new Pair(agents.getAgentsNames(e.getAgents()), id);
                this.complete(e, pair);
            });
            this.subscribeEvent(AgentsReleaseEvent.class, (AgentsReleaseEvent e) -> {
                agents.releaseAgents(e.getAgents());
                this.complete(e, true);
            });
        }

    }

    private void addTolistOfAgents(List<String> agentsSerialNumber) {
        for (String s : agentsSerialNumber) {
            if (!listOfAgents.contains(s))
                listOfAgents.add(s);
        }


    }
}
