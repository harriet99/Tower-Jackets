package com.cs2340.towerjackets.models.enemy;
import com.cs2340.towerjackets.R;

public class PurpleEnemy extends Enemy {
    public PurpleEnemy() {
        super();
        setDrawableID(R.drawable.purple);
        setHealth(100);
        setSpeed(100);
        setDamage(100);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}