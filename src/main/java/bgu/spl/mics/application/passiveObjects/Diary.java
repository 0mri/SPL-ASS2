package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;
import bgu.spl.mics.application.Main;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is
 * recorded. We are going to compare your recordings with the expected
 * recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and
 * setters.
 */

public class Diary {

    private AtomicInteger totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    private static class DiaryHolder {
        private static Diary instance = new Diary();
    }

    private Diary() {
        totalAttacks = new AtomicInteger();
    }

    public static Diary init() {
        return DiaryHolder.instance;
    }

    public static Diary getInstance() {
        return DiaryHolder.instance;
    }

    public void incrementTotalAttacks() {
        int num;
        do {
            num = totalAttacks.get();
        } while (!totalAttacks.compareAndSet(num, num + 1));
    }

    public void setHanSoloFinish(long val) {
        HanSoloFinish = val - Main.StartTime;
    }

    public void setHanSoloTerminate(long val) {
        HanSoloTerminate = val - Main.StartTime;
    }

    public void setR2D2Deactivate(long val) {
        R2D2Deactivate = val - Main.StartTime;
    }

    public void setR2D2Terminate(long val) {
        R2D2Terminate = val - Main.StartTime;
    }

    public void setC3POTerminate(long val) {
        C3POTerminate = val - Main.StartTime;
    }

    public void setC3POFinish(long val) {
        C3POFinish = val - Main.StartTime;
    }

    public void setLeiaTerminate(long val) {
        LeiaTerminate = val - Main.StartTime;
    }

    public void setLandoTerminate(long val) {
        LandoTerminate = val - Main.StartTime;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public AtomicInteger getTotalAttacks() {
        return this.totalAttacks;
    }

    public String toString() {
        String o = String.format("There are %s attacks. \n", this.totalAttacks) + String.format(getGreaterFinish())
                + String.format("finish their tasks %d milliseconds one after the other. \n",
                        Math.abs(HanSoloFinish - C3POFinish))
                + "All threads terminate " + (LandoTerminate - getGreater()) + " milliseconds later.";

        return o;
    }

    private String getGreaterFinish() {
        return (HanSoloFinish > C3POFinish) ? "HanSolo and C3PO " : "C3PO and HanSolo ";
    }

    private long getGreater() {
        return (HanSoloFinish > C3POFinish) ? HanSoloFinish : C3POFinish;
    }

}
