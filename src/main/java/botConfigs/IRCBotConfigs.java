package botConfigs;

/**
* 
* @author JKyte
*
*	This class will pull configs from the IRCBot class into a separate class so 
*	code will be more decoupled.
*/
public class IRCBotConfigs {

	private String nick;
	private String nickpass;
	private String startChannel;
	private String ajoins;
	
	private String ircServer;
	private int ircPort = 6667;
	private long heartBeatInMillis = 120000;
	
	private String trustedUsers;
	private String storyChannels;
	
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getStartChannel() {
		return startChannel;
	}

	public void setStartChannel(String startChannel) {
		this.startChannel = startChannel;
	}

	public String getAJoins() {
		return ajoins;
	}

	public void setAJoins(String ajoins) {
		this.ajoins = ajoins;
	}

	public String getIrcServer() {
		return ircServer;
	}

	public void setIrcServer(String ircServer) {
		this.ircServer = ircServer;
	}

	public String getNickpass() {
		return nickpass;
	}

	public void setNickpass(String nickpass) {
		this.nickpass = nickpass;
	}

	public int getIrcPort() {
		return ircPort;
	}

	public void setIrcPort(int ircPort) {
		this.ircPort = ircPort;
	}

	public long getHeartBeatInMillis() {
		return heartBeatInMillis;
	}

	public void setHeartBeatInMillis(long heartBeatInMillis) {
		this.heartBeatInMillis = heartBeatInMillis;
	}

	public String getTrustedUsers() {
		if( trustedUsers == null ){
			return "";
		}else{
			return trustedUsers;			
		}
	}

	/**
	 * @param trustedUsers - a comma-deliminated string of usernames who may issue commands to the IRC bot
	 */
	public void setTrustedUsers(String trustedUsers) {
		this.trustedUsers = trustedUsers;
	}

	public String getStoryChans() {
		return storyChannels;
	}

	/**
	 * @param trustedUsers - a comma-deliminated string of channels where kytebot may tell stories
	 */
	public void setStoryChans(String storyChans) {
		this.storyChannels = storyChans;
	}
}
