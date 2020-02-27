package bgu.spl.mics.application.messages;


import bgu.spl.mics.Event;
import bgu.spl.mics.Pair;

public class GadgetAvailableEvent implements Event<Pair<Boolean,Long>> {
    private String gadgetName;
    public GadgetAvailableEvent(String name ){           // get name from bgu.spl.mics.json
        gadgetName=name;
    }

    public String getName(){
        return gadgetName;
    }

}
