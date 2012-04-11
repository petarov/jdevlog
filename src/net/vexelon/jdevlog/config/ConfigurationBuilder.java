package net.vexelon.jdevlog.config;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ConfigurationBuilder {
	
	Map<ConfigOptions, String> parsedOptions = null;
	
	public ConfigurationBuilder() {
		
	}
	
	public Options getCmdLineOptions() {
		
		Options options = new Options();
		
		options.addOption(ConfigOptions.SOURCE.getShortName(), ConfigOptions.SOURCE.getName(), 
				true, "http://sipdroid.googlecode.com/svn/trunk/");
		options.addOption(ConfigOptions.DESTINATION.getShortName(), ConfigOptions.DESTINATION.getName(), 
				true, "Destination where to write the RSS file, e.g., C:\\Tests\\scm.xml");		
		options.addOption(ConfigOptions.TYPE.getShortName(), ConfigOptions.TYPE.getName(), 
				true, "[svn|git]");
		options.addOption(ConfigOptions.USERNAME.getShortName(), ConfigOptions.USERNAME.getName(), 
				true, "Username for authentication.").getOption(ConfigOptions.USERNAME.getShortName()).setRequired(false);
		options.addOption(ConfigOptions.PASSWORD.getShortName(), ConfigOptions.PASSWORD.getName(), 
				true, "Password for authentication.").getOption(ConfigOptions.PASSWORD.getShortName()).setRequired(false);
		options.addOption(ConfigOptions.VERBOSE.getShortName(), ConfigOptions.VERBOSE.getName(), 
				false, "Additional logging.");		
		options.addOption("h", "help", false, "Display command line parameters.").getOption("h").setOptionalArg(true);

		return options;
	}
	
	public void parse(String[] args) throws PrintHelpException, ParseException {
		
		CommandLineParser parser = new GnuParser();
		Options options = getCmdLineOptions();
		
		CommandLine cmdLine = parser.parse(options, args);
		if (cmdLine.hasOption("h") || cmdLine.hasOption("help")) {
			// throw exception for help menu. 
			// NOTE: This is an ugly and incorrect thing to do, but saves time at least for the prototype ;)
			throw new PrintHelpException();
		}
		
		parsedOptions = new HashMap<ConfigOptions, String>(10);
		
		for (ConfigOptions option :  ConfigOptions.values()) {
			if (cmdLine.hasOption(option.getName()) || cmdLine.hasOption(option.getShortName())) {
				parsedOptions.put(option, cmdLine.getOptionValue(option.getShortName()));
			}
		}
	}
	
	public Configuration build() {
		return new Configuration(this);
	}
	
	public Map<ConfigOptions, String> getParsedOptions() {
		return parsedOptions;
	}
}
