package com.youthhackersclub.irc.bot;

import java.io.FileReader;
import java.util.Properties;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class Main extends ListenerAdapter<PircBotX> {

	private static PircBotX bot;
	private static PluginManager pluginManager;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {
		Properties config = new Properties();
		config.load(new FileReader(args[0]));
		
		Configuration.Builder builder = new Configuration.Builder()
			.setAutoNickChange(false)
			.setCapEnabled(true)
			.addListener(new Main())
//			.setName("YIB_")
//			.setLogin("YIB_")
//			.setServerHostname("irc.freenode.net")
//			.addAutoJoinChannel("#YHC")
			.setName(config.getProperty("irc.name", "YIB"))
			.setLogin(config.getProperty("irc.login", "YIB"))
			.setServerHostname(config.getProperty("irc.serverHostname"))
			.setNickservPassword(config.getProperty("irc.nickservPassword"));
		for (int i = 0; i < config.getProperty("irc.autoJoinChannels", "").split("\\ ").length; i++) {
			builder.addAutoJoinChannel(config.getProperty("irc.autoJoinChannels").split(" ")[i]);
		}
		Configuration configuration = builder.buildConfiguration();
		
		bot = new PircBotX(configuration);
		pluginManager = new PluginManager(bot);

		try {
			bot.startBot();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public Main() {
		System.out.println("Main.Main()");
	}
	
	@Override
	public void onEvent(Event<PircBotX> event) throws Exception {
		pluginManager.onEvent(event);
		if (event instanceof MessageEvent) {
			onMessage((MessageEvent<PircBotX>) event);
		}
	}
	
	@Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception {
		System.out.println("|MSG|"+event.getMessage()+"|");
	}
}