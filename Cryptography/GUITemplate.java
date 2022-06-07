import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

class ClockLabel extends JLabel implements ActionListener 
{
	String type;
	SimpleDateFormat sdf;

	public ClockLabel(String type) 
	{
		this.type = type;
		setForeground(Color.RED);

		switch (type) 
		{
			case "date":
				sdf = new SimpleDateFormat("MMMM dd yyyy");
				setFont(new Font("Consolas", Font.BOLD, 17));
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case "time":
				sdf = new SimpleDateFormat("hh:mm:ss a");
				setFont(new Font("Consolas", Font.BOLD, 17));
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
			case "day":
				sdf = new SimpleDateFormat("EEEE");
				setFont(new Font("Consolas", Font.BOLD, 17));
				setHorizontalAlignment(SwingConstants.RIGHT);
				break;
			default:
				sdf = new SimpleDateFormat();
				break;
		}

		Timer t = new Timer(1000, this);
		t.start();
	}

	public void actionPerformed(ActionEvent ae) 
	{
		Date d = new Date();
		setText(sdf.format(d));
	}
}

class GUITemplate extends JFrame implements ActionListener 
{
	JPanel header;
	JPanel content;
	JPanel top;
	Image img;
	ClockLabel dayLabel;
	ClockLabel timeLabel;
	ClockLabel dateLabel;
	JButton minimize, exit;
	Dimension fSize;
	
	// To store username and password and to serialize it into a file
	static HashMap<String, String> creds = new HashMap<String, String>();
	File serializeFile;
	ObjectInputStream mapInput;
	ObjectOutputStream mapOutput;
	FileInputStream serializeFis = null;
	FileOutputStream deserializeFos = null;

	public GUITemplate() 
	{
		try 
		{
			setIconImage(Toolkit.getDefaultToolkit().getImage("filepackerunpacker.jpg"));
			fSize = Toolkit.getDefaultToolkit().getScreenSize();
			this.setBounds(fSize.width / 4, fSize.height / 5, 700, 450);
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(this, e, "File Packer-Unpacker", JOptionPane.ERROR_MESSAGE);
		}

		getContentPane().setLayout(null);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		top = new JPanel();
		top.setBounds(0, 0, 684, 25);
		top.setBackground(Color.LIGHT_GRAY);
		top.setLayout(null);
		getContentPane().add(top);

		header = new JPanel();
		header.setBounds(0, 25, 684, 80);
		header.setBackground(Color.white);
		getContentPane().add(header);
		header.setLayout(null);

		content = new JPanel();
		content.setBounds(0, 105, 685, 306);
		content.setLayout(null);
		content.setBackground(new Color(0, 50, 120));
		getContentPane().add(content);

		this.setTitle("File Packer Unpacker");
		this.clock();
		this.closeAndMin();
		this.setVisible(true);
		this.setResizable(false);
		
	}

	void closeAndMin() 
	{
		minimize = new JButton("-");
		minimize.setBounds(2, 0, 45, 25);
		minimize.setHorizontalAlignment(SwingConstants.CENTER);
		minimize.setBackground(Color.LIGHT_GRAY);
		minimize.setFont(new Font("Consolas", Font.BOLD, 15));
		minimize.setHorizontalTextPosition(0);

		exit = new JButton("X");
		exit.setBounds(47, 0, 45, 25);
		exit.setHorizontalAlignment(SwingConstants.CENTER);
		exit.setFont(new Font("Consolas", Font.BOLD, 15));
		exit.setBackground(Color.LIGHT_GRAY);
		exit.setHorizontalTextPosition(0);

		top.add(minimize);
		top.add(exit);

		minimize.addActionListener(this);
		exit.addActionListener(this);
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
	}

	void clock() 
	{
		dateLabel = new ClockLabel("date");
		timeLabel = new ClockLabel("time");
		dayLabel = new ClockLabel("day");
		dayLabel.setHorizontalAlignment(SwingConstants.CENTER);

		dateLabel.setForeground(Color.RED);
		dateLabel.setAlignmentX(CENTER_ALIGNMENT);
		dateLabel.setAlignmentY(CENTER_ALIGNMENT);

		timeLabel.setForeground(Color.RED);
		timeLabel.setAlignmentX(CENTER_ALIGNMENT);
		timeLabel.setAlignmentY(CENTER_ALIGNMENT);

		dayLabel.setForeground(Color.RED);
		dayLabel.setAlignmentX(CENTER_ALIGNMENT);
		dayLabel.setAlignmentY(CENTER_ALIGNMENT);

		dateLabel.setFont(new Font("Consolas", Font.BOLD, 17));
		timeLabel.setFont(new Font("Consolas", Font.BOLD, 17));
		dayLabel.setFont(new Font("Consolas", Font.BOLD, 17));

		dayLabel.setBounds(270, 0, 130, 25);
		dateLabel.setBounds(395, 0, 150, 25);
		timeLabel.setBounds(545, 0, 140, 25);

		top.add(dayLabel);
		top.add(dateLabel);
		top.add(timeLabel);
	}
}
