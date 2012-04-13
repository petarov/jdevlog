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
package net.vexelon.jdevlog;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.vexelon.jdevlog.config.ConfigurationBuilder;
import net.vexelon.jdevlog.config.PrintHelpException;

public class shellWorkflow extends Workflow {
	
	final static Logger log = LoggerFactory.getLogger(shellWorkflow.class);
	
	private String[] arguments;

	public shellWorkflow(String[] args) {
		this.arguments = args;
	}
	
	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("jdevlog.jar [options]", options);
	}	
	
	@Override
	public void configure() throws Exception {
		
		ConfigurationBuilder builder = new ConfigurationBuilder();
		try {
			log.info("Parsing command line ...");
			
			builder.parse(arguments);
			this.configuration = builder.build();
			
			log.info("Validating parameters ...");
			this.configuration.validate();
		}
		catch (PrintHelpException e) {
			printHelp(builder.getCmdLineOptions());
			throw new PrintHelpException();
		}
		catch(Exception e) {
			log.error("One or more parameters missing or not valid! " + e.getMessage());
			printHelp(builder.getCmdLineOptions());
			throw new PrintHelpException();
		}
	}
	
	
}
