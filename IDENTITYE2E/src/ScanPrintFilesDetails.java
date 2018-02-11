import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import com.sun.org.apache.bcel.internal.generic.NEW;


public class ScanPrintFilesDetails {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			Properties prop = readProperties();
		    String scandir = prop.getProperty("scandirectorypath");
		
		    System.out.println("***************List of all files in directory*************");
		
			File[] listOfFiles = scanFilesInDirectoryPrint(scandir);
			 for (int i = 0; i < listOfFiles.length; i++) {
			      if (listOfFiles[i].isFile()) {
			    	  String fileName = listOfFiles[i].getName();
			    	  String[] file = fileName.split("\\.");
			          System.out.println("FileName:" + file[0] +"  |FileMimeType: " + new MimetypesFileTypeMap().getContentType(listOfFiles[i]) +" |FileSize:" +listOfFiles[i].length()+ " |Extension:" +file[1]);
			      } 
			    }
			
		    System.out.println("\n\n");
		    System.out.println("***************List of specific files(xlsx, csv) in directory*************");
		    String supportedmimetypes =  prop.getProperty("supportedversions");
		    String[] extensions = supportedmimetypes.split("\\,");
		    File[] listOfSpecifiedFiles =scanSpecifiedFilesInDirectoryPrint(listOfFiles, extensions);
		    
		    for (int i = 0; i < listOfSpecifiedFiles.length; i++) {
		    	if(listOfSpecifiedFiles[i] != null)
			      if (listOfSpecifiedFiles[i].isFile()) {
			    	  String fileName = listOfSpecifiedFiles[i].getName();
			    	  String[] file = fileName.split("\\.");
			          System.out.println("FileName:" + file[0] +"  |FileMimeType: " + new MimetypesFileTypeMap().getContentType(listOfSpecifiedFiles[i]) +" |FileSize:" +listOfSpecifiedFiles[i].length()+ " |Extension:" +file[1]);
			      }
		    }
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	public static Properties readProperties() throws FileNotFoundException,
			IOException {
		Properties prop = new Properties();
		InputStream input = null;
		input = new FileInputStream("config.properties");
		prop.load(input);
		return prop;
	}

	public static File[] scanSpecifiedFilesInDirectoryPrint(File[] listOfFiles,
			String[] extensions) {
		File[] listOfSpecifiedFiles = new File[listOfFiles.length];
		int count = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
			    	  for(int j =0; j< extensions.length; j++)
			    	  {
				    	  String fileName = listOfFiles[i].getName();
				    	  String[] file = fileName.split("\\.");
				  		  if(extensions[j].equals(file[1]))
				  		  {
				    		  	listOfSpecifiedFiles[count] = listOfFiles[i];
				    		  	count++;
						  }
			    	  }
		    	  } 
		    }
		return listOfSpecifiedFiles;
	}

	private static  File[] scanFilesInDirectoryPrint(String scandir) {
		File[] listOfFiles = readFilesList(scandir);
		return listOfFiles;
	}

	public static File[] readFilesList(String scandir) {
		File folder = new File(scandir);
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
	}

}
