package com.youthhackersclub.irc.bot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import javax.script.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Scott Ramsay on 27/05/2014.
 */
@SuppressWarnings({"javadoc", "nls"})
public class PluginManager extends ListenerAdapter<PircBotX> {
	
	private static final String defaultJS = 
			"hashCode = 0" +"\n"+
			"function getName() {return \"No-Name\"}" +"\n"+
			"function getCommands() {return [];}" +"\n"+
			"function init(manager) {}" +"\n"+
			"function exec(command, args) {return \"No-Commands\"}" + "\n" +
			"function close(manager) {}" +"\n"+
			"function equals(p) {return p == this}" +"\n"+
			"function hashCode() {return hashCode}" +"\n"+
			"";

	private HashMap<String, Plugin> commandsMap;
	private HashMap<String, Plugin> namesMap;
	private ArrayList<Plugin> plugins;

	private ScriptEngineManager engineManager;

	private PircBotX pircBotX;

	public PluginManager(PircBotX pircBotX) {
		this.pircBotX = pircBotX;
		plugins = new ArrayList<>();
		commandsMap = new HashMap<String, Plugin>();
		namesMap = new HashMap<String, Plugin>();
		engineManager = new ScriptEngineManager();
	}

	public Plugin addJS(String code) throws ScriptException {
		Plugin p = new Plugin.JSPlugin(engineManager, code, defaultJS);
		add(p);
		return p;
	}

	public void add(Plugin plugin) {
		plugins.add(plugin);
		plugin.init(this);
		namesMap.put(plugin.getName(), plugin);
		for (int i = 0; i < plugin.getCommands().length; i++) {
			commandsMap.put(plugin.getCommands()[i], plugin);
		}
	}

	public void remove(Plugin plugin) {
		plugins.remove(plugin);
		namesMap.remove(plugin);
		for (int i = 0; i < plugin.getCommands().length; i++) {
			commandsMap.remove(plugin.getCommands()[i]);
		}
		plugin.close(this);
	}
	
	public void remove(String name) {
		remove(namesMap.get(name));
	}
	
	public void removeJS(String code) throws ScriptException {//TODO Shorten
		ScriptEngine javascriptEngine = engineManager.getEngineByName("JavaScript");
		javascriptEngine.eval(code);
		Invocable invocable = (Invocable) javascriptEngine;
		Plugin p = invocable.getInterface(Plugin.class);
		remove(namesMap.get(p.getName()));
	}

	public String exec(String command, Object[] args) {
		try {
			return commandsMap.get(command).exec(command, args);
		} catch (NullPointerException e) {
			return "Command " + command + " not found";
		}
	}

	public String execStatic(String command, Object... args) {
		return exec(command, args);
	}

	@Override
	public void onMessage(MessageEvent event) throws Exception {
		String message = event.getMessage();
		String[] parts = message.split(" ");
		String output = exec(parts[0], Arrays.copyOfRange(parts, 1, parts.length));
		event.respond(output);
	}

	public PircBotX getPircBotX() {
		return pircBotX;
	}
	
	/**
	 * @return the commandsMap
	 */
	public HashMap<String, Plugin> getCommandsMap() {
		return commandsMap;
	}

	/**
	 * @return the namesMap
	 */
	public HashMap<String, Plugin> getNamesMap() {
		return namesMap;
	}

	/**
	 * @return the plugins
	 */
	public Plugin[] getPlugins() {
		return plugins.toArray(new Plugin[0]);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@SuppressWarnings("nls")
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PluginManager [commandsMap=");
		builder.append(commandsMap);
		builder.append(", namesMap=");
		builder.append(namesMap);
		builder.append(", plugins=");
		builder.append(plugins);
		builder.append(", engineManager=");
		builder.append(engineManager);
		builder.append(", pircBotX=");
		builder.append(pircBotX);
		builder.append("]");
		return builder.toString();
	}
}