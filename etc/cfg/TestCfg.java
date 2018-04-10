import java.net.URL;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;

import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.ex.ConversionException;


public class TestCfg {

  private TestCfg() {
    Parameters params = new Parameters();

    FileBasedConfigurationBuilder<XMLConfiguration> builder
      = new FileBasedConfigurationBuilder<XMLConfiguration>(
        XMLConfiguration.class)
	.configure(params.xml().setFileName("mycfg.xml")
	.setSchemaValidation(true));

    XMLConfiguration cfg = null;
    try {
      cfg = builder.getConfiguration();

    } catch (ConfigurationException e) {
      System.out.println("ConfigurationException = " + e);
      return;
    }

    int port = 0;
    try {
      port = cfg.getInt("port");

    } catch (ConversionException e) {
      System.out.println("ConversionException = " + e);
      return;
    }

    System.out.println("port = " + port);
  }

  public static TestCfg getInstance() {
    TestCfg testCfg = new TestCfg();

    return testCfg;
  }


  public static void main(String[] args) {
    TestCfg.getInstance();
  }
}
