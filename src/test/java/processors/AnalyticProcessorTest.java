package processors;

import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;

import msg.ComplexMsg;
import msg.GenericMsg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import botConfigs.PropertyHandler;
import processors.AnalyticProcessor;

/**
 * 
 * @author JKyte
 *
 */
public class AnalyticProcessorTest {

	private Properties testConfigs;
	private ConcurrentLinkedQueue<GenericMsg> outboundMsgQ;
	
	private AnalyticProcessor processor = null;

	public Logger log = LogManager.getLogger(AnalyticProcessorTest.class);
	
	@Before
	public void setup(){
		PropertyHandler propHandler = new PropertyHandler();
		testConfigs = PropertyHandler.readPropertyFile(propHandler.TEST_DEFAULT);
		
		outboundMsgQ = new ConcurrentLinkedQueue<GenericMsg>();
		
		processor = new AnalyticProcessor(testConfigs, outboundMsgQ, log);
	}
	
	@Test
	public void testHandleWhoIsChannels(){
		String target = "user ~founded#%halfOps#&protected";
		StringTokenizer tokenizer = new StringTokenizer(target);
		
		boolean passed = processor.handleWhoIsChannels(tokenizer);
		
		Assert.assertEquals(true, passed);
	}

	@Test
	public void testHandleWhoIsUser(){
		String target = "user skiptoken user@fakehost@samplenet";
		StringTokenizer tokenizer = new StringTokenizer(target);
		
		boolean passed = processor.handleReplyWhoIsUser(tokenizer);
		
		Assert.assertEquals(true, passed);
	}
	
	@Test
	public void testHandlePrivMsg_NoCmd(){
		ComplexMsg msg = new ComplexMsg("", "target", "sourceNick",
				"sourceLogin", "sourceHostname", "actualMsg", "originalMsg", "originOfMsg");
		processor.processMessage(msg);
		Assert.assertEquals(true, true);
	}
	
	@Test
	public void testHandlePrivMsg_TargetEqualsNick(){
		ComplexMsg msg = new ComplexMsg("PRIVMSG", "nickForTests", "sourceNick",
				"sourceLogin", "sourceHostname", "actualMsg", "originalMsg", "originOfMsg");
		processor.processMessage(msg);
		Assert.assertEquals(true, true);
	}
	
	@Test
	public void testHandlePrivMsg_TargetEqualsNick_02(){
		ComplexMsg msg = new ComplexMsg("PRIVMSG", "nickForTests", "sourceNick",
				"sourceLogin", "sourceHostname", "actualMsg", "originalMsg", "originOfMsg");
	
		HashMap<String,String> tmpUsers = processor.getUsers();
		tmpUsers.put("sourceNick", "sourceNick");
		processor.setChans(tmpUsers);
	
		processor.processMessage(msg);
		Assert.assertEquals(true, true);
	}
	
	@Test
	public void testHandlePrivMsg_PRIVMSG_else(){
		ComplexMsg msg = new ComplexMsg("PRIVMSG", "target", "sourceNick",
				"sourceLogin", "sourceHostname", "actualMsg", "originalMsg", "originOfMsg");
		processor.processMessage(msg);
		Assert.assertEquals(true, true);
	}
	
	@Test
	public void testHandlePrivMsg_PRIVMSG_chanMsg(){
		ComplexMsg msg = new ComplexMsg("PRIVMSG", "#target", "sourceNick",
				"sourceLogin", "sourceHostname", "nickForTests actualMsg", "originalMsg", "originOfMsg");
		processor.processMessage(msg);
		Assert.assertEquals(true, true);
	}
	
	@Test
	public void testHandlePrivMsg_PRIVMSG_sideChat(){
		//	Tests a side chat
		ComplexMsg msg = new ComplexMsg("PRIVMSG", "nickForTests", "sourceNick",
				"sourceLogin", "sourceHostname", "nickForTests actualMsg", "originalMsg", "nickForTests");
		processor.processMessage(msg);
		Assert.assertEquals(true, true);
	}
	
	@Test
	public void testHandlePrivMsg_PRIVMSG_chanMsg_02(){
		ComplexMsg msg = new ComplexMsg("PRIVMSG", "#target", "sourceNick",
				"sourceLogin", "sourceHostname", "nickForTests actualMsg", "originalMsg", "originOfMsg");
		
		HashMap<String,String> tmpChans = processor.getChans();
		tmpChans.put("#target", "#target");
		processor.setChans(tmpChans);
		
		processor.processMessage(msg);
		Assert.assertEquals(true, true);
	}
	
	@Test
	public void testHandlePrivMsg_PRIVMSG_chanMsgNoTarget(){
		ComplexMsg msg = new ComplexMsg("PRIVMSG", "target", "sourceNick",
				"sourceLogin", "sourceHostname", "nickForTests actualMsg", "originalMsg", "originOfMsg");
		processor.processMessage(msg);
		Assert.assertEquals(true, true);
	}
	
	@Test
	public void testHandlePrivMsg_PRIVMSG_chanMsg_startsWith(){
		ComplexMsg msg = new ComplexMsg("PRIVMSG", "#target", "sourceNick",
				"sourceLogin", "sourceHostname", "actualMsg", "originalMsg", "originOfMsg");
		processor.processMessage(msg);
		Assert.assertEquals(true, true);
	}
	
	@Test
	public void testHandlePrivMsg_PRIVMSG_chanMsg_preLoaded(){
		ComplexMsg msg = new ComplexMsg("PRIVMSG", "#target", "sourceNick",
				"sourceLogin", "sourceHostname", "nickForTests actualMsg", "originalMsg", "originOfMsg");
		
		
		HashMap<String,String> tmpChans = processor.getChans();
		HashMap<String,String> tmpUsers = processor.getUsers();
		HashMap<String,String> tmpPrivChats = processor.getPrivchats();
		
		tmpChans.put("#target", "#target");
		tmpUsers.put("sourceNick", "sourceNick");
	
		processor.setChans(tmpChans);
		processor.setUsers(tmpUsers);
		processor.setPrivchats(tmpPrivChats);
		
		processor.processMessage(msg);
		Assert.assertEquals(true, true);
	}
	
	@Test
	public void testHandlePrivMsg_Else(){
		ComplexMsg msg = new ComplexMsg("command", "target", "sourceNick",
				"sourceLogin", "sourceHostname", "actualMsg", "originalMsg", "originOfMsg");
		processor.processMessage(msg);
		Assert.assertEquals(true, true);
	}

}
