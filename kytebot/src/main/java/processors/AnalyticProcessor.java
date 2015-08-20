package processors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import botConfigs.IRCBotConfigs;
import msg.ComplexMsg;
import msg.GenericMsg;

/**
 * 
 * @author JKyte
 * 
 * This class parses complex IRCMsg messages and will do more interesting things
 */
public class AnalyticProcessor {

	private IRCBotConfigs configsPointer;
	@SuppressWarnings("unused")
	private ConcurrentLinkedQueue<GenericMsg> outboundMsgQ;

	private KytebotCommandProcessor kcp = null;

	public Logger log;
	
	// session-specific look-aside buffer for special loggers
	private HashMap<String,String> chans;
	private HashMap<String,String> users;
	private HashMap<String,String> privchats;

	@SuppressWarnings("unused")
	private String _channelPrefixes = "#&+!";

	public AnalyticProcessor( IRCBotConfigs configsPointer, ConcurrentLinkedQueue<GenericMsg> outboundMsgQ, 
			Logger log){
		this.configsPointer = configsPointer;
		this.outboundMsgQ = outboundMsgQ;
		
		if( log == null ){
			this.log = LogManager.getLogger(AnalyticProcessor.class);
			this.kcp = new KytebotCommandProcessor(outboundMsgQ, configsPointer, null);
		}else{
			this.log = log;
			this.kcp = new KytebotCommandProcessor(outboundMsgQ, configsPointer, log);
		}
		
		

		chans = new HashMap<String,String>();
		users = new HashMap<String,String>();
		privchats = new HashMap<String,String>();

	}

	/**
	 * Gateway message processor, will contain logic for making decisions. Add any hooks
	 * here for processing messages.
	 *
	 * @param ComplexMsg - a complex message object to aid parsing
	 * 
	 */
	public void processMessage( ComplexMsg msg ){

		//	TODO -- need a way to handle /me msgs
		if ( msg.getCommand().equals("PRIVMSG") ){
			handlePrivMsg( msg );
		} else if ( msg.getCommand().equals("") ){
			log.error( msg.getCommand() );
			log.error( msg.getOriginalMsg());
			log.error( msg.getSourceHostname());
			log.error( msg.getSourceLogin());
			log.error( msg.getSourceNick());
			log.error( msg.getTarget());
			log.error( msg.getActualMsg() );

		} else { 

			log.info(msg.toString());
		}

	}

	/**
	 * This method acts as a gateway into kytebot's integrated analytics and custom commands. A major
	 * responsibility of this method is to set the ComplexMsg.originOfMsg variable so the bot knows where
	 * to respond.
	 * 
	 * @param msg - a ComplexMsg object
	 */
	private void handlePrivMsg(ComplexMsg msg) {
		/**	
		 * System.out.println("Handling PRIVMSG");
		 * System.out.println(">" + msg.getTarget());
		 * System.out.println(">" + msg.getActualMsg());
		 */

		if( msg.getTarget().startsWith("#") ){	//then channel msg. This may be expanded for users as well.
			msg.setOriginOfMsg(msg.getTarget());


			if( !chans.containsKey(msg.getTarget() ) ){
				log.info(msg.getTarget());
				chans.put(msg.getTarget(), msg.getTarget() );	//	K,V is #chan,#chan
			}
			log.info( msg.getTarget()+":"+msg.getSourceNick()+":"+msg.getActualMsg() );

			//	hook for processing kytebot commands within a channel
			if( msg.getActualMsg().startsWith(configsPointer.getNick() ) ){
				kcp.processKytebotCmd(msg);
			}

		} else if ( msg.getTarget().equals( configsPointer.getNick() ) ){	//then side chat
			
			//	TODO write unit test to validate setting this variable + verify output msg does not
			//	collide with Config.nick
			msg.setOriginOfMsg(msg.getSourceNick());

			//			hook for processing kytebot commands within a channel
			if( msg.getActualMsg().startsWith(configsPointer.getNick() ) ){
				kcp.processKytebotCmd(msg);
			}


			if( !users.containsKey(msg.getSourceNick()) ){
				log.info(msg.getSourceNick());
				users.put(msg.getSourceNick(), msg.getSourceNick());
			}
			log.info( msg.getSourceNick()+":"+msg.getTarget()+":"+msg.getActualMsg() );

		} else {

			//	debug info
			System.out.println( msg.getCommand() );
			System.out.println( msg.getOriginalMsg());
			System.out.println( msg.getSourceHostname());
			System.out.println( msg.getSourceLogin());
			System.out.println( msg.getSourceNick());
			System.out.println( msg.getTarget());
			System.out.println( msg.getActualMsg());
		}

	}


	/**
	 * Handle 311    RPL_WHOISUSER
	 * @param payload
	 */
	public boolean handleReplyWhoIsUser( StringTokenizer tokenizer ){
		String user = tokenizer.nextToken();

		tokenizer.nextToken();

		String userHost = tokenizer.nextToken();

		System.out.println( user + "\t " + userHost );

		return true;
	}

	//	TODO expand logging and parsing for this method
	public boolean handleWhoIsChannels( StringTokenizer tokenizer ){
		String user = tokenizer.nextToken();

		ArrayList<String> chans = new ArrayList<String>();
		while( tokenizer.hasMoreTokens() ){
			String infoPlusChan = tokenizer.nextToken();
			chans.add( infoPlusChan );

			String perms = infoPlusChan.split("#")[0];
			String chan = infoPlusChan.split("#")[1];
			System.out.println( user + "\t" + perms + "\t" + chan );

			//	~ indicates user is a channel founder
			//	& indicates protected user			
			//	@ indicates user has Ops in a chan
			//	% user has HALF_OPS
			//	+ indicates user has voice in moderated chan

		}

		return true;
	}

	public HashMap<String, String> getChans() {
		return chans;
	}

	public void setChans(HashMap<String, String> chans) {
		this.chans = chans;
	}

	public HashMap<String, String> getUsers() {
		return users;
	}

	public void setUsers(HashMap<String, String> users) {
		this.users = users;
	}

	public HashMap<String, String> getPrivchats() {
		return privchats;
	}

	public void setPrivchats(HashMap<String, String> privchats) {
		this.privchats = privchats;
	}
}
