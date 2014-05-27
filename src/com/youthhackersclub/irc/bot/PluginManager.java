package com.youthhackersclub.irc.bot;

import javax.script.*;
import java.util.ArrayList;

/**
 * Created by Scott Ramsay on 27/05/2014.
 */
public class PluginManager {
	private ArrayList<Plugin> plugins;
    private ScriptEngineManager engineManager;

	public PluginManager() {
        plugins = new ArrayList();
        engineManager = new ScriptEngineManager();
	}

    public void addJS(String js) {
        ScriptEngine javascriptengine = engineManager.getEngineByName("JavaScript");
        javascriptengine.evel(js);
        Plugin p = (Plugin) javascriptengine.get("plugin");
        add(p);
    }

    public void add(Plugin plugin) {
        plugins.add(plugin);
        plugin.init(this);
    }

    public void remove(Plugin plugin) {
        plugins.remove(plugin);
        plugin.close(this);
    }
}