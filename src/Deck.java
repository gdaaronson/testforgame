import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 5/18/2017.
 */
public class Deck {

    private List<Card> cards;

    /**
     * Create a default deck of 30 cards all of which are 1/1 for 1
     */
    public Deck(){
        cards = new ArrayList<>();
        for(int i = 0; i < 30; i++){
            cards.add(new Summon());
        }
    }

    /**
     * Create an empty deck
     * @param i enter a boolean for empty
     */
    public Deck(boolean i){
        cards = new ArrayList<>();
    }

    /**
     * Gets the Card objects in the deck
     * @return the Card objects
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Gets a specific card based on its 'art'
     * @param cardArt the 'art' from the GUI
     * @return the Card object
     */
    public Card getCard(Rectangle cardArt){
        for(Card card: cards){
            if(cardArt.equals(card.getCardArt())){
                return card;
            }
        }
        //TODO make a try catch
        return null;
    }
}
