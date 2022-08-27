package com.cs2340.towerjackets;

import org.junit.Test;
import static org.junit.Assert.*;

import com.cs2340.towerjackets.models.Monument;
import com.cs2340.towerjackets.models.Player;
import com.cs2340.towerjackets.models.enemy.BlueEnemy;
import com.cs2340.towerjackets.models.enemy.Enemy;
import com.cs2340.towerjackets.models.enemy.GreenEnemy;
import com.cs2340.towerjackets.models.enemy.PurpleEnemy;
import com.cs2340.towerjackets.models.game_config.Difficulty;
import com.cs2340.towerjackets.models.game_config.GameConfiguration;
import com.cs2340.towerjackets.models.tower.BeeTower;
import com.cs2340.towerjackets.models.tower.HornetTower;
import com.cs2340.towerjackets.models.tower.WaspTower;
import com.cs2340.towerjackets.viewmodels.GameActivityViewModel;


public class M5UnitTest {

    private GameActivityViewModel gameActivityViewModel;

    @Test
    // Anh Le - tests that placing a Bee Tower on the board increases monument/player health.
    public void beeTowerFunctionality() {
        Player player = new Player("player1", new GameConfiguration(Difficulty.Easy));
        BeeTower beeTower = new BeeTower(player);
        beeTower.placeTower();
        assertEquals(120, player.getHealth());
    }

    @Test
    // Anh Le - Tests that enemies are removed from the list when their health reaches zero.
    public void enemiesHealthZero() {
        gameActivityViewModel = new GameActivityViewModel();
        gameActivityViewModel.addEnemy(1, 10, 10); // adding blue enemy
        gameActivityViewModel.addEnemy(2, 20, 20); // adding green enemy
        for (Enemy e: gameActivityViewModel.getListOfEnemy()) {
            if (e instanceof BlueEnemy) {
                e.setHealth(0);
            }
        }
        assertEquals(1, gameActivityViewModel.getListOfEnemy().size());
    }

    @Test
    // Helen Chen - tests that placing a Hornet Tower and enemy not within proximity does not do anything.
    public void enemyNotWithinProximity() {
        HornetTower hornetTower = new HornetTower(500, 500);
        BlueEnemy enemy = new BlueEnemy(100, 100);
        hornetTower.attackEnemy(enemy);
        assertEquals(200, enemy.getHealth());
    }

    @Test
    // Helen Chen - tests that placing a Hornet Tower and enemy within proximity decreases enemy health.
    public void enemyWithinProximity() {
        HornetTower hornetTower = new HornetTower(500, 500);
        BlueEnemy enemy = new BlueEnemy(450, 550);
        hornetTower.attackEnemy(enemy);
        assertEquals(150, enemy.getHealth());
    }

    @Test
    // Ori Yoked - tests that placing a Wasp Tower generates one coin.
    public void waspTowerFunctionality() {
        Player player = new Player("player1", new GameConfiguration(Difficulty.Easy));
        WaspTower waspTower = new WaspTower(player);
        waspTower.placeTower();
        assertEquals(10, waspTower.getCoin().getValue());
    }

    @Test
    // Ori Yoked - tests that collecting the coin increases player money.
    public void coinFunctionality() {
        Player player = new Player("player1", new GameConfiguration(Difficulty.Easy));
        WaspTower waspTower = new WaspTower(player);
        waspTower.placeTower();
        waspTower.getCoin().collectCoin(player);
        assertEquals(1010, player.getMoney());
    }

    @Test
    // Tomer Shmul - tests that a moving enemy can still be attacked twice by the same tower.
    public void enemyAttackedTwice() {
        HornetTower hornetTower = new HornetTower(500, 500);
        BlueEnemy enemy = new BlueEnemy(450, 450);
        hornetTower.attackEnemy(enemy);
        enemy.move();
        hornetTower.attackEnemy(enemy);
        assertEquals(100, enemy.getHealth());
    }

    @Test
    // Tomer Shmul - tests if each enemy has different functionality; ie., different damage potentials.
    // Tested on three enemies: BlueEnemy, PurpleEnemy, GreenEnemy
    public void differentEnemyFunctionality() {
        BlueEnemy blue = new BlueEnemy();
        PurpleEnemy purple = new PurpleEnemy();
        GreenEnemy green = new GreenEnemy();
        assertEquals(false, blue.getDamage() == green.getDamage());
        assertEquals(false, purple.getDamage() == green.getDamage());
        assertEquals(false, blue.getDamage() == purple.getDamage());
    }

    @Test
    // Harriet Kim - ensures that all towers have distinct behaviors and can exist simultaneously.
    public void distinctBehavior() {
        Player player = new Player("player1", new GameConfiguration(Difficulty.Easy));
        BeeTower beeTower = new BeeTower(player);
        HornetTower hornetTower = new HornetTower(500, 500);
        WaspTower waspTower = new WaspTower(player);
        BlueEnemy enemy = new BlueEnemy(450, 550);

        beeTower.placeTower();
        waspTower.placeTower();
        hornetTower.attackEnemy(enemy);
        assertEquals(120, player.getHealth());
        assertEquals(10, waspTower.getCoin().getValue());
        assertEquals(150, enemy.getHealth());
    }

    @Test
    // Harriet Kim - ensures that combat does not happen in an instant, but rather happens slowly over time.
    public void slowDeath() {
        HornetTower hornetTower = new HornetTower(500, 500);
        BlueEnemy enemy = new BlueEnemy(450, 450);
        hornetTower.attackEnemy(enemy);
        hornetTower.attackEnemy(enemy);
        assertEquals(true, enemy.getAlive());
        hornetTower.attackEnemy(enemy);
        hornetTower.attackEnemy(enemy);
        hornetTower.attackEnemy(enemy);
        hornetTower.attackEnemy(enemy);
        hornetTower.attackEnemy(enemy);
        assertEquals(false, enemy.getAlive());
    }
}

