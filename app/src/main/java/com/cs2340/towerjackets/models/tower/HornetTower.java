package com.cs2340.towerjackets.models.tower;
import com.cs2340.towerjackets.R;
import com.cs2340.towerjackets.models.enemy.Enemy;

public class HornetTower extends Tower {

    public HornetTower() {
        super();
        setDrawableID(R.drawable.old_hornet);
        setCost(55);
    }

    // M5 JUnit Things
    public HornetTower(int x, int y) {
        setLocationX(x);
        setLocationY(y);
    }
    public void attackEnemy(Enemy e) {
        if (e.getLocationX() + 100 > getLocationX() && e.getLocationX() - 100 < getLocationX()
            && e.getLocationY() + 100 > getLocationY() && e.getLocationY() - 100 < getLocationY()) {
            e.setHealth(e.getHealth() - 50);
            if (e.getHealth() < 0) {
                e.setAlive(false);
            }
        }
    }
    // End of M5 JUnit Things

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
