package com.youthhackersclub.irc.bot;

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
		Configuration configuration = new Configuration.Builder()
			.setName("YIB_")
			.setLogin("YIB_")
			.setAutoNickChange(false)
			.setCapEnabled(true)
			.addListener(new Main())
			.setServerHostname("irc.freenode.net")
			.addAutoJoinChannel("#YHC")
			.setNickservPassword(args[0])
			.buildConfiguration();

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
		System.out.println("|"+event+"|");
		if (event instanceof MessageEvent) {
			onMessage((MessageEvent<PircBotX>) event);
		}
	}
	
	@Override
	public void onMessage(MessageEvent<PircBotX> event) throws Exception {
		System.out.println("|"+event.getMessage()+"|");
	}
}