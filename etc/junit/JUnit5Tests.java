import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import static org.junit.jupiter.api.DynamicContainer.dynamicContainer;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

import org.junit.jupiter.api.condition.DisabledOnJre;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

import org.junit.jupiter.api.extension.ExtensionContext;

import org.junit.jupiter.api.function.ThrowingConsumer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConverter;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;


public class JUnit5Tests {

  @Test
  @EnabledOnJre(JRE.JAVA_8)
  @EnabledOnOs(OS.WINDOWS)
  void test1(TestInfo testInfo) {
    assertEquals("test1(TestInfo)", testInfo.getDisplayName());
    assumeTrue(false);
    assertEquals(2, 1 + 1);
  }

  @Test
  @DisabledOnJre(JRE.JAVA_9)
  @DisabledOnOs(OS.LINUX)
  void test2(TestReporter testReporter) {
    testReporter.publishEntry("a key", "a value");
    assertEquals(2, 1 + 2);
    assertEquals(2, 1 + 3);
  }

  @Test
  @EnabledIfSystemProperty(named = "os.arch", matches = ".*86.*")
  @EnabledIfEnvironmentVariable(named = "USERDOMAIN", matches = "HURACAN")
  void groupedAssertions() {
    assertAll(() -> assertEquals(2, 1), () -> assertEquals(2, 3));
  }

  @Nested
  class TimerTest {
    @Test
    @EnabledIf("2 * 3 == 6")
    void test11() {
      assertEquals(3, 1 + 2);
    }
  }
}


class RepetitionTests {

  @BeforeEach
  void beforeEach(TestInfo testInfo, RepetitionInfo repetitionInfo) {
    int currentRepetition = repetitionInfo.getCurrentRepetition();
    int totalRepetition = repetitionInfo.getTotalRepetitions();
    System.out.println(
      "Repetition " + currentRepetition + " of " + totalRepetition);
  }

  @RepeatedTest(value = 3, name = RepeatedTest.LONG_DISPLAY_NAME)
  void repeatedTest() {
    assertEquals(4, 1 + 3);
  }

  @RepeatedTest(value = 1, name = "{displayName} {currentRepetition}/{totalRepetitions}")
  @DisplayName("Repeat!")
  void customDisplayName(TestInfo testInfo) {
    assertEquals(testInfo.getDisplayName(), "Repeat! 1/1");
  }
}


class ParameterTests {

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3})
  void testWithValueSource(int argument) {
    assertTrue(argument > 0 && argument < 4);
  }

  @ParameterizedTest
  @EnumSource(value = TimeUnit.class, mode = Mode.MATCH_ALL, names = ".*SECONDS")
  void testWithEnumSource(TimeUnit timeUnit) {
    System.out.println("timeUnit = " + timeUnit);
  }

  @ParameterizedTest
  @MethodSource("stringProvider")
  void testWithMethodSource(String argument, TestReporter testReporter) {
    testReporter.publishEntry("argument", argument);
    assertNotNull(argument);
  }

  static Stream<String> stringProvider() {
    return Stream.of("foo", "bar");
  }

  @ParameterizedTest
  @MethodSource("stringIntAndListProvider")
  void testWithMultiArgMethodSource(String str, int num, List<String> list) {
    assertEquals(3, str.length());
    assertTrue(num >= 1 & num <= 2);
    assertEquals(2, list.size());
  }

  static Stream<Arguments> stringIntAndListProvider() {
    return Stream.of(
      Arguments.of("foo", 1, Arrays.asList("a", "b")),
      Arguments.of("bar", 2, Arrays.asList("x", "y", "z"))
      );
  }

  @ParameterizedTest(name = "{index} ==> first =''{0}'', second={1}")
  @CsvSource({ "foo, 1", "bar, 2", "'baz, qux', 0" })
  void testWithCsvSource(String first, int second) {
    assertNotNull(first);
    assertNotEquals(0, second);
  }

  @ParameterizedTest
  @ArgumentsSource(MyArgumentsProvider.class)
  void testWithArgumentsSource(String str, int num) {
    assertEquals(3, str.length());
    assertTrue(num >= 1 & num <= 2);
  }

  static class MyArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(
      ExtensionContext context) {
      return Stream.of(
        Arguments.of("foo", 1),
	Arguments.of("bar", 3)
	);
    }
  }
}


class ConversionTests {

  @ParameterizedTest
  @ValueSource(strings = "42 cats")
  void testWithImplicitConversion(Book book) {
    assertEquals("42 cats", book.getTitle());
  }

  static class Book {
    private final String title;

    public Book(String title) {
      this.title = title;
      System.out.println("Constructor");
    }

    public static Book fromTitle(String title) {
      System.out.println("Factory");
      return new Book(title);
    }

    public String getTitle() {
      return title;
    }
  }

  @ParameterizedTest
  @EnumSource(TimeUnit.class)
  void testWithExplicitConversion(
    @ConvertWith(ToStringArgumentConverter.class) String argument) {

    System.out.println(argument);
    assertNotNull(TimeUnit.valueOf(argument));
  }

  static class ToStringArgumentConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object source, Class<?> targetType) {
      assertEquals(String.class, targetType, "Can only convert to String");
      return String.valueOf(source);
    }
  }

  @ParameterizedTest
  @ValueSource(strings = { "01.01.2017", "31.12.2017" })
  void testWithExplicitTime(
    @JavaTimeConversionPattern("dd.MM.yyyy") LocalDate argument) {
    assertEquals(2017, argument.getYear());
  }

  @BeforeEach
  void beforeEach(TestInfo testInfo) {
    System.out.println(testInfo.getDisplayName());
  }
}


class DynamicFactoryTests {
  @TestFactory
  List<String> dynamicTestsWithInvalidReturnType() {
    return Arrays.asList("Hello");
  }

  @TestFactory
  Collection<DynamicTest> dynamicTestsFromCollection() {
    return Arrays.asList(
      dynamicTest("1st dynamic test", () -> assertTrue(true)),
      dynamicTest("2nd dynamic test", () -> assertEquals(4, 2 * 2))
    );
  }

  @TestFactory
  Iterable<DynamicTest> dynamicTestsFromIterable() {
    return Arrays.asList(
      dynamicTest("3rd dynamic test", () -> assertTrue(true)),
      dynamicTest("4th dynamic test", () -> assertEquals(4, 2 * 2))
    );
  }

  @TestFactory
  Iterator<DynamicTest> dynamicTestsFromIterator() {
    return Arrays.asList(
      dynamicTest("5th dynamic test", () -> assertTrue(true)),
      dynamicTest("6th dynamic test", () -> assertEquals(4, 2 * 2))
    ).iterator();
  }

  @TestFactory
  Stream<DynamicTest> dynamicTestFromStream() {
    return Stream.of("A", "B", "C")
      .map(str -> dynamicTest("test" + str, () -> { assertEquals(str, str); }));
  }

  @TestFactory
  Stream<DynamicTest> dynamicTestFromIntStream() {
    return IntStream.iterate(0, n -> n + 2).limit(10)
      .mapToObj(n -> dynamicTest("test" + n, () -> assertTrue(n % 2 == 0)));
  }

  @TestFactory
  Stream<DynamicTest> generateRandomNumberOfTests() {
    Iterator<Integer> inputGenerator = new Iterator<Integer>() {
      Random random = new Random();
      int current;

      @Override
      public boolean hasNext() {
        current = random.nextInt(100);
	return current % 7 != 0;
      }

      @Override
      public Integer next() {
        return current;
      }
    };

    Function<Integer, String> displayNameGenerator = (input) -> "input:" + input;

    ThrowingConsumer<Integer> testExecutor = (input) -> assertTrue(input % 7 != 0);

    return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
  }

  @TestFactory
  Stream<DynamicTest> dynamicTestsWithSimpleStream() {
    return Stream.of(
      dynamicTest("length > 0", () -> assertTrue(1 > 0)),
      dynamicTest("non empty", () -> assertFalse("D".isEmpty()))
    );
  }

  @TestFactory
  Stream<DynamicTest> dynamicTestsWithContainers() {
    return Stream.of("A", "B", "C")
      .map(input -> //dynamicContainer("Container " + input, Stream.of(
        //dynamicTest("not null", () -> assertNotNull(input)),
	dynamicContainer("properties", Stream.of(
	  dynamicTest("length > 0", () -> assertTrue(input.length() > 0)),
	  dynamicTest("non empty", () -> assertFalse(input.isEmpty()))
	  //))
	)));
  }
}


/*
interface TestLifecycleLogger {
  @BeforeEach
  default void beforeEachTest() {
    System.out.println("beforeEachTest");
  }
}

class TestInterfaceDemo implements TestLifecycleLogger {
  @Test
  void isEqualValue() {
    assertEquals(1, 2, "is always equal");
  }
}
*/
