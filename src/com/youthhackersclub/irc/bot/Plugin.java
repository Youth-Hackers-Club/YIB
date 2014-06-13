package com.youthhackersclub.irc.bot;

import java.io.Reader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.pircbotx.hooks.Event;

/**
 * @author Scott Ramsay
 */
public interface Plugin {
	/**
	 * @return The plugin's name
	 */
	public String getName();
	/**
	 * @return An array of commands
	 */
	public String[] getCommands();
	/**
	 * @param manager The PluginManager object that the plugin interacts with for lower level functions
	 */
	public void init(PluginManager manager);
	/**
	 * @param command The command name
	 * @param args arguments for the command
	 * @return The output of the command
	 */
	public String exec(String command, Event event, Object[] args);
	/**
	 * @param manager The PluginManager object that the plugin interacts with for lower level functions
	 */
	public void close(PluginManager manager);
	
	public static class JSPlugin implements Plugin {
		
		private static final String defaultDefaultJS = 
				"function getName() {return \"No-Name\"}" +"\n"+
				"function getCommands() {return \"\";}" +"\n"+
				"function init(manager) {}" +"\n"+
				"function exec(command, args) {return \"No-Commands\"}" + "\n" +
				"function close(manager) {}" +"\n"+
				"function equals(p) {return p == this}" +"\n"+
				"";
		
		ScriptEngine javascriptEngine;
		Invocable invocable;
		
		public JSPlugin(ScriptEngineManager engineManager, String jsCode) throws ScriptException {
			this(engineManager, jsCode, defaultDefaultJS);
		}

		public JSPlugin(ScriptEngineManager engineManager, String code, String defaultjs) throws ScriptException {
			javascriptEngine = engineManager.getEngineByName("JavaScript");
			javascriptEngine.eval(defaultjs);
			javascriptEngine.eval(code);
			invocable = (Invocable) javascriptEngine;
		}
		
		public JSPlugin(ScriptEngineManager engineManager, Reader jsCode) throws ScriptException {
			this(engineManager, jsCode, defaultDefaultJS);
		}

		public JSPlugin(ScriptEngineManager engineManager, Reader code, String defaultjs) throws ScriptException {
			javascriptEngine = engineManager.getEngineByName("JavaScript");
			javascriptEngine.eval(defaultjs);
			javascriptEngine.eval(code);
			invocable = (Invocable) javascriptEngine;
		}

		@Override
		public String getName() {
			try {
				return (String) invocable.invokeFunction("getName");
			} catch (NoSuchMethodException | ScriptException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public String[] getCommands() {
			try {
				String s = (String) invocable.invokeFunction("getCommands");
				return s.split("\\|");
			} catch (NoSuchMethodException | ScriptException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void init(PluginManager manager) {
			try {
				invocable.invokeFunction("init", new Object[]{manager});
			} catch (NoSuchMethodException | ScriptException e) {
				e.printStackTrace();
			}
		}

		@Override
		public String exec(String command, Event event, Object[] args) {
			try {
				return (String) invocable.invokeFunction("exec", command, event, args);
			} catch (NoSuchMethodException | ScriptException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void close(PluginManager manager) {
			try {
				invocable.invokeFunction("close", new Object[]{manager});
			} catch (NoSuchMethodException | ScriptException e) {
				e.printStackTrace();
			}
		}
	}
}