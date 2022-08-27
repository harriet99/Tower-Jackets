package com.cs2340.towerjackets.models.enemy;
import com.cs2340.towerjackets.R;

public class GreenEnemy extends Enemy {
    public GreenEnemy() {
        super();
        setDrawableID(R.drawable.green);
        setHealth(250);
        setSpeed(300);
        setDamage(300);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}