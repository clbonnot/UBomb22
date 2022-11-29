package fr.ubx.poo.ubomb.launcher;

import fr.ubx.poo.ubomb.game.Configuration;
import fr.ubx.poo.ubomb.game.Game;
import fr.ubx.poo.ubomb.game.Level;
import fr.ubx.poo.ubomb.game.Position;

import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.util.List;
import java.util.Properties;

public class GameLauncher {

    public static Game loadDefault() {
        Configuration configuration = new Configuration(new Position(0, 0), 3, 5, 4000, 5, 1000);
        return new Game(configuration, new Level(new MapLevelDefault()));
    }

    public static Game load(File file) throws IOException {
        GetFileInfo getFileInfo = new GetFileInfo(file);
        Configuration configuration = getFileInfo.getConfigInfo();
        MapLevel mapLevel = getFileInfo.getMapInfo(1);
        for(int i = 0; i < mapLevel.width(); i++) {
            for (int j = 0; j < mapLevel.height(); j++) {
                if(mapLevel.get(i,j) != null)
                    System.out.printf(mapLevel.get(i,j).toString());
                else System.out.println("null");
            }
        }
        Level level = new Level(mapLevel);
        return new Game(configuration, level);

    }


}
