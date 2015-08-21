package processors;

import java.util.concurrent.ConcurrentLinkedQueue;

import msg.GenericMsg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import botConfigs.IRCBotConfigs;
import processors.GenericMessageParser;

/**
 * 
 * @author JKyte
 *
 */
public class GenericMessageParserTest {
	
	private IRCBotConfigs testConfigs;
	private GenericMessageParser parser;

	private ConcurrentLinkedQueue<GenericMsg> inboundMsgQ;
	private ConcurrentLinkedQueue<GenericMsg> outboundMsgQ;
	
	private Logger log = LogManager.getLogger(GenericMessageParserTest.class);
	
	@Before
	public void setup(){
		testConfigs = new IRCBotConfigs();
		testConfigs.setStartChannel("#startChanForTests");
		testConfigs.setNick("nickForTests");
		
		inboundMsgQ = new ConcurrentLinkedQueue<GenericMsg>();
		outboundMsgQ = new ConcurrentLinkedQueue<GenericMsg>();
		
		parser = new GenericMessageParser(testConfigs, inboundMsgQ, outboundMsgQ, log);
	}
	
	@Test
	public void testParseRawMsg_PING(){
		String rawMsg = "PING a1b2c3";
		parser.parseRawMsg(rawMsg);
		
		GenericMsg ircMsg = outboundMsgQ.poll();
		Assert.assertEquals("IRC", ircMsg.getSource() );
		Assert.assertEquals("PONG a1b2c3", ircMsg.getData() );
	}

	@Test
	public void testParseRawMsg_server_353(){
		//	Provides coverage, nothing to Assert
		String rawMsg = ":irc.server.net 353 nickForTests = #chan :nickForTests @Nick";
		parser.parseRawMsg(rawMsg);
	}
	
	@Test
	public void testParseRawMsg_server_366(){
		//	Provides coverage, nothing to Assert
		String rawMsg = ":irc.server.net 366 nickForTests #chan :End of /NAMES list.";
		parser.parseRawMsg(rawMsg);
	}
	
	
	@Test
	public void testParseRawMsg_server_372(){
		//	Provides coverage, nothing to Assert
		String rawMsg = ":irc.server.net 372 nickForTests : MOTD";
		parser.parseRawMsg(rawMsg);
	}
	
	@Test
	public void testParseRawMsg_server_376(){
		//	Provides coverage, nothing to Assert
		String rawMsg = ":irc.server.net 376 msg :End of /MOTD command.";
		parser.parseRawMsg(rawMsg);
	}
	
	@Test
	public void testParseRawMsg_server_451(){
		//	Provides coverage, nothing to Assert
		String rawMsg = ":irc.server.net 451 msg :You have not registered";
		parser.parseRawMsg(rawMsg);
	}
	
	@After
	public void tearDown(){
		testConfigs = null;
		inboundMsgQ = null;
		outboundMsgQ = null;
		log = null;
		parser = null;
	}

}
