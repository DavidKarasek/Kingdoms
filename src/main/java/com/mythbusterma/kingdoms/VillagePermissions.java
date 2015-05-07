package com.mythbusterma.kingdoms;

public class VillagePermissions {
    // set defaults here
    
    private boolean outsiderBuild = false;
    private boolean outsiderEnter = true;
    private boolean outsiderChest = false;
    private boolean outsiderDamageEntity = false;
    private boolean residentBuild = true;
    private boolean residentChest = true;
    private boolean residentIgnite = true;
    private boolean residentDamageEntity = true;

    public boolean isResidentIgnite() {
        return residentIgnite;
    }

    public void setResidentIgnite(boolean residentIgnite) {
        this.residentIgnite = residentIgnite;
    }

    public boolean isOutsiderBuild() {
        return outsiderBuild;
    }

    public void setOutsiderBuild(boolean outsiderBuild) {
        this.outsiderBuild = outsiderBuild;
    }

    public boolean isOutsiderEnter() {
        return outsiderEnter;
    }

    public void setOutsiderEnter(boolean outsiderEnter) {
        this.outsiderEnter = outsiderEnter;
    }

    public boolean isOutsiderChest() {
        return outsiderChest;
    }

    public void setOutsiderChest(boolean outsiderChest) {
        this.outsiderChest = outsiderChest;
    }

    public boolean isResidentBuild() {
        return residentBuild;
    }

    public void setResidentBuild(boolean residentBuild) {
        this.residentBuild = residentBuild;
    }

    public boolean isResidentChest() {
        return residentChest;
    }

    public void setResidentChest(boolean residentChest) {
        this.residentChest = residentChest;
    }

    public boolean isResidentDamageEntity() {
        return residentDamageEntity;
    }

    public void setResidentDamageEntity(boolean residentDamageEntity) {
        this.residentDamageEntity = residentDamageEntity;
    }

    public boolean isOutsiderDamageEntity() {
        return outsiderDamageEntity;
    }

    public void setOutsiderDamageEntity(boolean outsiderDamageEntity) {
        this.outsiderDamageEntity = outsiderDamageEntity;
    }
    
    // set bit flags to generate a unique value
    public int getValue () {
        int temp = 0;
        if (outsiderBuild) {
            temp += 1;
        }
        if (outsiderEnter) {
            temp += 2;
        }
        if (outsiderChest) {
            temp += 4;
        }
        if (outsiderDamageEntity) {
            temp += 8;
        }
        if (residentBuild) {
            temp += 16;
        }
        if (residentChest) {
            temp += 32;
        }
        if (residentIgnite) {
            temp += 64;
        }
        if (residentDamageEntity) {
            temp += 128;
        }
        return temp;
    }
    
    public static VillagePermissions fromValue(int value) {
        VillagePermissions retVal = new VillagePermissions();
        
        if (value >= 128) {
            retVal.residentDamageEntity = true;
            value -= 128;
        }
        if (value >= 64) {
            retVal.residentIgnite = true;
            value -= 64;
        }
        if (value >= 32) {
            retVal.residentChest = true;
            value -= 32;
        }
        if (value >= 16) {
            retVal.residentBuild = true;
            value -= 16;
        }
        if (value >= 8) {
            retVal.outsiderDamageEntity = true;
            value -=8;
        }
        if (value >= 4) {
            retVal.outsiderChest = true;
            value -= 4;
        }
        if (value >= 2) {
            retVal.outsiderEnter = true;
            value -= 2;
        }
        if (value >= 1) {
            retVal.outsiderBuild = true;
        }
        return retVal;
    }
}
