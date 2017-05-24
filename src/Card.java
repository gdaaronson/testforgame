import javafx.scene.shape.Rectangle;

/**
 * Created by Greg on 5/18/2017.
 */
public class Card {

    private int manaCost;
    private Rectangle cardArt;

    public Rectangle getCardArt() {
        return cardArt;
    }

    public void setCardArt(Rectangle cardArt) {
        this.cardArt = cardArt;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }
}
