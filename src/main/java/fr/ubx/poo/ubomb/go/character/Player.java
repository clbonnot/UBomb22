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
import fr.ubx.poo.ubomb.go.decor.Door;
import fr.ubx.poo.ubomb.go.decor.bonus.*;

public class Player extends GameCharacter implements Movable, TakeVisitor {

    private Direction direction;
    private boolean moveRequested = false;
    private int lives;
    private int nbKeys;
    private int bombRange;
    private int bombNumber;
    private final int bombBagCapacity;
    private final long invisibilyTime;
    public Player(Game game, Position position) {
        super(game, position);
        this.direction = Direction.DOWN;
        this.lives = game.configuration().playerLives();
        nbKeys = 0;
        bombRange = 1;
        bombNumber = 0;
        bombBagCapacity = game.configuration().bombBagCapacity();
        invisibilyTime = game.configuration().playerInvisibilityTime();
    }


    @Override
    public void take(Bonus bonus) {
        switch (bonus.toString()) {
            case "Key":
                nbKeys++;
                bonus.remove();
                break;
            case "Heart":
                lives++;
                bonus.remove();
                break;
            case "bombRangInc":
                this.bombRange++;
                bonus.remove();
                break;
            case "bombRangDec":
                if (this.bombRange > 0) {
                    this.bombRange--;
                    bonus.remove();
                }
                break;
            case "bombNumberInc":
                if(this.bombBagCapacity >bombNumber ) {
                    this.bombNumber++;
                    bonus.remove();
                }
                break;
            case "bombNumberDec":
                if (this.bombNumber > 0) {
                    this.bombNumber--;
                    bonus.remove();
                }
                break;
            default:
                System.out.println("error taking bonus");
        }
    }

    public void doMove(Direction direction) {
        // This method is called only if the move is possible, do not check again
        Position nextPos = direction.nextPosition(getPosition());
        GameObject next = game.grid().get(nextPos);
        if (next instanceof Bonus bonus)
            bonus.takenBy(this);
        if(next instanceof Door door)
            door.isTaken(this);
        if(next instanceof Box) {
            ((Box) next).doMove(direction);
            game.grid().set(direction.nextPosition(nextPos),game.grid().get(nextPos) );
            game.grid().remove(nextPos);
        }
        setPosition(nextPos);
        for (Monster m : game.monsters().get(game.getCurrentLevel() - 1)) {
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
        if(d instanceof Door door)
            if(!door.isOpened())
                System.out.println("La porte est fermée, trouve une clé pour l'ouvrir");
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

    public void takeDoor(boolean next) {
        int numLevel = game.changeFloor(next);
        System.out.println("Niveau " + numLevel);
    }
    public int getBombRange() {
        return bombRange;
    }
    public int getBombNumber() {
        return bombNumber;
    }
    public void setBombNumber(int nbBomb) {
        this.bombNumber = nbBomb;
    }
    public long getInvisibilityTime(){
        return invisibilyTime;
    }
}
