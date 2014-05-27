package com.youthhackersclub.irc.bot;

public abstract class Plugin {
	public abstract String[] getCommands();
	public void init(PluginManager manager) {}
	public abstract String exec(String command, Object[] args);
	public String execStatic(String command, Object... args) { return exec(command, args);}
	public void close(PluginManager manager) {}
}