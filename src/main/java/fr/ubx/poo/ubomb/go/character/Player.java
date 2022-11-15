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
import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.go.decor.bonus.*;

public class Player extends GameCharacter implements Movable, TakeVisitor {

    private boolean moveRequested = false;
    private int lives;
    private int nbKeys;

    public Player(Game game, Position position) {
        super(game, position);
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
        for (Monster m : game.monsters()) {
            if(nextPos.equals(m.getPosition())) {
                lives--;
            }
        }
        setPosition(nextPos);
    }


    public int getLives() {
        return lives;
    }



    public void requestMove(Direction direction) {
        if (direction != getDirection()) {
            setDirection(direction);
            setModified(true);
        }
        moveRequested = true;
    }

    public final boolean canMove(Direction direction) {
        Position next = direction.nextPosition(getPosition());
        Decor d = game.grid().get(next);
        return game.grid().inside(next) && (d == null || d.walkableBy(game.player()));
    }

    public void update(long now) {
        if (moveRequested) {
            if (canMove(getDirection())) {
                doMove(getDirection());
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
