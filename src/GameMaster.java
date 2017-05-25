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
    
    private Player[] p;
    private int turn;
    private Group root;

    public static void main(String args[]) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        //declare and initalize stuff
        p = new Player[2];
        p[0] = new Player();
        p[1] = new Player();
        p[0].drawCard(3);
        p[1].drawCard(4);
        root = new Group();
        turn = 0;
        drawBattleField();
        newTurn();
        drawManaInfo();
        drawCardsInHandP1();
        drawCardsInHandP2();
        EventHandler<MouseEvent> onDrag = this::onDrag;
        EventHandler<MouseEvent> onDrop = (MouseEvent event) -> {
            onDrop(event);
            drawLinesOnBattleField();
            drawManaInfo();
            drawBattleFieldSummons();
            drawCardsInHandP1();
            drawCardsInHandP2();
        };
        allowMouseToAccessCardsInHandP1(onDrag, onDrop);
        allowMouseToAccessCardsInHandP2(onDrag, onDrop);
        Button endTurnButton = new Button("End Turn");
        endTurnButton.setOnAction(event -> {
            newTurn();
            drawManaInfo();
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

    private void updateBattleField(Player p, Summon summon, int newHealth, boolean attacker) {
        if (newHealth <= 0) {
            p.getBattleField().getCards().remove(summon);
            p.getBelow().getCards().add(summon);
        } else {
            summon.setHealth(newHealth);
            if(attacker){
                summon.setAttacked(true);
            }
        }
    }

    private void battle21(Summon attacker, Summon defender) {
        updateBattleField(p[0], defender, defender.getHealth() - attacker.getAttack(), false);
        updateBattleField(p[1], attacker, attacker.getHealth() - defender.getAttack(), true);
    }

    private void battle12(Summon attacker, Summon defender) {
        updateBattleField(p[1], defender, defender.getHealth() - attacker.getAttack(), false);
        updateBattleField(p[0], attacker, attacker.getHealth() - defender.getAttack(), true);
    }

    private void onDrag(MouseEvent event) {
        if (turn % 2 == 1) {
            locateAndMoveCard(event, p[0]);
        } else if (turn % 2 == 0) {
            locateAndMoveCard(event, p[1]);
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
                Summon summon = (Summon) card;
                if (!summon.hasAttacked()) {
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
            Card cardToBePlayed = identifyCardToBePlayed(event, p[0]);
            if(cardToBePlayed != null){
                p[0].playCard(cardToBePlayed);
                return;
            }
            Card attacker = getCardFromBattleField(event, p[0]);
            Card defender = getCardFromBattleField(event, p[1]);
            if(attacker != null && defender != null){
                battle12((Summon) attacker,(Summon) defender);
                if(p[0].getBelow().getCards().contains(attacker)){
                    attacker.getCardArt().setFill(Color.BROWN);
                }
                if(p[1].getBelow().getCards().contains(defender)){
                    defender.getCardArt().setFill(Color.BROWN);
                }
            }
            drawCardsInHandP1();
        } else if (turn % 2 == 0) {
            Card cardToBePlayed = identifyCardToBePlayed(event, p[1]);
            if(cardToBePlayed != null){
                p[1].playCard(cardToBePlayed);
                return;
            }
            Card attacker = getCardFromBattleField(event, p[1]);
            Card defender = getCardFromBattleField(event, p[0]);
            if(attacker != null && defender != null){
                battle21((Summon) attacker,(Summon) defender);
                if(p[1].getBelow().getCards().contains(attacker)){
                    attacker.getCardArt().setFill(Color.BROWN);
                }
                if(p[0].getBelow().getCards().contains(defender)){
                    defender.getCardArt().setFill(Color.BROWN);
                }
            }
            drawCardsInHandP2();
        }
    }

    private void allowMouseToAccessCardsInHandP2(EventHandler<MouseEvent> dragged, EventHandler<MouseEvent> dropped) {
        for (Card card : p[1].getCardsInHand().getCards()) {
            card.getCardArt().addEventFilter(MouseEvent.MOUSE_DRAGGED, dragged);
            card.getCardArt().addEventFilter(MouseEvent.MOUSE_RELEASED, dropped);
        }
    }

    private void allowMouseToAccessCardsInHandP1(EventHandler<MouseEvent> dragged, EventHandler<MouseEvent> dropped) {
        for (Card card : p[0].getCardsInHand().getCards()) {
            card.getCardArt().addEventFilter(MouseEvent.MOUSE_DRAGGED, dragged);
            card.getCardArt().addEventFilter(MouseEvent.MOUSE_RELEASED, dropped);
        }
    }


    private void drawCardsInHandP2() {
        for (int i = 0; i < p[1].getCardsInHand().getCards().size(); i++) {
            Rectangle rectangle = p[1].getCardsInHand().getCards().get(i).getCardArt();
            rectangle.setX(500 + i * 600 / p[1].getCardsInHand().getCards().size());
            rectangle.setY(0);
            p[1].getCardsInHand().getCards().get(i).setCardArt(rectangle);
            if (!root.getChildren().contains(rectangle)) {
                root.getChildren().add(rectangle);
            }
        }
    }

    private void drawCardsInHandP1() {
        for (int i = 0; i < p[0].getCardsInHand().getCards().size(); i++) {
            Rectangle rectangle = p[0].getCardsInHand().getCards().get(i).getCardArt();
            rectangle.setX(500 + i * 600 / p[0].getCardsInHand().getCards().size());
            rectangle.setY(700);
            p[0].getCardsInHand().getCards().get(i).setCardArt(rectangle);
            if (!root.getChildren().contains(rectangle)) {
                root.getChildren().add(rectangle);
            }
        }
    }

    private void newTurn() {
        turn++;
        if (turn % 2 == 1) {
            drawCardAndGetManaCheck(p[0]);
            drawCardsInHandP1();
            setSummonsToUntapped(p[0]);
        } else {
            drawCardAndGetManaCheck(p[1]);
            drawCardsInHandP2();
            setSummonsToUntapped(p[1]);
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

    private void drawBattleField() {
        Rectangle battleFieldImage = new Rectangle(0, 250, 1600, 400);
        battleFieldImage.setFill(Color.BROWN);
        root.getChildren().add(battleFieldImage);
        drawLinesOnBattleField();
    }

    private void drawLinesOnBattleField() {
        root.getChildren().add(new Line(0, 250, 1600, 250));
        root.getChildren().add(new Line(0, 450, 1600, 450));
        root.getChildren().add(new Line(0, 650, 1600, 650));
        root.getChildren().add(new Line(600, 250, 600, 650));
    }

    private void drawBattleFieldSummons() {
        for (int i = 0; i < p[0].getBattleField().getCards().size(); i++){
            Rectangle rectangle = p[0].getBattleField().getCards().get(i).getCardArt();
            rectangle.setX(650 + 150 * i);
            rectangle.setY(500);
            if(!root.getChildren().contains(rectangle)){
                root.getChildren().add(rectangle);
            }
        }
        for (int i = 0; i < p[1].getBattleField().getCards().size(); i++){
            Rectangle rectangle = p[1].getBattleField().getCards().get(i).getCardArt();
            rectangle.setX(650 + 150 * i);
            rectangle.setY(300);
            if(!root.getChildren().contains(rectangle)){
                root.getChildren().add(rectangle);
            }
        }
    }

    private void drawManaInfo() {
        Rectangle p1Cover = new Rectangle(1500, 800, 100, 50);
        p1Cover.setFill(Color.WHITE);
        root.getChildren().add(p1Cover);
        Text manaCount1 = new Text(p[0].getMana() + " / " + p[0].getManaTotal());
        manaCount1.setX(1500);
        manaCount1.setY(850);
        root.getChildren().add(manaCount1);

        Rectangle p2Cover = new Rectangle(1500, 0, 100, 50);
        p2Cover.setFill(Color.WHITE);
        root.getChildren().add(p2Cover);
        Text manaCount2 = new Text(p[1].getMana() + " / " + p[1].getManaTotal());
        manaCount2.setX(1500);
        manaCount2.setY(50);
        root.getChildren().add(manaCount2);
    }
}
