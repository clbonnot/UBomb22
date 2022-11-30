package fr.ubx.poo.ubomb.go.decor;

import fr.ubx.poo.ubomb.go.decor.bonus.Key;

public interface WalkVisitor {

    boolean canWalkOn(Key k);
}
