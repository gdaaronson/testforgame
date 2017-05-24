import javafx.scene.shape.Rectangle;

/**
 * Created by Greg on 5/18/2017.
 */
public class Summon extends Card {

    private int attack;
    private int health;
    private boolean attacked;

    public Summon(){
        setManaCost(1);
        attack = 1;
        health = 1;
        attacked = true;
        setCardArt(new Rectangle(60,120));
        attacked = true;
    }

    public Summon(int manaCost, int attack, int health){
        setManaCost(manaCost);
        this.attack = attack;
        this.health = health;
        attacked = true;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean hasAttacked() {
        return attacked;
    }

    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }
}
