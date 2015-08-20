package msg;

/**
 * 
 * @author JKyte
 *
 * This helper class contains a set of easy methods used to construct basic
 * IRC commands
 */
public class IRCCommandHelper {

	/**
	 * 
	 * @param channel
	 * @param msg
	 * @return
	 */
	public String slashMeCmd( String channel, String msg ){
		return "PRIVMSG " + channel + " :\001ACTION" + msg + "\001";
	}
	
	/**
	 * 
	 * @param channel
	 * @param msg
	 * @return
	 */
	public String sendChannelMsg( String channel, String msg ){
		return "PRIVMSG " + channel + " :" + msg;		
	}
	
	/**
	 * 
	 * @param user
	 * @param msg
	 * @return
	 */
	public String sendPrivateMsg( String user, String msg ){
		return "PRIVMSG " + user + " :" + msg;
	}
	
	/**
	 * @param targetChannel -- raw channel name, should be passed in as #channelName
	 * @return
	 */
	public String joinCmd( String targetChannel ){
		return "JOIN " + targetChannel;
	}
	
	/**
	 * @param user
	 * @return
	 */
	public String whoIsCmd( String user ){
		return "WHOIS " + user;
	}
	
	/**
	 * 
	 * @param channel
	 * @return
	 */
	public String namesCmd( String channel ){
		return "NAMES " + channel;
	}
}
