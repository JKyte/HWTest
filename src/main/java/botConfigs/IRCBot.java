package botConfigs;

import io.InputThread;
import io.OutputThread;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import processors.GenericMessageParser;
import msg.GenericMsg;
import msg.IRCMsg;


/**
 * 
 * @author JKyte
 *
 * A basic implementation of an IRCBot modeled closely after the PIRCBot framework
 */
public class IRCBot implements Runnable {

	private Properties configs;
	private Socket socket;
	private long heartBeatInMillis = -1;

	private ConcurrentLinkedQueue<GenericMsg> inboundMsgQ;
	private ConcurrentLinkedQueue<GenericMsg> outboundMsgQ;

	private GenericMessageParser parser;
	
	public static final Logger log = LogManager.getLogger(IRCBot.class);

	/**
	 * Only initialize the queues at first
	 */
	public IRCBot( Properties prop ){
		this.configs = prop;
		this.heartBeatInMillis = Long.parseLong(prop.getProperty("heartbeat"));
		inboundMsgQ = new ConcurrentLinkedQueue<GenericMsg>();
		outboundMsgQ = new ConcurrentLinkedQueue<GenericMsg>();
	}


	/**
	 * Handles initializing all threads, initial authorization
	 */
	@Override
	public void run() {

		log.info("IRCBot is running");

		try {
			String host = configs.getProperty("ircserver");
			int port = Integer.parseInt(configs.getProperty("ircport"));
			socket = new Socket( host, port );

			InputThread it = new InputThread(socket, inboundMsgQ, null);			
			OutputThread ot = new OutputThread(socket, outboundMsgQ, null);
			parser = new GenericMessageParser(configs, inboundMsgQ, outboundMsgQ, null);
			
			Thread t1 = new Thread( it );
			Thread t2 = new Thread( ot );
			Thread t3 = new Thread( parser );

			t1.start();
			t2.start();
			t3.start();

			/**
			 * Sleep to let things initialize
			 */
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				log.error(e);
			}

			String nick = configs.getProperty("nick");
			String pass = configs.getProperty("passwd");
			outboundMsgQ.add( new IRCMsg("nick " + nick) );
			outboundMsgQ.add( new IRCMsg("USER "+nick+" 0 * :"+nick) );
			outboundMsgQ.add( new IRCMsg("msg NickServ identify " + pass) );
			
			while( true ){

				log.info("IRCBot keep alive loop");

				try {
					Thread.sleep(heartBeatInMillis);
				} catch (InterruptedException e) {
					log.error(e);
				}
			}

		} catch (UnknownHostException e1) {
			log.error(e1.getStackTrace());
		} catch (IOException e2) {
			log.error(e2.getStackTrace());
		}
	}
	
}
