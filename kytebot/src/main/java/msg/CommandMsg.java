package msg;

/**
 * 
 * @author JKyte
 *
 * This class encapsulates all variables needed for a CommandMsg (action given by a user)
 */
public class CommandMsg extends GenericMsg {

	private final static String COMMAND = "COMMAND";
	
	public CommandMsg(String data) {
		super(COMMAND, data);
	}

}
