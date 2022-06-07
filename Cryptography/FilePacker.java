import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FilePacker extends JFrame 
{

	// Create FileOutputStream object for destination file
	FileOutputStream outStream = null;
	// We support files with following extensions to be packed
	String invalidExtensions[] = { ".txt", ".c", ".cpp", ".java", ".cs", ".js", ".class", ".jpge", ".jpg", ".pdf",
			".py", ".doc", ".docx", ".png", ".wav", ".mp3", ".mp4", ".pptx", ".ppt" };
	String validExt;
	static boolean flag = false;
	boolean isDirExists = true;

	// Constructor
	public FilePacker(String src, String extension, String dest, String destPath, String username) throws Exception 
	{
		FilePackerUnpacker.log.info("File Packing started..");
		// Implement Magic number code and write it on file
		String magic = "LeafyBeacon";
		byte arr[] = magic.getBytes();
		outStream = new FileOutputStream(destPath + FilePackerUnpacker.fileSep + dest);
		outStream.write(arr, 0, arr.length);

		// Check whether user gave directory or not
		File folder = new File(src);
		if (folder.exists() && folder.isDirectory()) 
		{
			listAllFiles(folder.getAbsolutePath(), extension);
		} 
		else 
		{
			this.isDirExists = false;
		}

		outStream.close();

		// After closing the file perform the encryption....
		new FileEncryption(destPath + FilePackerUnpacker.fileSep + dest);
		// Now file is encrypted
		FilePackerUnpacker.log.info("File Packing completed..");
	}

	public void listAllFiles(String path, String extension) throws IOException 
	{
		try (Stream<Path> paths = Files.walk(Paths.get(path))) 
		{
			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					try 
					{
						String name = filePath.getFileName().toString();
						String ext = name.substring(name.lastIndexOf("."));
						List<String> list = Arrays.asList(invalidExtensions);

						if (extension.equals("All")) 
						{
								filePack(filePath.toString());
						} 
						else 
						{
							if (list.contains(ext)) 
							{
								if (ext.equals(extension)) 
								{
									filePack(filePath.toString());
								}
							}
						}
					} 
					catch (Exception e) 
					{
					}
				}
			});
		} 
		finally 
		{
			if (flag == false) 
			{
				String str = new String("Source Directory does not contain file with selected extension");
				JOptionPane.showMessageDialog(this, str, "File Packer-Unpacker", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public void filePack(String filePath) throws Exception 
	{
		flag = true;
		FileInputStream inStream = null;
		byte[] buffer = new byte[1024];
		int length = 0;
		byte[] temp = new byte[256];
		File fObj = new File(filePath);
		StringBuffer header = new StringBuffer();
		long fileSize = fObj.length();
		header.append(filePath).append(" ").append(fileSize);

		// Filling extra space with whitespaces
		for (int i = header.length(); i < 256; i++) 
		{
			header.append(" ");
		}

		// For simplicity convert header into byte array
		temp = header.toString().getBytes();

		// Create object of FileInputStream to read data from file
		inStream = new FileInputStream(filePath);

		// First write our header to Dest file whose object is outstream
		outStream.write(temp, 0, temp.length);

		// Read all data from that directory file and write it into Dest file(Pack file)
		while ((length = inStream.read(buffer)) > 0) 
		{
			outStream.write(buffer, 0, length);
		}

		// Close file that we read in this function
		inStream.close();
	}
}
