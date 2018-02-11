import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SeleniumUtil {

	public static void waitUntilElementDisplayed(WebDriver driver, String elementId)
	{
	WebDriverWait wait = new WebDriverWait(driver, 10);
	wait.until(ExpectedConditions.or(
	    ExpectedConditions.visibilityOfElementLocated(By.id(elementId))));
	}


	public static void takeScreenShot(WebDriver driver, String filePath, String fileName) throws IOException
	{
		File src= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(src, new File(filePath+fileName));
	}
}
