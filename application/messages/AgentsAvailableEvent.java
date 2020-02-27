package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Pair;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.List;

public class AgentsAvailableEvent implements Event< Pair<Boolean,Integer>> {
    private List<String> serials;

    public AgentsAvailableEvent(List<String> agents){
    serials=agents;}

    public List<String>  getAgents(){
        return serials;
    }

    public void setSerials(List<String> serials) {
        this.serials = serials;
    }
}
