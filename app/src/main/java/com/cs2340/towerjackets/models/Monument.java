package com.cs2340.towerjackets.models;


public class Monument {
    private int health;

    public Monument(Player player) {
        this.health = player.getHealth();
    }

    public Monument(int health) {
        this.health = health;
    }

    // M4 Junit Things
    private boolean gameOver = false;
    public void checkStatus() {
        if (health <= 0) {
            gameOver = true;
        }
    }
    public boolean getGameOver() {
        return gameOver;
    }
    // End of M4 Junit Things

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }
}
