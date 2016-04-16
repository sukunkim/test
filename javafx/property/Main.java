import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class Main {

  public static void main(String[] args) {
    Bill bill1 = new Bill();
    Bill bill2 = new Bill();
    Bill bill3 = new Bill();

    NumberBinding total = Bindings.add(
      bill1.amountDueProperty().add(bill2.amountDueProperty()),
      bill3.amountDueProperty());

    total.addListener(new InvalidationListener() {
        @Override public void invalidated(Observable o) {
	  System.out.println("The binding is now invalid");
	}
      });
    total.addListener(new ChangeListener() {
        @Override public void changed(
	  ObservableValue o, Object oldVal, Object newVal) {
	  System.out.println("The binding has changed");
	}
      });

    bill1.setAmountDue(200.00);

    bill2.setAmountDue(100.00);
    bill3.setAmountDue(75.00);

    System.out.println(total.getValue());

    bill3.setAmountDue(150.00);

    System.out.println(total.getValue());


    final DoubleProperty a = new SimpleDoubleProperty(1);
    final DoubleProperty b = new SimpleDoubleProperty(2);
    final DoubleProperty c = new SimpleDoubleProperty(3);
    final DoubleProperty d = new SimpleDoubleProperty(4);

    DoubleBinding db = new DoubleBinding() {
        {
	  super.bind(a, b, c, d);
	}

        @Override protected double computeValue() {
	  return (a.get() * b.get()) + (c.get() * d.get());
	}
      };

    System.out.println(db.get());
    b.set(3);
    System.out.println(db.get());
  }
}


class Bill {
  private DoubleProperty amountDue = new SimpleDoubleProperty();

  public final double getAmountDue() { return amountDue.get(); }

  public final void setAmountDue(double value) { amountDue.set(value); }

  public DoubleProperty amountDueProperty() { return amountDue; }
}
