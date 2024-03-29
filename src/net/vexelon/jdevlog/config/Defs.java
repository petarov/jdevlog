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

public final class Defs {
	
	public final static String NEWLINE = System.getProperty("line.separator");
	public final static String HTML_NEWLINE = "<br>";

	public final static String DEFAULT_FEED_TYPE = "rss_2.0";
	public final static String DEFAULT_AUTHOR = "jdevlog";
	public final static String DEFAULT_TITLE = "jdevlog - Version Control RSS generator - https://github.com/petarov/jdevlog";
	public final static int DEFAULT_EXCERPT_SIZE = 30;
	public final static int DEFAULT_MAX_ENTRIES = 50;
	public final static int DEFAULT_GIT_SHORTREV_SIZE = 7;
	
	public final static String SCM_SVN = "svn";
	public final static String SCM_GIT = "git";

}
