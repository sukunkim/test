import java.net.URL;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;

import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;

import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.ex.ConversionException;

import org.apache.commons.configuration2.resolver.CatalogResolver;
import org.apache.commons.configuration2.resolver.DefaultEntityResolver;


public class TestCfg {

  private TestCfg() {
    Parameters params = new Parameters();

    DefaultEntityResolver defaultEntityResolver = new DefaultEntityResolver();
    URL dtdURL = getClass().getResource("mycfg.dtd");
    defaultEntityResolver.registerEntityId(
      "-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN", dtdURL);

    /*
    CatalogResolver resolver = new CatalogResolver();
    resolver.setCatalogFiles("mycfg.xsd");
    */

    FileBasedConfigurationBuilder<XMLConfiguration> builder
      = new FileBasedConfigurationBuilder<XMLConfiguration>(
        XMLConfiguration.class)
	.configure(params.xml().setFileName("mycfg.xml")
	  .setEntityResolver(defaultEntityResolver).setValidating(true));
//	  .setEntityResolver(resolver).setSchemaValidation(true));

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
