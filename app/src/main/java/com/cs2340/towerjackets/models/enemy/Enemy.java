package com.cs2340.towerjackets.models.enemy;
import com.cs2340.towerjackets.models.Monument;

import java.util.Random;

public abstract class Enemy {
    private int drawableID;
    private int locationX = 0;
    private int locationY = 0;
    private int health;
    private int speed;
    private int damage;
    private boolean alive;
    private static final int TOTAL_ENEMY_TYPES = 4;


    public Enemy() {
        alive = true;
        // children set the other fields
    }

    public boolean getAlive() {
        return alive;
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

    public int getHealth() {
        return health;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public static int getTotalEnemyTypes() {
        return TOTAL_ENEMY_TYPES;
    }

    public void setAlive(boolean b) {
        alive = b;
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

    public void setHealth(int health) {
        this.health = health;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    // M4 Junit Things
    private boolean atMonument = false;
    public void damageMonument(Monument monument) {
        if (monument.getHealth() >= 0 && locationX >= 2000 && alive) {
            monument.setHealth(Math.max(monument.getHealth() - damage, 0));
            monument.checkStatus();
        }
    }
    public void placeEnemy() {
        Random rand = new Random();
        locationX = 25 + rand.nextInt(5);
        locationY = 325 + rand.nextInt(5);
    }
    // End of M4 Junit Things

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}