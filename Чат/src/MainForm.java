import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.DefaultListModel;

import java.awt.GridBagLayout;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.sql.Date;
import java.awt.GridBagConstraints;
import java.awt.Color;

import javax.swing.text.BadLocationException;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JList;
import javax.swing.JOptionPane;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class MainForm implements Observer {

	private JFrame frame;
	private JTextField localLog;
	private JTextField remoteLog;
	private JTextField remoteAddr;
	private JTextField msg;
	private CallListenerThread callListenerThread;
	public static MainForm window;
	private DefaultListModel<Object> dlm;
	private JList<Object> list;
	private Connection connection;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new MainForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MainForm() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.getContentPane().setMinimumSize(new Dimension(300, 400));
		frame.setResizable(false);
		frame.setMinimumSize(new Dimension(310, 400));
		frame.setBounds(100, 100, 300, 471);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 50, 300, 353);
		frame.getContentPane().add(scrollPane);

		list = new JList<Object>();
		scrollPane.setViewportView(list);

		localLog = new JTextField();
		localLog.setText("Enter your name");
		localLog.setToolTipText("Enter your name\r\n");
		localLog.setBackground(Color.WHITE);
		localLog.setBounds(0, 0, 100, 23);
		frame.getContentPane().add(localLog);
		localLog.setColumns(10);

		JButton applyBut = new JButton("Apply");
		applyBut.setBackground(Color.WHITE);
		applyBut.setBounds(0, 24, 100, 25);
		frame.getContentPane().add(applyBut);

		remoteLog = new JTextField();
		remoteLog.setText("\r\nFriend");
		remoteLog.setVerifyInputWhenFocusTarget(false);
		remoteLog.setBounds(202, 0, 100, 23);
		frame.getContentPane().add(remoteLog);
		remoteLog.setColumns(10);

		remoteAddr = new JTextField();
		remoteAddr.setToolTipText("Enter your IP");
		remoteAddr.setText("Enter your IP");
		remoteAddr.setBounds(101, 0, 100, 23);
		frame.getContentPane().add(remoteAddr);
		remoteAddr.setColumns(10);

		JButton disconnectBut = new JButton("Disconnect");
		disconnectBut.setBackground(Color.RED);
		disconnectBut.setBounds(202, 24, 100, 25);
		frame.getContentPane().add(disconnectBut);

		JButton connectBut = new JButton("Connect");
		connectBut.setBackground(Color.GREEN);
		connectBut.setBounds(101, 24, 100, 25);
		frame.getContentPane().add(connectBut);

		msg = new JTextField();
		msg.setBounds(0, 401, 200, 41);
		frame.getContentPane().add(msg);
		msg.setColumns(10);

		final JButton sendBut = new JButton("Send");
		sendBut.setBackground(Color.WHITE);
		sendBut.setBounds(202, 401, 100, 41);
		frame.getContentPane().add(sendBut);
		sendBut.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if ((localLog.getText().equals("")) || (remoteLog.getText().equals(""))
						|| (remoteAddr.getText().equals(""))) {
					JOptionPane.showMessageDialog(frame, "Not enough data for sending the message");
				} else {
					String name = new String();
					if (localLog.getText().length() > 10) {
						try {
							name = localLog.getText(0, 10);
						} catch (BadLocationException ignore) {
						}
						name = name + "...";
					} else
						name = localLog.getText();
					long date = System.currentTimeMillis();
					dlm.addElement("<html>" + name + " " + new Date(date).toLocaleString() + ":<br>" + msg.getText()
							+ " </span></html>");
					list.setModel(dlm);

					try {
						connection.sendMessage(msg.getText());
						System.out.println("Sended");
					} catch (IOException ex) {
						System.out.println("No internet connection");
					}

				}
				msg.setText("");
				msg.requestFocus();
			}
		});

		msg.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					sendBut.doClick();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}

			@Override
			public void keyTyped(KeyEvent arg0) {

			}
		});

		connectBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Caller caller = new Caller(localLog.getText(), remoteAddr.getText());
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							connection = caller.call();

							if (caller.getCallStatus().toString().equals("OK"))
								remoteLog.setText(caller.getRemoteNick());
							else if (caller.getCallStatus().toString().equals("BUSY")) {
								JOptionPane.showMessageDialog(frame, "User " + caller.getRemoteNick() + " is busy");
							} else {
								JOptionPane.showMessageDialog(frame,
										"User " + caller.getRemoteNick() + " has declined your call.");
								connection = null;
							}

						} catch (Exception ex) { // Show message that remote
							// user is offline or wrong
							// ip
							JOptionPane.showMessageDialog(frame,
									"Connection error. User with ip does not exist or there is no Internet connection");
							connection = null;
						}
					}
				}).start();
			}

		});

		disconnectBut.addActionListener(e -> {
			if (connection != null)
				try {
					remoteLog.setText("");
					remoteAddr.setText("");
					connection.disconnect();
					if (callListenerThread != null)
						callListenerThread.setBusy(false);

				} catch (IOException ignored) {
				}
		});

		applyBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (callListenerThread == null) {
					System.out.println("Added obs");
					callListenerThread = new CallListenerThread(new CallListener(localLog.getText()));
					callListenerThread.addObserver(window);
				} else {
					callListenerThread.setLocalNick(localLog.getText());
				}
			}
		});

		dlm = new DefaultListModel<Object>();
	}

	public boolean question(String nick, String remoteAddress) {
		Object[] options = { "Receive", "Reject" };
		int dialogResult = JOptionPane.showOptionDialog(frame,
				"User " + nick + " with ip " + remoteAddress + " is trying to connect with you", "Recieve connection",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		if (dialogResult == JOptionPane.YES_OPTION) {
			System.out.println("Receive");
			remoteLog.setText(nick);
			remoteAddr.setText(remoteAddress);
			return true; // Receive
		}
		System.out.println("Rejected");
		return false; // Reject

	}

	@SuppressWarnings("deprecation")
	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof CallListener) {
			CallListener c = (CallListener) arg;
			callListenerThread.suspend();
			callListenerThread.setReceive(question(c.getRemoteNick(), c.getRemoteAddress()));
			callListenerThread.resume();
		} else if (arg instanceof Connection) {
			connection = (Connection) arg;
			System.out.println("Output connection created");
		} else {
			System.out.println("Receive message");
			System.out.println(arg.toString());
			Command command = (Command) arg;
			System.out.println(command.toString());

			if (command instanceof MessageCommand) {
				dlm.addElement(
						"<html>" + remoteLog.getText() + " " + new Date(System.currentTimeMillis()).toLocaleString()
								+ ":<br>" + arg.toString() + " </span></html>");
				list.setModel(dlm);
			} else if (command.toString().toLowerCase().equals("disconnect")) {
				remoteLog.setText("");
				remoteAddr.setText("");
				if (callListenerThread != null)
					callListenerThread.setBusy(false);
			}
		}
	}
}