package net.amicom.customizedphone;

public class TimeForm {
    private String name;
    private String startTime;
    private String endTime;
    private String daySum;
    private int currentWifiChecking;
    private int currentSoundChecking;
    private int wifiChecking;
    private int soundChecking;
    private boolean startFlag = false;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDaySum() {
        return daySum;
    }

    public void setDaySum(String daySum) {
        this.daySum = daySum;
    }

    public int getCurrentWifiChecking() {
        return currentWifiChecking;
    }

    public void setCurrentWifiChecking(int currentWifiChecking) {
        this.currentWifiChecking = currentWifiChecking;
    }

    public int getCurrentSoundChecking() {
        return currentSoundChecking;
    }

    public void setCurrentSoundChecking(int currentSoundChecking) {
        this.currentSoundChecking = currentSoundChecking;
    }

    public int getWifiChecking() {
        return wifiChecking;
    }

    public void setWifiChecking(int wifiChecking) {
        this.wifiChecking = wifiChecking;
    }

    public int getSoundChecking() {
        return soundChecking;
    }

    public void setSoundChecking(int soundChecking) {
        this.soundChecking = soundChecking;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStartFlag() {
        return startFlag;
    }

    public void setStartFlag(boolean startFlag) {
        this.startFlag = startFlag;
    }

}
