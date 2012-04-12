/*
 * The MIT License
 * 
 * Copyright (c) 2012 Petar Petrov
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
				true, "SCM location, e.g., for SVN: http://svn.apache.org/repos/asf/spamassassin");
		options.addOption(ConfigOptions.DESTINATION.getShortName(), ConfigOptions.DESTINATION.getName(), 
				true, "Destination where to write the RSS file, e.g., C:\\Tests\\scm.xml");		
		options.addOption(ConfigOptions.TYPE.getShortName(), ConfigOptions.TYPE.getName(), 
				true, "[svn|git]");
		options.addOption(ConfigOptions.MAXLOG.getShortName(), ConfigOptions.MAXLOG.getName(), 
				true, "Amount of (latest) log messages to fetch.");
		options.addOption(ConfigOptions.USERNAME.getShortName(), ConfigOptions.USERNAME.getName(), 
				true, "Username for authentication.").getOption(ConfigOptions.USERNAME.getShortName()).setRequired(false);
		options.addOption(ConfigOptions.PASSWORD.getShortName(), ConfigOptions.PASSWORD.getName(),
				true, "Password for authentication.").getOption(ConfigOptions.PASSWORD.getShortName()).setRequired(false);
		options.addOption(ConfigOptions.VERBOSE.getShortName(), ConfigOptions.VERBOSE.getName(), 
				false, "Additional logging.").getOption(ConfigOptions.VERBOSE.getShortName()).setOptionalArg(true);
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
				if (option == ConfigOptions.VERBOSE) {
					parsedOptions.put(option, Boolean.toString(true));
				}
				else {
					parsedOptions.put(option, cmdLine.getOptionValue(option.getShortName()));
				}
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
