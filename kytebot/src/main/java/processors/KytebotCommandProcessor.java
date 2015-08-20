package processors;

import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import botConfigs.IRCBotConfigs;
import msg.ComplexMsg;
import msg.GenericMsg;
import msg.IRCCommandHelper;
import msg.IRCMsg;


/**
 * 
 * @author JKyte
 *
 * Here be where the Kytebot commands be processed
 */
public class KytebotCommandProcessor {

	private IRCBotConfigs configs;
	private ConcurrentLinkedQueue<GenericMsg> outboundMsgQ; 

	private Random rand;
	private String[] sideChatResponses = { "Hello, I am kytebot", 
			"I'm not allowed to talk to strangers", 
	"Ask Kyte if it's okay to talk to me" };
	
	private IRCCommandHelper cmdHelper = new IRCCommandHelper();

	private Logger log;
	
	public KytebotCommandProcessor(ConcurrentLinkedQueue<GenericMsg> outboundMsgQ, IRCBotConfigs configs, Logger log){
		this.outboundMsgQ = outboundMsgQ;
		this.rand = new Random();
		this.configs = configs;
		
		if( log == null ){
			this.log = LogManager.getLogger(KytebotCommandProcessor.class);
		}else{
			this.log = log;
		}
	}

	/**
	 * This is the public-facing method used for hooking in kytebot commands. It shall determine trusted user status status
	 * before determining what action to take
	 * 
	 * Sample kytebot commands
	 *  - "kytebot tell a story" --> kytebot sends a story
	 *  - "kytebot where is [USER]" --> kytebot runs a WHOIS and reports back
	 * 
	 * @param msg - A ComplexMsg object
	 * @returns a boolean indicating whether the user was trusted or not
	 */
	public boolean processKytebotCmd( ComplexMsg msg ){

		//	logging for debug/devel purposes TODO -- remove in the future
		log.info("target: " + msg.getTarget() );
		log.info("sourceNick/sourcLogin: " + msg.getSourceNick() + "/" + msg.getSourceLogin() );
		log.info("sourceHostname: " + msg.getSourceHostname() );
		log.info("originOfMsg: " + msg.getOriginOfMsg() );
		log.info(msg.getActualMsg() );

		boolean trusted = false;
		if( configs.getTrustedUsers().contains(msg.getSourceNick()) ){
			processMsgInternal( msg, true);
			trusted = true;
		}else{
			processMsgInternal( msg, false);
		}

		return trusted;
	}

	/**
	 * Where kingmakers plot, decisions are made, and, oh. Wrong window :)
	 * @param msg - A ComplexMsg object
	 * @param trusted - whether the msg is from a trusted user
	 */
	public void processMsgInternal( ComplexMsg msg, boolean trusted ){
		
		String[] wordOrderForStory = { "tell", "a,me,us", "story" };
		int storyIndex = 0;
		
		StringTokenizer basicNLP = new StringTokenizer( msg.getActualMsg() );
		String token = null;
		while( basicNLP.hasMoreTokens() ){
			token = basicNLP.nextToken();
			
			if( wordOrderForStory[storyIndex].contains(token) ){
				storyIndex++;
				
				if( storyIndex == wordOrderForStory.length ){
					break;
				}
			}
		}
		
		if( storyIndex == wordOrderForStory.length ){
			
			goTellAStory(msg, trusted);
			
		} else {
			
			if( msg.getTarget().equals( configs.getNick() ) ){
				sendUntrustedResponse( msg.getOriginOfMsg() );
			}
			
		}
	}
	
	/**
	 * Conditions for telling a story
	 * 1. Request comes from a trusted channel
	 * 2. Request comes from a trusted user in a side chat
	 * 
	 * @param msg
	 * @param trusted
	 */
	public void goTellAStory( ComplexMsg msg, boolean trusted ){
		
		//	1. Trusted Channel condition	
		if( configs.getStoryChans().contains(msg.getOriginOfMsg()) ){
			sendStoryResponse( msg.getOriginOfMsg() );
			
		//	2. Request comes from trusted user in a side chat	
		}else if( trusted && msg.getTarget().equals( configs.getNick() ) ){
			sendStoryResponse( msg.getOriginOfMsg() );
		}
		System.out.println( msg.getTarget() + " " + configs.getNick() + " " + trusted );
	}

	public void sendUntrustedResponse( String msgTarget ){
		// canned response for now		
		String response = cmdHelper.slashMeCmd( msgTarget, " wishes he had the ability to talk");
		outboundMsgQ.add( new IRCMsg( response ) );
	}
	
	/**
	 * For now a default response. This method may only be called if 
	 * @param msgTarget - the recipient of the story
	 */
	public void sendStoryResponse( String msgTarget ){
		// canned response for now		
		String response = cmdHelper.slashMeCmd( msgTarget, " knows a few stories, but isn't willing to share just yet.");
		outboundMsgQ.add( new IRCMsg( response ) );
	}

	public void processSideChat( ComplexMsg msg) {

		int randIndex = rand.nextInt(sideChatResponses.length);
		String response = cmdHelper.sendPrivateMsg( msg.getSourceNick(), sideChatResponses[randIndex] );
		outboundMsgQ.add( new IRCMsg( response ) );

	}



}
