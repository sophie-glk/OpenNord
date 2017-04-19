package main;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.JTextArea;


public class Client {

	private JFrame frame;
	private JList<String> list;
	private DefaultListModel<String> model;
	public Process p;
	public static int isroot = 0 ;
	private List<String> results;
	private JTextField textField;
	private JTextArea ip_field;
	private JTextArea ip_description;
	private JTextArea Country_field; 
	private JTextPane console;
	private JButton btnSettings;
	private JButton btnServerlist;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client window = new Client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Client() {
		if(check_for_root()){
			isroot = 1;
		}
	 else{
			JOptionPane.showMessageDialog(null, "Please run as root", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
	    ArrayList<String> a = 	Settings.readsettings();
	    if( a == null || a.isEmpty() || a.size() < 2){
	    	JOptionPane.showMessageDialog(null, "No password and/or username set.\nYou can change that in the Settings.", "Info", JOptionPane.INFORMATION_MESSAGE);
	    }
		initialize();
		setup();
		 redirectSystemStreams();
		 checkip();
		
	}

	private void redirectSystemStreams() {
		  OutputStream out = new OutputStream() {
		    @Override
		    public void write(final int b) throws IOException {
		      updateTextPane(String.valueOf((char) b));
		    }
		 
		    @Override
		    public void write(byte[] b, int off, int len) throws IOException {
		      updateTextPane(new String(b, off, len));
		    }
		 
		    @Override
		    public void write(byte[] b) throws IOException {
		      write(b, 0, b.length);
		    }
		  };
		 
		  System.setOut(new PrintStream(out, true));
		  System.setErr(new PrintStream(out, true));
		}
	
	private void updateTextPane(final String text) {
		  SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		      Document doc = console.getDocument();
		      try {
		        doc.insertString(doc.getLength(), text, null);
		      } catch (BadLocationException e) {
		        throw new RuntimeException(e);
		      }
		      console.setCaretPosition(doc.getLength() - 1);
		    }
		  });
		}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 557, 470);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("OpenNord alpha");
		 model = new DefaultListModel<>();
		JPanel panel = new JPanel();
	
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 556, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(1, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
		);
		
		 Country_field = new JTextArea();
		 Country_field.setBounds(369, 121, 92, 15);
		 Country_field.setEditable(false);
		 Country_field.setOpaque(true);
		 Country_field.setBackground(null);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setBounds(12, 12, 112, 25);
		
			
			JButton btnConnect = new JButton("Connect");
			btnConnect.setBounds(262, 12, 92, 25);
			
			textField = new JTextField();
			textField.setBounds(142, 15, 114, 19);
			textField.setToolTipText("Search");
			
			textField.getDocument().addDocumentListener(new DocumentListener(){
	            @Override public void insertUpdate(DocumentEvent e) { filter(); }
	            @Override public void removeUpdate(DocumentEvent e) { filter(); }
	            @Override public void changedUpdate(DocumentEvent e) {}
					private void filter() {
	                String filter = textField.getText();
	                filterModel((DefaultListModel<String>)list.getModel(), filter);
	           
	            }
	        });
			textField.setColumns(10);
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(12, 43, 345, 241);
			ip_description = new JTextArea();
			ip_description.setBounds(369, 45, 110, 15);
			ip_description.setText("Public IP:");
			ip_description.setBackground(null);
			ip_description.setOpaque(true);
			ip_description.setEditable(false);
			ip_description.setColumns(10);
			
			ip_field  = new JTextArea();
			ip_field.setBounds(369, 60, 145, 19);
			ip_field.setEditable(false);
			ip_field.setOpaque(true);
			ip_field.setBackground(null);
			
		
			list = new JList<String>( model );
			scrollPane.setViewportView(list);
			list.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					if(event.getClickCount() == 2 && event.getComponent() != null){
				     try {
						connector.connect(event);
					} catch (Exception e) {
						e.printStackTrace();
					}
						
					}
				}
			});
			panel.setLayout(null);
			panel.add(btnDisconnect);
			panel.add(textField);
			panel.add(btnConnect);
			panel.add(scrollPane);
			panel.add(ip_field);
			panel.add(Country_field);
			panel.add(ip_description);
			console = new JTextPane();
			JScrollPane jsp = new JScrollPane( console );
			jsp.setBounds(12, 293, 523, 132);
			panel.add(jsp);
			
			
			console.setEditable(false);
			
		   JTextArea Country_description = new JTextArea();
		   Country_description.setBounds(369, 107, 100, 19);
			Country_description.setOpaque(true);
			Country_description.setBackground(null);
			Country_description.setEditable(false);
			Country_description.setText("Country:");
			panel.add(Country_description);
			
			btnSettings = new JButton("Settings");
			btnSettings.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					new Settings().setVisible(true);
				}
			});
			btnSettings.setBounds(368, 255, 117, 25);
			panel.add(btnSettings);
			
			btnServerlist = new JButton("Serverlist");
			btnServerlist.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					 try {
					        Desktop.getDesktop().browse(new URL("https://nordvpn.com/servers/").toURI());
					    } catch (Exception e) {
					        e.printStackTrace();
					    }
				}
			});
			btnServerlist.setBounds(369, 187, 117, 25);
			panel.add(btnServerlist);
			btnConnect.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent event) {
					try {
				
						connector.connect(list);
					} catch (Exception e) {
						System.out.println("nothing selected");
					}
					
					
				}
			});
		btnDisconnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				disconnect();
				
			}
		});
		frame.getContentPane().setLayout(groupLayout);
		

	}
	
	private  boolean check_for_root(){
	     Process r;
		try {
			r = Runtime.getRuntime().exec("whoami");
			InputStream in = r.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String result = br.readLine();
			if(result.equals("root")){
				return true;
			}
			else{
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
				
		
	}
	
	

  private void setup(){
		results = new ArrayList<String>();

		File[] files = new File("/opt/opennord").listFiles();

		for (File file : files) {
		    if (file.isFile() && file.getName().contains(".ovpn")) {
		        results.add(file.getName());
		    }
		Collections.sort(results);
	}
		
		for(String s : results){

			model.addElement(s);
		    
		}
		checkip();
		
	}
	
	private void checkip(){
		int delay = 0;   
		int period = 10000;  
		Timer timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {
		        public void run() {
		        	String ip = "not found";
		        	String country = "not found";
		    		
		    		try {
		    			ip = get_public_ip.getIp();
		    			country = get_public_ip.getCountry();
		    		
		    		} catch (Exception e) {
		    			System.out.println("Data not found. Do you have an Internet-connection?");
		    		}
		    		
		    		ip_field.setText(ip);
		    		Country_field.setText(country);
		    			
		        }
    
		    }, delay, period);
	}
	

	
	private void disconnect(){
		 try {
			Runtime.getRuntime().exec("gksudo killall openvpn");
			System.out.println("Trying to kill openvpn...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void filterModel(DefaultListModel<String> model, String filter) {
		    ArrayList<String> save = new ArrayList<String>();
		        model.clear();
		         
	        for (String s : results) {
	            if (!s.startsWith(filter)) {
	                if (model.contains(s)) {
	                    model.removeElement(s);
	                }
	            } else {
	                if (!model.contains(s)) {
	                
	                    save.add(s);
	                }
	            }
	        }
	    	Collections.sort(save);
	    		
	    		for(String s : save){
	    			model.addElement(s);
	    		    
	    		}
	       
	    
		       
		         }
}

