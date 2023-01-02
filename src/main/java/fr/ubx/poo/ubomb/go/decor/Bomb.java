package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.engine.Timer;
import fr.ubx.poo.ubomb.game.Position;

public class Bomb extends Decor {
    private final Timer timer;


    public Bomb(Position position) {
        super(position);
        timer = new Timer(3*1000);
        timer.start();
    }

    public Timer getTimer() {
        return timer;
    }

    public void updateBomb() {
        setModified(true);
    }
}
