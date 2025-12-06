package com.nbb.aaa.flower;

public class Details extends GenericInfo {

    long plantId;
    long whenWasWatered;//mili since 1970 תאריך השקיה אחרון
    long water;//כמה זמן הושקע
    int bByRain; // true if rain

    public Details(long _id, long plantId, long whenWasWatered, long water, int bByRain) {
        super(_id);
        this.plantId = plantId;
        this.whenWasWatered = whenWasWatered;
        this.water = water;
        this.bByRain = bByRain;
    }

    public Details(long plantId, long whenWasWatered, long water, int bByRain) {
        this.plantId = plantId;
        this.whenWasWatered = whenWasWatered;
        this.water = water;
        this.bByRain = bByRain;
    }


    public long getPlantId() {
        return plantId;
    }

    public void setPlantId(long plantId) {
        this.plantId = plantId;
    }

    public long getWhenWasWatered() {
        return whenWasWatered;
    }

    public void setWhenWasWatered(long whenWasWatered) {
        this.whenWasWatered = whenWasWatered;
    }

    public long getWater() {
        return water;
    }

    public void setWater(long water) {
        this.water = water;
    }

    public int getByRain() {
        return bByRain;
    }

    public void setByRain(int bByRain) {
        this.bByRain = bByRain;
    }

    @Override
    public String toString() {
        return "Details{" +
                "id=" + id +
                ", plantId=" + plantId +
                ", whenWasWatered=" + whenWasWatered +
                ", water=" + water +
                ", bByRain=" + bByRain +
                '}';
    }
}
