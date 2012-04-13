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

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import net.vexelon.jdevlog.config.ConfigOptions;
import net.vexelon.jdevlog.config.Configuration;
import net.vexelon.jdevlog.vcs.SCMException;
import net.vexelon.jdevlog.vcs.SCMSource;

/**
 * 
 * @author petarov
 *
 * (By example of the SVNKit wikis)
 */
public class SvnSource implements SCMSource {
	
	final static Logger log = LoggerFactory.getLogger(SvnSource.class);
	
	protected SVNRepository repository;
	protected Configuration configuration;
	
	protected SvnSource(Configuration configuration) {
		this.configuration = configuration;
	}
	
	protected void setup() {
		
		// For using over http:// and https://
		log.trace("Init http:// and https:// usage.");
		DAVRepositoryFactory.setup();
		
		// For using over svn:// and svn+xxx://
		log.trace("Init svn:// and svn+xxx:// usage.");
		SVNRepositoryFactoryImpl.setup();
		
		// For using over file:///
		log.trace("Init file:// usage.");
		FSRepositoryFactory.setup();		
	}

	@Override
	public void initialize() throws SCMException {
		setup();

		// create repository
		repository = null;
		
		try {
			repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(configuration.get(ConfigOptions.SOURCE)));
		} 
		catch (SVNException e) {
			throw new SCMException("Error while creating SVNRepository location!", e);
		}
	}
	
	@Override
	public void authenticate() throws SCMException {
		// check if authentication is required
		if (configuration.valid(ConfigOptions.USERNAME)) {
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(
					configuration.get(ConfigOptions.USERNAME), configuration.get(ConfigOptions.PASSWORD));
			repository.setAuthenticationManager(authManager);
		}		
	}
	
	@Override
	public Iterator<?> getLastHistory(long maxEntries) throws SCMException {
        
		long startRevision = 0;
        long endRevision = -1;//HEAD (the latest) revision
        
        try {
            endRevision = repository.getLatestRevision();
        } 
        catch (SVNException e) {
        	throw new SCMException("Failed to fetch latest revision!", e);
        }
        
        startRevision = endRevision - maxEntries;
        
        if (log.isTraceEnabled())
        	log.trace("Fetching entries from rev {} to rev {} ...", startRevision, endRevision );

        Collection<?> logEntries = null;
        try {
            /*
             * Collects SVNLogEntry objects for all revisions in the range
             * defined by its start and end points [startRevision, endRevision].
             * For each revision commit information is represented by
             * SVNLogEntry.
             * 
             * the 1st parameter (targetPaths - an array of path strings) is set
             * when restricting the [startRevision, endRevision] range to only
             * those revisions when the paths in targetPaths were changed.
             * 
             * the 2nd parameter if non-null - is a user's Collection that will
             * be filled up with found SVNLogEntry objects; it's just another
             * way to reach the scope.
             * 
             * startRevision, endRevision - to define a range of revisions you are
             * interested in; by default in this program - startRevision=0, endRevision=
             * the latest (HEAD) revision of the repository.
             * 
             * the 5th parameter - a boolean flag changedPath - if true then for
             * each revision a corresponding SVNLogEntry will contain a map of
             * all paths which were changed in that revision.
             * 
             * the 6th parameter - a boolean flag strictNode - if false and a
             * changed path is a copy (branch) of an existing one in the repository
             * then the history for its origin will be traversed; it means the 
             * history of changes of the target URL (and all that there's in that 
             * URL) will include the history of the origin path(s).
             * Otherwise if strictNode is true then the origin path history won't be
             * included.
             * 
             * The return value is a Collection filled up with SVNLogEntry Objects.
             */
            logEntries = repository.log(new String[] {""}, null, startRevision, endRevision, true, true);
        } 
        catch (SVNException e) {
        	throw new SCMException("Failed to to collect log information!", e);
        }
        
        return logEntries.iterator();
	}
	
	/**
	 * Get path (URL) to specific repository revision
	 * @param revision
	 * @return
	 * @throws SCMException
	 */
	public String getRevisionPath(long revision, boolean stripRelativePath) throws SCMException {
		
		String url = this.configuration.get(ConfigOptions.SOURCE);
		
        try {
        	url = url.replace(repository.getRepositoryRoot(false).getPath(), 
        			String.format("%s/!svn/bc/%d", repository.getRepositoryRoot(false).getPath(), revision));
        	
        	if (stripRelativePath && !repository.getRepositoryPath("").equals("/") ) {
        		url = url.replace(repository.getRepositoryPath(""), "");
        	}
        } 
        catch (SVNException e) {
        	throw new SCMException("Failed to fetch latest revision!", e);
        }	
        
        return url;
	}
	
	public String getRevisionPath(long revision) throws SCMException {
		return getRevisionPath(revision, false);
	}
	
	// ----------------------------------------------------
	
	public static SvnSource newInstance(Configuration configuration) {
		return new SvnSource(configuration);
	}

}
