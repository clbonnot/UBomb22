package fr.ubx.poo.ubomb.game;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;
import fr.ubx.poo.ubomb.go.character.Princess;
import fr.ubx.poo.ubomb.go.decor.Decor;
import fr.ubx.poo.ubomb.go.decor.Door;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Game {
    private final int NB_MONSTER = 5;

    private int monsterVelocity = 5;
    private final Configuration configuration;
    private final Player player;
    private final Princess princess;
    private final List<Monster[]> monsters = new ArrayList<>();
    private final List<Grid> grids;
    private Grid currentGridLevel;


    public Game(Configuration configuration, Grid grid) {
        this.configuration = configuration;

        List<Grid> grids = new ArrayList<>(); grids.add(grid);
        this.grids = grids;
        player = new Player(this, configuration.playerPosition());
        princess = new Princess(this, configuration.princessPosition());
        generateLevels(grids);
    }

    public Game(Configuration configuration, List<Grid> grids) {
        this.configuration = configuration;
        this.grids = grids;
        player = new Player(this, configuration.playerPosition());
        princess = new Princess(this, configuration.princessPosition());

        generateLevels(grids);
    }

    private void generateLevels(List<Grid> grids){
        for(int i = 1; i <= grids.size(); i++){
            setCurrentGridLevel(i);
            generateMonster();
        }
        setCurrentGridLevel(1);
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
        return currentGridLevel;
    }

    public Player player() {
        return this.player;
    }

    public Princess princess() {
        return this.princess;
    }

    public List<Monster[]> monsters() {
        return monsters;
    }


    private void generateMonster() {
        int m = 0;
        Monster[] currentGridMonsters = new Monster[NB_MONSTER];
        while(NB_MONSTER > m) {
            Position pos = new Position(new Random().nextInt(currentGridLevel.width()), new Random().nextInt(currentGridLevel.height()));
            Decor d = grid().get(pos);
            Monster monster = new Monster(this, pos);
            if(grid().inside(pos) && (d == null || d.walkableBy(monster))) {
                currentGridMonsters[m] = monster;
                m++;
            }
        }
        monsters.add(getCurrentLevel() - 1, currentGridMonsters);
    }
    public int getMonsterVelocity() {
        return monsterVelocity;
    }

    public void setMonsterVelocity(int monsterVelocity) {
        this.monsterVelocity = monsterVelocity;
    }

    public void setCurrentGridLevel(int level){
        currentGridLevel = grids.get(level - 1);
    }

    public int getCurrentLevel(){
        return grids.indexOf(currentGridLevel) + 1;
    }

    public int changeFloor(boolean up){
        int numLevel = getCurrentLevel();
        if(up && numLevel < grids.size())
            numLevel++;
        if(!up && numLevel > 1)
            numLevel--;
        setCurrentGridLevel(numLevel);
        return numLevel;
    }

    public int getNbLevel(){
        return grids.size();
    }

    public Grid getGridLevel(int level) {
        return grids.get(level - 1);
    }

    public Position getDoorPosition(boolean next) {
        for(Decor d : grid().values()){
            if(d instanceof Door door) {
                if(door.isNext() && next || !door.isNext() && !next)
                    return d.getPosition();
            }
        }
        return null;
    }

    public Decor OpenIfDoorClosed(boolean getKey) {
        Position p = player.getDirection().nextPosition(player.getPosition());
        if(p == null) return null;
        if(grid().get(p) instanceof Door d){
            if(!getKey) {
                System.out.println("Tu as besoin d'une clef pour ouvrir la porte");
                return null;
            }
            if(!d.isOpened()) {
                d.openDoor();
                d.setModified(true);
                System.out.println("Porte ouverte !");
                return d;
            } else System.out.println("La porte est déjà ouverte !");
        }

        return null;
    }
    public boolean hasCharacter(Position position) {
        for (Monster[] monsters1: monsters) {
            for(Monster m : monsters1) {
                if (monsters.indexOf(monsters1) + 1 != getCurrentLevel() && position.equals(m.getPosition())) {
                    return true;
                }
            }
        }
        return position.equals(princess.getPosition());
    }

}
