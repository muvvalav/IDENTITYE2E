import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Log4jConfiguration {

	static Logger logger = Logger.getLogger(Log4jConfiguration.class);

	public static void xmlConfig() {
		String log4jConfigFile = System.getProperty("user.dir")
				+ File.separator + "log4j.xml";
		DOMConfigurator.configure(log4jConfigFile);
	}

}
