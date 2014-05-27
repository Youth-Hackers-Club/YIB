package com.youthhackersclub.irc.bot;

public abstract class Plugin {
	public abstract String[] getCommands();
	public void init(PluginManager manager) {}
	public abstract void exec(String command, Object[] args);
	public void execStatic(String command, Object... args) { exec(command, args);}
	public void close(PluginManager manager) {}
}