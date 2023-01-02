package fr.ubx.poo.ubomb.launcher;

import fr.ubx.poo.ubomb.game.*;

import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GameLauncher {

    public static Game loadDefault() {
        Configuration configuration = new Configuration(new Position(0, 0), new Position(5,5), 3, 5, 4000, 5, 1000);
        return new Game(configuration, new Level(new MapLevelDefault()));
    }

    public static Game load(File file) throws IOException {
        GetFileInfo getFileInfo = new GetFileInfo(file);
        Configuration configuration = getFileInfo.getConfigInfo();
        List<Grid> grids = new ArrayList<>();
        for(int i = 1; i <= getFileInfo.getNbLevel(); i++){
            MapLevel mapLevel = getFileInfo.getMapInfo(i);
            grids.add(new Level(mapLevel));
        }
        return new Game(configuration, grids);
    }


}
