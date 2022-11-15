package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.go.character.GameCharacter;
import fr.ubx.poo.ubomb.go.character.Monster;
import fr.ubx.poo.ubomb.go.character.Player;

public interface Walkable {
    default boolean walkableBy(GameCharacter gc) { return false; }
}
