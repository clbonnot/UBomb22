package fr.ubx.poo.ubomb.launcher;

import fr.ubx.poo.ubomb.game.Configuration;
import fr.ubx.poo.ubomb.game.Position;

import java.io.*;
import java.util.*;

public class GetFileInfo {

    File file;
    Properties config = new Properties();
    char EOL = 'x';

    public GetFileInfo(File file){
        this.file = file;
        loadConfig();
    }

    private void loadConfig() throws MapException {
        try {
            Reader in = new FileReader(file);
            config.load(in);
        } catch (Exception e) {
            throw new MapException("Erreur lors du chargement de la map");
        }
    }

    private int integerProperty(Properties config, String name, int defaultValue) {
        return Integer.parseInt(config.getProperty(name, Integer.toString(defaultValue)));
    }

    public Configuration getConfigInfo() throws MapException {
        Position playerPos = getPlayerPosition();
        Position princessPos = getPrincessPosition();
        String[] configProperties = { "bombBagCapacity", "playerLives", "playerInvincibilityTime",
                "monsterVelocity", "monsterInvincibilityTime" };
        List<Integer> prop = new ArrayList<>();
        for(String key : configProperties){
            if(!config.containsKey(key)) throw new MapException("propriété de configuration manquante : " + key);
            prop.add(integerProperty(config, key, 0));
        }
        return new Configuration(playerPos, princessPos,prop.get(0), prop.get(1), prop.get(2), prop.get(3), prop.get(4));
    }

    private Position getPlayerPosition() {
        if(!config.containsKey("player")) {
            System.out.println("WARNING : Position de départ du joueur non précisée");
            return new Position(0,0);
        }
        String[] playerPos = config.getProperty("player").split("x");
        if(playerPos.length != 2 || !isNumeric(playerPos[0]) || !isNumeric(playerPos[1]))
            throw new MapException("propriété de configuration erronée : player");
        return new Position(Integer.parseInt(playerPos[0]), Integer.parseInt(playerPos[1]));
    }

    private Position getPrincessPosition() {
        if(!config.containsKey("princess")) {
            System.out.println("WARNING : Position de la princesse non précisée");
            return new Position(0,0);
        }
        String[] princessPos = config.getProperty("princess").split("x");
        if(princessPos.length != 2 || !isNumeric(princessPos[0]) || !isNumeric(princessPos[1]))
            throw new MapException("propriété de configuration erronée : player");
        return new Position(Integer.parseInt(princessPos[0]), Integer.parseInt(princessPos[1]));
    }

    public int getNbLevel() {
        return Integer.parseInt(config.getProperty("levels"));
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public MapLevel getMapInfo(int numLevel){
        if (!config.containsKey("levels"))
            throw new MapException("Nombre de niveau non précisé");
        String compStr = config.getProperty("compression");
        boolean comp = !Objects.equals(compStr, "false");
        int nbLevel = Integer.parseInt(config.getProperty("levels"));
        if (nbLevel < numLevel) throw new MapException("Erreur numero du niveau");
        if (!config.containsKey("level" + numLevel)) throw new MapException("Niveau " + numLevel + " non implementé");
        String level = config.getProperty("level" + numLevel);
        return parseStringLevel(comp ? parseStringRLE(level) : level);
    }

    private String parseStringRLE(String level){
        StringBuilder strb = new StringBuilder();
        for(int i = 0; i < level.length(); i++){
            char c = level.charAt(i);
            if(isNumeric(String.valueOf(c))) continue;
            if(i+1 >= level.length()) break;
            char next = level.charAt(i+1);
            int nbTimes = 1;
            if(isNumeric(String.valueOf(next))) nbTimes = Integer.parseInt(String.valueOf(next));
            strb.append(String.valueOf(c).repeat(Math.max(0, nbTimes)));
        }
        return strb.toString();
    }

    private MapLevel parseStringLevel(String level){
        int width = level.indexOf(EOL);
        String[] lines = level.split(String.valueOf(EOL));
        System.out.println(level);
        int height = lines.length;
        int numLine = 1;
        for(String str : lines){
            if(str.length() != width) throw new MapException("Erreur Map : taille de la ligne "
                    + numLine + " différente de la première ligne");
            numLine++;
        }
        MapLevel mapLevel = new MapLevel(width, height);
        int i = 0; int j = 0;
        String levelNoEOL = level.replaceAll(String.valueOf(EOL), "");
        while(height * i + j < levelNoEOL.length()){
            System.out.println(width + ":" + height);
            System.out.println(height * i + j + ":" + i + ":" + j + ":" + levelNoEOL.length());
            char c = levelNoEOL.charAt(height * i + j);
            mapLevel.set(i,j, ENtity.fromCode(c));
            if(j >= height - 1 && i < width - 1) { j = 0; i++; }
            else j++;
        }

        return mapLevel;
    }
}
