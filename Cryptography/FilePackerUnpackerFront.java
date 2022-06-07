import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

class FilePackerUnpackerFront extends GUITemplate implements ActionListener 
{
	JButton pack, unpack, logout;
	JLabel task, welcome;
	String username;

	FilePackerUnpackerFront(String username) 
	{
		FilePackerUnpacker.log.info("To the window of File Packer Unpacker Front");
		this.username = username;
		welcome = new JLabel("Welcome : " + username);
		welcome.setBounds(200, 25, 350, 25);
		welcome.setFont(new Font("Courier New", Font.BOLD, 20));
		welcome.setForeground(new Color(34, 177, 76));
		header.add(welcome);

		task = new JLabel("Select Task");
		task.setBounds(235, 52, 203, 30);
		content.add(task);
		task.setHorizontalAlignment(SwingConstants.CENTER);
		task.setForeground(Color.WHITE);
		task.setFont(new Font("Courier New", Font.BOLD, 20));
		task.setAlignmentX(JLabel.CENTER);

		pack = new JButton("Pack Files");
		pack.setHorizontalAlignment(SwingConstants.CENTER);
		pack.setFont(new Font("Courier New", Font.BOLD, 20));
		pack.setBounds(140, 120, 155, 50);
		pack.setAlignmentX(JButton.CENTER);
		content.add(pack);

		unpack = new JButton("Unpack File");
		unpack.setHorizontalAlignment(SwingConstants.CENTER);
		unpack.setFont(new Font("Courier New", Font.BOLD, 20));
		unpack.setBounds(380, 120, 180, 50);
		unpack.setAlignmentX(JButton.CENTER);
		content.add(unpack);

		logout = new JButton("Logout");
		logout.setHorizontalAlignment(SwingConstants.CENTER);
		logout.setFont(new Font("Courier New", Font.BOLD, 20));
		logout.setBounds(262, 210, 150, 45);
		logout.setAlignmentX(JButton.CENTER);
		content.add(logout);

		pack.addActionListener(this);
		unpack.addActionListener(this);
		logout.addActionListener(this);

		this.setBounds(fSize.width / 4, fSize.height / 5, 700, 450);
		this.setResizable(false);
		FilePackerUnpacker.log.info("Moving from window of File Packer Unpacker Front");
	}

	public void actionPerformed(ActionEvent ae) 
	{
		if (ae.getSource() == exit) 
		{
			final Frame[] frames = Frame.getFrames();
			if (frames != null) 
			{
				for (final Frame f : frames) 
				{
					f.dispose();
				}
			}
			System.exit(0);
		}
		if (ae.getSource() == minimize) 
		{
			setState(JFrame.ICONIFIED);
		}
		if (ae.getSource() == pack) 
		{
			try 
			{
				FilePackerFront nextPage = new FilePackerFront(username);
				this.setVisible(false);
				nextPage.setVisible(true);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						FilePackerFront.srcDirField.requestFocus();
					}
				});
			} 
			catch (Exception e) 
			{
				FilePackerUnpacker.log.info("Exception in FilePackerUnpackerFront:Pack");
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}

		if (ae.getSource() == unpack) 
		{
			try 
			{
				FileUnpackerFront nextPage = new FileUnpackerFront(username);
				this.setVisible(false);
				nextPage.setVisible(true);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						nextPage.sourceFile.requestFocus();
					}
				});
			} 
			catch (Exception e) 
			{
				FilePackerUnpacker.log.info("Exception in FilePackerUnpackerFront:Unpack");
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}

		if (ae.getSource() == logout) 
		{
			try 
			{
				FilePackerUnpackerLogin nextPage = new FilePackerUnpackerLogin();
				this.setVisible(false);
				nextPage.setVisible(true);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						nextPage.unameField.requestFocus();
					}
				});
			} 
			catch (Exception e) 
			{
				FilePackerUnpacker.log.info("Exception in FilePackerUnpackerFront:Logout");
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}
}
