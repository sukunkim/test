import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

  @Test
  void groupedAssertions() {
    assertAll(() -> assertEquals(2, 1), () -> assertEquals(2, 3));
  }

  @Nested
  class TimerTest {
    @Test
    void test11() {
      assertEquals(3, 1 + 2);
    }
  }

  @ParameterizedTest
  @ValueSource(ints = {6, 7, 8})
  void findById(Integer id) {
    assertEquals(id, id);
  }
}
