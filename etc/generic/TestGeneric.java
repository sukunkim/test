public class TestGeneric {
  public static <T extends Object & Comparable<T>>
    void syncMem(T[] in, T[] out) {
  }

  public static void main(String[] args) {
  }
}
