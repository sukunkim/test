import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;


public class TestDBCP {

  private BasicDataSource ds;


  private TestDBCP() {
    ds = new BasicDataSource();
    ds.setUrl("jdbc:mysql://124.137.25.50:23306/test_data");
    ds.setUsername("data_server");
    ds.setPassword("Sinbinet12#$");

    ds.setMinIdle(5);
    ds.setMaxIdle(10);
    ds.setMaxOpenPreparedStatements(100);

    try {
      Connection connection = ds.getConnection();
      PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM site");

      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        System.out.println(rs.getInt(1) + "," + rs.getString(2));
      }

    } catch (SQLException e) {
    }
  }

  public static TestDBCP getInstance() {
    TestDBCP testDBCP = new TestDBCP();

    return testDBCP;
  }


  public static void main(String[] args) {
    TestDBCP.getInstance();
  }
}
