package com.mod_snmp.Snmp.Protocol;
import java.util.Date;

/**
 * EnginTime which is a special handling of a timer needed for the
 * time synchronization with a remote SNMP engine.
 */
public class EngineTime {
    /**
     * The timer value when set.
     */
    private int time;
    /**
     * The time when the timer was set.
     */
    private long timeAtUpdate;

    /**
     * Default constructor that initializes the timer at '0' at the time
     * the object is created.
     */
    public EngineTime() {
        updateTime(0);
    }
    /**
     * Retrieval of the current timer value.
     */
    public int getTime() {
        Date engineClock = new Date();
        int calcTime = time +
                (int)((engineClock.getTime() - timeAtUpdate) / 100);
        if (calcTime < 0) {
            return 0;
        }
        return calcTime;
    }
    /**
     * Update of the timer value.
     * @param t The timer value.
     */
    public void updateTime(int t) {
        time = t;
        Date engineClock = new Date();
        timeAtUpdate = engineClock.getTime();
    }
    /**
     * A dumper for debugging purposes.
     */
    public void dump() {
        System.out.println("    engineTime       : " + getTime());
    }
    /**
     * A test program.
     */
    public static void main(String args[]) {
        try {
            EngineTime target = new EngineTime();
            int et = 200;

            System.out.println("engine time " + et);
            target.updateTime(et);
            Thread.sleep(50);
            System.out.println("engine time " + target.getTime() +
                                        " updated");
        } catch (InterruptedException e) {
        }
    }
}
