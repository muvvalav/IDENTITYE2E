import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ReadFileDataUtil {

	public static void loadVehiclesExcelData(String  filePath, List<VehicleVO> vehicles) throws IOException
	{
		FileInputStream inputStream = new FileInputStream(new File(filePath));
		Workbook workbook = new XSSFWorkbook(inputStream);
		try{
			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = firstSheet.iterator();
         
        while (iterator.hasNext()) {
        	
            Row nextRow = iterator.next();
            if(nextRow.getRowNum() >0)
            {
	            Iterator<Cell> cellIterator = nextRow.cellIterator();
	            VehicleVO vehicleVO = new VehicleVO();
	             
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                if(cell.getColumnIndex() == 0)
	                {
	               	 vehicleVO.setRegistrationNumber(cell.getStringCellValue());
	                }
	                else if(cell.getColumnIndex() == 1)
	                 {
	                	 vehicleVO.setMake(cell.getStringCellValue());
	                 }
	                 else if(cell.getColumnIndex() == 2)
	                 {
	                	 vehicleVO.setColor(cell.getStringCellValue()); 
	                 }
	            }
	            vehicles.add(vehicleVO);
            }
        }
         
		}
		catch(Exception e)
		{
		 e.getStackTrace();	
		}
		finally{
	        workbook.close();
	        inputStream.close();
		}
		
	}
	
	public static void loadVehiclesCsvData(String  filePath, List<VehicleVO> vehicles) throws IOException
	{
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String rowDataSplitBy = "-";

        try {

            br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {

                String[] rows = line.split(cvsSplitBy);
                for(int i =0 ; i< rows.length; i++)
                {
	                String[] rowData = rows[i].split(rowDataSplitBy);
	                VehicleVO vehicleVO = new VehicleVO();
	                for(int j =0; j<rowData.length; j++)
	                {
	                	if(j == 0)
		                {
		               	 vehicleVO.setRegistrationNumber(rowData[j]);
		                }
		                else if(j == 1)
		                 {
		                	 vehicleVO.setMake(rowData[j]);
		                 }
		                 else if(j == 2)
		                 {
		                	 vehicleVO.setColor(rowData[j]); 
		                 }
	                }
                	vehicles.add(vehicleVO);
                
                }


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
		
	}

}
