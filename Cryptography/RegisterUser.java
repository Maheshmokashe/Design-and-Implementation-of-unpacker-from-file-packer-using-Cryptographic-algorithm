import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class RegisterUser extends GUITemplate implements ActionListener, KeyListener//,Runnable 
{
	JButton create, previous;
	JLabel topLabel, status, name, pwd, confirmPwd, warning;
	JTextField nameField;
	JPasswordField pwdField, confirmpwdField;
	String userValue, pwdValue, confirmpwdValue;
	JCheckBox show1, show2;

	public RegisterUser() throws Exception 
	{
		FilePackerUnpacker.log.info("User Entered into Register window");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		//Thread t = new Thread(this);
		topLabel = new JLabel("Register New User");
		topLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topLabel.setBounds(185, 23, 320, 30);
		topLabel.setFont(new Font("Courier New", Font.BOLD, 20));
		topLabel.setAlignmentX(JLabel.CENTER);
		topLabel.setForeground(Color.BLUE);
		header.add(topLabel);

		name = new JLabel("*New Username:");
		name.setHorizontalAlignment(SwingConstants.CENTER);
		name.setBounds(40, 45, 260, 30);
		name.setBackground(new Color(0, 50, 120));
		name.setForeground(Color.WHITE);
		name.setFont(new Font("Courier New", Font.BOLD, 17));
		name.setAlignmentX(JLabel.LEFT);
		name.requestFocusInWindow();
		content.add(name);

		pwd = new JLabel("*Enter new password:");
		pwd.setHorizontalAlignment(SwingConstants.CENTER);
		pwd.setForeground(Color.WHITE);
		pwd.setFont(new Font("Courier New", Font.BOLD, 17));
		pwd.setBounds(40, 100, 260, 30);
		pwd.addKeyListener(this);
		content.add(pwd);

		confirmPwd = new JLabel("*Confirm new password:");
		confirmPwd.setHorizontalAlignment(SwingConstants.CENTER);
		confirmPwd.setForeground(Color.WHITE);
		confirmPwd.setFont(new Font("Courier New", Font.BOLD, 17));
		confirmPwd.setBounds(40, 155, 260, 30);
		confirmPwd.addKeyListener(this);
		content.add(confirmPwd);

		nameField = new JTextField();
		nameField.setBounds(300, 45, 260, 25);
		content.add(nameField);
		nameField.setColumns(10);
		nameField.addKeyListener(this);
		nameField.setToolTipText("Enter a new username");

		pwdField = new JPasswordField();
		pwdField.setBounds(300, 100, 260, 25);
		content.add(pwdField);
		pwdField.setEchoChar((char) 0);
		pwdField.addKeyListener(this);
		pwdField.setToolTipText("New user password, it must of length >= 8 characters ");

		show1 = new JCheckBox("Show", true);
		show1.setBounds(580, 100, 65, 25);
		show1.setToolTipText("See password");
		content.add(show1);

		confirmpwdField = new JPasswordField();
		confirmpwdField.setBounds(300, 155, 260, 25);
		content.add(confirmpwdField);
		confirmpwdField.setEchoChar((char) 0);
		confirmpwdField.setToolTipText("Confirm Entered password");

		warning = new JLabel();
		warning.setBounds(300, 190, 300, 30);
		warning.setHorizontalAlignment(SwingConstants.LEFT);
		warning.setForeground(Color.RED);
		warning.setFont(new Font("Courier New", Font.BOLD, 17));
		content.add(warning);
		//t.start();

		show2 = new JCheckBox("Show", true);
		show2.setBounds(580, 155, 65, 25);
		show2.setToolTipText("See password");
		content.add(show2);

		create = new JButton("Create");
		create.setFont(new Font("Courier New", Font.BOLD, 17));
		create.setBounds(125, 235, 145, 25);
		content.add(create);

		previous = new JButton("Previous");
		previous.setFont(new Font("Courier New", Font.BOLD, 17));
		previous.setBounds(400, 235, 145, 25);
		content.add(previous);

		this.setBounds(fSize.width / 4, fSize.height / 5, 700, 450);
		this.setResizable(false);

		create.addActionListener(this);
		previous.addActionListener(this);
		show1.addActionListener(this);
		show2.addActionListener(this);
		FilePackerUnpacker.log.info("User exiting from Register Window");
	}

	public boolean isValid(String userValue, String pwdValue, String confirmpwdValue) {
		if (userValue.length() < 8 || pwdValue.length() < 8 || confirmpwdValue.length() < 8) {
			JOptionPane.showMessageDialog(this, "Short Username or Password", "File Packer-Unpacker",
					JOptionPane.ERROR_MESSAGE);
			return false;
		} else if (pwdValue.equals(confirmpwdValue) == false) {
			JOptionPane.showMessageDialog(null, "Password didn\'t confirmed, try again", "File Packer-Unpacker",
					JOptionPane.INFORMATION_MESSAGE);
			confirmpwdField.setText("");
			return false;
		} else {
			return true;
		}
	}

	public void actionPerformed(ActionEvent ae) {
		userValue = nameField.getText();
		pwdValue = new String(pwdField.getPassword());
		confirmpwdValue = new String(confirmpwdField.getPassword());

		if (show1.getModel().isSelected()) {
			pwdField.setEchoChar((char) 0);
		} else {
			pwdField.setEchoChar('*');
		}
		if (show2.getModel().isSelected()) {
			confirmpwdField.setEchoChar((char) 0);
		} else {
			confirmpwdField.setEchoChar('*');
		}

		if (ae.getSource() == exit) {
			final Frame[] frames = Frame.getFrames();
			if (frames != null) {
				for (final Frame f : frames) {
					f.dispose();
				}
			}
			System.exit(0);
		}
		if (ae.getSource() == minimize) {
			setState(JFrame.ICONIFIED);
		}
		if (ae.getSource() == create) {
			createTask();
		}

		if (ae.getSource() == previous) {
			try {
				this.dispose();
				FilePackerUnpackerLogin nextPage = new FilePackerUnpackerLogin();
				nextPage.setVisible(true);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						nextPage.unameField.requestFocus();
					}
				});
			} catch (Exception e) {
				FilePackerUnpacker.log.info("Exception in RegisterUser");
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}

	public void createTask() {
		if (nameField.getText().isEmpty() || pwdField.getPassword().toString().isEmpty()
				|| confirmpwdField.getPassword().toString().isEmpty()) {
			JOptionPane.showMessageDialog(this, "All Fields are Mandatory", "File Packer-Unpacker",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (isValid(userValue, pwdValue, confirmpwdValue) == false) {
			nameField.setText("");
			pwdField.setText("");
			confirmpwdField.setText("");
		} else {
			serializeFile = new File("credentials");
			if (serializeFile.exists() && serializeFile.isFile()) {
				try {
					serializeFis = new FileInputStream("credentials");
					mapInput = new ObjectInputStream(serializeFis);
					creds = (HashMap<String, String>) mapInput.readObject();
					serializeFis.close();
					mapInput.close();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

			Set<String> keys = creds.keySet();
			if (keys.contains(userValue)) {
				JOptionPane.showMessageDialog(null, "Sorry!! username already exists, try different username",
						"File Packer-Unpacker", JOptionPane.INFORMATION_MESSAGE);
				nameField.setText("");
				pwdField.setText("");
				confirmpwdField.setText("");
			} else {
				creds.put(userValue, pwdValue);
				JOptionPane.showMessageDialog(null, "New User Registered Successfully!", "File Packer-Unpacker",
						JOptionPane.INFORMATION_MESSAGE);
				nameField.setText("");
				pwdField.setText("");
				confirmpwdField.setText("");
				this.setVisible(false);
				try {
					deserializeFos = new FileOutputStream("credentials");
					mapOutput = new ObjectOutputStream(deserializeFos);
					mapOutput.writeObject(creds);
					deserializeFos.close();
					mapOutput.close();
					FilePackerUnpackerLogin nextPage = new FilePackerUnpackerLogin();
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							nextPage.unameField.requestFocus();
						}
					});
				} catch (Exception e) {
					FilePackerUnpacker.log.info("Exception in RegisterUser");
					JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public void keyPressed(KeyEvent ke) {
		String keyName = KeyEvent.getKeyText(ke.getKeyCode());
		if (keyName.equals("Enter")) {
			createTask();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	/*
	public void run() {
		for (;;) {
			if (nameField.isFocusOwner() || pwdField.isFocusOwner() || confirmpwdField.isFocusOwner()) {
				if (Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK)) {
					warning.setText("Warning : CAPS LOCK is on");
					
				} else {
					warning.setText("");
				}
			} else {
				warning.setText("");
			}
		}
	}*/
}
