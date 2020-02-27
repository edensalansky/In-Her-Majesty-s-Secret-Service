package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class AgentsReleaseEvent implements Event<Boolean> {
    private List<String> agents;

    public AgentsReleaseEvent(List<String> serials){
        this.agents = serials;
    }

    public List<String> getAgents(){
        return agents;
    }
}
