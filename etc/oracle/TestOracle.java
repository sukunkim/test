import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestOracle {

  private TestOracle() {
    try {
      Class.forName("oracle.jdbc.OracleDriver");

    } catch (ClassNotFoundException e) {
      System.out.println("ClassNotFoundException = " + e);
    }

    try {
      Connection conn = DriverManager.getConnection(
        "",
        "",
        "");

      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM location_update");

      while (rs.next()) {
        System.out.println("MobileId = " + rs.getInt(1));
      }

      conn.close();

    } catch (SQLException e) {
      System.out.println("SQLException = " + e);
    }
  }

  public static TestOracle getInstance() {
    TestOracle testOracle = new TestOracle();

    return testOracle;
  }


  public static void main(String[] args) {
    TestOracle.getInstance();
  }
}
