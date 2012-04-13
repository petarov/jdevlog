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
package net.vexelon.jdevlog.biztalk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.vexelon.jdevlog.helpers.IOHelper;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedOutput;

public abstract class Transformer {
	
	final static Logger log = LoggerFactory.getLogger(Transformer.class);
	
//	protected Configuration configuration;
	
//	public Transformer() {
//	}
	
	/**
	 * Outputs RSS feed object to configured output file
	 * @param rssFeed
	 * @throws Exception
	 */
	public void saveFeed(SyndFeed rssFeed, File fileOut) throws Exception  {
		
		log.info("Saving to file {} ...", fileOut.getAbsolutePath());
		
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;		
	    
		try {		
			fos = new FileOutputStream(fileOut);
			osw = new OutputStreamWriter(fos, "UTF-8");
			bw = new BufferedWriter(osw);
			
		    SyndFeedOutput syndOutput = new SyndFeedOutput();
	    	syndOutput.output(rssFeed, bw);
	    }
		catch(Exception e) {
			throw e;
		}
	    finally {
	    	// gracefully close streams
	    	IOHelper.closeQuietly(bw);
	    	IOHelper.closeQuietly(osw);
	    	IOHelper.closeQuietly(fos);
	    }
	    
	    log.debug("Saved.");		
	}	
	
	/**
	 * Transforms a list of log entries into an RSS file on the file-system
	 * @param entries
	 * @throws Exception
	 */
	public abstract void transformHistoryLog(Collection<?> entries) throws Exception;

}
