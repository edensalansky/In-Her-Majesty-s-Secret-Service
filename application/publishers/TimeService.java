package bgu.spl.mics.application.publishers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.killAll;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private long timeExpire;
    private Timer timer;
    private int currentTime = 0;

    public TimeService(int time) {
        super("TimeService");
        this.timeExpire = time;
        timer = new Timer();
    }


    @Override
    protected void initialize() {

    }

    @Override
    public void run() {
        SimplePublisher m = this.getSimplePublisher();
        TimerTask timerT = new TimerTask() {

            @Override
            public void run() {
                Broadcast b;
                if (currentTime == timeExpire) {
                    b = new killAll();
                    timer.cancel();
                } else {
                    b = new TickBroadcast(currentTime);
                }
                m.sendBroadcast(b);
                currentTime++;
            }
        };
        timer.scheduleAtFixedRate(timerT, 0, 100);
    }

}


