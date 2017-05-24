import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by Greg on 5/22/2017.
 */

public class PlayerTest {
    Player player;


    @Before
    public void setUp(){
        player = new Player();
    }
    @Test
    public void defaultPlayerTest(){
        assertEquals(30,player.getDeck().getCards().size());
        for (Card card : player.getDeck().getCards()){
            assertEquals(1, card.getManaCost());
            assertTrue(card instanceof Summon);
        }
    }

    @Test
    public void drawCard() {
        assertTrue(player.getCardsInHand().getCards().isEmpty());
        assertEquals(30,player.getDeck().getCards().size());
        player.drawCard();
        assertEquals(1, player.getCardsInHand().getCards().size());
        assertEquals(29,player.getDeck().getCards().size());
        player.drawCard(2);
        assertEquals(3, player.getCardsInHand().getCards().size());
        assertEquals(27,player.getDeck().getCards().size());
        assertTrue(player.getBattleField().getCards().isEmpty());
    }


    @Test
    public void playCard() {
        player.drawCard();
        Card card = player.getCardsInHand().getCards().get(0);
        player.playCard(card);
        assertTrue(player.getCardsInHand().getCards().isEmpty());
        assertEquals(1, player.getBattleField().getCards().size());
        
    }

}
