package com.cs2340.towerjackets;

import org.junit.Test;
import static org.junit.Assert.*;

import com.cs2340.towerjackets.models.Monument;
import com.cs2340.towerjackets.models.Player;
import com.cs2340.towerjackets.models.enemy.BlueEnemy;
import com.cs2340.towerjackets.models.enemy.GreenEnemy;
import com.cs2340.towerjackets.models.enemy.PurpleEnemy;
import com.cs2340.towerjackets.models.game_config.Difficulty;
import com.cs2340.towerjackets.models.game_config.GameConfiguration;


public class M4UnitTest {

    @Test
    // Anh Le - tests if placeEnemy places enemy on path.
    public void enemyOnPath() {
        Player player = new Player("player1", new GameConfiguration(Difficulty.Easy));
        PurpleEnemy purple = new PurpleEnemy();
        purple.placeEnemy();
        assertEquals(false, player.checkValidPlacement(purple.getLocationX(),
                purple.getLocationY()));
        // checkValidPlacement is method used for checking whether towers can be placed on path,
        // which is why the expected result is false.
    }

    @Test
    // Anh Le - tests that only enemies who have reached monument can damage monument.
    // Tested on two enemies: one on monument and one not on monument.
    public void enemiesOnMonument() {
        Monument monument = new Monument(300);
        BlueEnemy blue = new BlueEnemy();
        GreenEnemy green = new GreenEnemy();
        blue.setLocationX(2000); // on monument
        green.setLocationX(1000); // not on monument
        green.damageMonument(monument);
        assertEquals(300, monument.getHealth()); // no damage
        blue.damageMonument(monument);
        assertEquals(100, monument.getHealth()); // does 200 damage
    }

    @Test
    // Helen Chen - tests if different enemies have different starting health.
    // Tested on three enemies: BlueEnemy, PurpleEnemy, GreenEnemy
    public void differentEnemyHealth() {
        BlueEnemy blue = new BlueEnemy();
        PurpleEnemy purple = new PurpleEnemy();
        GreenEnemy green = new GreenEnemy();
        assertEquals(false, blue.getHealth() == green.getHealth());
        assertEquals(false, purple.getHealth() == green.getHealth());
        assertEquals(false, blue.getHealth() == purple.getHealth());
    }

    @Test
    // Helen Chen - tests if different enemies hitting monument reduces the monument health by
    // different damages. Tested on three enemies: BlueEnemy, PurpleEnemy, GreenEnemy
    public void reduceMonumentHealth() {
        BlueEnemy blue = new BlueEnemy();
        PurpleEnemy purple = new PurpleEnemy();
        GreenEnemy green = new GreenEnemy();
        Monument monument = new Monument(1000);
        blue.setLocationX(2000); // setting blue in place to attack monument
        blue.damageMonument(monument);
        assertEquals(800, monument.getHealth());
        purple.setLocationX(2000); // setting purple in place to attack monument
        purple.damageMonument(monument);
        assertEquals(700, monument.getHealth());
        green.setLocationX(2000); // setting green in place to attack monument
        green.damageMonument(monument);
        assertEquals(400, monument.getHealth());
    }

    @Test
    // Ori Yoked - tests if different enemies have different starting damages.
    // Tested on three enemies: BlueEnemy, PurpleEnemy, GreenEnemy
    public void differentEnemyDamage() {
        BlueEnemy blue = new BlueEnemy();
        PurpleEnemy purple = new PurpleEnemy();
        GreenEnemy green = new GreenEnemy();
        assertEquals(false, blue.getDamage() == green.getDamage());
        assertEquals(false, purple.getDamage() == green.getDamage());
        assertEquals(false, blue.getDamage() == purple.getDamage());
    }

    @Test
    // Ori Yoked - tests that dead enemies can no longer damage monument.
    // Tested on two enemies: one alive and one dead
    public void deadEnemies() {
        Monument monument = new Monument(300);
        BlueEnemy blue = new BlueEnemy(); // alive
        blue.setLocationX(2000); // on monument
        GreenEnemy green = new GreenEnemy();
        green.setLocationX(2000); // on monument
        green.setAlive(false); // dead
        green.damageMonument(monument);
        assertEquals(300, monument.getHealth()); // no damage
        blue.damageMonument(monument);
        assertEquals(100, monument.getHealth()); // does 200 damage
    }

    @Test
    // Tomer Shmul - tests if enough damage to monument causes game over.
    public void checkGameStatus() {
        BlueEnemy blue = new BlueEnemy();
        Monument monument = new Monument(1000);
        blue.setLocationX(2000); // setting blue in place to attack monument
        blue.damageMonument(monument);
        blue.damageMonument(monument);
        blue.damageMonument(monument);
        blue.damageMonument(monument);
        blue.damageMonument(monument);
        assertEquals(true, monument.getGameOver());
    }

    @Test
    // Tomer Shmul - tests if placeEnemy places enemy randomly (not always in same position).
    public void towersGameOver() {
        PurpleEnemy purple = new PurpleEnemy();
        purple.placeEnemy();
        BlueEnemy blue = new BlueEnemy();
        blue.placeEnemy();
        GreenEnemy green = new GreenEnemy();
        green.placeEnemy();
        boolean samePosition = false;
        if (purple.getLocationX() == blue.getLocationX()
                && purple.getLocationY() == blue.getLocationY()) {
            samePosition = true;
        } else if (purple.getLocationX() == green.getLocationX()
                && purple.getLocationY() == green.getLocationY()) {
            samePosition = true;
        } else if (blue.getLocationX() == green.getLocationX()
                && blue.getLocationY() == green.getLocationY()) {
            samePosition = true;
        }
        assertEquals(false, samePosition);
        // note - it is highly unlikely for the random generator to generate three of the same
        // position in a row; however, it is possible.
    }

    @Test
    // Harriet Kim - tests if different enemies have different starting speeds.
    // Tested on three enemies: BlueEnemy, PurpleEnemy, GreenEnemy
    public void differentEnemySpeeds() {
        BlueEnemy blue = new BlueEnemy();
        PurpleEnemy purple = new PurpleEnemy();
        GreenEnemy green = new GreenEnemy();
        assertEquals(false, blue.getSpeed() == green.getSpeed());
        assertEquals(false, purple.getSpeed() == green.getSpeed());
        assertEquals(false, blue.getSpeed() == purple.getSpeed());
    }

    @Test
    // Harriet Kim - monument stops taking damage after its health reaches 0 and monument health
    // does not become negative.
    public void monumentHealthZero() {
        GreenEnemy green = new GreenEnemy();
        Monument monument = new Monument(1000);
        green.setLocationX(2000); // setting green in place to attack monument
        green.damageMonument(monument);
        green.damageMonument(monument);
        green.damageMonument(monument);
        green.damageMonument(monument);
        green.damageMonument(monument);
        assertEquals(0, monument.getHealth());
    }
}
