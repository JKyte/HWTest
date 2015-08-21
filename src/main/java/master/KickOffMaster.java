package master;

import java.util.Properties;

import botConfigs.IRCBot;
import botConfigs.PropertyHandler;

/**
 * 
 * @author JKyte
 * 
 * This is the master class for an IRC Bot framework based on PIRCBot
 *
 */
public class KickOffMaster {

	public static void main(String[] args){

		PropertyHandler propHandler = new PropertyHandler();
		Properties configs = PropertyHandler.readPropertyFile(propHandler.PRODUCTION_DEFAULT);
		IRCBot bot = new IRCBot(configs);
		Thread t0 = new Thread(bot);
		t0.start();	
	}
}
