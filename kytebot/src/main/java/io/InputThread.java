package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import msg.GenericMsg;
import msg.IRCMsg;

/**
 * 
 * @author JKyte
 *
 * Simple class that reads lines off a socket's input stream, wraps the line in an IRCMsg object, 
 * and adds the IRCMsg to a ConcurrentLinkedQueue for further processing
 */
public class InputThread implements Runnable{

	private Socket socket;
	private ConcurrentLinkedQueue<GenericMsg> inboundMsgQ; //	so we can add to the queue the GenericMessageParser reads from
	
	private Logger log = null;
	
	public InputThread( Socket socket, ConcurrentLinkedQueue<GenericMsg> inboundMsgQ, Logger log){
		
		this.socket = socket;
		this.inboundMsgQ = inboundMsgQ;

		if( log == null ){
			this.log = LogManager.getLogger(InputThread.class);
		}else{
			this.log = log;
		}
		
	}
	
	@Override
	public void run(){
		
		String msg = null;
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			
			while ( true ){
				
				msg = br.readLine();
				
				if( msg != null ){
					log.info(msg);	//	log every msg by default
					inboundMsgQ.add(new IRCMsg(msg) );
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			log.error(msg);
			log.fatal("FATAL: InputThread crashed.");
		}
	}
}
