package com.cs2340.towerjackets.models.game_config;

public class GameConfiguration {
    private Difficulty gameDifficulty;

    /**
     * Constructor to set gameDifficulty
     * @param diff - the difficulty of the game
     */
    public GameConfiguration(Difficulty diff) {
        if (diff == null) {
            throw new java.lang.IllegalArgumentException("Can not accept null difficulty.");
        }
        gameDifficulty = diff;
    }

    /**
     * Getter for gameDifficulty
     * @return Difficulty
     */
    public Difficulty getGameDifficulty() {
        return gameDifficulty;
    }

    /**
     * Setter for gameDifficulty
     * @param gameDifficulty - to set the game difficulty
     */
    public void setGameDifficulty(Difficulty gameDifficulty) {
        if (gameDifficulty == null) {
            throw new IllegalArgumentException("Game difficulty can't be null");
        }
        this.gameDifficulty = gameDifficulty;
    }
}
