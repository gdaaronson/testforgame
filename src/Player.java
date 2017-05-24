/**
 * Created by Greg on 5/18/2017.
 */
public class Player {
    private int life;
    private int mana;
    private int manaTotal;
    private Deck deck;
    private Deck cardsInHand;
    private Deck below;
    private Deck beyond;
    private Deck battleField;

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

    public void drawCard(){
        Card cardDrawn = deck.getCards().remove(0);
        cardsInHand.getCards().add(cardDrawn);
        deck.getCards().remove(cardDrawn);
    }

    public void drawCard(int num){
        for(int i = 0; i < num; i++){
            drawCard();
        }
    }
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

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getManaTotal() {
        return manaTotal;
    }

    public void setManaTotal(int manaTotal) {
        this.manaTotal = manaTotal;
    }

    public Deck getBattleField() {
        return battleField;
    }

    public void setBattleField(Deck battleField) {
        this.battleField = battleField;
    }

    public Deck getCardsInHand() {
        return cardsInHand;
    }

    public void setCardsInHand(Deck cardsInHand) {
        this.cardsInHand = cardsInHand;
    }

    public Deck getBelow() {
        return below;
    }

    public void setBelow(Deck below) {
        this.below = below;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }
}
