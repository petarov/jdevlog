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
