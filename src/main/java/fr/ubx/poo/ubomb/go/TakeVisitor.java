package fr.ubx.poo.ubomb.go;

import fr.ubx.poo.ubomb.go.character.Princess;
import fr.ubx.poo.ubomb.go.decor.bonus.*;

// Double dispatch visitor pattern
public interface TakeVisitor {
    default void take(Bonus Bonus) {}
    default void take(Princess p) {}

}
