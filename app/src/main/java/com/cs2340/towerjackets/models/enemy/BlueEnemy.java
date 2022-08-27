package com.cs2340.towerjackets.models.enemy;
import com.cs2340.towerjackets.R;

public class BlueEnemy extends Enemy {
    public BlueEnemy() {
        super();
        setDrawableID(R.drawable.blue);
        setHealth(200);
        setSpeed(200);
        setDamage(200);
    }

    // M5 JUnit Things
    public BlueEnemy(int x, int y) {
        this();
        setLocationX(x);
        setLocationY(y);
    }
    public void move() {
        setLocationX(getLocationX() + 50);
        setLocationY(getLocationY() + 50);
    }
    // End of M5 JUnit Things

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}