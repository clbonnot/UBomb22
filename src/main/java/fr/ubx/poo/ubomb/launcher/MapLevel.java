package fr.ubx.poo.ubomb.launcher;

public class MapLevel {

    private final int width;
    private final int height;
    private final ENtity[][] grid;

    public MapLevel(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new ENtity[height][width];
    }

    public int width() {
        return width;    }

    public int height() {
        return height;
    }

    public ENtity get(int i, int j) {
        return grid[j][i];
    }

    public void set(int i, int j, ENtity entity) {
        grid[j][i] = entity;
    }

}
