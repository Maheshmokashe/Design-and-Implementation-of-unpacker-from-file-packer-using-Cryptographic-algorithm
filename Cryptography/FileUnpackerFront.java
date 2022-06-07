import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

class InvalidFileException extends RuntimeException {
	public InvalidFileException(String str) {
		super(str);
	}
}

class BadEncryptionFile extends RuntimeException {
	public BadEncryptionFile(String str) {
		super(str);
	}
}

class FileUnpackerFront extends GUITemplate implements ActionListener, KeyListener 
{
	JButton extract, previous, extractTo, browse;
	JLabel topLabel, sourceFile;
	static JTextField sourceFileField;
	String username, destPath = "";
	JFileChooser fileChooser1, fileChooser2;

	public FileUnpackerFront(String username) 
	{
		FilePackerUnpacker.log.info("To the window of File Unpacker Front");
		this.username = username;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		topLabel = new JLabel("File Unpacker Portal");
		topLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topLabel.setBounds(185, 23, 320, 30);
		topLabel.setFont(new Font("Courier New", Font.BOLD, 20));
		topLabel.setAlignmentX(JLabel.CENTER);
		topLabel.setForeground(Color.BLUE);
		header.add(topLabel);

		sourceFile = new JLabel("Packed File Name:");
		sourceFile.setHorizontalAlignment(SwingConstants.CENTER);
		sourceFile.setBounds(50, 57, 258, 30);
		sourceFile.setBackground(new Color(0, 50, 120));
		sourceFile.setForeground(Color.WHITE);
		sourceFile.setFont(new Font("Courier New", Font.BOLD, 17));
		sourceFile.setAlignmentX(JLabel.LEFT);
		sourceFile.requestFocusInWindow();
		content.add(sourceFile);

		sourceFileField = new JTextField();
		sourceFileField.setBounds(325, 60, 285, 25);
		content.add(sourceFileField);
		sourceFileField.setColumns(30);
		sourceFileField.addKeyListener(this);
		sourceFileField.setToolTipText("Name of file to be unpacked");

		browse = new JButton("Browse");
		browse.setFont(new Font("Courier New", Font.BOLD, 17));
		browse.setBounds(325, 100, 120, 25);
		browse.addActionListener(this);
		content.add(browse);

		extract = new JButton("Extract Here");
		extract.setFont(new Font("Courier New", Font.BOLD, 17));
		extract.setBounds(75, 200, 170, 25);
		extract.addActionListener(this);
		content.add(extract);

		extractTo = new JButton("Extract To");
		extractTo.setFont(new Font("Courier New", Font.BOLD, 17));
		extractTo.setBounds(280, 200, 150, 25);
		extractTo.addActionListener(this);
		content.add(extractTo);

		previous = new JButton("Previous");
		previous.setFont(new Font("Courier New", Font.BOLD, 17));
		previous.setBounds(465, 200, 135, 25);
		previous.addActionListener(this);
		content.add(previous);

		this.setBounds(fSize.width / 4, fSize.height / 5, 700, 450);
		this.setResizable(false);
		FilePackerUnpacker.log.info("Moving from window of File Unpacker Front");
	}

	public void extractTask() 
	{
		if (sourceFileField.getText().isEmpty()) 
		{
			String s = new String("Please enter name of packed file");
			JOptionPane.showMessageDialog(this, s, "File Packer-Unpacker", JOptionPane.INFORMATION_MESSAGE);
		} 
		else 
		{
			try 
			{
				FileUnpacker unpacker = new FileUnpacker(sourceFileField.getText(), destPath);
				if (unpacker.isFileThere == false) 
				{
					JOptionPane.showMessageDialog(this, "Packed File Does not exists", "Error",
							JOptionPane.ERROR_MESSAGE);
					sourceFileField.setText("");
					this.setVisible(true);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							FileUnpackerFront.sourceFileField.requestFocus();
						}
					});
				} else if (unpacker.cannotCreateDir == true) {
					JOptionPane.showMessageDialog(this,
							"Cannot Create Directory in specified path, select different directory", "Error",
							JOptionPane.ERROR_MESSAGE);
					sourceFileField.setText("");
					this.setVisible(true);
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							FileUnpackerFront.sourceFileField.requestFocus();
						}
					});
				} else {
					FilePackerUnpackerFront nextPage = new FilePackerUnpackerFront(username);
					this.setVisible(false);
					nextPage.setVisible(true);
				}
			} catch (InvalidFileException ie) {
				FilePackerUnpacker.log.info("Exception occur in FileUnpackerFront:Invalid Packed File");
				JOptionPane.showMessageDialog(this, "Invalid Packed File", "Error", JOptionPane.ERROR_MESSAGE);
				sourceFileField.setText("");
			} catch (BadEncryptionFile ie) {
				FilePackerUnpacker.log.info("Exception occur in FileUnpckerFront:Invalid Encrypted File");
				JOptionPane.showMessageDialog(this, "Invalid Encrypted File", "Error", JOptionPane.ERROR_MESSAGE);
				sourceFileField.setText("");
			} catch (Exception e) {
				FilePackerUnpacker.log.info("Exception occur in FileUnpackerFront");
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				sourceFileField.setText("");
			}
		}
	}

	public void actionPerformed(ActionEvent ae) {
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
		if (ae.getSource() == extract) {
			destPath = ".";
			extractTask();
		}
		if (ae.getSource() == extractTo) {
			if (sourceFileField.getText().isEmpty()) {
				String s = new String("Please enter name of packed file");
				JOptionPane.showMessageDialog(this, s, "File Packer-Unpacker", JOptionPane.INFORMATION_MESSAGE);
			} else {
				fileChooser2 = new JFileChooser();
				fileChooser2.setCurrentDirectory(new java.io.File("."));
				fileChooser2.setDialogTitle("Extract To");
				fileChooser2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser2.setMultiSelectionEnabled(false);
				fileChooser2.setAcceptAllFileFilterUsed(false);
				if (fileChooser2.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
					destPath = fileChooser2.getSelectedFile().toString();
				}
				extractTask();
			}
		}
		if (ae.getSource() == browse) {
			fileChooser1 = new JFileChooser();
			fileChooser1.setCurrentDirectory(new java.io.File("."));
			fileChooser1.setDialogTitle("Select File to Unpack");
			fileChooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser1.setMultiSelectionEnabled(false);
			fileChooser1.setAcceptAllFileFilterUsed(false);
			if (fileChooser1.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				sourceFileField.setText(fileChooser1.getSelectedFile().toString());
			}
		}
		if (ae.getSource() == previous) {
			try {
				FilePackerUnpackerFront nextPage = new FilePackerUnpackerFront(username);
				this.setVisible(false);
				nextPage.setVisible(true);
			} catch (Exception e) {
				FilePackerUnpacker.log.info("Exception occur in FileUnpackerFront");
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}

	public void keyPressed(KeyEvent ke) {
		String keyName = KeyEvent.getKeyText(ke.getKeyCode());
		if (keyName.equals("Enter")) {
			extractTask();
		}
	}

	public void keyReleased(KeyEvent ke) {
	}

	public void keyTyped(KeyEvent e) {
	}
}
