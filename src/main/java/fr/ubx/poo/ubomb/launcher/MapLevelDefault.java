package fr.ubx.poo.ubomb.launcher;

import static fr.ubx.poo.ubomb.launcher.ENtity.*;

public class MapLevelDefault extends MapLevel {
    private final static ENtity[][] level1 = {
            {Empty, BombNumberInc, BombNumberInc, BombNumberInc, BombNumberInc, BombNumberDec, BombNumberDec, Empty, Empty, Empty, Empty, Empty},
            {Empty, Heart, Stone, Empty, Stone, Empty, Stone, Stone, Stone, Stone, Empty, Empty},
            {Empty, Box, Empty, Empty, Stone, Empty, Stone, Empty, Empty, Stone, Empty, Empty},
            {Empty, Empty, Empty, Empty, Stone, Empty, Stone, Empty, Empty, Stone, Empty, Empty},
            {Empty, Box, Empty, Empty, Stone, Stone, Stone, Empty, Empty, Empty, Empty, Empty},
            {Empty, Empty, Empty, Empty, Empty, Empty, Empty, Key, Empty, Stone, Empty, Empty},
            {Empty, Tree, Empty, Tree, Empty, Empty, Empty, Empty, Empty, Stone, Empty, Empty},
            {Empty, Empty, Empty, Tree, Empty, Empty, Empty, Empty, Empty, Stone, Empty, Empty},
            {Empty, Tree, Tree, Tree, Empty, Empty, Empty, Empty, Empty, Stone, Empty, Empty},
            {Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
            {Stone, Stone, Stone, Stone, Stone, Empty, Empty, Empty, Stone, Stone, Empty, Stone},
            {Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty},
            {Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty, Empty}
    };
    private final static int width = 12;
    private final static int height = 13;

    public MapLevelDefault() {
        super(width, height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                set(i, j, level1[j][i]);
    }
}
