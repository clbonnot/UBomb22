package fr.ubx.poo.ubomb.go.character;

import fr.ubx.poo.ubomb.game.Direction;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.GameObject;

public class Monster extends GameObject {
    private Direction direction;
    public Monster(Game game, Position position) {
        super(game, position);
        direction = Direction.random();
    }
    public Direction getDirection() {
        return direction;
    }

}
