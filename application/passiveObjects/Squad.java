package bgu.spl.mics.application.passiveObjects;

import java.util.*;

import static java.lang.Thread.sleep;


/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {
    private static class SingletonHolder {
        private static Squad instance = new Squad();
    }
    private boolean terminated=false;
    private Map<String, Agent> Agents = new HashMap<String, Agent>();

    private synchronized void terminate(){
        terminated=true;
        notifyAll();
    }
    /**
     * Retrieves the single instance of this class.
     */
    public static Squad getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization
     *               of the squad.
     */
    public void load(Agent[] agents) {
        for (int i = 0; i < agents.length; i++)
            Agents.put(agents[i].getSerialNumber(), agents[i]);
    }

    /**
     * Releases agents.
     */
    public void releaseAgents(List<String> serials) {
        for (int i = 0; i < serials.size() & i < Agents.size(); i++) {
            if (Agents.containsKey(serials.get(i))) {
                Agent toRelease = Agents.get(serials.get(i));
                synchronized (this) {
                    toRelease.release();
                    notifyAll();
                }
            }
        }
    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time milliseconds to sleep
     */
    public void sendAgents(List<String> serials, int time) {
        synchronized (this) {
            try {
                sleep(time);
                releaseAgents(serials);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    public synchronized boolean getAgents(List<String> serials) {
        boolean answer = true;
        for (int i = 0; i < serials.size() & i < Agents.size() & answer; i++) {
            if (!Agents.containsKey(serials.get(i)))
                answer = false;
            if (answer) {
                try {
                    while (!Agents.get(serials.get(i)).isAvailable()) {
                        this.wait();
                        if(terminated)
                            return false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Agents.get(serials.get(i)).acquire();

            }
        }
        return answer;
    }

    /**
     * gets the agents names
     *
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials) {
        if(serials==null) {
            terminate();
            return null;
        }
        List<String> agentsName = new ArrayList<>();
        for (int i = 0; i < serials.size(); i++) {
            agentsName.add(Agents.get(serials.get(i)).getName());
        }
        return agentsName;
    }
}

