package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.GameObject;
import fr.ubx.poo.ubomb.go.decor.Door;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteDoor extends Sprite{

    public SpriteDoor(Pane layer, Door door) {
        super(layer, null, door);
    }

    @Override
    public void updateImage() {
        Door door = (Door) getGameObject();
        Image image = ImageResourceFactory.getDoor(door.isOpened()).getImage();
        setImage(image);
    }
}
