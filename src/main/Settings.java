package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextArea;

public class Settings extends JFrame {

	private JPanel contentPane;
	private JTextField usrname;
	private JPasswordField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Settings frame = new Settings();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Settings() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		usrname = new JTextField();
		usrname.setBounds(35, 55, 293, 19);
		contentPane.add(usrname);
		usrname.setColumns(10);
		
		password = new JPasswordField();
		password.setBounds(35, 113, 293, 19);
		contentPane.add(password);
		
		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				try {
					save();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnSave.setBounds(35, 154, 117, 25);
		contentPane.add(btnSave);
		
		JTextArea txtrPassword = new JTextArea();
		txtrPassword.setText("Password:");
		txtrPassword.setBounds(35, 86, 150, 15);
		txtrPassword.setEditable(false);
		txtrPassword.setOpaque(true);
		txtrPassword.setBackground(null);
		contentPane.add(txtrPassword);
		
		JTextArea txtrUsername = new JTextArea();
		txtrUsername.setText("Username:");
		txtrUsername.setBounds(35, 28, 108, 15);
		txtrUsername.setEditable(false);
		txtrUsername.setOpaque(true);
		txtrUsername.setBackground(null);
		contentPane.add(txtrUsername);
		try {
			read();
		} catch (Exception e) {
		
		}
		
	}
	
	private void read() throws Exception{
       ArrayList<String> lines = readsettings();
		usrname.setText(lines.get(0));
		password.setText(lines.get(1));
	}
	
	public static ArrayList<String> readsettings() {
		
		BufferedReader i = null;
		try {
			i = new BufferedReader(new FileReader("/opt/opennord/client.conf"));
		} catch (FileNotFoundException e1) {
			return null;
		}
		  String line;
		  ArrayList<String> lines = new ArrayList<>();
		    try {
				while ((line = i.readLine()) != null) {
				    lines.add(line);
				}
			} catch (IOException e) {
			}
		    
		    return lines;
	}
	
	private void save() throws Exception{
		FileOutputStream out = new FileOutputStream("/opt/opennord/client.conf");
		String u = usrname.getText();
		String p = String.valueOf(password.getPassword());
		
		out.write(u.getBytes());
		out.write("\n".getBytes());
		out.write(p.getBytes());
		out.close();
		JOptionPane.showMessageDialog(null, "Settings saved", "Saved", JOptionPane.INFORMATION_MESSAGE);
		this.dispose();
		
	}
}
