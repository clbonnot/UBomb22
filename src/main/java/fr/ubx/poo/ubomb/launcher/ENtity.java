package fr.ubx.poo.ubomb.launcher;

public enum ENtity {
    Empty('_'),
    Box('B'),
    Stone('S'),
    Tree('T'),

    BombRangeInc('>'),
    BombRangeDec('<'),
    BombNumberInc('+'),
    BombNumberDec('-'),
    Heart('H'),
    Key('K'),
    DoorPrevOpened('V'),
    DoorNextOpened('N'),
    DoorNextClosed('n'),
    Monster('M'),
    Princess('W');

    private final char code;

    ENtity(char c) {
        this.code = c;
    }

    public char getCode() { return this.code; }

    public static ENtity fromCode(char c) {
        for (ENtity entity : values()) {
            if (entity.code == c)
                return entity;
        }
        throw new MapException("Invalid character " + c);
    }

    @Override
    public String toString() {
        return Character.toString(code);
    }

}
