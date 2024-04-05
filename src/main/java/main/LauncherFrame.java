package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.DefaultButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import fr.theshark34.openlauncherlib.util.Saver;
import fr.theshark34.openlauncherlib.util.ramselector.RamSelector;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame{
	
	public static LauncherFrame instance;
	
	public String pseudo;
	public String password;
	public String email;
	
	public String step;
	
	
	private static String folderName = LauncherMain.folderName;
	public String workingDirectory;
	String OS = (System.getProperty("os.name")).toUpperCase();
	// IMPORTANT !!!! POUR LE DEV RETIRER /ressources/ NAN C BON C FIX
	public URL background = LauncherFrame.class.getResource("/pics/background.png");
	public URL icon = LauncherFrame.class.getResource("/pics/icon.png");
	
	private Point mouseCoDown;
	
	public String cheminRamSave;
	
    public JTextField downloadInfo = new JTextField();

	@SuppressWarnings("deprecation")
	RamSelector ramSelector = new RamSelector(new File(getWorkingDir() + folderName + "/ram.txt"));

	public String getWorkingDir() {
		 if (OS.contains("WIN"))
		    {
		        this.workingDirectory = System.getenv("AppData");
		    }
		    else
		    {
		    	this.workingDirectory = System.getProperty("user.home");
		    	this.workingDirectory += "/Library/Application Support";
		    }
		 return this.workingDirectory;
		 }
	
	public void MainWindows() {
		if(background == null || icon == null) {
			System.out.println("background null, on change le chemin");
			background = LauncherFrame.class.getResource("/resources/pics/background.png");
			icon = LauncherFrame.class.getResource("/resources/pics/icon.png");
		}
		System.out.println("Si on a 'true' il y a une erreur avec l'icon");
		System.out.println(icon == null);
		System.out.println("background: " + background);
		getWorkingDir();

		 
	       JFrame frame = new JFrame("Launcher");
	       
	       frame.setSize(new Dimension(761,870));
	       frame.setUndecorated(true);
	       frame.setResizable(true);
	       frame.setLocationRelativeTo(null);
	       
	       
	       frame.setIconImage((new ImageIcon(icon).getImage()));   
	       ImageIcon icone = new ImageIcon(background);
	       JLabel background = new JLabel(icone, JLabel.CENTER);
	       frame.setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
	       frame.getContentPane().setBackground(new Color(0.0f,0.0f,0.0f,0.0f));
	       
	       //CloseButton
	       JButton closeButton = new JButton("");
	       closeButton.setContentAreaFilled(false);
	       closeButton.setBounds(309+46, 142, 29, 32);
	       closeButton.setModel(new FixedStateButtonModel());
	       closeButton.setBorderPainted(false);
	       frame.getContentPane().add(closeButton);
	       closeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
					
				}
			});
	       
	       //minButton
	       JButton minButton = new JButton("");
	       minButton.setContentAreaFilled(false);
	       minButton.setBounds(309+46, 171, 29, 29);
	       minButton.setModel(new FixedStateButtonModel());
	       minButton.setBorderPainted(false);
	       frame.getContentPane().add(minButton);
	       minButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setState(JFrame.ICONIFIED);
				
			}
	       });
	       
	       
	     //ramButton
	       JButton ramButton = new JButton("");
	       ramButton.setContentAreaFilled(false);
	       ramButton.setBounds(309+46, 200, 29, 29);
	       ramButton.setModel(new FixedStateButtonModel());
	       ramButton.setBorderPainted(false);
	       frame.getContentPane().add(ramButton);
	       ramButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ramSelector.display();
			
			}
	       });
	       
	       
	      
	       
	       Font font1 = new Font("Arial", Font.BOLD, 20);
	       Font font2 = new Font("Arial", Font.BOLD, 15);

	       JTextField usernameField = new JTextField();
	       usernameField.setToolTipText("Email ou pseudo");
	       usernameField.setOpaque(false);
	       usernameField.setBorder(null);
	       usernameField.setFont(font1);
	       usernameField.setForeground(Color.WHITE);
	       frame.getContentPane().add(usernameField);
	       usernameField.setBounds(215+46, 558-10, 319, 57);
	       usernameField.setSize(319, 57);


	       JPasswordField passwordField = new JPasswordField();
	       passwordField.setToolTipText("Mot de passe");
	       passwordField.setOpaque(false);
	       passwordField.setBorder(null);
	       passwordField.setFont(font1);
	       passwordField.setForeground(Color.WHITE);
	       frame.getContentPane().add(passwordField);
	       passwordField.setBounds(215+46, 683-10, 317, 74);
	       passwordField.setSize(317, 74);


	       if(!new File(workingDirectory + folderName).exists())
	        {
	    	   new File(workingDirectory + folderName).mkdirs();
	    	   System.out.println("DOSSIER CREER");
	    	   
	        }
	        

				
			 @SuppressWarnings("deprecation")
			Saver saver = new Saver(new File(workingDirectory + folderName + "launcher.properties"));
			 
		       try {
		    	   passwordField.setText(saver.get("password"));
		       }catch(Exception errorSaverPass) {
		    	   System.out.println("Erreur saver password: " + errorSaverPass);
		       }
		       
		       try {
		    	   usernameField.setText(saver.get("username"));
		       }catch(Exception errorSaverUsername) {
		    	   System.out.println("Erreur saver username: " + errorSaverUsername);
		    	   
		       }
		       
		       

	       move(frame);
	       
	       
	       
	       downloadInfo.setText("En attente");
	       downloadInfo.setForeground(Color.MAGENTA);
	       downloadInfo.setEditable(false);
	       downloadInfo.setOpaque(false);
	       downloadInfo.setBorder(null);
	       downloadInfo.setFont(font2);
	       frame.getContentPane().add(downloadInfo);
	       downloadInfo.setBounds(553+46, 380, 153, 310);
	       downloadInfo.setSize(153, 310);
	       
	       
	       
	       
	       //PlayButton
	       JButton playButton = new JButton("");
	       playButton.setContentAreaFilled(false);
	       playButton.setBounds(222+46, 812, 488, 862);
	       playButton.setModel(new FixedStateButtonModel());
	       playButton.setBorderPainted(false);
	       frame.getContentPane().add(playButton);
	       playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("PLAY");
				Thread playThread = new Thread(() -> {
				    PlayEvent(frame, usernameField, passwordField, ramButton, playButton, saver);
				    
				});
				playThread.start();			
			}
	       });
	       
	        
		       frame.add(background);
		       frame.validate();
		       frame.setVisible(true);
	}

	public void PlayEvent(JFrame frame, JTextField usernameField, JPasswordField passwordField, JButton ramButton, JButton playButton, Saver saver) {
		
		//PLAY EVENT
		usernameField.setEnabled(false);
		passwordField.setEnabled(false);
		ramButton.setEnabled(false);
		playButton.setEnabled(false);
		ramSelector.save();

							    
	    
		setEmail(usernameField.getText());
		setPassword(String.valueOf(passwordField.getPassword()));
		
		saver.set("username", getEmail());
		saver.set("password", getPassword());
		
		//ON MET LE LAUNCHER A JOUR
		try {
			LauncherUpdate.updateMinecraftVanilla();
			System.out.println("MAJ FINI -------------");
			}catch(Exception errorMaj) {
				System.out.println("erreur en le mettant à jour: " + errorMaj);
				JOptionPane.showMessageDialog(frame, "Erreur en mettant le jeu à jour" + errorMaj);
				usernameField.setEnabled(true);
				passwordField.setEnabled(true);
				ramButton.setEnabled(true);
				playButton.setEnabled(true);
			}
		

		
		try {
			if(getPassword().length() > 0 ) {
			//ON RECUPERE LE TOKEN
			System.out.println("CONNECTION PREMIUM");
			ArrayList<String> InfoMojang = LauncherMain.getTokenMojang(getEmail(), getPassword());
			if(InfoMojang.size() < 3) {
				System.out.println("PROBLEME d'autentification");
				System.out.println(InfoMojang.get(0));
				
				JOptionPane.showMessageDialog(frame, "Mauvais identifiants / Mot de passe: " + InfoMojang.get(0));
				usernameField.setEnabled(true);
				passwordField.setEnabled(true);
				ramButton.setEnabled(true);
				playButton.setEnabled(true);
				
			}
			else {
				String id = InfoMojang.get(0);
				String pseudo = InfoMojang.get(1);
				String token = InfoMojang.get(2);
				
				
				//ON LANCE LE JEU
				LauncherMain.LaunchMinecraft(pseudo, token, id);
				Thread.sleep(2000);
				System.exit(0); 
				}
			
			
			}else {
				
				System.out.println("CRACK");
				LauncherMain.LaunchMinecraft(getEmail(), "sorry", "nope");
				Thread.sleep(2000);
				System.exit(0);

			}
		} catch (Exception errorLaunch) {
			errorLaunch.printStackTrace();
			System.out.println("erreur  en lançant minecraft: " + errorLaunch);
			JOptionPane.showMessageDialog(frame, "Erreur en lançant minecraft (essayer de supprimer le .launcher) " + errorLaunch);
			usernameField.setEnabled(true);
			passwordField.setEnabled(true);
			ramButton.setEnabled(true);
			playButton.setEnabled(true);

		}
		
	} 
	
	public void move(JFrame frame) {
	       frame.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				setMouseCoDown(new Point(0, 0));					
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				setMouseCoDown(new Point(e.getX(), e.getY()));	
			}

			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
	       });
	       
	       
	       
        frame.addMouseMotionListener(new MouseMotionAdapter() {
     	   public void mouseDragged(MouseEvent e)
     	   {
     	     frame.setLocation(e.getXOnScreen() - getMouseCoDown().x, e.getYOnScreen() - getMouseCoDown().y);
     	   }
     	   });

	}
	
	public class FixedStateButtonModel extends DefaultButtonModel    {

        @Override
        public boolean isPressed() {
            return false;
        }

        @Override
        public boolean isRollover() {
            return false;
        }

        @Override
        public void setRollover(boolean b) {
            //NOOP
        }

    }

	
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	public RamSelector getRamSelector() {
		return ramSelector;
	}
	
	public static LauncherFrame getInstance() {
		return instance;
	}
	
	private Point getMouseCoDown() {
		return mouseCoDown;
	}
	public String getDownloadInfo() {
		return downloadInfo.getText();
	}
	
	//TEMP
	public URL getIcon() {
		return icon;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	private void setMouseCoDown(Point mouseCoDown) {
		this.mouseCoDown =  mouseCoDown;
	}
	public void setInstance(LauncherFrame instance) {
		LauncherFrame.instance = instance;
	}
	public void setDownloadInfo(String text) {
		downloadInfo.setText(text);
	}

}
