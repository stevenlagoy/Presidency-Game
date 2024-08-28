import java.io.*;
import java.lang.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PresidencyGameGUI extends Application {

    public static void main(String[] args)
    {
        launch(args);
    }

    public static Stage primaryStage = new Stage();

    static Group startMenu_group = new Group();
    static Scene startMenu_scene = new Scene(startMenu_group);
    static Group newGame_group = new Group();
    static Scene newGame_scene = new Scene(newGame_group);
    static Group openSave_group = new Group();
    static Scene openSave_scene = new Scene(openSave_group);
    static Group characterRoll_group = new Group();
    static Scene characterRoll_scene = new Scene(characterRoll_group);
    static Group characterView_group = new Group();
    static Scene characterView_scene = new Scene(characterView_group);
    static Group partyView_group = new Group();
    static Scene partyView_scene = new Scene(partyView_group);
    static Group mapView_group = new Group();
    static Scene mapView_scene = new Scene(mapView_group);
    static Group blocsView_group = new Group();
    static Scene blocsView_scene = new Scene(blocsView_group);

    @Override
    public void start(Stage primaryStage){
        SceneController controller = new SceneController(primaryStage);
        primaryStage.setTitle("Presidency Game");
        //primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        
        /*
        //set Stage boundaries to visible bounds of the main screen
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(primaryScreenBounds.getMinX());
        primaryStage.setY(primaryScreenBounds.getMinY());
        primaryStage.setWidth(primaryScreenBounds.getWidth());
        primaryStage.setHeight(primaryScreenBounds.getHeight());

        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setMinWidth(primaryStage.getWidth());
        */

        // create startMenu_scene
        ImageView titleCard_view = new ImageView(new Image("gfx/title_card.png"));
        Label titleCard_label = new Label();
        titleCard_label.setGraphic(titleCard_view);
        Button newGame_button = new Button("New Game");
        newGame_button.setOnAction(e -> {
            controller.switchScene(newGame_scene);
            //primaryStage.setScene(newGame_scene);
        });
        Button continueGame_button = new Button("Continue Game");
        continueGame_button.setOnAction(e -> {
            controller.switchScene(openSave_scene);
            //primaryStage.setScene(openSave_scene);
        });
        Button tutorial_button = new Button("Tutorial");
        Button about_button = new Button("About");
        Button closeGame_button = new Button("Exit to Desktop");
        closeGame_button.setOnAction(e -> {
            primaryStage.close();
        });
        VBox startMenuPanel_box = new VBox(titleCard_label, newGame_button, continueGame_button, tutorial_button, about_button, closeGame_button);
        //startMenuPanel_box.setPrefSize(primaryStage.getWidth(), primaryStage.getHeight());
        startMenu_group.getChildren().add(startMenuPanel_box);
        // -------------------------------------------------------

        // create newGame_scene
        Label newGame_label = new Label("Start a New Game");
        Button newGameBack_Button = new Button("Back");
        newGameBack_Button.setOnAction(e -> {
            controller.switchScene(startMenu_scene);
            //primaryStage.setScene(startMenu_scene);
        });
        VBox newGamePanel_box = new VBox(newGame_label, newGameBack_Button);
        newGame_group.getChildren().add(newGamePanel_box);
        // -------------------------------------------------------

        // create openSave_scene
        Label openSave_label = new Label("Open a Save Game:");
        // import the list of saves here
        String[] saves = new String[3];
        saves[0] = "Save 1";
        saves[1] = "Save 2";
        saves[2] = "Save 3";
        ListView<String> saves_list = new ListView<String>();
        saves_list.getItems().addAll(saves);
        Button loadSave_Button = new Button("Load Save");
        loadSave_Button.setOnAction(e -> {
            System.out.printf("Loading save: %s%n", saves_list.getSelectionModel().getSelectedItem());
            // this will be where the main game is opened
        });
        Button openSaveBack_button = new Button("Back");
        openSaveBack_button.setOnAction(e -> {
            controller.switchScene(startMenu_scene);
            //primaryStage.setScene(startMenu_scene);
        });
        VBox openSavePanel_box = new VBox(openSave_label, saves_list, loadSave_Button, openSaveBack_button);
        openSave_group.getChildren().add(openSavePanel_box);
        // ---------------------------------------------------------

        primaryStage.setScene(startMenu_scene);
        primaryStage.show();
    }

    public void open_save(String path){
        String save_data = new String;
        try {
        }
        catch FileNotFoundException {
        }
}
