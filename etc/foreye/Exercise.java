import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
  private final static Color RECTANGLE_COLOR = Color.SEAGREEN;

  private final static double DEFAULT_DURATION = 5 * 1000;

  private Pane pane;
  private Slider slider;
  private Rectangle rectangle;
  private TranslateTransition translate1;
  private TranslateTransition translate2;
  private TranslateTransition translate3;
  private TranslateTransition translate4;
  private SequentialTransition sequential;


  @Override
  public void start(Stage stage) {
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
    double width = primaryScreenBounds.getWidth();
    double height = primaryScreenBounds.getHeight();
    System.out.println("width = " + width + " height = " + height);
    height *= 0.95;

    GridPane gridPane = new GridPane();
    Scene scene = new Scene(gridPane, width, height, Color.BLACK);

    HBox hBox = new HBox();
    hBox.setPadding(new Insets(10, 10, 10, 10));
    hBox.setStyle("-fx-background-color: gray;");
    gridPane.add(hBox, 0, 0);

    pane = new Pane();
    pane.setPrefSize(width, height);
    pane.setStyle("-fx-background-color: black;");
    gridPane.add(pane, 0, 1);
    pane.widthProperty().addListener(new ChangeListener<Number>() {
        public void changed(ObservableValue<? extends Number> ov,
	  Number old_val, Number new_val) {
	  adjustTransition();
	}
      });
    pane.heightProperty().addListener(new ChangeListener<Number>() {
        public void changed(ObservableValue<? extends Number> ov,
	  Number old_val, Number new_val) {
	  adjustTransition();
	}
      });


    Label label = new Label("Duration");
    hBox.getChildren().add(label);

    slider = new Slider();
    slider.setMin(1000);
    slider.setMax(9000);
    slider.setValue(DEFAULT_DURATION);
    slider.setShowTickLabels(true);
    slider.setShowTickMarks(true);
    slider.setMajorTickUnit(4000);
    slider.setMinorTickCount(1);
    hBox.getChildren().add(slider);
    slider.valueProperty().addListener(new ChangeListener<Number>() {
        public void changed(ObservableValue<? extends Number> ov,
	  Number old_val, Number new_val) {
	  adjustTransition();
	}
      });


    rectangle = new Rectangle(RECTANGLE_SIZE, RECTANGLE_SIZE);
    rectangle.setArcWidth(RECTANGLE_ARC_SIZE);
    rectangle.setArcHeight(RECTANGLE_ARC_SIZE);
    rectangle.setFill(RECTANGLE_COLOR);
    pane.getChildren().add(rectangle);


    stage.setScene(scene);
    stage.setTitle("For Eye Exercise");
    stage.show();

    adjustTransition();
  }

  private void adjustTransition() {
    double width = pane.getWidth();
    double height = pane.getHeight();
    double duration = slider.getValue();
    System.out.println("width = " + width + " height = " + height
      + " duration = " + duration);

    translate1 = new TranslateTransition();
    translate2 = new TranslateTransition();
    translate3 = new TranslateTransition();
    translate4 = new TranslateTransition();

    translate1.setDuration(Duration.millis(duration));
    translate2.setDuration(Duration.millis(duration));
    translate3.setDuration(Duration.millis(duration));
    translate4.setDuration(Duration.millis(duration));

    translate1.setFromX(RECTANGLE_PADDING);
    translate1.setToX(RECTANGLE_PADDING);
    translate1.setFromY(RECTANGLE_PADDING);
    translate1.setToY(height - RECTANGLE_PADDING - RECTANGLE_SIZE);

    translate2.setFromX(RECTANGLE_PADDING);
    translate2.setToX(width - RECTANGLE_PADDING - RECTANGLE_SIZE);
    translate2.setFromY(height - RECTANGLE_PADDING - RECTANGLE_SIZE);
    translate2.setToY(height - RECTANGLE_PADDING - RECTANGLE_SIZE);

    translate3.setFromX(width - RECTANGLE_PADDING - RECTANGLE_SIZE);
    translate3.setToX(width - RECTANGLE_PADDING - RECTANGLE_SIZE);
    translate3.setFromY(height - RECTANGLE_PADDING - RECTANGLE_SIZE);
    translate3.setToY(RECTANGLE_PADDING);

    translate4.setFromX(width - RECTANGLE_PADDING - RECTANGLE_SIZE);
    translate4.setToX(RECTANGLE_PADDING);
    translate4.setFromY(RECTANGLE_PADDING);
    translate4.setToY(RECTANGLE_PADDING);

    sequential = new SequentialTransition(
      rectangle, translate1, translate2, translate3, translate4);
    sequential.setCycleCount(Timeline.INDEFINITE);
    sequential.play();
  }


  public static void main(String[] args) {
    launch(args);
  }
}
