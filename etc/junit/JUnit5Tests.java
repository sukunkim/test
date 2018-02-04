import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class JUnit5Tests {

  @Test
  void test1() {
    assumeTrue(false);
    assertEquals(2, 1 + 1);
  }

  @Test
  void test2() {
    assertEquals(2, 1 + 2);
    assertEquals(2, 1 + 3);
  }

  @Nested
  class TimerTest {
    @Test
    void test11() {
      assertEquals(3, 1 + 2);
    }
  }
}
