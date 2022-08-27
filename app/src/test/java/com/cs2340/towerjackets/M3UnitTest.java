package com.cs2340.towerjackets;

import org.junit.Test;
import com.cs2340.towerjackets.models.Player;
import com.cs2340.towerjackets.models.game_config.Difficulty;
import com.cs2340.towerjackets.models.game_config.GameConfiguration;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class M3UnitTest {
    @Test
    public void additionIsCorrect() {
        assertEquals(4, 2 + 2);
    }

    // M3 - Anh Le - This tests whether our program would throw an exception when the input name
    // is null.
    @Test (expected = IllegalArgumentException.class)
    public void nameIsNull() {
        Difficulty diff = Difficulty.Easy;
        // This test should pass since the following line should throw an IllegalArgumentException
        Player newPlayer = new Player(null, new GameConfiguration(diff));
    }

    // M3 - Anh Le - This tests whether our program would correctly store the information input
    // by the users.
    // Tested on all difficulties. Tested with 3 different input name strings.
    @Test
    public void testNameDifficultyCorrectlyStored() {
        Difficulty diff1 = Difficulty.Easy;
        Player newPlayer1 = new Player("Bob", new GameConfiguration(diff1));
        assertEquals("Bob", newPlayer1.getName());
        assertEquals(Difficulty.Easy, newPlayer1.getConfig().getGameDifficulty());

        Difficulty diff2 = Difficulty.Normal;
        Player newPlayer = new Player("Agatha", new GameConfiguration(diff2));
        assertEquals("Agatha", newPlayer.getName());
        assertEquals(Difficulty.Normal, newPlayer.getConfig().getGameDifficulty());

        Difficulty diff3 = Difficulty.Hard;
        Player newPlayer3 = new Player("Calvin", new GameConfiguration(diff3));
        assertEquals("Calvin", newPlayer3.getName());
        assertEquals(Difficulty.Hard, newPlayer3.getConfig().getGameDifficulty());
    }

    // M3 - Helen Chen - This tests whether the purchase of a tower in the shop is stored in Player.
    // Tested at easy and normal difficulty with tower two and tower one respectively.
    @Test
    public void testTowerPurchaseStored() {
        Player player1 = new Player("tower tester", new GameConfiguration(Difficulty.Easy));
        player1.initialConfiguration(0); // easy
        player1.buyTower(1); // tower two
        assertEquals(920, player1.getMoney());
        assertEquals(1, player1.getTowerInv(1));

        Player player2 = new Player("tower tester", new GameConfiguration(Difficulty.Normal));
        player2.initialConfiguration(1); // normal
        player2.buyTower(0); // tower one
        assertEquals(745, player2.getMoney());
        assertEquals(1, player2.getTowerInv(0));
    }

    // M3 - Helen Chen - This tests whether a purchase with insufficient funds is processed or not.
    // Tested at hard difficulty, tower three. Expects not purchasable after 3 successful purchases.
    @Test
    public void testTowerPurchaseInsufficientFunds() {
        Player player3 = new Player("tower tester", new GameConfiguration(Difficulty.Hard));
        player3.initialConfiguration(2); // money = 500
        player3.buyTower(2); // tower three: money = 370
        player3.buyTower(2); // tower three: money = 240
        player3.buyTower(2); // tower three: money = 110
        player3.buyTower(2); // tower three: money = 110 (not enough funds)
        assertEquals(110, player3.getMoney());
        assertEquals(3, player3.getTowerInv(2));
    }

    // M3 - Hyun Soo (Harriet) Kim - This tests whether the placement of a tower decreases the tower
    // inventory in the Player.
    // Tested at easy difficulty, tower one and two.
    @Test
    public void testPlaceTowerDecreaseInv() {
        Player testPlayer1 = new Player("Harriet", new GameConfiguration(Difficulty.Easy));
        testPlayer1.initialConfiguration(0); // Easy
        testPlayer1.buyTower(0); // buy tower one
        testPlayer1.buyTower(1); // buy tower two
        testPlayer1.buyTower(1); // buy tower two
        testPlayer1.placeTower(0, 0, 0); // place tower one
        testPlayer1.placeTower(1, 0, 0); // place tower two
        assertEquals(0, testPlayer1.getTowerInv(0));
        assertEquals(1, testPlayer1.getTowerInv(1));
    }

    // M3 - Hyun Soo (Harriet) Kim - This tests whether a placement with insufficient inventory is
    // processed or not.
    // Tested at normal difficulty, tower three. Expects not place the tower after 2 successful
    // placements.
    @Test
    public void testPlaceTowerInsufficientInv() {
        Player testPlayer2 = new Player("Harriet", new GameConfiguration(Difficulty.Normal));
        testPlayer2.initialConfiguration(1); // Normal
        testPlayer2.buyTower(2); // buy tower three
        testPlayer2.buyTower(2); // buy tower three
        testPlayer2.placeTower(2, 0, 0); // place tower three
        assertEquals(1, testPlayer2.getTowerInv(2));
        testPlayer2.placeTower(2, 0, 0); // place tower three
        assertEquals(0, testPlayer2.getTowerInv(2));
        testPlayer2.placeTower(2, 0, 0); // place tower three (not enough inventory for tower three)
        assertEquals(0, testPlayer2.getTowerInv(2));
    }

    // M3 - Tomer Shmul - This tests the functions for changing the player's health and getting the
    // player's health, which will both be used when enemies damage the monument hive. When health
    // reaches a value below 0, it should default to 0.
    @Test
    public void testChangingPlayerHealth() {
        Player testPlayer1 = new Player("Tomer", new GameConfiguration(Difficulty.Easy));

        testPlayer1.setHealth(80);
        assertEquals(testPlayer1.getHealth(), 80);

        testPlayer1.setHealth(0);
        assertEquals(testPlayer1.getHealth(), 0);

        // A value below 0 for health should return 0 health regardless
        testPlayer1.setHealth(-10);
        assertEquals(testPlayer1.getHealth(), 0);
    }

    // M3 - Tomer Shmul - This tests the functions for changing the player's money and getting the
    // player's money, which will both be used when enemies are killed and coins are picked up by
    // the player.
    @Test
    public void testChangingPlayerMoney() {
        Player testPlayer1 = new Player("Tomer", new GameConfiguration(Difficulty.Normal));

        testPlayer1.setMoney(80);
        assertEquals(testPlayer1.getMoney(), 80);

        testPlayer1.setMoney(0);
        assertEquals(testPlayer1.getMoney(), 0);

        testPlayer1.setMoney(1000);
        assertEquals(testPlayer1.getMoney(), 1000);
    }

    // M3 - Ori Yoked - This tests whether buying a tower increases the tower inventory in the
    // Player.
    // Tested at easy difficulty, tower one and two.
    @Test
    public void testBuyTowerIncreaseInv() {
        Player testPlayer = new Player("Ori", new GameConfiguration(Difficulty.Easy));
        testPlayer.initialConfiguration(0); // Easy
        testPlayer.buyTower(0); // buy tower one
        testPlayer.buyTower(1); // buy tower two
        testPlayer.buyTower(1); // buy tower two
        assertEquals(1, testPlayer.getTowerInv(0));
        assertEquals(2, testPlayer.getTowerInv(1));
    }

    // M3 - Ori Yoked - This tests whether choosing a difficulty gives the correct health to the
    // Player.
    // Tested at easy, normal, and hard difficulty
    @Test
    public void testCorrectHealthForDifficulty() {
        Player testPlayer1 = new Player("Ori", new GameConfiguration(Difficulty.Easy));
        testPlayer1.initialConfiguration(0); // Easy
        assertEquals(100, testPlayer1.getHealth());

        Player testPlayer2 = new Player("Ori", new GameConfiguration(Difficulty.Easy));
        testPlayer2.initialConfiguration(1); // Normal
        assertEquals(80, testPlayer2.getHealth());

        Player testPlayer3 = new Player("Ori", new GameConfiguration(Difficulty.Easy));
        testPlayer3.initialConfiguration(2); // Hard
        assertEquals(50, testPlayer3.getHealth());
    }


}