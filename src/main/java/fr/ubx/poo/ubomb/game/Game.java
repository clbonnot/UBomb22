package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.character.Princess;
import fr.ubx.poo.ubomb.go.decor.Decor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Game {
    private final int NB_MONSTER = 5;
    private final Configuration configuration;
    private final Player player;
    private final Princess princess;
    private final Monster[] monsters = new Monster[NB_MONSTER];
    private final Grid grid;


    public Game(Configuration configuration, Grid grid) {
        this.configuration = configuration;
        this.grid = grid;
        player = new Player(this, configuration.playerPosition());
        princess = new Princess(this, new Position(3,3));
        generateMonster();
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


    private void generateMonster() {
        int m = 0;
        while(NB_MONSTER > m) {
            Position pos = new Position(new Random().nextInt(grid.width()), new Random().nextInt(grid.height()));
            Decor d = grid().get(pos);
            Monster monster = new Monster(this, pos);
            if(grid().inside(pos) && (d == null || d.walkableBy(monster))) {
                monsters[m] = monster;
                m++;
            }

        }
    }
}
