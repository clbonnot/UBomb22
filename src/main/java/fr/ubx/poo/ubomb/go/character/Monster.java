package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.decor.Decor;

public class Monster extends GameCharacter {

    private int lives;

    public Monster(Game game, Position position, int lives) {
        super(game, position);
        this.lives = lives;
    }

    public void update(long now) {
        boolean move = false;
        while(!move) {
            Direction d = Direction.random();
            if (canMove(d)) {
                doMove(d);
                move = true;
            }
        }

    }

    public final boolean canMove(Direction direction) {
        Position next = direction.nextPosition(getPosition());
        Decor d = game.grid().get(next);
        return game.grid().inside(next) && (d == null || d.walkableBy(this));
    }

    public void doMove(Direction direction) {
        Position nextPos = direction.nextPosition(getPosition());
        setPosition(nextPos);
        if(nextPos.equals(game.player().getPosition())) {
            game.player().removeLives();
        }
    }

    public boolean isInvincibility() {
        return invincibility;
    }
    public void setInvincibility(boolean choice){
        invincibility = choice;
    }

    public int getLives() {
        return lives;
    }
}
