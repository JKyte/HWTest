package processors;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import msg.CommandMsg;
import msg.GenericMsg;
import msg.IRCMsg;

/**
 * 
 * @author JKyte
 * 
 * This class is based on code found on stackoverflow
 */
@SuppressWarnings("serial")
public class UserInputBox extends JFrame implements ActionListener {

	private JButton sendMsgBtn = new JButton("Send msg");
	private JButton sendCmdBtn = new JButton("Send cmd");

	private JTextArea textArea = new JTextArea(8, 40);

	private JScrollPane scrollPane = new JScrollPane(textArea);

	private ConcurrentLinkedQueue<GenericMsg> outboundMsgQ;
	
	public static final Logger log = LogManager.getLogger(UserInputBox.class);

	@SuppressWarnings("deprecation")
	public UserInputBox(  ConcurrentLinkedQueue<GenericMsg> msgQ ) {

		this.outboundMsgQ = msgQ;

		JPanel p = new JPanel();

		p.add(sendMsgBtn);
		sendMsgBtn.addActionListener(this);
		
		p.add(sendCmdBtn);
		sendCmdBtn.addActionListener(this);

		getContentPane().add(p, "South");

		getContentPane().add(scrollPane, "Center");

		setTitle("TextAreaTest");
		setSize(300, 300);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.show();
	}

	protected JButton createButton(String name, int virtualKey) {
        JButton btn = new JButton(name);
        btn.addActionListener(new ActionListener() {
            
            @Override
			public void actionPerformed(ActionEvent e) {
                log.info(e.getActionCommand() + " was clicked");
            }
        });
        btn.setMargin(new Insets(8, 8, 8, 8));
        InputMap im = btn.getInputMap(0);
        ActionMap am = btn.getActionMap();
        im.put(KeyStroke.getKeyStroke(virtualKey, 0), "clickMe");
        am.put("clickMe", new AbstractAction() {
            
            @Override
			public void actionPerformed(ActionEvent e) {
                JButton btn = (JButton) e.getSource();
                btn.doClick();
            }
        });
        return btn;
    }
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == sendMsgBtn){

			String cmd = textArea.getText();
			IRCMsg msg = new IRCMsg(cmd);
			outboundMsgQ.add(msg);
			textArea.setText("");
			
		}else if (source == sendCmdBtn) {
			
			String cmd = textArea.getText();
			CommandMsg cmdMsg = new CommandMsg(cmd);
			outboundMsgQ.add(cmdMsg);
			textArea.setText("");
		}
	}

}
