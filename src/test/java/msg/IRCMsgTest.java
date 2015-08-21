package msg;

import msg.IRCMsg;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author JKyte
 *
 */
public class IRCMsgTest {
	
	@Test
	public void testIRCMsgPayload(){
		
		IRCMsg testMsg = new IRCMsg("datas");
		Assert.assertEquals("datas", testMsg.getData());
	}
	
	@Test
	public void testIRCMsgSource(){
		String expectedSource = "IRC";
		IRCMsg testMsg = new IRCMsg("datas");
		Assert.assertEquals(expectedSource, testMsg.getSource());
	}

	@Test
	public void testIRCMsgToString(){
		IRCMsg testMsg = new IRCMsg("datas");
		Assert.assertEquals("IRC datas", testMsg.toString());	
	}
}
