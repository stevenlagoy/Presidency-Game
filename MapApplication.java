import java.lang.Math;

import java.io.FileInputStream;
import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.effect.Light.Point;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MapApplication extends Application
{
    static Engine engine = new Engine();

    public static double getDistance(double p1, double p2) { return p2 - p1; }

    static double scrollSensitivity = 100.0;
    static double aspectRatio;

    static double startX, startY;
    static double deltaX, deltaY;
    static double endX, endY;
    static double offsetX, offsetY;

    @Override
    public void start(Stage primaryStage){
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Map App");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);

        ImageView imageView = new ImageView();
        try {
            Image mapBackground = new Image(new FileInputStream("gfx\\United_States_of_America.png"));
            imageView.setImage(mapBackground);
            imageView.setX(0);
            imageView.setY(0);
            imageView.setFitWidth(2560);
            imageView.setPreserveRatio(true);
            aspectRatio = mapBackground.getWidth() / mapBackground.getHeight();
        }
        catch(IOException e){
            engine.log(e);
        }
        imageView.setOnMousePressed(event -> {
            startX = event.getSceneX();
            startY = event.getSceneY();
            offsetX = imageView.getX() - startX;
            offsetY = imageView.getY() - startY;

            // user has started a pan action
            if(event.getButton() == MouseButton.PRIMARY){
            }

            // user has started a rotate action
            else if(event.getButton() == MouseButton.MIDDLE){
            }
        });
        imageView.setOnMouseDragged(event -> {
            deltaX = getDistance(startX, event.getSceneX());
            deltaY = getDistance(startY, event.getSceneY());
            
            // panning action - image follows the cursor
            if(event.getButton() == MouseButton.PRIMARY){
                imageView.setX(startX + deltaX + offsetX);
                imageView.setY(startY + deltaY + offsetY);
                //System.out.printf("%f %f %f %f %n", startX, startY, offsetX, offsetY);
            }
            
            // rotating action - image rotates around the cursor
            else if(event.getButton() == MouseButton.MIDDLE){
                imageView.setRotate(getDistance(startX, event.getSceneX()) * 0.25);
            }
        });

        imageView.setOnMouseReleased(event -> {
            endX = event.getSceneX();
            endY = event.getSceneY();

            if(event.getButton() == MouseButton.MIDDLE){
                imageView.setRotate(0.0);
            }
        });

        imageView.setOnScroll(event -> {
            // zoom into the image
            // the more zoomed out, the less each zoom will change the view
            // make the zoom center on the cursor

            // get the location of the cursor
            // determine the scale
            // determine where the cursor will point after scaling
            // zoom the image
            // adjust the image's position to make the cursor line up in the same spot
            
            startX = imageView.getX();
            startY = imageView.getY();
            double mouseX = event.getSceneX() - startX, mouseY = event.getSceneY() - startY;
            double widthAdjustment = Math.sqrt(scrollSensitivity * imageView.getFitWidth());
            System.out.printf("%f %f %f %n", mouseX, mouseY, widthAdjustment);

            // scroll in event - zoom in
            if(event.getDeltaY() > 0){
                // zoom in, scale < 1
                double zoomScale = imageView.getFitWidth() / (imageView.getFitWidth() + widthAdjustment);
                double expectedX = mouseX * zoomScale, expectedY = mouseY * zoomScale;
                System.out.printf("%f %f %f %n", expectedX, expectedY, zoomScale);
                
                imageView.setX((startX - expectedX*zoomScale) / 2);
                imageView.setY((startY - expectedY*zoomScale) / 2);
                imageView.setFitWidth(imageView.getFitWidth() + widthAdjustment);
            }
            // scroll out event - zoom out
            else if(event.getDeltaY() < 0){
                // zoom out, scale > 1
                double zoomScale = imageView.getFitWidth() / (imageView.getFitWidth() - widthAdjustment);
                double expectedX = mouseX * zoomScale, expectedY = mouseY * zoomScale;
                System.out.printf("%f %f %f %n", expectedX, expectedY, zoomScale);
                
                imageView.setX((startX + expectedX/zoomScale) / 2);
                imageView.setY((startY + expectedY/zoomScale) / 2);
                imageView.setFitWidth(imageView.getFitWidth() - widthAdjustment);
            }
            else {
                // unreachable
                engine.log("IO_MOUSE", "A mouse Y-Delta of 0 was passed. Ignorable.", "MapApplication.java");
            }
        });
        System.out.println(aspectRatio);
        Group root = new Group(imageView);
        Scene scene = new Scene(root, 2560, 1440);
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.HOME){
                imageView.setX(0);
                imageView.setY(0);
                imageView.setFitWidth(2560);
            }
            else if(e.getCode() == KeyCode.PAGE_DOWN){
                imageView.setX(imageView.getX() + (scrollSensitivity / 2));
                imageView.setY(imageView.getY() + (scrollSensitivity/aspectRatio / 2));
                imageView.setFitWidth(imageView.getFitWidth() - scrollSensitivity);
            }
            else if(e.getCode() == KeyCode.PAGE_UP){
                imageView.setX(imageView.getX() - (scrollSensitivity / 2));
                imageView.setY(imageView.getY() - (scrollSensitivity/aspectRatio / 2));
                imageView.setFitWidth(imageView.getFitWidth() + scrollSensitivity);
            }
            else if(e.getCode() == KeyCode.UP){
                imageView.setY(imageView.getY() + scrollSensitivity);
            }
            else if(e.getCode() == KeyCode.DOWN){
                imageView.setY(imageView.getY() - scrollSensitivity);
            }
            else if(e.getCode() == KeyCode.LEFT){
                imageView.setX(imageView.getX() + scrollSensitivity);
            }
            else if(e.getCode() == KeyCode.RIGHT){
                imageView.setX(imageView.getX() - scrollSensitivity);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}