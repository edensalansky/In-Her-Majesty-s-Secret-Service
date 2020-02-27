package bgu.spl.mics.application;

import bgu.spl.mics.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.subscribers.Q;
import bgu.spl.mics.json.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        MessageBroker messageBroker = MessageBrokerImpl.getInstance();
        List<Thread> listOfThreads = new LinkedList();
        Gson gson = new Gson();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(args[0]));
            Parsejson json = gson.fromJson(br, Parsejson.class);
            Inventory inv = Inventory.getInstance();
            inv.load(json.getInventory());
            Thread Q = new Thread(new Q());
            listOfThreads.add(Q);
            Q.start();
            Squad squad = Squad.getInstance();
            squad.load(json.getSquad());
            addMoneyPenny(listOfThreads, json);
            addM(listOfThreads, json);
            addIntelligence(listOfThreads, json);
            int executeTime = json.getServices().getTime();
            TimeService time = new TimeService(executeTime);
            Thread timer = new Thread(time);
            listOfThreads.add(timer);
            timer.sleep(300);
            timer.start();
            for (Thread listOfThread : listOfThreads) {
                listOfThread.join();
            }
            Diary diary = Diary.getInstance();
            diary.printToFile(args[2]);
            inv.printToFile(args[1]);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void addMoneyPenny(List<Thread> listOfThreads, Parsejson json) {
        for (int i = 1; i <= json.getServices().getMoneypenny(); i++) {
            Moneypenny m = new Moneypenny(i);
            Thread t = new Thread(m);
            listOfThreads.add(t);
            t.start();
        }
    }

    private static void addM(List<Thread> listOfThreads, Parsejson json) {
        for (int i = 1; i <= json.getServices().getM(); i++) {
            M m = new M(i);
            Thread t = new Thread(m);
            listOfThreads.add(t);
            t.start();
        }
    }

    private static void addIntelligence(List<Thread> listOfThreads, Parsejson json) {
        for (int i = 0; i < json.getServices().getIntelligence().size(); i++) {
            List<MissionInfo> missionInfos = new LinkedList<MissionInfo>();
            for (int j = 0; j < json.getServices().getIntelligence().get(i).getMissions().size(); j++) {
                MissionInfo mission = new MissionInfo();
                List<Mission> temp = json.getServices().getIntelligence().get(i).getMissions();
                mission.setMissionName(temp.get(j).getName());
                mission.setDuration(temp.get(j).getDuration());
                mission.setGadget(temp.get(j).getGadget());
                mission.setSerialAgentsNumbers(temp.get(j).getSerialAgentsNumbers());
                mission.setTimeExpired(temp.get(j).getTimeExpired());
                mission.setTimeIssued(temp.get(j).getTimeIssued());
                missionInfos.add(mission);
            }
            Intelligence intelligence = new Intelligence(missionInfos);
            Thread t = new Thread(intelligence);
            listOfThreads.add(t);
            t.start();
        }
    }


}





