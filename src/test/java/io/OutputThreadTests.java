package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentLinkedQueue;

import io.OutputThread;
import msg.CommandMsg;
import msg.GenericMsg;
import msg.IRCMsg;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author JKyte
 * 
 * Class for OutputThread unit tests.
 *
 */
public class OutputThreadTests {

	private OutputThread ot;
	private Socket socket;
	private ConcurrentLinkedQueue<GenericMsg> outboundMsgQ;

	private static int test_port = 1234;
	
	public static final Logger log = LogManager.getLogger(OutputThreadTests.class);


	@Before
	public void before(){

		incrementPortCount();

		connectOrRebindSocket();

		outboundMsgQ = new ConcurrentLinkedQueue<GenericMsg>();

		ot = new OutputThread(socket, outboundMsgQ, log);
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
	public void sendGoodIRCMsg(){

		try {

			final String IRC_PAYLOAD = "IRC datas";
			IRCMsg testMsg = new IRCMsg( IRC_PAYLOAD );

			BufferedReader socketReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			String lineRead = null;
			Thread t0 = new Thread(ot);
			t0.start();

			outboundMsgQ.add(testMsg);

			lineRead = socketReader.readLine();
			if( lineRead != null ){
				try{
					t0.interrupt();
				} catch (Exception e2){
					log.error(e2);
				}
			}

			Assert.assertEquals(IRC_PAYLOAD, lineRead);
		} catch (IOException e) {
			log.error(e);
		}
	}

	@Test
	public void sendNotHandledMsgType(){

		try {

			final String BAD_PAYLOAD = "Bad datas";
			CommandMsg testMsg = new CommandMsg( BAD_PAYLOAD );

			BufferedReader socketReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			Thread t0 = new Thread(ot);
			t0.start();

			outboundMsgQ.add(testMsg);

			boolean socketReady = socketReader.ready();
			if( !socketReady ){
				try{
					t0.interrupt();
				} catch (Exception e2){
					log.error(e2);
				}
			}

			Assert.assertEquals( false, socketReady);
		} catch (IOException e) {
			log.error(e);
		}
	}


	@Test
	public void testSendMsg(){

		final String TEST_MSG = "test msg";
		try {
			BufferedReader socketReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			ot.sendMsg(TEST_MSG);

			String lineSent = socketReader.readLine();
			Assert.assertEquals(TEST_MSG, lineSent);
		} catch (IOException e) {
			log.error(e);
		}
	}

	@After
	public void afterTest(){
		/**
		 * Sockets are not closed, rather the JVM is instructed to re-use the port number. The only method that writes
		 * to the socket also flushes, so we don't have to worry about latent data getting stuck in the pipe (in effect
		 * causing cascading test failures).
		 */
		ot = null;
	}
}
