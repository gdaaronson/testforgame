
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Greg on 5/22/2017.
 */

public class SummonTest {

    @Test
    public void defaultSummonTest(){
        Summon card = new Summon();
        assertEquals(1, card.getManaCost());
        assertEquals(1, card.getAttack());
        assertEquals(1, card.getHealth());
        assertTrue(card.hasAttacked());
    }

    @Test
    public void summonTest(){
        Summon card = new Summon(2, 2, 3);
        assertEquals(2, card.getManaCost());
        assertEquals(2, card.getAttack());
        assertEquals(3, card.getHealth());
        assertTrue(card.hasAttacked());
    }

}
