package com.cs2340.towerjackets.models.enemy;
import com.cs2340.towerjackets.R;

public class FinalBoss extends Enemy {
    public FinalBoss() {
        super();
        setDrawableID(R.drawable.boss);
        setHealth(500);
        setSpeed(500);
        setDamage(500);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}