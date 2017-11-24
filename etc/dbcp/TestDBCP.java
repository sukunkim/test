public class TestDBCP {


  private TestDBCP() {
  }

  public static TestDBCP getInstance() {
    TestDBCP testDBCP = new TestDBCP();

    return testDBCP;
  }


  public static void main(String[] args) {
    TestDBCP.getInstance();
  }
}
