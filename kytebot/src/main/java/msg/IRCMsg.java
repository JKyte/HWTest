package msg;

/**
 * 
 * @author JKyte
 *
 * This class encapsulates all information related to an IRCMsg
 */
public class IRCMsg extends GenericMsg {
	
	private final static String IRC = "IRC";

	public IRCMsg(String data) {
		super(IRC, data);
	}

}
