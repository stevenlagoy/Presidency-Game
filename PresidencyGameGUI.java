import java.io.*;
import java.lang.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;

public class PresidencyGameGUI extends Application {

    public static void main(String[] args)
    {
        launch(args);
    }

    static final File savesDirectory = new File("savegames/");

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
            Alert newGame_alert = new Alert(AlertType.CONFIRMATION, "Start a New Game?", ButtonType.OK, ButtonType.CANCEL);
            newGame_alert.setTitle("Confirmation");
            newGame_alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK){
                    controller.switchScene(newGame_scene);
                }
            });
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
        Button startGame_button = new Button("Start Game");
        Label age_label = new Label("Candidate Age:");
        TextField age_field = new TextField();
        age_field.setPromptText("35-100");
        HBox age_box = new HBox(age_label, age_field);
        Label origin_label = new Label("Origin State:");
        ComboBox<String> origin_field = new ComboBox<>();
        HBox origin_box = new HBox(origin_label, origin_field);
        Label education_label = new Label("Education:");
        Label educationSlider_label = new Label("6 - Bachelors Degree");
        Slider education_slider = new Slider(1, 8, 6);
        education_slider.setBlockIncrement(1);
        education_slider.setSnapToTicks(true);
        education_slider.setMajorTickUnit(1);
        education_slider.setMinorTickCount(0);
        education_slider.setShowTickMarks(true);
        education_slider.setOnMouseDragged(e -> {
            switch ((int) Math.round(education_slider.getValue())){
                case 1:
                    educationSlider_label.setText("1 - Primary Education");
                    break;
                case 2:
                    educationSlider_label.setText("2 - Lower Secondary Education");
                    break;
                case 3:
                    educationSlider_label.setText("3 - Upper Secondary Education");
                    break;
                case 4:
                    educationSlider_label.setText("4 - Post-secondary Education");
                    break;
                case 5:
                    educationSlider_label.setText("5 - Tertiary Education");
                    break;
                case 6:
                    educationSlider_label.setText("6 - Bachelors Degree");
                    break;
                case 7:
                    educationSlider_label.setText("7 - Masters Degree");
                    break;
                case 8:
                    educationSlider_label.setText("8 - Doctoral Degree");
                    break;
            }
        });
        HBox education_box = new HBox(education_label, education_slider, educationSlider_label);
        education_box.setPrefWidth(10.0);
        Button newGameBack_Button = new Button("Back");
        newGameBack_Button.setOnAction(e -> {
            controller.switchScene(startMenu_scene);
            //primaryStage.setScene(startMenu_scene);
        });
        VBox newGamePanel_box = new VBox(newGame_label, age_box, origin_box, education_box, startGame_button, newGameBack_Button);
        newGame_group.getChildren().add(newGamePanel_box);
        // -------------------------------------------------------

        // create openSave_scene
        Label openSave_label = new Label("Open a Save Game:");
        // import the list of saves here
        // list all the files inside the savegames folder
        // gather information from the first line of each save
        // list that information on the listview
        List<String> savesStringList = new ArrayList<>();
        ListView<String> savesList = new ListView<String>();
        for(File saveFile : savesDirectory.listFiles()){
            try {
            Scanner scan = new Scanner(saveFile);
            String infoline = scan.nextLine();
            savesStringList.add(infoline);
            savesList.getItems().add(infoline);
            scan.close(); // possible that the scanner is not closed after exception? 
            }
            catch (FileNotFoundException e){
                System.out.println(e);
            }
            finally {
            }
        }
        Button loadSave_Button = new Button("Load Save");
        loadSave_Button.setOnAction(e -> {
            System.out.printf("Loading save: %s%n", savesList.getSelectionModel().getSelectedItem());
            // this will be where the main game is opened
        });
        Button openSaveBack_button = new Button("Back");
        openSaveBack_button.setOnAction(e -> {
            controller.switchScene(startMenu_scene);
            //primaryStage.setScene(startMenu_scene);
        });
        VBox openSavePanel_box = new VBox(openSave_label, savesList, loadSave_Button, openSaveBack_button);
        openSave_group.getChildren().add(openSavePanel_box);
        // ---------------------------------------------------------

        primaryStage.setScene(startMenu_scene);
        primaryStage.show();
    }

    public String getSaveData(File file){
        String saveData = new String();
        try {
            // read in the data from the savefile
            Scanner scan = new Scanner(file);
            while(scan.hasNext()){
                saveData += scan.nextLine();
            }
            scan.close();
            return saveData;
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
            return "";
        }
    }
}
