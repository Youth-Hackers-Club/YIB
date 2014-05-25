package com.youthhackersclub.irc.bot;

import com.youthhackersclub.irc.bot.com.youthhackersclub.irc.bot.commands.Time;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;

public class Main {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws Exception {
		Configuration configuration = new Configuration.Builder()
			.setName("YIB") //Set the nick of the bot. CHANGE IN YOUR CODE
			.setLogin("YIB") //login part of hostmask, eg name:login@host
			.setAutoNickChange(false) //Automatically change nick when the current one is in use
			.setCapEnabled(true) //Enable CAP features
			.addListener(new Time()) //This class is a listener, so add it to the bots known listeners
			.setServerHostname("irc.freenode.net")
			.addAutoJoinChannel("#YHC") //Join the official #pircbotx channel
			.setNickservPassword("YoureTheBestAround")
			.buildConfiguration();
		
		PircBotX bot = new PircBotX(configuration);


		try {
			bot.startBot();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}


}
