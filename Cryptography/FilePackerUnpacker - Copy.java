import java.io.File;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class FilePackerUnpacker 
{
	static Logger log = Logger.getLogger("FilePackerUnpackerLog");
	static FileHandler logHandler;
	static String fileSep = File.separator;

	public static void main(String[] args) 
	{
		try 
		{
			logHandler = new FileHandler("." + fileSep + "FilePackerUnpacker.log", true);
			log.addHandler(logHandler);
			log.setUseParentHandlers(false);

			// Print a brief summary of the LogRecord in a human readable format.
			SimpleFormatter formatter = new SimpleFormatter();
			logHandler.setFormatter(formatter);
			log.info("Application Started, Now Creating frame for login window");

			FilePackerUnpackerLogin frame = new FilePackerUnpackerLogin();
			frame.setVisible(true);
			SwingUtilities.invokeLater(new Runnable() 
			{
				public void run() 
				{
					frame.unameField.requestFocus();
				}
			});
		} 
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
