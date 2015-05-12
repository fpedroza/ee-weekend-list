package fmp.ee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Pedroza
 */
public class ExcelReader
{

  /** Creates a new instance of POIExcelReader */
  public ExcelReader()
  {
  }

  /**
   * This method is used to display the Excel content to command line.
   * 
   * @param xlsPath
   */
  public List<List<String>> read(File xlsPath)
    throws IOException
  {
    InputStream inputStream = null;

    try {
      inputStream = new FileInputStream(xlsPath);
    }
    catch (FileNotFoundException ex) {
      throw new IOException(String.format("File not found: [%s]", xlsPath.getAbsolutePath()), ex);
    }

    List<List<String>> results = new ArrayList<>();

    POIFSFileSystem fileSystem = new POIFSFileSystem(inputStream);

    Workbook workBook = new HSSFWorkbook(fileSystem);
    Sheet sheet = workBook.getSheetAt(0);
    Iterator<Row> rows = sheet.rowIterator();
    
    DataFormatter formatter = new DataFormatter();

    while (rows.hasNext()) {
      boolean isEmpty = true;
      List<String> currentRow = new ArrayList<>();
      
      Row row = rows.next();
      
      // once get a row its time to iterate through cells.
      Iterator<Cell> cells = row.cellIterator();

      while (cells.hasNext()) {
        Cell cell = cells.next();

        String cellValue = null;
        switch (cell.getCellType()) {
          case Cell.CELL_TYPE_BLANK:
            break;
          case Cell.CELL_TYPE_STRING:
            cellValue = cell.getStringCellValue();
            break;
          case Cell.CELL_TYPE_NUMERIC:
            cellValue = formatter.formatCellValue(cell);
            break;
          default:
            System.out.println("cellType: " + cell.getCellType());
        }
        
        cellValue = StringUtils.trimToNull(cellValue);
        if (isEmpty && cellValue != null) {
          isEmpty = false;
        }
        System.out.println(String.format("Cell No.: (%s,%s) = [%s]", 
            cell.getRowIndex(), cell.getColumnIndex(), cellValue));
        currentRow.add(cellValue);
      }
      
      if (!isEmpty) {
        results.add(currentRow);
      }
    }
    
    return results;
  }

  public static void main(String[] args) 
    throws IOException
  {
    ExcelReader poiExample = new ExcelReader();
    String xlsPath = "C:/home-frank/UpcomingWeekendRegistration.xls";

    List<List<String>> results = poiExample.read(new File(xlsPath));
    System.out.println(results);
  }

}
