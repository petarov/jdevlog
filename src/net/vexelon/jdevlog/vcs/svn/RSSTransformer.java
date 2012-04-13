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
package net.vexelon.jdevlog.vcs.svn;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

import net.vexelon.jdevlog.config.ConfigOptions;
import net.vexelon.jdevlog.config.Configuration;
import net.vexelon.jdevlog.config.Defs;
import net.vexelon.jdevlog.helpers.StringHelper;
import net.vexelon.jdevlog.vcs.SCMException;
import net.vexelon.jdevlog.vcs.Transformer;

public class RSSTransformer extends Transformer {
	
	final static Logger log = LoggerFactory.getLogger(RSSTransformer.class);
	
	protected SvnSource source;
	
	public RSSTransformer(Configuration configuration, SvnSource source) {
		super(configuration);
		this.source = source;
	}

	@Override
	public void transformHistoryLog(Iterator<?> entriesIterator) throws Exception {
		
		SyndFeed feed = createFeed(configuration);
		List<SyndEntry> syndEntries = new ArrayList<SyndEntry>(configuration.getInt(ConfigOptions.MAXLOG));
		
	    for (; entriesIterator.hasNext();) {

	    	SVNLogEntry logEntry = (SVNLogEntry) entriesIterator.next();
	    	
	    	// construct <item>
	    	String title = String.format("r%d: %s", 
	    			logEntry.getRevision(), 
	    			StringHelper.excerpt(logEntry.getMessage(), Defs.DEFAULT_EXCERPT_SIZE)
	    			);
	    	
	    	String linkPath;
	    	try {
	    		linkPath = source.getRevisionPath(logEntry.getRevision());
	    	}
	    	catch (SCMException e) {
	    		log.warn("Failed to set specific revision path: " + e.getMessage());
	    		linkPath = configuration.get(ConfigOptions.SOURCE);
			}
	    	
	    	String message = logEntry.getMessage();
	    	
	    	SyndEntry entry = new SyndEntryImpl();
	    	entry.setTitle(title);
	    	entry.setLink(linkPath);
	    	entry.setAuthor(logEntry.getAuthor());
	    	entry.setPublishedDate(logEntry.getDate());
	    	
        	SyndContent description = new SyndContentImpl();
//        	ContentItem content = new ContentItem(); // Rome RSS module
////        	content.setContentEncoding("text/html");
//        	content.setContentFormat("http://www.w3.org/TR/html4/");
//        	content.setContentAbout("Paragraph");
        	
	    	// get detailed changes
            if (logEntry.getChangedPaths().size() > 0) {
            	description.setType("text/html");

            	Set<?> changedPathsSet = logEntry.getChangedPaths().keySet();

                StringBuilder fullMessage = new StringBuilder(100);
                fullMessage.append(String.format("<b>Revision %d</b>", logEntry.getRevision()));
                fullMessage.append(Defs.HTML_NEWLINE);
                fullMessage.append(logEntry.getMessage());
                fullMessage.append(Defs.HTML_NEWLINE);
                
                String svnRevPath = source.getRevisionPath(logEntry.getRevision(), true);
                
                for (Iterator<?> changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
                    SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
                    
                    fullMessage.append(Defs.HTML_NEWLINE);
                    fullMessage.append(entryPath.getType());
                    fullMessage.append(" <a href=\"");
                    fullMessage.append(svnRevPath);
                    fullMessage.append(entryPath.getPath());
                    fullMessage.append(" \">");
                    fullMessage.append(entryPath.getPath());
                    fullMessage.append("</a>");
                }
                
                message = fullMessage.toString();
            }
            
            // set contents (Thanks!! goes to Mark McLaren / http://stackoverflow.com/questions/9887432/putting-contentencoded-in-rss-feed-using-rome)
            description.setValue(message.replace("\n", Defs.HTML_NEWLINE));
//            content.setContentValue(message);
//            
//            CDATA cdata = new CDATA(message);
//            List<Content> contentValueDOM = new ArrayList<Content>(); 
//            contentValueDOM.add(cdata);
//            content.setContentValueDOM(contentValueDOM);
//            
//            List<ContentItem> contents = new ArrayList<ContentItem>();
//            contents.add(content);
//            
//            ContentModule contentModule = new ContentModuleImpl(); 
//            contentModule.setContents(contents);
//            contentModule.setContentItems(contents);
//            entry.getModules().add(contentModule);
            
            entry.setDescription(description);
            
            // set RSS <item>
            syndEntries.add(entry);
        }   
	    
	    feed.setEntries(syndEntries);
	    saveFeed(feed, new File(configuration.getString(ConfigOptions.DESTINATION)));
	}
}
