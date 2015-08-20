package msg;

import msg.ComplexMsg;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author JKyte
 *
 */
public class ComplexMsgTest {
	
	@Test
	public void testComplexMsgConstructorNoColon(){
		ComplexMsg msg = new ComplexMsg("command", "target", "sourceNick",
				"sourceLogin", "sourceHostname", "actualMsg", "originalMsg", "originOfMsg");
		
		Assert.assertEquals("command", msg.getCommand());
		Assert.assertEquals("target", msg.getTarget());
		Assert.assertEquals("sourceNick", msg.getSourceNick());
		Assert.assertEquals("sourceLogin", msg.getSourceLogin());
		Assert.assertEquals("sourceHostname", msg.getSourceHostname());
		Assert.assertEquals("actualMsg", msg.getActualMsg());
		Assert.assertEquals("originalMsg", msg.getOriginalMsg());
		Assert.assertEquals("originOfMsg", msg.getOriginOfMsg());
		Assert.assertEquals("command originalMsg", msg.toString());
	}
	
	@Test
	public void testComplexMsgConstructorWithColon(){
		ComplexMsg msg = new ComplexMsg("command", "target", "sourceNick",
				"sourceLogin", "sourceHostname", ":actualMsg", "originalMsg", "originOfMsg");
		
		Assert.assertEquals("actualMsg", msg.getActualMsg());	
	}
	
	
}
