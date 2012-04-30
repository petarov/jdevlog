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

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import net.vexelon.jdevlog.config.PrintHelpException;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	final static Logger log = LoggerFactory.getLogger(Main.class);
	final static String LOG4PROPSFILE = "log4j.properties";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		File log4propsFile = new File(LOG4PROPSFILE);
		if (!log4propsFile.exists()) {
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(log4propsFile);
				writer.println("log4j.rootLogger=TRACE, WP");
				writer.println("log4j.appender.WP=org.apache.log4j.ConsoleAppender");
				writer.println("log4j.appender.WP.layout=org.apache.log4j.PatternLayout");
				writer.println("log4j.appender.WP.layout.ConversionPattern=%-4r [%t] %-5p %c %x - %m%n");
				writer.flush();
				writer.close();
			}
			catch (IOException e) {
				e.printStackTrace(); // Uh, oh...
			}
			finally {
				if (writer != null) writer.close();
			}
		}

		PropertyConfigurator.configure("log4j.properties"); // (try) init logging

		Workflow work = new shellWorkflow(args);

		try {
			work.configure();
			work.start();
		}
		catch(PrintHelpException e) {
			// Empty
		}
		catch (Exception e) {
			log.error(e.getMessage());
			log.trace("", e);
		}
	}
}
