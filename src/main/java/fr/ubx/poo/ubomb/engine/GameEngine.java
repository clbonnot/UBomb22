/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.engine;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.character.Princess;
import fr.ubx.poo.ubomb.go.decor.Bomb;
import fr.ubx.poo.ubomb.go.decor.Door;
import fr.ubx.poo.ubomb.go.decor.bonus.Key;
import fr.ubx.poo.ubomb.view.*;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;


public final class GameEngine {

    private static AnimationTimer gameLoop;
    private final Game game;
    private final Player player;
    private final Princess princess;
    private final List<Monster[]> monsters;
    private final ArrayList<Bomb> bombs = new ArrayList<>();
    private final HashMap<Bomb, Integer> bombsLevel = new HashMap<>();
    private final List<Sprite> sprites = new LinkedList<>();
    private final Set<Sprite> cleanUpSprites = new HashSet<>();
    private final Stage stage;
    private StatusBar statusBar;
    private Pane layer;
    private Input input;
    private int currentLevel = 1;

    public GameEngine(Game game, final Stage stage) {
        this.stage = stage;
        this.game = game;
        this.player = game.player();
        this.princess = game.princess();
        this.monsters = game.monsters();
        initialize();
        buildAndSetGameLoop();
    }

    private void initialize() {
        Group root = new Group();
        layer = new Pane();

        int height = game.grid().height();
        int width = game.grid().width();
        int sceneWidth = width * ImageResource.size;
        int sceneHeight = height * ImageResource.size;
        Scene scene = new Scene(root, sceneWidth, sceneHeight + StatusBar.height);
        scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.hide();
        stage.show();

        input = new Input(scene);
        root.getChildren().add(layer);
        statusBar = new StatusBar(root, sceneWidth, sceneHeight, game);

        // Create sprites
        for (var decor : game.grid().values()) {
            if (decor instanceof Door d) sprites.add(new SpriteDoor(layer, d));
            else sprites.add(SpriteFactory.create(layer, decor));
            decor.setModified(true);
        }

        if (game.getCurrentLevel() == 1)
            sprites.add(new SpritePlayer(layer, player));
        if (game.getCurrentLevel() == game.getNbLevel())
            sprites.add(new SpritePrincess(layer, princess));
        for (Monster m : monsters.get(0)) {
            sprites.add(new SpriteMonster(layer, m));
        }

    }

    void buildAndSetGameLoop() {
        gameLoop = new AnimationTimer() {
            public void handle(long now) {
                // Check keyboard actions
                processInput(now);
                // Do actions
                update(now);
                checkCollision(now);
                checkExplosions();
                // Graphic update
                cleanupSprites();
                render();
                statusBar.update(game);
            }
        };
    }

    private void checkExplosions() {
        // Check explosions of bombs
    }

    private void animateExplosion(Position src, Position dst) {
        ImageView explosion = new ImageView(ImageResource.EXPLOSION.getImage());
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), explosion);
        tt.setFromX(src.x() * Sprite.size);
        tt.setFromY(src.y() * Sprite.size);
        tt.setToX(dst.x() * Sprite.size);
        tt.setToY(dst.y() * Sprite.size);
        tt.setOnFinished(e -> {
            layer.getChildren().remove(explosion);
        });
        layer.getChildren().add(explosion);
        tt.play();
    }

    private void createNewBombs(long now) {
        Bomb bomb = new Bomb(player.getPosition());
        game.grid().set(player.getPosition(), bomb);
        sprites.add(new SpriteBomb(layer, bomb));
        bombs.add(bomb);
        bombsLevel.put(bomb, currentLevel);
        bomb.updateBomb();
    }

    private void checkCollision(long now) {
        // Check a collision between a monster and the player
    }

    private void processInput(long now) {
        if (input.isExit()) {
            gameLoop.stop();
            Platform.exit();
            System.exit(0);
        } else if (input.isMoveDown()) {
            player.requestMove(Direction.DOWN);
        } else if (input.isMoveLeft()) {
            player.requestMove(Direction.LEFT);
        } else if (input.isMoveRight()) {
            player.requestMove(Direction.RIGHT);
        } else if (input.isMoveUp()) {
            player.requestMove(Direction.UP);
        } else if (input.isKey()) {
            openDoor();
        } else if (input.isBomb()) {
            if (player.getBombNumber() > 0) {
                createNewBombs(now);
                player.setBombNumber(player.getBombNumber() - 1);
            }
        }
        input.clear();
    }

    private void openDoor() {
        var d = game.OpenIfDoorClosed(player.getNbKeys() > 0);
        if (d == null) return;
        for (Sprite sprite : sprites) {
            if (sprite.getGameObject() instanceof Door door) {
                if (door.equals(d)) {
                    sprite.remove();
                    sprites.add(SpriteFactory.create(layer, door));
                    player.removeKey();
                    return;
                }
            }
        }

    }

    private void showMessage(String msg, Color color) {
        Text waitingForKey = new Text(msg);
        waitingForKey.setTextAlignment(TextAlignment.CENTER);
        waitingForKey.setFont(new Font(60));
        waitingForKey.setFill(color);
        StackPane root = new StackPane();
        root.getChildren().add(waitingForKey);
        Scene scene = new Scene(root, 400, 200, Color.WHITE);
        stage.setScene(scene);
        input = new Input(scene);
        stage.show();
        new AnimationTimer() {
            public void handle(long now) {
                processInput(now);
            }
        }.start();
    }

    private void update(long now) {
        player.update(now);
        int level = 1;
        for (Monster[] monsters1 : monsters) {
            for (Monster m : monsters1) {
                if (!m.getTimer().isRunning()) {
                    m.update(now);
                    m.getTimer().start(60 / game.getMonsterVelocity(level) * 1000);
                    if (monsters.indexOf(monsters1) + 1 != currentLevel) m.setModified(false);
                }
                m.getTimerInvincibility().update(now);
                if (!m.getTimerInvincibility().isRunning()) {
                    m.setInvincibility(false);
                }
                level++;
            }
        }

        // Si on change de niveau (le joueur a traversé une porte)
        if (currentLevel != game.getCurrentLevel()) {
            for (Sprite sprite : sprites) {
                sprite.remove();
            }
            player.setPosition(game.getDoorPosition(game.getCurrentLevel() < currentLevel));
            for (var decor : game.grid().values()) {
                sprites.add(SpriteFactory.create(layer, decor));
                decor.setModified(true);
            }
            currentLevel = game.getCurrentLevel();
            for (Monster m : monsters.get(currentLevel - 1)) {
                sprites.add(new SpriteMonster(layer, m));
                m.setModified(true);
            }
        }
        for (Iterator<Bomb> it = bombs.iterator(); it.hasNext(); ) {
            Bomb b = it.next();
            b.getTimer().update(now);
            if (!b.getTimer().isRunning()) {
                b.remove();
                it.remove();
                makeExplosion(b);
                bombsLevel.remove(b);
            } else if(bombsLevel.get(b) == currentLevel){
                b.updateBomb();
            }

        }

        if (player.getPosition().equals(game.princess().getPosition()) && currentLevel == game.getNbLevel()) {
            gameLoop.stop();
            showMessage("Gagné!", Color.GREEN);
        }
        // Si le joueur n'a plus de vie
        if (player.getLives() < 0) {
            gameLoop.stop();
            showMessage("Perdu!", Color.RED);
        }
    }

    public void cleanupSprites() {
        sprites.forEach(sprite -> {
            if (sprite.getGameObject().isDeleted()) {
                game.grid().remove(sprite.getPosition());
                cleanUpSprites.add(sprite);
            }
        });
        cleanUpSprites.forEach(Sprite::remove);
        sprites.removeAll(cleanUpSprites);
        cleanUpSprites.clear();
    }

    private void render() {
        sprites.forEach(Sprite::render);
    }

    public void start() {
        gameLoop.start();
    }

    public void makeExplosion(Bomb b) {
        Position p = b.getPosition();
        int range = player.getBombRange();
        if (p.equals(player.getPosition())) {
            player.removeLives();
        }
        for (Direction d : Direction.values()) {
            Position pos = p;
            boolean stop = false;
            for (int i = 0; i < range; i++) {
                if (!stop) {
                    pos = d.nextPosition(pos);
                    int bombLevel = bombsLevel.get(b);
                    GameObject next = game.getGridLevel(bombLevel).get(pos);
                    if (pos.equals(player.getPosition()) && !player.isInvincibility()) {
                        player.removeLives();
                        player.setInvincibility(true);
                        player.getTimerInvincibility().start();
                    }
                    if (next != null) {
                        stop = true;
                        if (!(next instanceof Key || next instanceof Bomb || next instanceof Door)) {
                            if(currentLevel == bombLevel)
                                animateExplosion(p, pos);
                            next.remove();
                        }
                    }
                    for (Monster m : monsters.get(currentLevel - 1)) {
                        if(m.getPosition().equals(pos) && !m.isInvincibility()) {
                            if(m.getLives() > 1) {
                                m.removeLive();
                                m.setInvincibility(true);
                                m.getTimerInvincibility().start();
                            }
                            else {
                                m.remove();
                            }
                        }
                        m.setModified(true);
                    }
                }
            }
        }
    }
}