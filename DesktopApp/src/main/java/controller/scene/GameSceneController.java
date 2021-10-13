package controller.scene;

import controller.GameController;
import controller.GameState;
import domain.Point;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import opponent.default_opponents.RandomOpponent;

import java.io.IOException;

public class GameSceneController {
    /*TODO:
    disable restart Button if the Game field is empty
    disable surrender button if the game field is empty
    enable restart after surrender
    make menu button return back to the main menu
    format code
    clean up code
    add confirm popups to restart button and menu button

     */


    private GameController controller;
    private Image crossImage, circleImage;

    @FXML
    Button restart, surrender;

    @FXML
    Button  field00,
            field10,
            field20,
            field01,
            field11,
            field21,
            field02,
            field12,
            field22;

    public GameSceneController(/*Opponent opponent*/) {
       // controller = new GameController(opponent);
        controller = new GameController(new RandomOpponent());
    }

    public void restartGame() {

        restart.setDisable(true);
        restart.setStyle("-fx-text-fill:gray");;

        surrender.setDisable(true);
        surrender.setStyle("-fx-text-fill:gray");

        for (Button button : allButtons()) {
            button.setDisable(false);
            button.setGraphic(null);
        }

        controller = new GameController(new RandomOpponent()); //TODO opponent
    }


    public void surrenderGame() { //TODO: something weird happens
        if(controller.getState()!=GameState.running) {
            return;
        }

        restart.setDisable(false);
        restart.setStyle("-fx-text-fill:black");

        surrender.setDisable(true);
        surrender.setStyle("-fx-text-fill:gray");
        for (Button button : allButtons()) {
            button.setDisable(true);
        }
    }

    @FXML
    public void initialize() {
        crossImage = new Image(getClass().getResource("/images/cross.png").toExternalForm());

        circleImage = new Image(getClass().getResource("/images/circle.png").toExternalForm());

        restart.setDisable(true);
        restart.setStyle("-fx-text-fill:gray");

        surrender.setDisable(true);
        surrender.setStyle("-fx-text-fill:gray");

        for (Button button : allButtons()) {
            button.setDisable(false);
            button.setGraphic(null);
        }
    }

    private ImageView imageViewFromImage(Image image) {
        final ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        return imageView;
    }

    private Button[] allButtons() {
        return new Button[]{field00, field10, field20, field01, field11, field21, field02, field12, field22};
    }

    private Point getPointForButton(Button button) {
        final Button[] allButtons = allButtons();
        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (allButtons[i] == button) {
                    return new Point(x, y);
                }
                i++;
            }
        }
        throw new Error("Button not inside the grid");
    }

    private Button getButtonForPoint(Point point) {
        final Button[] allButtons = allButtons();
        int i = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (x == point.x() && y == point.y()) {
                    return allButtons[i];
                }
                i++;
            }
        }
        throw new Error("Button not found");
    }

    public void selectButton(ActionEvent e) {
        final Button playerButton = (Button) e.getSource();
        final Point playerPoint = getPointForButton(playerButton);

        playerButton.setGraphic(imageViewFromImage(crossImage));
        playerButton.setDisable(true);
        playerButton.setStyle("-fx-opacity: 1;");

        final Point opponentPoint = controller.setPoint(playerPoint);
        if(opponentPoint!=null) {
            final Button opponentButton = getButtonForPoint(opponentPoint); //sometimes null
            opponentButton.setGraphic(imageViewFromImage(circleImage));
            opponentButton.setDisable(true);
            opponentButton.setStyle("-fx-opacity: 1;");
        }
        if (controller.getState() != GameState.running) {
            for (Button button : allButtons()) {
                button.setDisable(true);
                button.setStyle("-fx-opacity: 1;");
            }
        }

            restart.setDisable(false);
            restart.setStyle("-fx-text-fill:black");

            surrender.setDisable(false);
            surrender.setStyle("-fx-text-fill:black");
    }

    public void backToMenu(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/content/main-scene.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
