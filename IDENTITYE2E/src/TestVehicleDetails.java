import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestVehicleDetails {
	
	public static Properties prop;
	ScanPrintFilesDetails scanPrintFilesDetail = new ScanPrintFilesDetails();
	public static WebDriver driver;
	public final static Logger log = Logger.getLogger(TestVehicleDetails.class);

	
	@Before
	public void TestVehicleDetails() throws FileNotFoundException, IOException{
		//To initiate log file applog.txt
		Log4jConfiguration.xmlConfig();
		log.info("TestVehicleDetails START");
		//To read config.properties data into properties object
		prop =scanPrintFilesDetail.readProperties();
		//setting system property for invoking firefox browser  using selenium
		System.setProperty("webdriver.gecko.driver", "/Users/srinivasmuvvala/Desktop/Project/IDENTITYE2E/libs/geckodriver");
		driver = new FirefoxDriver();
	}
	
	@After
	public void postTest(){
		if(driver!=null)
			driver.close();
	}
	
	@Test
	public void testVehicleDetails() {
		String scandir = prop.getProperty("scandirectorypath");
	    //Read all files list in a directory
		File[] listOfFiles = scanPrintFilesDetail.readFilesList(scandir);
	    String supportedmimetypes =  prop.getProperty("supportedversions");
	    //populating extensions array for valid extensions
	    String[] extensions = supportedmimetypes.split("\\,");

	    //Filtering specified file list
	    File[] listOfSpecifiedFiles =scanPrintFilesDetail.scanSpecifiedFilesInDirectoryPrint(listOfFiles, extensions);
	    List<VehicleVO> vehicles = new ArrayList<VehicleVO>();
	    
	    try {
			for (int i = 0; i < listOfSpecifiedFiles.length; i++) {
				if(listOfSpecifiedFiles[i] != null)
			      if (listOfSpecifiedFiles[i].isFile()) {
			    	  String fileName = listOfSpecifiedFiles[i].getName();
			    	  String[] file = fileName.split("\\.");
			          if(file[1].equalsIgnoreCase("xls"))
			          {
			        	  //To extract data from excel file
			        	  ReadFileDataUtil.loadVehiclesExcelData(listOfSpecifiedFiles[i].toString(), vehicles);
			          }
			          else if(file[1].equalsIgnoreCase("csv"))
			          {
			        	  //To extract data from csv file
			        	  ReadFileDataUtil.loadVehiclesCsvData(listOfSpecifiedFiles[i].toString(), vehicles);
			          }
			          
			      }
			}
			log.info("govurl: "+ prop.getProperty("govurl"));
			//To open url
			driver.get(prop.getProperty("govurl"));
			String screenshotdir = prop.getProperty("screenshotsdirectorypath");
			String govUrlHomePage = "govurlhomepage.png";
			SeleniumUtil.takeScreenShot(driver, screenshotdir, govUrlHomePage);
			//to click on start now button
			driver.findElement(By.linkText("Start now")).click();

			String govUrlSearchPage = "govurlsearchpage.png";
			SeleniumUtil.takeScreenShot(driver, screenshotdir, govUrlSearchPage);
			
			//Code to navigate through the page and log the respective outcome by comparing with actual data 
			for(VehicleVO vehicleVO: vehicles)
			{
				String inputSearchPage = "inputsearch"+vehicleVO.getRegistrationNumber()+".png";
				String outputResultsPage = "outputresult"+vehicleVO.getRegistrationNumber()+".png";
				//wait until search page appers
				SeleniumUtil.waitUntilElementDisplayed(driver, "Vrm");
				//Clear any pretext in search input box
				driver.findElement(By.id("Vrm")).clear();
				//enter registration number from test data into search input box
				driver.findElement(By.id("Vrm")).sendKeys(vehicleVO.getRegistrationNumber());
				SeleniumUtil.takeScreenShot(driver, screenshotdir, inputSearchPage);
				//submit continue to get results
				driver.findElement(By.name("Continue")).click();
				log.info("**************************************************************************************");
				log.info("Start Test Execution for Registeration number: "+ vehicleVO.getRegistrationNumber());
				Thread.sleep(2000);
				//case to handle invalid format of  registration number
				if(driver.getPageSource().contains("You must enter your registration number in a valid format"))
				{
					log.info(driver.findElement(By.id("Vrm-error")).getText());
					SeleniumUtil.takeScreenShot(driver, screenshotdir, outputResultsPage);
				}
				else if(driver.getPageSource().contains("Vehicle details could not be found"))
				{
					log.info("Invalid Registeration number: "+ vehicleVO.getRegistrationNumber());
					SeleniumUtil.takeScreenShot(driver, screenshotdir, outputResultsPage);
					driver.findElement(By.linkText("Search again")).click();

				}
				else{
					//if vehicle number is valid. check make and color
					String make = driver.findElement(By.xpath("/html/body/main/form/div/div/ul/li[2]/span[2]/strong")).getText();
					String color = driver.findElement(By.xpath("/html/body/main/form/div/div/ul/li[3]/span[2]/strong")).getText();

					log.info("Test data Maker Name: "+ vehicleVO.getMake() + " Actual Maker Name: "+ make);
					log.info("Test data Color Name: "+ vehicleVO.getColor() + " Actual Color Name: "+ color);

					if(vehicleVO.getMake().equalsIgnoreCase(make))
						log.info("Test data maker name matches with actual data");
					else
						log.info("Test data maker name mismatches with actual data");
					if(vehicleVO.getColor().equalsIgnoreCase(color))
						log.info("Test data color matches with actual data");
					else
						log.info("Test data color mismatches with actual data");
					SeleniumUtil.takeScreenShot(driver, screenshotdir, outputResultsPage);
					//Return back to search criteria page
			    	if(driver.findElement(By.className("link-back"))!=null)
			    		driver.findElement(By.className("link-back")).click();
				}
				log.info("End Test Execution for Registeration number: "+ vehicleVO.getRegistrationNumber());
				log.info("**************************************************************************************");
			}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("Exception: "+e);
		}
	   }
}
