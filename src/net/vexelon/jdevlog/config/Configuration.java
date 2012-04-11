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

import java.util.HashMap;
import java.util.Map;

public class Configuration {

	Map<ConfigOptions, String> parsedOptions = null;

	public Configuration(ConfigurationBuilder builder) {
		parsedOptions = new HashMap<ConfigOptions, String>(builder.getParsedOptions());
	}
	
	public void validate() throws ConfigurationException {
		if (!parsedOptions.containsKey(ConfigOptions.SOURCE)) {
			throw new ConfigurationException("(source) Source not specified!");
		}
		else if (!parsedOptions.containsKey(ConfigOptions.DESTINATION)) {
			throw new ConfigurationException("(out) Destination not specified!");
		}
		
		if (!parsedOptions.containsKey(ConfigOptions.TYPE)) {
//			throw new ConfigurationException("(type) Version control type not specified!");
			parsedOptions.put(ConfigOptions.TYPE, Defs.SCM_SVN);
		}
		
		if (!parsedOptions.containsKey(ConfigOptions.MAXLOG)) {
			parsedOptions.put(ConfigOptions.MAXLOG, String.valueOf(Defs.DEFUALT_MAX_ENTRIES));
		}
	}
	
	public boolean valid(ConfigOptions option) {
		return parsedOptions.containsKey(option);
	}
	
	public String get(ConfigOptions option) {
		return getString(option);
	}	
	
	public byte getByte(ConfigOptions option) {
		return Byte.parseByte(parsedOptions.get(option));
	}		
	
	public int getInt(ConfigOptions option) {
		return Integer.parseInt(parsedOptions.get(option));
	}
	
	public long getLong(ConfigOptions option) {
		return Long.parseLong(parsedOptions.get(option));
	}	
	
	public float getFloat(ConfigOptions option) {
		return Float.parseFloat(parsedOptions.get(option));
	}	
	
	public String getString(ConfigOptions option) {
		return (String)parsedOptions.get(option);
	}
	
	public boolean getBool(ConfigOptions option) {
		return Boolean.parseBoolean(parsedOptions.get(option));
	}	
	
}
