package com.cs2340.towerjackets.models;

import com.cs2340.towerjackets.models.game_config.GameConfiguration;
import com.cs2340.towerjackets.models.tower.BeeTower;
import com.cs2340.towerjackets.models.tower.HornetTower;
import com.cs2340.towerjackets.models.tower.Tower;
import com.cs2340.towerjackets.models.tower.WaspTower;

public class Player {
    private String name;
    private GameConfiguration config;
    private int money;
    private int health;
    private Tower[] towerInv = new Tower[Tower.getTotalTowerTypes()];
    private int[] towerAvailable = new int[Tower.getTotalTowerTypes()];

    /**
     * Constructor for creating a new Player. More can be added later if needed.
     * @param name - player's name
     * @param config - a GameConfiguration objects which tell us the difficulty selected.
     */
    public Player(String name, GameConfiguration config) {
        setName(name);
        setConfig(config);

        towerInv[0] = new HornetTower();
        towerInv[1] = new BeeTower();
        towerInv[2] = new WaspTower();

        // ordinal() will get the corresponding integer to the difficulty level
        initialConfiguration(config.getGameDifficulty().ordinal());
    }

    public void setName(String name) {
        if (name == null || name.trim().length() <= 0) {
            throw new java.lang.IllegalArgumentException("Name must not be "
                    + "null, empty, or only contain white spaces.");
        }
        this.name = name;
    }

    public void setConfig(GameConfiguration config) {
        if (config == null) {
            throw new java.lang.IllegalArgumentException("Can't process without "
                    + "selecting Difficulty level.");
        }
        this.config = config;
    }

    public String getName() {
        return name;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    public GameConfiguration getConfig() {
        return config;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = Math.max(health, 0);
    }

    public int getTowerInv(int i) {
        return towerAvailable[i];
    }

    public int getTowerCost(int i) {
        return towerInv[i].getCost();
    }

    public int getTowerUpgradeCost(int i) {
        return towerInv[i].getUpgradeCost();
    }

    public void initialConfiguration(int difficulty) {
        if (difficulty == 0) { // easy
            money = 1000;
            health = 100;
            towerInv[0].setCost(60);
            towerInv[1].setCost(80);
            towerInv[2].setCost(110);
            towerInv[0].setUpgradeCost(45);
            towerInv[1].setUpgradeCost(65);
            towerInv[2].setUpgradeCost(95);
        } else if (difficulty == 1) { // normal
            money = 800;
            health = 80;
            towerInv[0].setCost(55);
            towerInv[1].setCost(90);
            towerInv[2].setCost(120);
            towerInv[0].setUpgradeCost(40);
            towerInv[1].setUpgradeCost(75);
            towerInv[2].setUpgradeCost(105);
        } else if (difficulty == 2) { // hard
            money = 500;
            health = 50;
            towerInv[0].setCost(50);
            towerInv[1].setCost(100);
            towerInv[2].setCost(130);
            towerInv[0].setUpgradeCost(35);
            towerInv[1].setUpgradeCost(85);
            towerInv[2].setUpgradeCost(115);
        }
    }

    public void buyTower(int i) {
        if (money >= towerInv[i].getCost()) {
            money -= towerInv[i].getCost();
            towerAvailable[i]++;
        }
    }

    public boolean placeTower(int tower, int x, int y) {
        if (checkValidPlacement(x, y)) {
            if (towerAvailable[tower] >= 1) {
                towerAvailable[tower]--;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean checkValidPlacement(int x, int y) {
        return !(x < 1149 && y > 205 && y < 436) && !(x > 899 && x < 1149 && y > 329 && y < 811)
                && !(x > 891 && y > 711 && y < 943);
    }
}
