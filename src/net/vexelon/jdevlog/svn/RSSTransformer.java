package net.vexelon.jdevlog.svn;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.jdom.CDATA;
import org.jdom.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

import com.sun.syndication.feed.module.content.ContentItem;
import com.sun.syndication.feed.module.content.ContentModule;
import com.sun.syndication.feed.module.content.ContentModuleImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedOutput;

import net.vexelon.jdevlog.biztalk.Transformer;
import net.vexelon.jdevlog.config.ConfigOptions;
import net.vexelon.jdevlog.config.Configuration;
import net.vexelon.jdevlog.config.Defs;
import net.vexelon.jdevlog.helpers.StringHelper;

public class RSSTransformer implements Transformer {
	
	final static Logger log = LoggerFactory.getLogger(RSSTransformer.class);
	
	protected Configuration configuration;
	
	public RSSTransformer(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public void transformHistoryLog(Collection<?> entries) throws Exception {
		
		Locale loc = Locale.ENGLISH;
		Calendar cal = Calendar.getInstance(loc);
		
		SyndFeed feed = new SyndFeedImpl();
		feed.setFeedType("rss_2.0");
		
		feed.setTitle(Defs.DEFAULT_TITLE);
		feed.setLink(configuration.get(ConfigOptions.SOURCE));
		feed.setDescription("LAINA");
		feed.setAuthor("jdevlog");
		feed.setPublishedDate(cal.getTime());
		
		List<SyndEntry>  syndEntries = new ArrayList<SyndEntry>(configuration.getInt(ConfigOptions.MAXLOG));
		
	    for (Iterator<?> it = entries.iterator(); it.hasNext();) {

	    	SVNLogEntry logEntry = (SVNLogEntry) it.next();
	    	
	    	// construct <entry>
	    	String title = String.format("r%d / %s", 
	    			logEntry.getRevision(), 
	    			StringHelper.excerpt(logEntry.getMessage(), Defs.DEFAULT_EXCERPT_SIZE)
	    			);
	    	SyndEntry entry = new SyndEntryImpl();
	    	
	    	entry.setTitle(title);
	    	entry.setLink("");
	    	entry.setAuthor(logEntry.getAuthor());
	    	entry.setPublishedDate(logEntry.getDate());
//	    	cal.setTime(logEntry.getDate());
//	    	entry.setPubDate(cal.getTime());
//	    	entry.setPubDate(cal.getTime());
	    	
        	SyndContent description = new SyndContentImpl();
        	ContentItem content = new ContentItem(); // Rome RSS module
//        	content.setContentEncoding("text/html");
        	content.setContentFormat("http://www.w3.org/TR/html4/");
        	content.setContentAbout("Paragraph");
        	
        	String message = null;//logEntry.getMessage();
        	
	    	// get detailed changes
            if (logEntry.getChangedPaths().size() > 0) {
            	description.setType("text/html");

            	Set<?> changedPathsSet = logEntry.getChangedPaths().keySet();

                StringBuilder fullMessage = new StringBuilder(100);
                fullMessage.append(logEntry.getMessage());
//                fullMessage.append(Defs.HTML_NEWLINE);
                
                for (Iterator<?> changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
                    SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
                    
                    fullMessage.append("<p>");
                    fullMessage.append(entryPath.getType());
                    fullMessage.append(" ");
                    fullMessage.append(entryPath.getPath());
                    fullMessage.append("</p>");
                }
                
                message = fullMessage.toString();
            }
            
            // set contents (Thanks!! goes to Mark McLaren / http://stackoverflow.com/questions/9887432/putting-contentencoded-in-rss-feed-using-rome)
            description.setValue(message);
            content.setContentValue(message);
            
            CDATA cdata = new CDATA(message);
            List<Content> contentValueDOM = new ArrayList<Content>(); 
            contentValueDOM.add(cdata);
            content.setContentValueDOM(contentValueDOM);
            
            List<ContentItem> contents = new ArrayList<ContentItem>();
            contents.add(content);
            
            ContentModule contentModule = new ContentModuleImpl(); 
            contentModule.setContents(contents);
            contentModule.setContentItems(contents);
            entry.getModules().add(contentModule);
            
//            entry.setDescription(description);
            
            // set RSS <entry>
            syndEntries.add(entry);
        }   
	    
	    feed.setEntries(syndEntries);
	    
	    File fileOut = new File(configuration.getString(ConfigOptions.DESTINATION));
		log.info("Saving to file {} ...", fileOut.getAbsolutePath());
	    SyndFeedOutput syndOutput = new SyndFeedOutput();
	    syndOutput.output(feed, fileOut);
	    log.debug("Saved.");
	}
}
