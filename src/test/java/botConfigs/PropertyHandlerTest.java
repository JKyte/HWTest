package botConfigs;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author JKyte
 *
 */
public class PropertyHandlerTest {

	@Test
	public void fullTest(){
		
		PropertyHandler ph = new PropertyHandler();

		//	Test the readPropertyFile method
		Properties prop = PropertyHandler.readPropertyFile(ph.TEST_DEFAULT);

		Assert.assertEquals("nick", prop.getProperty("nick"));
		Assert.assertEquals("120000", prop.getProperty("heartbeat"));
		Assert.assertEquals("passwd", prop.getProperty("passwd"));
		Assert.assertEquals("#chan1,#chan2,#chan3", prop.getProperty("ajoins"));
		Assert.assertEquals("6667", prop.getProperty("ircport"));
		Assert.assertEquals("tr1,tr2", prop.getProperty("trustedusers"));
		Assert.assertEquals("#chan1,#chan4", prop.getProperty("storychans"));
		Assert.assertEquals("#startchan", prop.getProperty("startchan"));
		Assert.assertEquals("irc.server.net", prop.getProperty("ircserver"));

		
		//	Test the updateExistingProperties method
		PropertyHandler.updateExistingPropertyFile(ph.TEST_DEFAULT, "nick", "nicky");	
		prop = PropertyHandler.readPropertyFile(ph.TEST_DEFAULT);

		Assert.assertEquals("nicky", prop.getProperty("nick"));


		//	Test the writePropertyFile as we fix the defaul nick
		prop.setProperty("nick", "nick");
		PropertyHandler.writePropertyFile(ph.TEST_DEFAULT, prop);

		
		//	Ensure nothing changed after the test
		prop = PropertyHandler.readPropertyFile(ph.TEST_DEFAULT);

		Assert.assertEquals("nick", prop.getProperty("nick"));
		Assert.assertEquals("120000", prop.getProperty("heartbeat"));
		Assert.assertEquals("passwd", prop.getProperty("passwd"));
		Assert.assertEquals("#chan1,#chan2,#chan3", prop.getProperty("ajoins"));
		Assert.assertEquals("6667", prop.getProperty("ircport"));
		Assert.assertEquals("tr1,tr2", prop.getProperty("trustedusers"));
		Assert.assertEquals("#chan1,#chan4", prop.getProperty("storychans"));
		Assert.assertEquals("#startchan", prop.getProperty("startchan"));
		Assert.assertEquals("irc.server.net", prop.getProperty("ircserver"));
	}
}
