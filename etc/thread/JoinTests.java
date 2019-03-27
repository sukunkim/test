public class JoinTests implements Runnable {

  public void run() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
    }
    System.out.println("run() finishes");
  }

  public static void main(String[] args) {
    Thread thread = new Thread(new JoinTests());
    try {
      thread.join();
    } catch (InterruptedException e) {
    }
    thread.start();
    System.out.println("main() finishes");
  }
}
