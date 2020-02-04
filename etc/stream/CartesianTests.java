import java.util.Arrays;
import java.util.stream.Stream;

public class CartesianTests {

  public static void main(String[] args) {
    String[] stat = {"min", "max", "acc"};
    String[] period = {"day", "month", "year", "alltime"};
    Stream<String> statStream = Arrays.<String>stream(stat);
    Stream<String> periodStream = Arrays.<String>stream(period);

    String[] cartesian = Arrays.<String>stream(period).flatMap(p ->
        Arrays.<String>stream(stat).map(s -> p + "_" + s))
      .toArray(String[]::new);

    System.out.println(Arrays.toString(cartesian));
  }
}
