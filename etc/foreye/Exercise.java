import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Exercise extends Application {

  private final static double RECTANGLE_SIZE = 40;
  private final static double RECTANGLE_ARC_SIZE = 20;
  private final static double RECTANGLE_PADDING = 20;
  private final static double RECTANGLE_DURATION = 1 * 1000;
  private final static Color RECTANGLE_COLOR = Color.SEAGREEN;

  private double width;
  private double height;


  @Override
  public void start(Stage stage) {
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    width = primaryScreenBounds.getWidth();
    height = primaryScreenBounds.getHeight() * 0.95;
    System.out.println("width = " + width + " height = " + height);

    GridPane gridPane = new GridPane();
    Scene scene = new Scene(gridPane, width, height, Color.BLACK);

    HBox hBox = new HBox();
    Pane pane = new Pane();
    pane.setStyle("-fx-background-color: black;");
    gridPane.add(hBox, 0, 0);
    gridPane.add(pane, 0, 1);


    Slider slider = new Slider();
    hBox.getChildren().add(slider);

    System.out.println("hBox.height = " + hBox.getHeight());
    System.out.println("hBox.prefHeight = " + hBox.getPrefHeight());
    System.out.println("hBox.MaxHeight = " + hBox.getMaxHeight());
    height -= hBox.getHeight();
    pane.setPrefSize(width, height);


    Rectangle rectangle = new Rectangle(RECTANGLE_PADDING, RECTANGLE_PADDING,
      RECTANGLE_SIZE, RECTANGLE_SIZE);
    rectangle.setArcWidth(RECTANGLE_ARC_SIZE);
    rectangle.setArcHeight(RECTANGLE_ARC_SIZE);
    rectangle.setFill(RECTANGLE_COLOR);
    pane.getChildren().add(rectangle);

    TranslateTransition translate1 = new TranslateTransition(
      Duration.millis(RECTANGLE_DURATION));
    translate1.setByY(height - RECTANGLE_SIZE - 2 * RECTANGLE_PADDING);

    TranslateTransition translate2 = new TranslateTransition(
      Duration.millis(RECTANGLE_DURATION));
    translate2.setByX(width - RECTANGLE_SIZE - 2 * RECTANGLE_PADDING);

    TranslateTransition translate3 = new TranslateTransition(
      Duration.millis(RECTANGLE_DURATION));
    translate3.setByY(-(height - RECTANGLE_SIZE - 2 * RECTANGLE_PADDING));

    TranslateTransition translate4 = new TranslateTransition(
      Duration.millis(RECTANGLE_DURATION));
    translate4.setByX(-(width - RECTANGLE_SIZE - 2 * RECTANGLE_PADDING));

    SequentialTransition sequential = new SequentialTransition(
      rectangle, translate1, translate2, translate3, translate4);
    sequential.setCycleCount(Timeline.INDEFINITE);
    sequential.play();


    stage.setScene(scene);
    stage.setTitle("For Eye Exercise");
    stage.show();
  }


  public static void main(String[] args) {
    launch(args);
  }
}
