package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;

public abstract class GameCharacter extends GameObject {
    private Direction direction;
    private Timer timerMove;

    public GameCharacter(Game game, Position position) {
        super(game,position);
        direction = Direction.random();
        timerMove = new Timer(60/ game.getMonsterVelocity()*1000);
        timerMove.start();
    }
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction d) {
        this.direction = d;

    }
    public Timer getTimer() {
        return timerMove;
    }
}
