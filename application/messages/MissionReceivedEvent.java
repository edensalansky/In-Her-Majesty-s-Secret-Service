package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.List;


public class MissionReceivedEvent implements Event<Boolean> {
    private String missionName;
    List<String> AgentsSerialNumbers;
    String GadgetName;
    int TimeIssued;
    int duration;
    int TimeExpired;

    public MissionReceivedEvent(MissionInfo mission){
        this.missionName=mission.getMissionName();
        this.AgentsSerialNumbers=mission.getSerialAgentsNumbers();
        this.duration=mission.getDuration();
        this.GadgetName=mission.getGadget();
        this.TimeIssued=mission.getTimeIssued();
        this.TimeExpired=mission.getTimeExpired();
    }

    public String getMissionName() {
        return missionName;
    }

    public int getTimeExpired(){
        return TimeExpired;
    }

    public List<String> getAgentsSerialNumbers() {
        return AgentsSerialNumbers;
    }

    public String getGadgetName() {
        return GadgetName;
    }

    public int getTimeIssued() {
        return TimeIssued;
    }

    public int getDuration() {
        return duration;
    }

}

