package msg;

import msg.GenericMsg;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author JKyte
 *
 */
public class GenericMsgTest {

	@Test
	public void testGenericMsgPayload(){
		
		GenericMsg testMsg = new GenericMsg("sauce", "datas");
		Assert.assertEquals("datas", testMsg.getData());
	}
	
	@Test
	public void testGenericMsgSource(){
		String expectedSource = "sauce";
		GenericMsg testMsg = new GenericMsg("sauce", "datas");
		Assert.assertEquals(expectedSource, testMsg.getSource());
		
		testMsg.setSource("nuSauce");
		testMsg.setData("nuDatas");
		Assert.assertEquals("nuSauce", testMsg.getSource());
		Assert.assertEquals("nuDatas", testMsg.getData());
	}
	
	@Test
	public void testGenericMsgToString(){
		GenericMsg testMsg = new GenericMsg("sauce", "datas");
		Assert.assertEquals("sauce datas", testMsg.toString());	
	}
}
