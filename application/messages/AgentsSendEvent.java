package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Pair;

import java.util.List;

public class AgentsSendEvent implements Event<Pair<List<String>,Integer>> {
    private List<String> serials;
    private int missionTime;

    public AgentsSendEvent(List<String> agents, int time) {
        serials = agents;
        missionTime =time;
    }



    public List<String>  getAgents(){
        return serials;
    }

    public int getMissionTime(){
        return missionTime;
    }

}