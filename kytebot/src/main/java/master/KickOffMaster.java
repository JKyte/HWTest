package master;

import botConfigs.IRCBot;
import botConfigs.IRCBotConfigs;

/**
 * 
 * @author JKyte
 * 
 * This is the master class for an IRC Bot framework based on PIRCBot
 *
 */
public class KickOffMaster {

	public static void main(String[] args){

		IRCBotConfigs configs = new IRCBotConfigs();
		configs.setNick("nick");
		configs.setNickpass("passwd");
		configs.setStartChannel("#startchan");	
		configs.setIrcServer("irc.server.net");
		
		configs.setTrustedUsers("user_1,user_2");
		configs.setStoryChans("");	//	Intentionally left blank

		IRCBot bot = new IRCBot(configs);
		Thread t0 = new Thread(bot);
		t0.start();	
	}
}
