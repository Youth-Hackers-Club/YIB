package com.youthhackersclub.irc.bot.com.youthhackersclub.irc.bot.commands;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;


import java.util.Date;

@SuppressWarnings("rawtypes")
public class Time extends ListenerAdapter {
        public void onMessage(MessageEvent event) {
                if (event.getMessage().equals(".time")) {
                    event.respond("The current time is " + new Date());
                }
        }
}