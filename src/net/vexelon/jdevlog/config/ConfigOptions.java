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

import java.lang.reflect.Type;

public enum ConfigOptions {
	
	SOURCE("source", "s", String.class),
	DESTINATION("out", "o", String.class),
	TYPE("type", "t", String.class),
	MAXLOG("maxlog", "m", Long.class),
	USERNAME("username", "u", String.class),
	PASSWORD("password", "p", String.class),
	VERBOSE("verbose", "v", Boolean.class)
	;
	
	private String name;
	private String shortName;
	private Type type;
	
	ConfigOptions(String name, String shortName, Type type) {
		this.name = name;
		this.shortName = shortName;
		this.type = type;
	}
	
	String getName() {
		return name;
	}
	
	String getShortName() {
		return shortName;
	}	

	Type getType() {
		return type;
	}
}
