package com.youthhackersclub.irc.bot;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class Main {

	private static PircBotX bot;
	private static PluginManager pluginManager;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {
		Configuration configuration = new Configuration.Builder()
			.setName("YIB") //Set the nick of the bot. CHANGE IN YOUR CODE
			.setLogin("YIB") //login part of hostmask, eg name:login@host
			.setAutoNickChange(false) //Automatically change nick when the current one is in use
			.setCapEnabled(true) //Enable CAP features
//			.addListener(new Time()) //This class is a listener, so add it to the bots known listeners
			.setServerHostname("irc.freenode.net")
			.addAutoJoinChannel("#YHC") //Join the official #pircbotx channel
			.setNickservPassword("YoureTheBestAround")
			.buildConfiguration();

		bot = new PircBotX(configuration);
		pluginManager = new PluginManager(bot);

		try {
			bot.startBot();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
}