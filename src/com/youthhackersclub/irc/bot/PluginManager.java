package com.youthhackersclub.irc.bot;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import javax.script.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

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
	private Properties properties;
	
	public PluginManager(PircBotX pircBotX, Properties properties) throws FileNotFoundException {
//	public PluginManager(PircBotX pircBotX, Properties properties) throws URISyntaxException {
		this.properties = properties;
		this.pircBotX = pircBotX;
		plugins = new ArrayList<>();
		commandsMap = new HashMap<String, Plugin>();
		namesMap = new HashMap<String, Plugin>();
		engineManager = new ScriptEngineManager();
//		reloadPlugins();
		reloadPluginsV2();
	}

	@Deprecated //This doesn't work. {Windows}
	public void reloadPlugins() throws URISyntaxException {
		Path folder = Paths.get(new URI(properties.getProperty("plugin.folder")));
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder)) {
			for (Path file : stream) {
				if (file.toString().endsWith(".js")) {
					System.out.println(file);
					try {
						try {
							removeJS(new FileReader(file.toFile()));
						} catch (NullPointerException e) {
							//Assuming JS file was never added
							//You're also assuming that this isn't a reload method. :)
						}
						addJS(new FileReader(file.toFile()));
					} catch (ScriptException e) {
						//TODO Handle nicely (Low Level)
					}
				}
			}
		} catch (IOException e) {
			//TODO Handle nicely (Bad Config)
			e.printStackTrace();
		}
	}

	public void reloadPluginsV2() throws FileNotFoundException {
		File folder = new File(properties.getProperty("plugin.folder"));
		if (!folder.exists()) {throw new FileNotFoundException();}
		if (!folder.isDirectory()) {throw new FileNotFoundException();}
		for (File file : folder.listFiles()) {
			if (file.getName().endsWith(".js")) {
				System.out.println(file.getName());
				try {
					try {
						removeJS(new FileReader(file));
					} catch (NullPointerException e) {
						//
					}
					addJS(new FileReader(file));
				} catch (ScriptException e) {
					//TODO Handle nicely (Low Level)
				}
			}
		}
	}
	
	public Plugin addJS(String code) throws ScriptException {
		Plugin p = new Plugin.JSPlugin(engineManager, code, defaultJS);
		add(p);
		return p;
	}
	
	public Plugin addJS(Reader code) throws ScriptException {
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
	
	public void removeJS(Reader code) throws ScriptException {
		Plugin p = new Plugin.JSPlugin(engineManager, code, defaultJS);
		remove(namesMap.get(p.getName()));
	}
	
	public void removeJS(String code) throws ScriptException {
		Plugin p = new Plugin.JSPlugin(engineManager, code, defaultJS);
		remove(namesMap.get(p.getName()));
	}
	
	public String exec(String command, Object[] args) {
		try {
			Plugin p = commandsMap.get(command);
			return p.exec(command, args);
		} catch (NullPointerException e) {
			return "Command " + command + " not found";
		}
	}
	
	public String execStatic(String command, Object... args) {
		return exec(command, args);
	}
	
	@Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception {
		String message = event.getMessage();
		String[] parts = message.split(" ");
		if (message.startsWith(properties.getProperty("plugin.prefix", "!"))) {
			String command = message.substring(properties.getProperty("plugin.prefix", "!").length());
			String output = exec(command, Arrays.copyOfRange(parts, 1, parts.length));
			if (!output.isEmpty()) {
				event.respond(output);
			}
		}
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