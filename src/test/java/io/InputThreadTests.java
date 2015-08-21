package io;

import io.InputThread;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import msg.GenericMsg;

/**
 * 
 * @author JKyte
 * 
 * Class for InputThread unit tests
 */
public class InputThreadTests {

	private InputThread it;
	private Socket socket;
	private ConcurrentLinkedQueue<GenericMsg> inboundMsgQ;
	
	public static final Logger log = LogManager.getLogger(InputThreadTests.class);

	private static int test_port = 2345;

	@Before
	public void setup(){
		incrementPortCount();

		connectOrRebindSocket();

		inboundMsgQ = new ConcurrentLinkedQueue<GenericMsg>();
		log.info("test msg");
		it = new InputThread(socket, inboundMsgQ, log);
	}

	public boolean connectOrRebindSocket(){
		boolean connected = false;

		try {
			log.info("Bind to port: " + test_port );
			if( socket == null){
				socket = new Socket();
			}
			socket.setReuseAddress(true);

			socket.bind(new InetSocketAddress("localhost",  test_port));
			socket.connect(new InetSocketAddress("localhost",  test_port));
			return true;

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connected;
	}

	public static synchronized void incrementPortCount() {
		test_port++;
	}

	@Test
	public void testGoodMsg(){

		try {

			final String IRC_PAYLOAD = "IRC datas";

			BufferedWriter socketWriter;

			socketWriter = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));

			Thread t0 = new Thread(it);
			t0.start();

			socketWriter.write(IRC_PAYLOAD + "\r\n");
			socketWriter.flush();

			while( inboundMsgQ.peek() == null ){
				//	wait for msg to be added to the queue
			}
			GenericMsg testMsg = inboundMsgQ.poll();

			Assert.assertEquals("IRC", testMsg.getSource());
			Assert.assertEquals(IRC_PAYLOAD, testMsg.getData());


			try{
				t0.interrupt();
			} catch (Exception e2){
				log.error(e2);
			}

		} catch (IOException e) {
			log.error(e);
		}
	}

	@After
	public void cleanup(){
		it = null;
	}

}
