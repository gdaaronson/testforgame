/**
 * Created by Greg on 5/18/2017.
 */
public class Player {

    /**
     * Players life total
     */
    private int life;

    /**
     * Players current mana
     */
    private int mana;

    /**
     * Players total mana
     */
    private int manaTotal;

    /**
     * The cards waiting to be drawn by the player
     */
    private Deck deck;

    /**
     * The cards in the players
     */
    private Deck cardsInHand;

    /**
     * The cards in the graveyard
     */
    private Deck below;

    /**
     * The cards that have been exiled
     */
    private Deck beyond;

    /**
     * The cards that are on the battle field
     */
    private Deck battleField;

    /**
     * The default constructor for Player, only to be used for testing
     */
    public Player(){
        deck = new Deck();
        cardsInHand = new Deck(true);
        below = new Deck(true);
        beyond = new Deck(true);
        battleField = new Deck(true);
        life = 30;
        mana = 0;
        manaTotal = 0;
    }


    /**
     * Draws a card from the players deck to his/her hand
     */
    public void drawCard(){
        Card cardDrawn = deck.getCards().remove(0);
        cardsInHand.getCards().add(cardDrawn);
        deck.getCards().remove(cardDrawn);
    }

    /**
     * Draws n cards
     * @param num n = num
     */
    public void drawCard(int num){
        for(int i = 0; i < num; i++){
            drawCard();
        }
    }

    /**
     * Plays a card from the players hand
     * @param card the card to be played
     */
    public void playCard(Card card){
        cardsInHand.getCards().remove(card);
        mana -= card.getManaCost();
        if(card instanceof Summon){
            battleField.getCards().add(card);
            //TODO: add more instanceof stuff
        }else{
            below.getCards().add(card);
        }
    }

    /**
     * Gets the players life total
     * @return the life total
     */
    public int getLife() {
        return life;
    }

    /**
     * Sets the players life total
     * @param life the new life total
     */
    public void setLife(int life) {
        this.life = life;
    }

    /**
     * Gets the players current mana
     * @return the players current mana
     */
    public int getMana() {
        return mana;
    }

    /**
     * Set the players current mana
     * @param mana the new current mana
     */
    public void setMana(int mana) {
        this.mana = mana;
    }

    /**
     * Gets a players mana total
     * @return the players mana total
     */
    public int getManaTotal() {
        return manaTotal;
    }

    /**
     * Sets the players mana total
     * @param manaTotal the players new mana total
     */
    public void setManaTotal(int manaTotal) {
        this.manaTotal = manaTotal;
    }

    /**
     * Gets the battle field of the player
     * @return the players battle field
     */
    public Deck getBattleField() {
        return battleField;
    }

    /**
     * Sets the players battle field
     * @param battleField the players new battle field
     */
    public void setBattleField(Deck battleField) {
        this.battleField = battleField;
    }

    /**
     * Gets the players cards in hand
     * @return the players cards in hand
     */
    public Deck getCardsInHand() {
        return cardsInHand;
    }

    /**
     * Sets the players cards in hand
     * @param cardsInHand the players new cards in hand
     */
    public void setCardsInHand(Deck cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    /**
     * Get the card below
     * @return the cards below
     */
    public Deck getBelow() {
        return below;
    }

    /**
     * Sets the cards below
     * @param below the new cards below
     */
    public void setBelow(Deck below) {
        this.below = below;
    }

    /**
     * Gets the players deck
     * @return
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Sets the players deck
     * @param deck the players new deck
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }
}
