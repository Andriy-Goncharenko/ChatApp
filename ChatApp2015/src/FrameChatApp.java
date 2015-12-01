import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;

import java.awt.Label;

import javax.swing.JTextField;
import javax.swing.JTextPane;

import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BoxLayout;

import java.awt.CardLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.List;
import java.awt.ScrollPane;
import javax.swing.SpringLayout;

class FrameChatApp {
	private static final int DEFAULT_WIDTH = 500;
	private static final int DEFAULT_HEIGHT = 300;
	static String Title = "ChatApp";
	static String name;
	static String sms;
	static String IP = "127.0.0.1";
	static JFrame frame = new JFrame();
	static JMenuBar menuBar = new JMenuBar();
	static JMenu m1 = new JMenu("Свойства");
	static JButton Sent = new JButton("Отправить");
	static JMenuItem M_name = new JMenuItem("Введите свое Имя");
	static JMenuItem M_ip = new JMenuItem("Введите IP");
	static JMenuItem M_Conect = new JMenuItem("Присоединется");
	static JMenuItem M_Disconect = new JMenuItem("Отсоевинится");
	static JMenuItem M_Exit = new JMenuItem("Выход из програми");
	static JMenuItem My_name = new JMenuItem("Ваше имя:");
	static JMenuItem you_ip = new JMenuItem("IP:");
	static JMenuItem you_nema = new JMenuItem("Собиседник:");
	static String nik;
	static JTextField text = new JTextField();
	static byte[] sendData = new byte[256];
	static byte[] receiveData = new byte[256];
	static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	static JTextArea textPane = new JTextArea();
	private static Conect conect;

	public static void main(String[] args) throws Exception {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {

					frame();

				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	private static void frame() throws Exception {
		
		conect = new Conect();
		frame.getContentPane().setBackground(new Color(255, 218, 185));
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 14));
		textPane.setText(name);

		text.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				sms = text.getText();
				switch (arg0.getKeyCode()) {
				case KeyEvent.VK_ENTER:
					Date currentDate = new Date();
					text.setText("");
					textPane.append(name + ":" + sms+"("+sdf.format(currentDate)+")"+"\n");
					try {
						Conect.sms(sms,IP);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				}
			}
		});
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, text, 206, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, text, 375, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, Sent, -31, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, Sent, -99, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, Sent, -8, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, Sent, -10, SpringLayout.EAST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, textPane, 11, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, textPane, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, textPane, 195, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, textPane, 474, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, text, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, text, 229, SpringLayout.NORTH, frame.getContentPane());
		frame.getContentPane().setLayout(springLayout);
		text.setEnabled(false);
		frame.getContentPane().add(text);
		text.setColumns(10);

		Sent.setEnabled(false);
		frame.getContentPane().add(Sent);
		
		textPane.setEditable(false);
		textPane.setBackground(new Color(255, 255, 255));
		frame.getContentPane().add(textPane);
		frame.setTitle(Title);
		frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		menuBar.add(m1);

		Sent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == Sent) {
					sms = text.getText();
					Date currentDate = new Date();
					text.setText("");
					textPane.append(name + ":" + sms+"("+sdf.format(currentDate)+")"+"\n");
					
				}
			}
		});
		M_name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == M_name) {
					name = JOptionPane.showInputDialog("Введите ваше имя");
					My_name.setText("Ваше имя: " + name);
				
				}
			}
		});
		M_ip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == M_ip) {
					IP = JOptionPane.showInputDialog("Введите ваше IP собиседника");
					you_ip.setText("IP: " + IP);
				}
			}
		});
		M_Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == M_Exit) {
					System.exit(0);
				}
			}
		});
		M_Conect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == M_Conect) {
					 try {
						Conect.name(name,IP);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					 you_nema.setText("Собиседник:"+nik);
					Sent.setEnabled(true);
					text.setEnabled(true);
					
				}
			}
		});
		M_Disconect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == M_Disconect) {
					Sent.setEnabled(false);
					text.setEnabled(false);
				}
			}
		});
		m1.add(M_name);
		m1.add(M_ip);
		M_Conect.setBackground(Color.GREEN);
		m1.add(M_Conect);
		M_Disconect.setBackground(Color.RED);
		m1.add(M_Disconect);
		m1.add(M_Exit);
		menuBar.add(My_name);

		menuBar.add(you_ip);

		menuBar.add(you_nema);
		frame.setVisible(true);
	}
}