package com.cs2340.towerjackets.models.tower;

public abstract class Tower {
    private int drawableID;
    private int locationX;
    private int locationY;
    private int cost;
    private int upgradeCost;
    private boolean upgraded;
    private static final int TOTAL_TOWER_TYPES = 3;

    public Tower() {
        // Let the children class sets the private fields.
    }

    public int getDrawableNumber() {
        return drawableID;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public int getCost() {
        return cost;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public boolean getUpgraded() {
        return upgraded;
    }

    public static int getTotalTowerTypes() {
        return TOTAL_TOWER_TYPES;
    }

    public void setDrawableID(int id) {
        drawableID = id;
    }

    public void setLocationX(int x) {
        locationX = x;
    }

    public void setLocationY(int y) {
        locationY = y;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setUpgradeCost(int upgradeCost) {
        this.upgradeCost = upgradeCost;
    }

    public void setUpgraded(boolean upgraded) {
        this.upgraded = upgraded;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
