/*
 * Copyright (c) 2020. Laurent Réveillère
 */

package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.Movable;
import fr.ubx.poo.ubomb.go.TakeVisitor;
import fr.ubx.poo.ubomb.go.decor.Box;
import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.go.decor.bonus.*;

public class Player extends GameCharacter implements Movable, TakeVisitor {

    private Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private int nbKeys;

    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.lives = game.configuration().playerLives();
        nbKeys = 0;
    }


    @Override
    public void take(Key key) {
        nbKeys++;
        key.remove();
    }

    public void doMove(Direction direction) {
        // This method is called only if the move is possible, do not check again
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid().get(nextPos);
        if (next instanceof Bonus bonus) {
            bonus.takenBy(this);
        }
        if(next instanceof Box) {
            ((Box) next).doMove(direction);
            game.grid().set(direction.nextPosition(nextPos),game.grid().get(nextPos) );
            game.grid().remove(nextPos);
        }
        setPosition(nextPos);
        for (Monster m : game.monsters()) {
            if(nextPos.equals(m.getPosition())) {
                lives--;
            }
        }
    }


    public int getLives() {
        return lives;
    }

    public void removeLives() {
        lives--;
    }

    public Direction getDirection() {
        return direction;
    }

    public void requestMove(Direction direction) {
        if (direction != this.direction) {
            this.direction = direction;
            setModified(true);
        }
        moveRequested = true;
    }

    public final boolean canMove(Direction direction) {
        Position next = direction.nextPosition(getPosition());
        Decor d = game.grid().get(next);
        if(d instanceof Box) {
            return canMoveBox(direction, next);
        }
        return game.grid().inside(next) && (d == null || d.walkableBy(game.player()));
    }

    public final boolean canMoveBox(Direction direction, Position position) {
        Position next = direction.nextPosition(position);
        Decor d = game.grid().get(next);
        return game.grid().inside(next) && d == null && !game.hasCharacter(next);
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(direction)) {
                doMove(direction);
            }
        }
        moveRequested = false;
    }

    @Override
    public void explode() {
        // TODO
    }
    public int getNbKeys() {
        return nbKeys;
    }
}
