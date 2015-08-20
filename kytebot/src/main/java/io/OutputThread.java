package io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import msg.GenericMsg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author JKyte
 * 
 * This class reads messages off of a queue and sends them to a specified IRC server
 *
 */
public class OutputThread implements Runnable{

	private Socket socket;
	private ConcurrentLinkedQueue<GenericMsg> outboundMsgQ;
	private BufferedWriter bw;
	private final static String CRLF = "\r\n";

	private Logger log;

	public OutputThread( Socket socket, ConcurrentLinkedQueue<GenericMsg> outboundMsgQ, Logger log){

		this.setSocket(socket);
		this.outboundMsgQ = outboundMsgQ;

		if( log == null ){
			this.log = LogManager.getLogger(OutputThread.class);
		}else{
			this.log = log;
		}

		try {
			bw = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		try {

			GenericMsg msg = null;
			while( true ){

				msg = outboundMsgQ.poll();

				if( msg != null ){
					//	source will be used to determine user generated msgs vs ktyebot-generated msgs
					log.info("SOURCE: " + msg.getSource() + " OUT MSG: " + msg.getData() );

					if( msg.getSource().equals("IRC") ){

						sendMsg(msg.getData());

					} else {
						//	give ourselves the option to handle things on the outbound queue
						log.warn("Unknown source type: " + msg.getSource() + " " + msg.getData() );
					}

				}

				Thread.sleep(1000);
			}
		} catch (InterruptedException e){
			//System.out.println("e0");
			//e.printStackTrace();
		} finally {
			
			log.fatal("FATAL: OutputThread has stopped.");
		}
	}

	public void sendMsg( String msg ){
		try {

			msg += CRLF;
			bw.write( msg );
			bw.flush();

		} catch (IOException e) {
			log.fatal("WARN: OutputThread failed to send command: " + msg );
			e.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
