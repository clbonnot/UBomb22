package fr.ubx.poo.ubomb.go.decor.bonus;

import fr.ubx.poo.ubomb.game.Position;
import fr.ubx.poo.ubomb.go.character.Player;

public class BombRange extends Bonus {
    boolean incrementation;
    public BombRange(Position position, boolean incrementation) {
        super(position);
        this.incrementation = incrementation;
    }

    @Override
    public void explode() {}

    public boolean getIncrementation() {
        return incrementation;
    }
    @Override
    public void takenBy(Player player) {
        player.take(this);
    }

    @Override
    public String toString() {
        if(incrementation) {
            return "bombRangInc";
        }
        else {
            return "bombRangDec";
        }
    }
}
