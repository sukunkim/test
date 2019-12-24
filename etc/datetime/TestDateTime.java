import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TestDateTime {
  public static void main(String[] args) {
    System.out.println("now = " + LocalDateTime.now());

    DateTimeFormatter format
      = DateTimeFormatter.ofPattern("MMM d yyyy  hh:mm a");

    LocalDateTime leaving = LocalDateTime.of(2013, Month.JULY, 20, 19, 30);

    ZoneId leavingZone = ZoneId.of("America/Los_Angeles");
    ZonedDateTime departure = ZonedDateTime.of(leaving, leavingZone);

    try {
      String out1 = departure.format(format);
      System.out.println("LEAVING: " + out1 + " (" + leavingZone + ")");
    } catch (DateTimeException e) {
    }

    ZoneId arrivingZone = ZoneId.of("Asia/Tokyo");
    ZonedDateTime arrival
      = departure.withZoneSameInstant(arrivingZone).plusMinutes(650);

    try {
      String out2 = arrival.format(format);
      System.out.println("ARRIVING: " + out2 + " (" + arrivingZone + ")");
    } catch (DateTimeException e) {
    }

    Instant timestamp = Instant.now();
    System.out.println("timestamp = " + timestamp);


    Instant epochI = Instant.EPOCH;
    System.out.println("EPOCH Instant = " + epochI);
    ZoneId zoneId = ZoneId.of("UTC");
    System.out.println("zoneId = " + zoneId);
    ZonedDateTime epochZ
      = ZonedDateTime.ofInstant(Instant.EPOCH, zoneId);
    System.out.println("EPOCH ZonedDateTime = " + epochZ);
  }
}
