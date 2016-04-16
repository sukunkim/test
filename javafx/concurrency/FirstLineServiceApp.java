import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class FirstLineServiceApp extends Application {

  private static final int MAX_ITERATIONS = 10;


  @Override
  public void start(Stage stage) {
    final Label label = new Label();
    label.setText("Default");

    Task<Integer> task = new Task<Integer>() {
        @Override protected Integer call() {
	  int iterations;
	  for (iterations = 0; iterations < MAX_ITERATIONS; iterations++) {
	    if (isCancelled()) {
	      break;
	    }
	    System.out.println("Iteration " + iterations);
	    updateMessage("Iteration " + iterations);
	    // Do stuffs
	    updateProgress(iterations + 1, MAX_ITERATIONS);

	    try {
	      Thread.sleep(100);
	    } catch (InterruptedException e) {
	      if (isCancelled()) {
	        break;
	      }
	    }
	  }

	  return iterations;
	}

	@Override protected void succeeded() {
	  super.succeeded();
	  updateMessage("Done!");
	  label.setText("Done");
	}

	@Override protected void cancelled() {
	  super.succeeded();
	  updateMessage("Cancelled!");
	}

	@Override protected void failed() {
	  super.failed();
	  updateMessage("Failed!");
	}
      };

    Group root = new Group();
    Scene scene = new Scene(root, 500, 500, Color.GREY);

    ProgressBar pb = new ProgressBar(0);
    root.getChildren().add(pb);

    root.getChildren().add(label);

    stage.setScene(scene);
    stage.show();

    pb.progressProperty().bind(task.progressProperty());
    new Thread(task).start();

    FirstLineService service = new FirstLineService();
    service.setUrl("http://google.com");
    service.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
        @Override public void handle(WorkerStateEvent t) {
	  System.out.println("done:" + t.getSource().getValue());
	  label.setText("google");
	}
      });
    service.start();
  }


  private static class FirstLineService extends Service<String> {
    private StringProperty url = new SimpleStringProperty();

    public final void setUrl(String value) {
      url.set(value);
    }

    public final String getUrl() {
      return url.get();
    }

    public final StringProperty urlProperty() {
      return url;
    }

    @Override
    protected Task<String> createTask() {
      return new Task<String>() {
          @Override protected String call()
	    throws IOException, MalformedURLException {
	    try (BufferedReader in = new BufferedReader(new InputStreamReader(
	      new URL(getUrl()).openStream()))) {
	      return in.readLine();
	    }
	  }
        };
    }
  }


  public static void main(String[] args) {
    launch(args);
  }
}
