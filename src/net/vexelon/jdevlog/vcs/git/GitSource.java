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
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.FS;

import net.vexelon.jdevlog.config.ConfigOptions;
import net.vexelon.jdevlog.config.Configuration;
import net.vexelon.jdevlog.vcs.SCMException;
import net.vexelon.jdevlog.vcs.SCMSource;
import net.vexelon.jdevlog.vcs.svn.SvnSource;

public class GitSource implements SCMSource {
	
	protected Configuration configuration;
	protected Repository repository;
	
	protected GitSource(Configuration configuration) {
		this.configuration = configuration;
	}	

	@Override
	public void initialize() throws SCMException {
		
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		File repoPath = new File(configuration.get(ConfigOptions.SOURCE));
		
		try {
//			repository = builder.setGitDir(repoPath).build();
			repository = RepositoryCache.open(
					RepositoryCache.FileKey.lenient(repoPath, FS.DETECTED), 
					true);
		} 
		catch (IOException e) {
			throw new SCMException("Could not locate Git repository in " + repoPath.getAbsolutePath(), e);
		}		
	}

	@Override
	public void authenticate() throws SCMException {
		// Empty
	}

	@Override
	public Iterator<?> getLastHistory(long maxEntries) throws SCMException {

		Iterable<RevCommit> log = null;
		
		try {
			Git git = new Git(repository);
			LogCommand cmd = git.log();
			
			cmd.setMaxCount((int)maxEntries);
//			cmd.addPath(Constants.MASTER);
			
			try {
				cmd.add(repository.resolve(Constants.MASTER));
			}
			catch (Exception e) {
				throw new SCMException("Error getting log messages!", e);
			}
			
			log = cmd.call();
		} 
		catch (NoHeadException e) {
			throw new SCMException("Error getting log messages!", e);
		}	
		
		return log.iterator();
	}

	// ----------------------------------------------------
	
	public static GitSource newInstance(Configuration configuration) {
		return new GitSource(configuration);
	}
	
}
