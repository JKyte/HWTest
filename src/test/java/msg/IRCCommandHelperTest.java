package msg;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import msg.IRCCommandHelper;


/**
 * 
 * @author JKyte
 * 
 * This is the first collection of unit tests
 *
 */
public class IRCCommandHelperTest {
	
	private IRCCommandHelper helper = null;
	
	@Before
	public void beforeTest(){
		//	ensures we have a fresh object to work with.
		//	currrently worthless as the IRCCH has no constructor
		helper = new IRCCommandHelper();
	}
	
	@Test
	public void testSlashMeCmd(){
		String msg = helper.slashMeCmd("#channel", "msg");
		Assert.assertEquals("PRIVMSG #channel :\u0001ACTIONmsg\u0001", msg);
	}
	
	
	@Test
	public void testSendChannelMsg(){
		String msg = helper.sendChannelMsg("#channel", "msg");
		Assert.assertEquals("PRIVMSG #channel :msg", msg);
	}
	
	@Test
	public void testSendPrivateMsg(){
		String msg = helper.sendPrivateMsg("user", "msg");
		Assert.assertEquals("PRIVMSG user :msg", msg);
	}
	
	@Test
	public void testJoinCmd(){
		String msg = helper.joinCmd("targetChannel");
		Assert.assertEquals("JOIN targetChannel", msg);
	}
	
	@Test
	public void testWhoIsCmd(){
		String msg = helper.whoIsCmd("user");
		Assert.assertEquals("WHOIS user", msg);
	}
	
	@Test
	public void testNamesCmd(){
		String msg = helper.namesCmd("targetChannel");
		Assert.assertEquals("NAMES targetChannel", msg);
	}
}
