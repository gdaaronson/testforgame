import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


/**
 * Created by Greg on 5/18/2017.
 */
public class GameMaster extends Application {


    private final Rectangle P1_HAND_SPACE = new Rectangle(500, 700, 600, 200);
    private final Rectangle P2_HAND_SPACE = new Rectangle(500, 0, 600, 200);
    private final Rectangle P1_BATTLEFIELD = new Rectangle(600, 450, 1000, 250);
    private final Rectangle P2_BATTLEFIELD = new Rectangle(600, 200, 1000, 250);
    private Player p1;
    private Player p2;
    private int turn;

    public static void main(String args[]) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        //declare and initalize stuff
        p1 = new Player();
        p2 = new Player();
        p1.drawCard(3);
        p2.drawCard(4);
        Group root = new Group();
        turn = 0;
        drawBattleField(root);
        newTurn(root);
        drawManaInfo(root);
        EventHandler<MouseEvent> onDrag = this::onDrag;
        EventHandler<MouseEvent> onDrop = (MouseEvent event) -> {
            onDrop(event);
            drawManaInfo(root);
            drawBattleFieldSummons(root);
        };
        allowMouseToAccessCardsInHandP1(onDrag, onDrop);
        allowMouseToAccessCardsInHandP2(onDrag, onDrop);
        Button endTurnButton = new Button("End Turn");
        endTurnButton.setOnAction(event -> {
            newTurn(root);
            drawManaInfo(root);
            if(turn % 2 == 1){
                allowMouseToAccessCardsInHandP1(onDrag, onDrop);
            }else {
                allowMouseToAccessCardsInHandP2(onDrag, onDrop);
            }
        });
        endTurnButton.setLayoutX(1500);
        endTurnButton.setLayoutY(450);
        root.getChildren().add(endTurnButton);
        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setTitle("Game Tester");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void battle21(Summon cardBeingDragged, Summon opponentsSummon) {
        int defendersNewHealth = opponentsSummon.getHealth() - cardBeingDragged.getAttack();
        if (defendersNewHealth <= 0) {
            p1.getBattleField().getCards().remove(opponentsSummon);
            p1.getBelow().getCards().add(opponentsSummon);
        } else {
            opponentsSummon.setHealth(defendersNewHealth);
            opponentsSummon.setAttacked(true);
        }
        int attackersNewHealth = cardBeingDragged.getHealth() - opponentsSummon.getAttack();
        if (attackersNewHealth <= 0) {
            p2.getBattleField().getCards().remove(cardBeingDragged);
            p2.getBelow().getCards().add(cardBeingDragged);
        } else {
            cardBeingDragged.setHealth(attackersNewHealth);
            cardBeingDragged.setAttacked(true);
        }
    }

    private void battle12(Summon cardBeingDragged, Summon opponentsSummon) {
        int defendersNewHealth = opponentsSummon.getHealth() - cardBeingDragged.getAttack();
        if (defendersNewHealth <= 0) {
            p2.getBelow().getCards().add(opponentsSummon);
            p2.getBattleField().getCards().remove(opponentsSummon);
        } else {
            opponentsSummon.setHealth(defendersNewHealth);
        }
        int attackersNewHealth = cardBeingDragged.getHealth() - opponentsSummon.getAttack();
        if (attackersNewHealth <= 0) {
            p1.getBelow().getCards().add(cardBeingDragged);
            p1.getBattleField().getCards().remove(cardBeingDragged);
        } else {
            cardBeingDragged.setHealth(attackersNewHealth);
            cardBeingDragged.setAttacked(true);
        }
    }

    private void onDrag(MouseEvent event) {
        if (turn % 2 == 1) {
            locateAndMoveCard(event, p1);
        } else if (turn % 2 == 0) {
            locateAndMoveCard(event, p2);
        }
    }

    private void locateAndMoveCard(MouseEvent event, Player p) {
        Rectangle rectangleBeingClicked = (Rectangle) event.getSource();
        //TODO add more conditions so only certian cards can be dragged like if a summon, make sure the battlefield is of certian size
        for(Card card : p.getCardsInHand().getCards()){
            if(card.getCardArt().equals(rectangleBeingClicked)){
                if(card.getManaCost() <= p.getMana()) {
                    rectangleBeingClicked.setX(event.getX());
                    rectangleBeingClicked.setY(event.getY());
                }
            }
        }
        for(Card card : p.getBattleField().getCards()){
            if(card.getCardArt().equals(rectangleBeingClicked)) {
                if (card.getManaCost() <= p.getMana()) {
                    rectangleBeingClicked.setX(event.getX());
                    rectangleBeingClicked.setY(event.getY());
                }
            }
        }
    }


    private Card identifyCardToBePlayed(MouseEvent event,Player p){
        Rectangle rectangleBeingClicked = (Rectangle) event.getSource();
        for(Card card : p.getCardsInHand().getCards()) {
            if (rectangleBeingClicked.intersects(card.getCardArt().getLayoutBounds())) {
                Card cardBeingClicked = p.getCardsInHand().getCard(rectangleBeingClicked);
                if (cardBeingClicked.getManaCost() <= p.getMana() && p.getBattleField().getCards().size() < 5) {
                    return card;
                }
            }
        }
        return null;
    }

    private Card getCardFromBattleField(MouseEvent event, Player p){
        Rectangle rectangleBeingClicked = (Rectangle) event.getSource();
        for(Card card : p.getBattleField().getCards()){
            if (rectangleBeingClicked.intersects(card.getCardArt().getLayoutBounds())) {
                return card;
            }
        }
        return null;
    }

    private void onDrop(MouseEvent event) {
        if (turn % 2 == 1) {
            Card cardToBePlayed = identifyCardToBePlayed(event, p1);
            if(cardToBePlayed != null){
                p1.playCard(cardToBePlayed);
                return;
            }
            Card attacker = getCardFromBattleField(event, p1);
            Card defender = getCardFromBattleField(event, p2);
            if(attacker != null && defender != null){
                battle12((Summon) attacker,(Summon) defender);
            }
        } else if (turn % 2 == 0) {
            Card cardToBePlayed = identifyCardToBePlayed(event, p2);
            if(cardToBePlayed != null){
                p2.playCard(cardToBePlayed);
                return;
            }
            Card attacker = getCardFromBattleField(event, p2);
            Card defender = getCardFromBattleField(event, p1);
            if(attacker != null && defender != null){
                battle21((Summon) attacker,(Summon) defender);
            }
        }
    }

    private void allowMouseToAccessCardsInHandP2(EventHandler<MouseEvent> dragged, EventHandler<MouseEvent> dropped) {
        for (Card card : p2.getCardsInHand().getCards()) {
            card.getCardArt().addEventFilter(MouseEvent.MOUSE_DRAGGED, dragged);
            card.getCardArt().addEventFilter(MouseEvent.MOUSE_RELEASED, dropped);
        }
    }

    private void allowMouseToAccessCardsInHandP1(EventHandler<MouseEvent> dragged, EventHandler<MouseEvent> dropped) {
        for (Card card : p1.getCardsInHand().getCards()) {
            card.getCardArt().addEventFilter(MouseEvent.MOUSE_DRAGGED, dragged);
            card.getCardArt().addEventFilter(MouseEvent.MOUSE_RELEASED, dropped);
        }
    }


    private void makeRectanglesForP2Hand(Group root) {
        Rectangle wipe = new Rectangle(P2_HAND_SPACE.getX(), P2_HAND_SPACE.getY(), P2_HAND_SPACE.getWidth(), P2_HAND_SPACE.getHeight());
        wipe.setFill(Color.WHITE);
        root.getChildren().add(wipe);
        for (int i = 0; i < p2.getCardsInHand().getCards().size(); i++) {
            Rectangle rectangle = new Rectangle(500 + i * 600 / p2.getCardsInHand().getCards().size(), 0, 60, 120);
            p2.getCardsInHand().getCards().get(i).setCardArt(rectangle);
            if (!root.getChildren().contains(rectangle)) {
                root.getChildren().add(rectangle);
            }
        }
    }

    private void drawCardsInHandP1(Group root) {
        Rectangle wipe = new Rectangle(P1_HAND_SPACE.getX(), P1_HAND_SPACE.getY(), P1_HAND_SPACE.getWidth(), P1_HAND_SPACE.getHeight());
        wipe.setFill(Color.WHITE);
        root.getChildren().add(wipe);
        for (int i = 0; i < p1.getCardsInHand().getCards().size(); i++) {
            Rectangle rectangle = new Rectangle(500 + i * 600 / p1.getCardsInHand().getCards().size(), 700, 60, 120);
            p1.getCardsInHand().getCards().get(i).setCardArt(rectangle);
            if (!root.getChildren().contains(rectangle)) {
                root.getChildren().add(rectangle);
            }
        }
    }

    private void newTurn(Group root) {
        turn++;
        if (turn % 2 == 1) {
            drawCardAndGetManaCheck(p1);
            drawCardsInHandP1(root);
            setSummonsToUntapped(p1);
        } else {
            drawCardAndGetManaCheck(p2);
            makeRectanglesForP2Hand(root);
            setSummonsToUntapped(p2);
        }
    }

    private void setSummonsToUntapped(Player p) {
        for (Card summon : p.getBattleField().getCards()) {
            if (summon instanceof Summon) {
                ((Summon) summon).setAttacked(false);
            }
        }
    }

    private void drawCardAndGetManaCheck(Player p) {
        if (turn < 10) {
            p.drawCard();
            p.setManaTotal(p.getManaTotal() + 1);
            p.setMana(p.getManaTotal());
        } else if (turn < 20) {
            p.drawCard(2);
            p.setManaTotal(p.getManaTotal() + 1);
            p.setMana(p.getManaTotal());
        } else {
            p.drawCard(2);
            p.setMana(p.getManaTotal());
        }
    }

    private void drawBattleField(Group root) {
        Rectangle battleFieldImage = new Rectangle(0, 250, 1600, 400);
        battleFieldImage.setFill(Color.BROWN);
        root.getChildren().add(battleFieldImage);
        root.getChildren().add(new Line(0, 250, 1600, 250));
        root.getChildren().add(new Line(0, 450, 1600, 450));
        root.getChildren().add(new Line(0, 650, 1600, 650));
        root.getChildren().add(new Line(600, 250, 600, 650));
    }

    private void drawBattleFieldSummons(Group root) {
        for (int i = 0; i < p1.getBattleField().getCards().size(); i++){
            Rectangle rectangle = p1.getBattleField().getCards().get(i).getCardArt();
            rectangle.setX(650 + 150 * i);
            rectangle.setY(500);
            if(!root.getChildren().contains(rectangle)){
                root.getChildren().add(rectangle);
            }
        }
        for (int i = 0; i < p2.getBattleField().getCards().size(); i++){
            Rectangle rectangle = p2.getBattleField().getCards().get(i).getCardArt();
            rectangle.setX(650 + 150 * i);
            rectangle.setY(300);
            if(!root.getChildren().contains(rectangle)){
                root.getChildren().add(rectangle);
            }
        }
    }

    private void drawManaInfo(Group root) {
        Rectangle p1Cover = new Rectangle(1500, 800, 100, 50);
        p1Cover.setFill(Color.WHITE);
        root.getChildren().add(p1Cover);
        Text manaCount1 = new Text(p1.getMana() + " / " + p1.getManaTotal());
        manaCount1.setX(1500);
        manaCount1.setY(850);
        root.getChildren().add(manaCount1);

        Rectangle p2Cover = new Rectangle(1500, 0, 100, 50);
        p2Cover.setFill(Color.WHITE);
        root.getChildren().add(p2Cover);
        Text manaCount2 = new Text(p2.getMana() + " / " + p2.getManaTotal());
        manaCount2.setX(1500);
        manaCount2.setY(50);
        root.getChildren().add(manaCount2);
    }
}
