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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.vexelon.jdevlog.biztalk.SCMSource;
import net.vexelon.jdevlog.config.ConfigOptions;
import net.vexelon.jdevlog.config.Configuration;
import net.vexelon.jdevlog.config.Defs;
import net.vexelon.jdevlog.svn.RSSTransformer;
import net.vexelon.jdevlog.svn.SVNSource;

abstract class Workflow {
	
	final static Logger log = LoggerFactory.getLogger(Workflow.class);
	
	protected Configuration configuration;
	
	public abstract void configure() throws Exception; 
	
	public void start() throws Exception {
		
		SCMSource scm = null;
		
		// determine proper source type
		log.debug("Constructing Version Control object ...");
		
		String type = configuration.getString(ConfigOptions.TYPE);
		if (type.equalsIgnoreCase(Defs.SCM_SVN)) {
			scm = SVNSource.newInstance(configuration);
		}
		else {
			throw new RuntimeException(type + " is not yet supported!");
		}
		
		scm.initialize();
		scm.authenticate();

		RSSTransformer transformer = new RSSTransformer(this.configuration);
		transformer.transformHistoryLog(scm.getLastHistory(this.configuration.getLong(ConfigOptions.MAXLOG)));
	}
	
}
