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
package net.vexelon.jdevlog.vcs.git;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.revwalk.RevCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;

import net.vexelon.jdevlog.config.ConfigOptions;
import net.vexelon.jdevlog.config.Configuration;
import net.vexelon.jdevlog.config.Defs;
import net.vexelon.jdevlog.helpers.StringHelper;
import net.vexelon.jdevlog.vcs.SCMException;
import net.vexelon.jdevlog.vcs.Transformer;

public class RSSTransformer extends Transformer {
	
	final static Logger log = LoggerFactory.getLogger(RSSTransformer.class);
	
	public RSSTransformer(Configuration configuration) {
		super(configuration);
	}

	@Override
	public void transformHistoryLog(Iterator<?> entriesIterator)
			throws Exception {
		
		SyndFeed feed = createFeed(configuration);
		List<SyndEntry> syndEntries = new ArrayList<SyndEntry>(configuration.getInt(ConfigOptions.MAXLOG));
		
		for (; entriesIterator.hasNext();) {
			RevCommit rc = (RevCommit) entriesIterator.next();
			
	    	// construct <item>
	    	String title = String.format("%s: %s", 
	    			rc.getName().substring(0, Defs.DEFAULT_GIT_SHORTREV_SIZE),
	    			StringHelper.excerpt(rc.getShortMessage(), Defs.DEFAULT_EXCERPT_SIZE)
	    			);
	    	
	    	String linkPath;
//	    	try {
	    		linkPath = rc.getName();
//	    	}
//	    	catch (SCMException e) {
//	    		log.warn("Failed to set specific revision path: " + e.getMessage());
////	    		linkPath = configuration.get(ConfigOptions.SOURCE);
//			}

	    	SyndEntry entry = new SyndEntryImpl();
	    	entry.setTitle(title);
	    	entry.setLink(linkPath);
	    	entry.setAuthor(rc.getAuthorIdent().getName()); //TODO: Name or EMail ?
	    	entry.setPublishedDate(rc.getAuthorIdent().getWhen());
	    	
	    	StringBuilder content = new StringBuilder(100);
	    	content.append("Commit: <b>");
	    	content.append(rc.getName());
	    	content.append("</b>");
	    	content.append(Defs.HTML_NEWLINE);
	    	content.append(rc.getFullMessage().replace("\n", Defs.HTML_NEWLINE));
	    	
	    	SyndContent description = new SyndContentImpl();
	    	description.setType("text/html");
	    	description.setValue(content.toString());
	    	entry.setDescription(description);
	    	
	    	// add <item>
            syndEntries.add(entry);	    	
		}
		
	    feed.setEntries(syndEntries);
	    saveFeed(feed, new File(configuration.getString(ConfigOptions.DESTINATION)));
	}
}
