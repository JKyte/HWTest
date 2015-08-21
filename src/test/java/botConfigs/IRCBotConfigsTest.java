package botConfigs;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author JKyte
 * 
 * Class for IRCBotConfigs unit tests
 */
public class IRCBotConfigsTest {

	@Test
	public void testConfigs(){
		IRCBotConfigs configs = new IRCBotConfigs();
		
		configs.setNick("nickForTests");
		configs.setNickpass("nickpassForTests");
		configs.setStartChannel("#startchan");
		configs.setAJoins("#ajoin_1,#ajoin_2");
		configs.setIrcPort(8080);
		configs.setHeartBeatInMillis(9999);
		configs.setIrcServer("irc.server.net");
		configs.setTrustedUsers("user1,user2");
		configs.setStoryChans("#chan_1,#chan_2");

		Assert.assertEquals("nickForTests", configs.getNick());
		Assert.assertEquals("nickpassForTests", configs.getNickpass());
		Assert.assertEquals("#startchan", configs.getStartChannel());
		Assert.assertEquals("#ajoin_1,#ajoin_2", configs.getAJoins());
		Assert.assertEquals(8080, configs.getIrcPort());
		Assert.assertEquals(9999, configs.getHeartBeatInMillis());
		Assert.assertEquals("irc.server.net", configs.getIrcServer());
		Assert.assertEquals("user1,user2", configs.getTrustedUsers());
		
		configs.setTrustedUsers(null);
		Assert.assertEquals("", configs.getTrustedUsers());
		
		Assert.assertEquals("#chan_1,#chan_2", configs.getStoryChans());
	}
}
