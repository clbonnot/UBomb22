package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.GameCharacter;
import fr.ubx.poo.ubomb.go.character.Player;

public class Door extends Decor{

    private final boolean next;
    private boolean opened;

    public Door(Position position, boolean next, boolean opened) {
        super(position);
        this.next = next;
        this.opened = opened;
    }

    @Override
    public boolean walkableBy(GameCharacter gc) {
        return isOpened() && gc instanceof Player;
    }

    public boolean isOpened(){
        return opened;
    }

    public boolean isNext(){
        return next;
    }

    public void isTaken(Player player) {
        player.takeDoor(next);

    }
    public void openDoor(){
        opened = true;
    }
}
