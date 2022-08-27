package com.cs2340.towerjackets.views;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.cs2340.towerjackets.R;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import android.view.MotionEvent;

import com.cs2340.towerjackets.models.Coin;
import com.cs2340.towerjackets.models.Monument;
import com.cs2340.towerjackets.models.Player;
import com.cs2340.towerjackets.models.enemy.Enemy;
import com.cs2340.towerjackets.models.tower.Tower;
import com.cs2340.towerjackets.models.tower.WaspTower;
import com.cs2340.towerjackets.viewmodels.GameActivityViewModel;

import java.util.LinkedList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private TextView moneyView;
    private TextView healthView;

    private TextView towerOneView;
    private TextView towerTwoView;
    private TextView towerThreeView;

    private int numT = 3; // number of towers

    private int killCount = 0; // number of enemies killed

    private int purchaseCount = 0; // number of towers purchased

    private int upgradeCount = 0; //number of upgrades

    private boolean killedEnemies = false;

    private boolean gameOver = false;

    private Button[] placeT = new Button[numT];

    private Button start;

    private Player player;

    private RelativeLayout areaLayout;
    private GameActivityViewModel gameActivityViewModel;

    private boolean[] placed = {false, false, false};

    private Monument hive;

    private LinkedList<Integer> usedTowers = new LinkedList<>();
    private LinkedList<Integer> usedHornetTowers = new LinkedList<>();
    private LinkedList<Integer> usedBeeTowers = new LinkedList<>();
    private LinkedList<Integer> usedWaspTowers = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        gameActivityViewModel = new ViewModelProvider(this).get(GameActivityViewModel.class);

        // Hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        areaLayout = findViewById(R.id.relativeLayout);

        player = InitialConfiguration.getPlayer();

        //Setting up the monument
        hive = new Monument(player);
        startHive();

        // Populating the views with appropriate text and images
        configViews();

        // Set buttons
        start = findViewById(R.id.startCombatB);
        start.setVisibility(View.VISIBLE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start.setVisibility(View.GONE);
                int x = 30;
                int y = 330;
                int[] xArr = {x, x - 10, x, x + 30, x + 10, x + 20, x - 20, x - 10, x};
                int[] yArr = {y - 40, y, y + 10, y + 20, y + 10, y + 20, y - 10, y - 20};
                int[] enemyTypeArr = {0, 1, 2, 1, 0, 1, 2, 1};

                setValues();
                RelativeLayout.LayoutParams param = createParam();

                for (int i = 0; i < 8; i++) {
                    ImageView iv = new ImageView(getApplicationContext());
                    TextView health = new TextView(getApplicationContext());
                    health.setTextColor(Color.WHITE);
                    Enemy curr = createEnemy(enemyTypeArr[i], xArr[i], yArr[i], iv, health);
                    health.setText(curr.getHealth() + "");
                    moveEnemy(curr, iv, health, enemyTypeArr[i]);
                }
            }
        });

        placeT[0] = findViewById(R.id.towerOneB);
        placeT[1] = findViewById(R.id.towerTwoB);
        placeT[2] = findViewById(R.id.towerThreeB);
        for (int i = 0; i < numT; i++) {
            placeT[i].setEnabled(false);
            if (player.getTowerInv(i) > 0) {
                placeT[i].setEnabled(true);
            }
        }

        // runs generateMoney() every 10 seconds (initial 5 second delay)
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                generateMoney();
                handler.postDelayed(this, 10000);
            }
        };
        handler.postDelayed(runnable, 5000);

        for (int i = 0; i < numT; i++) {
            int finalI = i;
            placeT[i].setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onClick(View view) {
                    enableAllFalse();
                    if (player.getTowerInv(finalI) > 0) {
                        placed[finalI] = true;
                        areaLayout.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN
                                        && placed[finalI]) {
                                    placed[finalI] = false;
                                    int x = (int) motionEvent.getX();  // get x-Coordinate
                                    int y = (int) motionEvent.getY();  // get y-Coordinate
                                    if (player.placeTower(finalI, x, y)) {
                                        RelativeLayout.LayoutParams param = createParam();
                                        ImageView iv = new ImageView(getApplicationContext());
                                        // Bee image size: 100x90 (100 is width, 90 is height)
                                        param.setMargins(x, y, 0, 0);
                                        iv.setLayoutParams(param);
                                        iv.getLayoutParams().width = 100;
                                        iv.getLayoutParams().height = 100;
                                        iv.requestLayout();
                                        if (finalI == 0) {
                                            iv.setImageResource(R.drawable.bee_tower_default);
                                        } else if (finalI == 1) {
                                            iv.setImageResource(R.drawable.heart_tower_default);
                                        } else if (finalI == 2) {
                                            iv.setImageResource(R.drawable.coin_tower_default);
                                        }
                                        areaLayout.addView(iv);
                                        setValues();
                                        monumentHealthTower(finalI);
                                        gameActivityViewModel.addTower(finalI, x, y);
                                    } else {
                                        alertPath();
                                    }
                                    updateEnabled(player);
                                }
                                return true;
                            }
                        });
                    }
                }
            });
        }
    }

    private void generateMoney() {
        for (int i = 0; i < gameActivityViewModel.getListOfTower().size(); i++) {
            if (gameActivityViewModel.getListOfTower().get(i) instanceof WaspTower) {
                WaspTower t = (WaspTower) gameActivityViewModel.getListOfTower().get(i);
                RelativeLayout.LayoutParams param = createParam();
                ImageView iv = new ImageView(getApplicationContext());
                int randX = t.getLocationX() + 80 + randInt(-25, 25);
                int randY = t.getLocationY() + 80 + randInt(-25, 25);
                param.setMargins(randX, randY, 0, 0);
                iv.setLayoutParams(param);
                iv.getLayoutParams().width = 50;
                iv.getLayoutParams().height = 50;
                iv.setImageResource(R.drawable.coin);
                areaLayout.addView(iv);
                iv.requestLayout();
                Coin c = new Coin(randX, randY);
                gameActivityViewModel.addCoin(c);
                iv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        player.setMoney(player.getMoney() + c.getValue());
                        setValues();
                        iv.setVisibility(View.GONE);
                        gameActivityViewModel.getListOfCoin().remove(c);
                    }
                });
            }
        }

    }

    //increment player's monument health by 20 if place a BeeTower
    private void monumentHealthTower(int towerNum) {
        healthView = findViewById(R.id.hpV);
        if (towerNum == 1) {
            int hpInt = hive.getHealth();
            hpInt += 20;
            hive.setHealth(hpInt);
            healthView.setText(Integer.toString(hpInt));
        } else {
            healthView.setText(Integer.toString(hive.getHealth()));
        }
    }

    private void configViews() {
        moneyView = findViewById(R.id.moneyV);
        healthView = findViewById(R.id.hpV);
        towerOneView = findViewById(R.id.towerOneV);
        towerTwoView = findViewById(R.id.towerTwoV);
        towerThreeView = findViewById(R.id.towerThreeV);
        setValues();


        ImageButton menuButton = findViewById(R.id.towerMenuB);

        // Add event listeners for button
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(menuButton.getContext(), view);
                popupMenu.inflate(R.menu.popup_buytower);

                MenuItem item;
                item = popupMenu.getMenu().findItem(R.id.HornetBuyTower);
                item.setTitle("Buy Bee Tower $" + player.getTowerCost(0));
                if (player.getMoney() < player.getTowerCost(0)) {
                    item.setEnabled(false);
                }
                item = popupMenu.getMenu().findItem(R.id.HornetUpgradeTower);
                item.setTitle("Upgrade Bee Tower $" + player.getTowerUpgradeCost(0));
                if (player.getMoney() < player.getTowerUpgradeCost(0) || gameActivityViewModel.getListOfHornetTower().size() == usedHornetTowers.size()) {
                    item.setEnabled(false);
                }
                item = popupMenu.getMenu().findItem(R.id.BeeBuyTower);
                item.setTitle("Buy Heart Tower $" + player.getTowerCost(1));
                if (player.getMoney() < player.getTowerCost(1)) {
                    item.setEnabled(false);
                }
                item = popupMenu.getMenu().findItem(R.id.BeeUpgradeTower);
                item.setTitle("Upgrade Heart Tower $" + player.getTowerUpgradeCost(1));
                if (player.getMoney() < player.getTowerUpgradeCost(1) || gameActivityViewModel.getListOfBeeTower().size() == usedBeeTowers.size()) {
                    item.setEnabled(false);
                }
                item = popupMenu.getMenu().findItem(R.id.WaspBuyTower);
                item.setTitle("Buy Coin Tower $" + player.getTowerCost(2));
                if (player.getMoney() < player.getTowerCost(2)) {
                    item.setEnabled(false);
                }
                item = popupMenu.getMenu().findItem(R.id.WaspUpgradeTower);
                item.setTitle("Upgrade Coin Tower $" + player.getTowerUpgradeCost(2));
                if (player.getMoney() < player.getTowerUpgradeCost(2) || gameActivityViewModel.getListOfWaspTower().size() == usedWaspTowers.size()) {
                    item.setEnabled(false);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        int idToBuy = -1;
                        int idToUpgrade = -1;
                        switch (id) {
                            case(R.id.HornetBuyTower):
                                idToBuy = 0;
                                purchaseCount += 1;
                                break;
                            case(R.id.HornetUpgradeTower):
                                idToUpgrade = 0;
                                upgradeCount += 1;
                                player.setMoney(player.getMoney() - player.getTowerUpgradeCost(0));
                                break;
                            case(R.id.BeeBuyTower):
                                idToBuy = 1;
                                purchaseCount += 1;
                                break;
                            case(R.id.BeeUpgradeTower):
                                idToUpgrade = 1;
                                upgradeCount += 1;
                                player.setMoney(player.getMoney() - player.getTowerUpgradeCost(1));
                                //increment player's monument health by 25 if upgrade a BeeTower
                                healthView = findViewById(R.id.hpV);
                                int hpInt = hive.getHealth();
                                hpInt += 25;
                                hive.setHealth(hpInt);
                                healthView.setText(Integer.toString(hpInt));
                                break;
                            case(R.id.WaspBuyTower):
                                idToBuy = 2;
                                purchaseCount += 1;
                                break;
                            case(R.id.WaspUpgradeTower):
                                idToUpgrade = 2;
                                upgradeCount += 1;
                                player.setMoney(player.getMoney() - player.getTowerUpgradeCost(2));
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid item to buy.");
                        }
                        if (idToBuy != -1) {
                            player.buyTower(idToBuy);
                        }
                        if (idToUpgrade != -1) {
                            upgradeTower(idToUpgrade);
                        }
                        setValues();
                        updateEnabled(player);
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

    }

    // Update the health, money, number of towers available on UI elements
    private void setValues() {
        Player player = InitialConfiguration.getPlayer();
        healthView.setText(Integer.toString(hive.getHealth()));
        if (killCount < 8) {
            moneyView.setText("$" + player.getMoney());
        }
        towerOneView.setText(Integer.toString(player.getTowerInv(0)));
        towerTwoView.setText(Integer.toString(player.getTowerInv(1)));
        towerThreeView.setText(Integer.toString(player.getTowerInv(2)));
    }

    // Disable all buttons used to place towers
    private void enableAllFalse() {
        for (int i = 0; i < numT; i++) {
            placeT[i].setEnabled(false);
        }
    }

    private void updateEnabled(Player player) {
        for (int i = 0; i < numT; i++) {
            if (player.getTowerInv(i) > 0) {
                placeT[i].setEnabled(true);
            }
        }
    }

    private void alertPath() {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(GameActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Invalid Tower Placement");
        builder.setMessage("You placed the tower on the path."
                + " Try again.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void alertFinalBoss() {
        AlertDialog.Builder test = new AlertDialog.Builder(GameActivity.this);
        test.setTitle("Final Boss");
        test.setCancelable(true);
        test.setMessage("The final boss is about to come! Add as many towers as you can before he attacks your hive!");
        AlertDialog testDialog = test.create();
        testDialog.show();  // to show
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                testDialog.dismiss();  // to dismiss
            }
        }, 5000);
    }

    private RelativeLayout.LayoutParams createParam() {
        RelativeLayout.LayoutParams param =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        return param;
    }

    private void startHive() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for (Enemy curr: gameActivityViewModel.getListOfEnemyMonument()) {
                    if (curr.getAlive()) {
                        if (hive.getHealth() - curr.getDamage() / 50 < 0) {
                            hive.setHealth(0);
                        } else {
                            hive.setHealth(hive.getHealth() - curr.getDamage() / 50);
                        }
                        healthView.setText(Integer.toString(hive.getHealth()));
                    }
                }
                if (hive.getHealth() <= 0 && !gameOver) {
                    gameOver = true;
                    // for game over screen statistics
                    TextView tv = (TextView)findViewById(R.id.moneyV);
                    String finalMoney = tv.getText().toString();
                    String finalTower = Integer.toString(purchaseCount);
                    String finalUpgrade = Integer.toString(upgradeCount);
                    Intent intention = new Intent(GameActivity.this, GameOverActivity.class);
                    intention.putExtra("finalMoney", finalMoney);
                    intention.putExtra("finalTower", finalTower);
                    intention.putExtra("finalUpgrade", finalUpgrade);
                    startActivity(intention);
                } else {
                    startHive();
                }
            }
        }, 1000);
    }

    private void moveView(Enemy curr, View iv, int rand, int rand2, int rand3, boolean isText, int enemyType) {
        //Move along first part of path
        final int[] moveX = new int[1];
        moveX[0] = 100;

        final ObjectAnimator[] animArr = new ObjectAnimator[3];
        animArr[0] = ObjectAnimator.ofFloat(iv, "translationX", moveX[0]);
        animArr[0].setDuration(1000);

        animArr[0].addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anima) {
                int[] location = new int[2];
                iv.getLocationOnScreen(location);
                curr.setLocationX(location[0]);
                curr.setLocationX(location[1]);
                if (isText) {
                    ((TextView) iv).setText(decreaseHealth(curr, location[0], location[1]) + "");
                }
                if (isHealthZero(curr)) {
                    killEnemy(curr, iv);
                }
                if (enemyType == 3) {
                    if (location[0] > 1070) {
                        animArr[1].start();
                    } else {
                        moveX[0] += rand;
                        animArr[0].setFloatValues(moveX[0]);
                        animArr[0].start();
                    }
                } else {
                    if (location[0] > 1150) {
                        animArr[1].start();
                    } else {
                        moveX[0] += rand;
                        animArr[0].setFloatValues(moveX[0]);
                        animArr[0].start();
                    }
                }
            }
        });
        animArr[0].start();

        final int[] moveY = new int[1];
        moveY[0] = 100;

        animArr[1] = ObjectAnimator.ofFloat(iv, "translationY", moveY[0]);
        animArr[1].setDuration(600);
        animArr[1].addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anima) {
                int[] location = new int[2];
                iv.getLocationOnScreen(location);
                curr.setLocationX(location[0]);
                curr.setLocationX(location[1]);
                if (isText) {
                    ((TextView) iv).setText(decreaseHealth(curr, location[0], location[1]) + "");
                }
                if (isHealthZero(curr)) {
                    killEnemy(curr, iv);
                }
                if (enemyType == 3) {
                    if (location[1] > 700) {
                        moveX[0] = 1200;
                        animArr[2].start();
                    } else {
                        moveY[0] += rand2;
                        animArr[1].setFloatValues(moveY[0]);
                        animArr[1].start();
                    }
                } else {
                    if (location[1] > 800) {
                        moveX[0] = 1200;
                        animArr[2].start();
                    } else {
                        moveY[0] += rand2;
                        animArr[1].setFloatValues(moveY[0]);
                        animArr[1].start();
                    }
                }
            }
        });

        animArr[2] = ObjectAnimator.ofFloat(iv, "translationX", 1100);
        animArr[2].setDuration(1000);
        animArr[2].addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anima) {
                int[] location = new int[2];
                iv.getLocationOnScreen(location);
                curr.setLocationX(location[0]);
                curr.setLocationX(location[1]);
                if (isText) {
                    ((TextView) iv).setText(decreaseHealth(curr, location[0], location[1]) + "");
                }
                if (isHealthZero(curr)) {
                    killEnemy(curr, iv);
                }
                if (location[0] > 2000) {
                    iv.clearAnimation();
                    gameActivityViewModel.addEnemyMonument(curr);
                } else {
                    moveX[0] += rand3;
                    animArr[2].setFloatValues(moveX[0]);
                    animArr[2].start();
                }
            }
        });
    }

    private void moveEnemy(Enemy curr, ImageView image, TextView text, int enemyType) {
        int randomInt = randInt(70, 130);
        int randomInt2 = randInt(30, 70);
        int randomInt3 = randInt(70, 130);
        moveView(curr, image, randomInt, randomInt2, randomInt3, false, enemyType);
        moveView(curr, text, randomInt, randomInt2, randomInt3, true, enemyType);
    }

    public Enemy createEnemy(int enemy, int x, int y, ImageView iv, TextView health) {
        RelativeLayout.LayoutParams param = createParam();
        RelativeLayout.LayoutParams param2 = createParam();
        param.setMargins(x, y, 0, 0);
        iv.setLayoutParams(param);
        if (enemy == 3) {
            iv.getLayoutParams().width = 250;
            iv.getLayoutParams().height = 250;
        } else {
            iv.getLayoutParams().width = 100;
            iv.getLayoutParams().height = 100;
        }
        iv.requestLayout();
        param2.setMargins(x, y - 25, 0, 0);
        health.setLayoutParams(param2);
        health.getLayoutParams().width = 100;
        health.getLayoutParams().height = 100;
        health.requestLayout();
        if (enemy == 0) {
            iv.setImageResource(R.drawable.purple);
        } else if (enemy == 1) {
            iv.setImageResource(R.drawable.blue);
        } else if (enemy == 2) {
            iv.setImageResource(R.drawable.green);
        } else if (enemy == 3) {
            iv.setImageResource(R.drawable.boss);

        } else {
            throw new java.lang.IllegalArgumentException("Invalid enemy type."
                    + "We only have 3 types of enemies.");
        }
        areaLayout.addView(iv);
        areaLayout.addView(health);
        gameActivityViewModel.addEnemy(enemy, x, y);
        return gameActivityViewModel.getListOfEnemy().peekLast();
    }

    public void killEnemy(Enemy enemy, View iv) {
        areaLayout.removeView(iv);
        if (enemy.getAlive()) {
            enemy.setAlive(false);
            killCount++;
        }
        if (killCount == 8 && !killedEnemies) {
            killedEnemies = true;
            finalBoss();
        }
        if (killCount == 9 && !gameOver) {
            gameOver = true;
            // for win screen statistics
            TextView tv = (TextView)findViewById(R.id.moneyV);
            String finalMoney = tv.getText().toString();
            String finalTower = Integer.toString(purchaseCount);
            String finalUpgrade = Integer.toString(upgradeCount);
            Intent intention = new Intent(GameActivity.this, WinGameActivity.class);
            intention.putExtra("finalMoney", finalMoney);
            intention.putExtra("finalTower", finalTower);
            intention.putExtra("finalUpgrade", finalUpgrade);
            startActivity(intention);
        }
    }

    public void finalBoss () {
        alertFinalBoss();
        player.setMoney(100000);
        moneyView.setText("No limit");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                RelativeLayout.LayoutParams param = createParam();
                ImageView iv = new ImageView(getApplicationContext());
                TextView health = new TextView(getApplicationContext());
                health.setTextColor(Color.WHITE);
                Enemy curr = createEnemy(3, 30, 230, iv, health);
                health.setText(curr.getHealth() + "");
                moveEnemy(curr, iv, health, 3);
            }
        }, 10000);
    }

    public int decreaseHealth(Enemy enemy, int enemyX, int enemyY) {
        LinkedList<Tower> list = gameActivityViewModel.getListOfHornetTower();
        for (int i = 0; i < list.size(); i++) {
            if (usedTowers.contains(i)) {
                continue;
            }
            int towerX = list.get(i).getLocationX();
            int towerY = list.get(i).getLocationY();
            //Log.i("", "Enemy x: " + enemyX + "Enemy y: " + enemyY);

            if (Math.abs(enemyY - towerY) < 200 && Math.abs(enemyX - towerX) < 100) {
                int newHealth;
                if (list.get(i).getUpgraded()) {
                    newHealth = enemy.getHealth() - 25;
                } else {
                    newHealth = enemy.getHealth() - 15;
                }
                enemy.setHealth(newHealth);
                //usedTowers.add(i);
                return newHealth;
            }
        }
        return enemy.getHealth();
    }

    public void upgradeTower(int id) {
        LinkedList<Tower> list;
        if (id == 0) {
            list = gameActivityViewModel.getListOfHornetTower();
        } else if (id == 1) {
            list = gameActivityViewModel.getListOfBeeTower();
        } else if (id == 2) {
            list = gameActivityViewModel.getListOfWaspTower();
        } else {
            list = gameActivityViewModel.getListOfWaspTower();
        }
        for (int i = 0; i < list.size(); i++) {
            if (id == 0 && usedHornetTowers.contains(i)) {
                continue;
            } else if (id == 1 && usedBeeTowers.contains(i)) {
                continue;
            } else if (id == 2 && usedWaspTowers.contains(i)) {
                continue;
            }
            int x = list.get(i).getLocationX();
            int y = list.get(i).getLocationY();
            RelativeLayout.LayoutParams param = createParam();
            ImageView iv = new ImageView(getApplicationContext());
            // Bee image size: 100x90 (100 is width, 90 is height)
            param.setMargins(x, y, 0, 0);
            iv.setLayoutParams(param);
            iv.getLayoutParams().width = 100;
            iv.getLayoutParams().height = 100;
            iv.requestLayout();
            if (id == 0) {
                iv.setImageResource(R.drawable.bee_tower_upgraded);
                usedHornetTowers.add(i);
                gameActivityViewModel.getListOfHornetTower().get(i).setUpgraded(true);
            } else if (id == 1) {
                iv.setImageResource(R.drawable.heart_tower_upgraded);
                usedBeeTowers.add(i);
                gameActivityViewModel.getListOfBeeTower().get(i).setUpgraded(true);
            } else if (id == 2) {
                iv.setImageResource(R.drawable.coin_tower_upgraded);
                usedWaspTowers.add(i);
                gameActivityViewModel.getListOfWaspTower().get(i).setUpgraded(true);
            }
            areaLayout.addView(iv);
            break;
        }
    }

    public boolean isHealthZero(Enemy enemy) {
        if (enemy.getHealth() <= 0) {
            return true;
        }
        return false;
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

}
