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
public class PluginManager extends ListenerAdapter {

	private HashMap<String, Plugin> commandsMap;
	private ArrayList<Plugin> plugins;
	private ScriptEngineManager engineManager;

	private PircBotX pircBotX;

	public PluginManager(PircBotX pircBotX) {
		this.pircBotX = pircBotX;
		plugins = new ArrayList();
		commandsMap = new HashMap<String, Plugin>();
		engineManager = new ScriptEngineManager();
	}

	public void addJS(String js) throws ScriptException {
		ScriptEngine javascriptEngine = engineManager.getEngineByName("JavaScript");
		javascriptEngine.eval(js);
		Plugin p = (Plugin) javascriptEngine.get("plugin");
		add(p);
	}

	public void add(Plugin plugin) {
		plugins.add(plugin);
		plugin.init(this);
		for (int i = 0; i < plugin.getCommands().length; i++) {
			commandsMap.put(plugin.getCommands()[i], plugin);
		}
	}

	public void remove(Plugin plugin) {
		plugins.remove(plugin);
		plugin.close(this);
	}

	public void exec(String command, Object[] args) {
		commandsMap.get(command).exec(command, args);
	}

	public void execStatic(String command, Object... args) {
		exec(command, args);
	}

	public PircBotX getPircBotX() {
		return pircBotX;
	}

	@Override
	public void onMessage(MessageEvent event) throws Exception {
		String message = event.getMessage();
		String[] parts = message.split(" ");
		exec(parts[0], Arrays.copyOfRange(parts, 1, parts.length));
	}
}