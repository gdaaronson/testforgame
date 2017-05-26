import javafx.scene.shape.Rectangle;

/**
 * Created by Greg on 5/18/2017.
 */
public class Summon extends Card {

    /**
     * A summons attack value
     */
    private int attack;

    /**
     * A summons current health
     */
    private int health;

    /**
     * Weather or not the summon has attacked
     */
    private boolean attacked;


    /**
     * The default constructor which makes a 1/1 for 1, without charge
     */
    public Summon(){
        manaCost = 1;
        attack = 1;
        health = 1;
        attacked = true;
        cardArt = new Rectangle(60, 120);
        attacked = true;
    }

    /**
     * The constructor that make a vanilla summon given the given stats
     * @param manaCost the summons mana cost
     * @param attack the summons attack
     * @param health the summons health
     */
    public Summon(int manaCost, int attack, int health){
        this.manaCost = manaCost;
        this.attack = attack;
        this.health = health;
        attacked = true;
    }

    /**
     * Gets the summons current attack
     * @return the summons current attack
     */
    public int getAttack() {
        return attack;
    }

    /**
     * Sets the summons current attack
     * @param attack the new attack of the summon
     */
    public void setAttack(int attack) {
        this.attack = attack;
    }

    /**
     * Gets the summons current health
     * @return the summons current health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the summons current health
     * @param health the new current health of the summon
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Tells us weather or not the summon has attacked
     * @return has the summon attacked
     */
    public boolean hasAttacked() {
        return attacked;
    }

    /**
     * Sets if the summon has attacked or not
     * @param attacked
     */
    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }
}
