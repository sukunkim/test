import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;


public class SubstitutorTests {

  public static void main(String[] args) {
    Map valueMap = new HashMap();
    valueMap.put("area", "7th");
    valueMap.put("zone", "Desk");

    String templateString = "${area} is in ${region}. ${area}";
    StringSubstitutor sub = new StringSubstitutor(valueMap);
    String resolvedString = sub.replace(templateString);

    System.out.println(resolvedString);
  }
}
