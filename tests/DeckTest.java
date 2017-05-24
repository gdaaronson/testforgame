import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Greg on 5/22/2017.
 */

public class DeckTest {

    @Test
    public void defaultDeckTest(){
        Deck deck = new Deck();
        assertEquals(30, deck.getCards().size());
        for(Card card: deck.getCards()){
            assertEquals(1, card.getManaCost());
            assertTrue(card instanceof Summon);
        }
    }

    @Test
    public void emptyDeckTest(){
        Deck deck = new Deck(true);
        assertTrue(deck.getCards().isEmpty());
    }

}
