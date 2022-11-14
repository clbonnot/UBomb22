package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.character.Princess;

import java.util.LinkedList;
import java.util.List;

public class Game {

    private final Configuration configuration;
    private final Player player;
    private final Princess princess;
    private final Monster[] monsters = new Monster[5];
    private final Grid grid;

    public Game(Configuration configuration, Grid grid) {
        this.configuration = configuration;
        this.grid = grid;
        player = new Player(this, configuration.playerPosition());
        princess = new Princess(this, new Position(3,3));
        monsters[0] = new Monster(this, new Position(2,3));
    }

    public Configuration configuration() {
        return configuration;
    }

    // Returns the player, monsters and bomb at a given position
    public List<GameObject> getGameObjects(Position position) {
        List<GameObject> gos = new LinkedList<>();
        if (player().getPosition().equals(position))
            gos.add(player);
        if (princess().getPosition().equals(position))
            gos.add(princess);

        return gos;
    }

    public Grid grid() {
        return grid;
    }

    public Player player() {
        return this.player;
    }

    public Princess princess() {
        return this.princess;
    }

    public Monster[] monsters() {
        return this.monsters;
    }


}
