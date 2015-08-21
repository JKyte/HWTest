package processors;

import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import botConfigs.IRCBotConfigs;
import msg.ComplexMsg;
import msg.GenericMsg;
import msg.IRCMsg;


/**
 * @author JKyte
 * 
 * This class reads messages off a queue and does some basic parsing before sending the encapsulated message
 * to additional classes
 */
public class GenericMessageParser implements Runnable {

	private IRCBotConfigs configsPointer = null;
	private ConcurrentLinkedQueue<GenericMsg> inboundMsgQ;
	private ConcurrentLinkedQueue<GenericMsg> outboundMsgQ;

	private AnalyticProcessor analytics;
	
	private Logger log;

	public GenericMessageParser( IRCBotConfigs pointer, ConcurrentLinkedQueue<GenericMsg> inboundMsgQ, 
			ConcurrentLinkedQueue<GenericMsg> outboundMsgQ, Logger log ){

		this.configsPointer = pointer;
		this.inboundMsgQ = inboundMsgQ;
		this.outboundMsgQ = outboundMsgQ;
		
		if( log == null ){
			this.log = LogManager.getLogger(GenericMessageParser.class);
			analytics = new AnalyticProcessor(this.configsPointer, this.outboundMsgQ, null);
		}else{
			this.log = log;
			analytics = new AnalyticProcessor(this.configsPointer, this.outboundMsgQ, log);	
		}
		
	}

	@Override
	public void run() {

		GenericMsg msg = null;

		try {	
			while ( true ){

				msg = inboundMsgQ.poll();

				if( null != msg ){
					try { 
						parseRawMsg(msg.getData());
					} catch (Exception e) {
						log.error("Exception thrown while parsing this msg:" + msg.toString());
						log.error(msg, e);
					} finally {
						//	don't need this yet, placeholder code
					}

				}
			}
		} catch (Exception e) {
			log.error(msg, e);
		} finally {
			log.fatal("FATAL:" + msg.toString());
			log.fatal("FATAL: InputThread crashed.");
		}

	}

	/**
	 * This method parses a raw IRCMsg into a more complex object which will be passed along to the analytic parser
	 * 
	 * To prioritize connection to the server, if the message is a PING, we PONG right away
	 * @param msgPayload
	 */
	public void parseRawMsg(String msgPayload) { 
		
		if( msgPayload.startsWith("PING") ){
			String newPayload = msgPayload.replaceAll("PING", "PONG");
			outboundMsgQ.add( new IRCMsg(newPayload) );		
			return;
		}

		log.info("SERVER: " + msgPayload);

		String sourceNick = "";
		String sourceLogin = "";
		String sourceHostname = "";

		StringTokenizer tokenizer = new StringTokenizer(msgPayload);
		String senderInfo = tokenizer.nextToken();
		String command = tokenizer.nextToken();
		String target = null;

		int exclamation = senderInfo.indexOf("!");
		int at = senderInfo.indexOf("@");
		if (senderInfo.startsWith(":")) {
			if (exclamation > 0 && at > 0 && exclamation < at) {
				sourceNick = senderInfo.substring(1, exclamation);
				sourceLogin = senderInfo.substring(exclamation + 1, at);
				sourceHostname = senderInfo.substring(at + 1);
			} else {

				if (tokenizer.hasMoreTokens()) {
					String token = command;

					int code = -1;
					try {
						code = Integer.parseInt(token);
					} catch (NumberFormatException e) {
						// Keep the existing value.
					}

					if (code != -1) {
						String errorStr = token;
						String response = msgPayload.substring(msgPayload.indexOf(errorStr, senderInfo.length()) + 4, msgPayload.length());						
						
						processServerResponseCode( code, response, tokenizer );

						// Return from the method.
						return;
					} else {
						// This is not a server response.
						// It must be a nick without login and hostname.
						// (or maybe a NOTICE or suchlike from the server)
						sourceNick = senderInfo;
						target = token;
					}
				} else {		
					log.error("UNKNOWN CMD: " + msgPayload );
					return;
				}

			}
		}

		command = command.toUpperCase();
		if (sourceNick.startsWith(":")) {
			sourceNick = sourceNick.substring(1);
		}
		if (target == null) {
			target = tokenizer.nextToken();
		}
		if (target.startsWith(":")) {
			target = target.substring(1);
		}

		/**
		 *  Check for CTCP requests. Currently we aren't doing much with this
		 */
		if (command.equals("PRIVMSG") && msgPayload.indexOf(":\u0001") > 0 && msgPayload.endsWith("\u0001")) {
			log.info(">>>>>"+msgPayload);
			String request = msgPayload.substring(msgPayload.indexOf(":\u0001") + 2, msgPayload.length() - 1);
			if ( request.equals("VERSION") ) {
				// VERSION request
				//	this.onVersion(sourceNick, sourceLogin, sourceHostname, target);
			} else if (request.startsWith("ACTION ")) {
				// ACTION request
				//	this.onAction(sourceNick, sourceLogin, sourceHostname, target, request.substring(7));
			} else if (request.startsWith("PING ")) {
				// PING request
				//	this.onPing(sourceNick, sourceLogin, sourceHostname, target, request.substring(5));
			} else if (request.equals("TIME")) {
				// TIME request
				//	this.onTime(sourceNick, sourceLogin, sourceHostname, target);
			} else if (request.equals("FINGER")) {
				// FINGER request
				//	this.onFinger(sourceNick, sourceLogin, sourceHostname, target);
			} else if ((tokenizer = new StringTokenizer(request)).countTokens() >= 5 && tokenizer.nextToken().equals("DCC")) {
				// This is a DCC request.
				log.warn("Don't know how to process a DCC request");
				//	boolean success = _dccManager.processRequest(sourceNick, sourceLogin, sourceHostname, request);
				//	if (!success) {
				// The DccManager didn't know what to do with the line.
				//	this.onUnknown(msg);
				//	}
			} else {
				// An unknown CTCP message - ignore it.
				//	this.onUnknown(line);
				log.warn("Unknown CTCP msg: " + msgPayload);
			}
		} 

		if (command.equals("NOTICE")) {
			// Someone is sending a notice.
			//	this.onNotice(sourceNick, sourceLogin, sourceHostname, target, line.substring(line.indexOf(" :") + 2));

			log.info("NOTICE: " + msgPayload);

			if( msgPayload.contains("Password accepted")){

				outboundMsgQ.add( new IRCMsg("join " + configsPointer.getStartChannel()) );

				log.info( "pw accepted, joining default channel -- msg: " + msgPayload );

			} else if( msgPayload.contains("This nickname is registered and protected")){

				@SuppressWarnings("unused")
				UserInputBox uib = new UserInputBox(outboundMsgQ);	// this is temporary, final version
				outboundMsgQ.add( new IRCMsg("nickserv identify " + configsPointer.getNickpass()) );	//shall have GUI pointed at inboundMsgQ

			}
		} 

		/**
		 * lastly, create a ComplexMsg object and send it off to the AnalyticProcessor
		 */
		StringBuilder actualMsg = new StringBuilder();
		while( tokenizer.hasMoreElements() ){
			actualMsg.append( tokenizer.nextElement() ).append(" ");
		}
		
		ComplexMsg compleMsg = new ComplexMsg(command, target, sourceNick, sourceLogin, sourceHostname, actualMsg.toString(), msgPayload, null);
		analytics.processMessage(compleMsg);
	}



	/**
	 * Check to see if it's a code we handle
	 * @param code
	 * @param response
	 * @return
	 */
	private boolean processServerResponseCode(int code, String response, StringTokenizer tokenizer) {
		tokenizer.nextToken();	//	this first token indicates that the server is replying to kytebot

		if( code == 318 || code == 372 || code == 366 ){
			//	do nothing for the following types of messages
			//	318 - End of /WHOIS list
			//	366 - End of /NAMES list
			//	372 - RPL_MOTD (from server)
			
		} else if( code == 311 ){
			//	Handle 311    RPL_WHOISUSER
			return analytics.handleReplyWhoIsUser(tokenizer);
			
		} else if( code == 319 ){
			//	Handle 319    RPL_WHOISCHANNELS
			return analytics.handleWhoIsChannels(tokenizer);
			
		} else if( code == 353 ){
			
			//	TODO will handle a response of a /NAMES command
			
		} else {		
			log.info("Code: " + code );
			log.info("Response: " + response );
		}
		return false;
	}


}
