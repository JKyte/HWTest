package msg;

import msg.CommandMsg;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author JKyte
 *
 */
public class CommandMsgTest {

	@Test
	public void testGenericMsgPayload(){
		
		CommandMsg testMsg = new CommandMsg("datas");
		Assert.assertEquals("datas", testMsg.getData());
	}
	
	@Test
	public void testGenericMsgSource(){
		String expectedSource = "COMMAND";
		CommandMsg testMsg = new CommandMsg("datas");
		Assert.assertEquals(expectedSource, testMsg.getSource());
	}
	
	@Test
	public void testGenericMsgToString(){
		CommandMsg testMsg = new CommandMsg("datas");
		Assert.assertEquals("COMMAND datas", testMsg.toString());	
	}
}
