package fr.ubx.poo.ubomb.view;

import fr.ubx.poo.ubomb.go.decor.Bomb;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class SpriteBomb extends Sprite {

    public SpriteBomb(Pane layer, Bomb bomb) {
        super(layer, null, bomb);
        updateImage();
    }

    @Override
    public void updateImage() {
        Bomb bomb = (Bomb) getGameObject();
        Image image = ImageResourceFactory.getBomb((int) (bomb.getTimer().remaining()/1000)).getImage();
        setImage(image);
    }
}
