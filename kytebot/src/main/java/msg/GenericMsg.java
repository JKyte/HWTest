package msg;

/**
 * 
 * @author JKyte
 *
 * This class shall act as a generic encapsulation of a message
 */
public class GenericMsg {

	String source;

	String data;

	public GenericMsg(String source, String data){
		this.source = source;
		this.data = data;
	}


	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String toString(){
		return source + " " + data;
	}

}
