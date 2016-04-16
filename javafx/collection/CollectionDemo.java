import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.FXCollections;

public class CollectionDemo {
  public static void main(String[] args) {
    List<String> list = new ArrayList<String>();
    ObservableList<String> observableList = FXCollections.observableList(list);

    observableList.addListener(new ListChangeListener() {
        @Override
	public void onChanged(ListChangeListener.Change change) {
	  System.out.println("Detected a change!");
	  while (change.next()) {
	    System.out.println("Was added? " + change.wasAdded());
	    System.out.println("Was removed? " + change.wasRemoved());
	    System.out.println("Was replaced? " + change.wasReplaced());
	    System.out.println("Was permutated? " + change.wasPermutated());
	  }
	}
      });
    observableList.addListener(new InvalidationListener() {
        @Override public void invalidated(Observable o) {
	  System.out.println("Detected an invalidation!");
	}
      });

    observableList.add("item d");
    list.add("item b");
    System.out.println("Size: " + observableList.size());


    Map<String, String> map = new HashMap<String, String>();
    ObservableMap<String, String> observableMap
      = FXCollections.observableMap(map);

    observableMap.addListener(new MapChangeListener() {
        @Override public void onChanged(MapChangeListener.Change change) {
	  System.out.println("Detected a change! ");
	}
      });

    observableMap.put("key 1", "value 1");
    System.out.println("Size: " + observableMap.size());
    map.put("key 2", "value 2");
    System.out.println("Size: " + observableMap.size());


    list.add("item a");
    list.add("item c");
    System.out.println("FXCollections.sort()");
    FXCollections.sort(observableList);
    System.out.println("Collections.reverse()");
    Collections.reverse(observableList);
    System.out.println("FXCollections.reverse()");
    FXCollections.reverse(observableList);
  }
}
