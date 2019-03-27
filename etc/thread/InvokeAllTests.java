import java.util.LinkedList;
import java.util.List;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InvokeAllTests implements Runnable {

  private ExecutorService executorService;

  public InvokeAllTests() {
    executorService = Executors.newFixedThreadPool(2);
  }

  public void run() {
    List<DelayedPrinter> callableList = new LinkedList<DelayedPrinter>();
    List<Future<Integer>> futureList = null;

    callableList.add(new DelayedPrinter(1));
    callableList.add(new DelayedPrinter(4));
    callableList.add(new DelayedPrinter(2));

    try {
      futureList = executorService.invokeAll(callableList);
    } catch (InterruptedException e) {
    }

    for (int i = 0; i < futureList.size(); i++) {
      int result = 0;
      try {
        result = futureList.get(i).get();
      } catch (ExecutionException e) {
      } catch (InterruptedException e) {
      }
      System.out.println("[" + i + "]" + result);
    }

    System.out.println("run() finishes");
  }


  private class DelayedPrinter implements Callable<Integer> {
    private int delay;

    private DelayedPrinter(int delay) {
      this.delay = delay;
    }

    public Integer call() {
      try {
        Thread.sleep(1000 * delay);
      } catch (InterruptedException e) {
      }

      System.out.println("delay = " + delay);

      return 2 * delay;
    }
  }


  public static void main(String[] args) {
    new Thread(new InvokeAllTests()).start();

    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
    }

    System.out.println("main() finishes");
  }
}
