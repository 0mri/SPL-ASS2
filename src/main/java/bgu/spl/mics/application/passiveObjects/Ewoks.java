package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton. You must not alter
 * any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private List<Ewok> ewoks;

    private static class EwoksHolder {
        private static Ewoks instance = new Ewoks();

        private static void init(int num) {
            instance.ewoks.add(0, null);
            for (int i = 1; i <= num; i++)
                instance.ewoks.add(new Ewok(i));
        }
    }

    private Ewoks() {
        ewoks = new ArrayList<>();
    }

    public static Ewoks init(int num) {
        EwoksHolder.init(num);
        return EwoksHolder.instance;
    }

    public static Ewoks getInstance() {
        return EwoksHolder.instance;
    }

    public synchronized void acquire(List<Integer> sn) {
        for (Integer n : sn) {
            while (!ewoks.get(n).isAvailable()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ewoks.get(n).acquire();
        }
    }

    public synchronized void release(List<Integer> sn) {
        for (Integer n : sn) {
            ewoks.get(n).release();
            notifyAll();
        }
    }

}
