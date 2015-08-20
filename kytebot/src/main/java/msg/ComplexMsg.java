package msg;

/**
 * 
 * @author JKyte
 * 
 * This class facilitates easier parsing of IRC messages
 *
 */
public class ComplexMsg {
	
	private String command;
	private String target;
	private String sourceNick;
	private String sourceLogin;
	private String sourceHostname;
	private String actualMsg;
	
	private String originalMsg;
	private String originOfMsg;
	
	/**
	 * 
	 * @param command - parsed command
	 * @param target - the target of the command
	 * @param sourceNick - who this is from
	 * @param sourceLogin - N/A
	 * @param sourceHostname - N/A
	 * @param actualMsg - msg payload
	 * @param originalMsg - full original msg
	 * @param originOfMsg - where the msg is from
	 */
	public ComplexMsg( String command, String target, String sourceNick,
			String sourceLogin, String sourceHostname, String actualMsg,
			String originalMsg, String originOfMsg ){
		
		this.setCommand(command);
		this.setTarget(target);
		this.setSourceNick(sourceNick);
		this.setSourceLogin(sourceLogin);
		this.setSourceHostname(sourceHostname);
		
		this.setActualMsg(actualMsg);
		if( this.actualMsg.startsWith(":") ){
			this.actualMsg = this.actualMsg.substring(1);
		}
		
		this.setOriginalMsg(originalMsg);
		this.setOriginOfMsg(originOfMsg);
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getSourceNick() {
		return sourceNick;
	}

	public void setSourceNick(String sourceNick) {
		this.sourceNick = sourceNick;
	}

	public String getSourceLogin() {
		return sourceLogin;
	}

	public void setSourceLogin(String sourceLogin) {
		this.sourceLogin = sourceLogin;
	}

	public String getSourceHostname() {
		return sourceHostname;
	}

	public void setSourceHostname(String sourceHostname) {
		this.sourceHostname = sourceHostname;
	}

	public String getActualMsg() {
		return actualMsg;
	}

	public void setActualMsg(String actualMsg) {
		this.actualMsg = actualMsg;
	}

	public String getOriginalMsg() {
		return originalMsg;
	}

	public void setOriginalMsg(String originalMsg) {
		this.originalMsg = originalMsg;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getOriginOfMsg() {
		return originOfMsg;
	}

	public void setOriginOfMsg(String originOfMsg) {
		this.originOfMsg = originOfMsg;
	}
	
	@Override
	public String toString(){
		return command + " " + getOriginalMsg();
	}
	
}
