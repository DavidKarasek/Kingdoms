package com.mythbusterma.kingdoms;

public class VillagePermissions {
    private boolean outsiderBuild;
    private boolean outsiderEnter;
    private boolean outsiderChest;
    private boolean outsiderDamageEntity;
    private boolean residentBuild;
    private boolean residentChest;
    private boolean residentIgnite;
    private boolean residentDamageEntity;

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
}
