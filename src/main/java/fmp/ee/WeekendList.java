package fmp.ee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fmp.ee.model.Couple;
import fmp.ee.model.Person;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class WeekendList
{
  private static final String PROPERTY_ASSIGN_ROOMS = "assignRooms";

  private final File inputListPath;
  private final boolean assignRooms = Boolean.getBoolean(PROPERTY_ASSIGN_ROOMS);
  private String weekend;
  
  public static void main(String[] args) 
    throws IOException
  { 
    if (args.length != 1) {
      System.err.printf("Invalid usage: %s <UpcomingWeekendRegistrationExport>.xls [-D%s=true/false]",
              WeekendList.class.getName(), PROPERTY_ASSIGN_ROOMS);
      return;
    }
    
    String inputFileName = args[0];
    
    new WeekendList(new File(inputFileName)).create();
  }
  
  public WeekendList(File inputListPath) {
    this.inputListPath = inputListPath;
  }
  
  public File create() throws IOException {
    List<Couple> couples = createCouples(readFile(inputListPath));

    if (assignRooms) {
      RoomAssignments ra = new RoomAssignments(couples);
      ra.assignRooms();
    }

    File outputFile = getOutFile(inputListPath);
    saveToFile(couples, outputFile);
    return outputFile;
  }
  
  protected File getOutFile(File inFile) {
    return new File(inFile.getParentFile(), (weekend + " weekend list.xls"));
  }
  
  protected List<List<String>> readFile(File inputListPath) throws IOException {
    ExcelReader xlsReader = new ExcelReader();
    return xlsReader.read(inputListPath);
  }
  
  protected List<Couple> createCouples(List<List<String>> results) {
    
    List<Couple> couples = null;
    
    // expected format
    // AssignedWeekend, 1stChoice, 2ndChoice, ConfirmationSent, Notes, WomanName, Address, City, State, Zip, ForeignAdrs, Email, HomePhone, OtherPhone, Age, Religion, ManName, Address, City, State, Zip, ForeignAdrs, Email, HomePhone, OtherPhone, Age, Religion, Church, Priest, EngagementDate, WeddingDate, DietaryRestrictions, AddedTime, AddedUser
    final int NUMBER_OF_COLUMNS = 34;
    
    if (!results.isEmpty()) {
      
      couples = new ArrayList<>(results.size());
      
      int rowCount = 0;
      for (List<String> row : results) {
        if (rowCount++ == 0) {
          continue;  // skip the header row
        }
        
        try {
          if (row.size() != NUMBER_OF_COLUMNS) {
            throw new IllegalStateException(String.format(
                "unexpected number of column values: expected(%d) != actual(%d)", 
                NUMBER_OF_COLUMNS, row.size()));
          }
  
          String currWeekend = row.get(0);
          currWeekend = StringUtils.replaceChars(currWeekend, '/', '-');
          
          if (weekend == null) {  // only assign this the first time
            weekend = currWeekend;
            System.out.println("assigned weekend: " + weekend);
          }
          // ensure all the assigned weekend values are the same
          else if (weekend.equals(currWeekend) == false) {
            throw new IllegalStateException(String.format(
                "unexpected weekend value: expected(%s) != actual(%s)", weekend, currWeekend));
          }
          
          int i = 4;
          String notes = row.get(i++);  //Notes
          Couple couple = new Couple(
              couples.size()+1,  //id
              new Person(row.get(i++),  //WomanName
                  toAddress(row.get(i++), row.get(i++), row.get(i++), row.get(i++), row.get(i++)),  //Address,City,State,Zip,ForeignAddress
                  row.get(i++),  //Email
                  toPhone(row.get(i++), row.get(i++)),  //HomePhone,OtherPhone
                  toAge(row.get(i++)),  //Age
                  row.get(i++)),  //Religion
              new Person(row.get(i++),  //ManName
                  toAddress(row.get(i++), row.get(i++), row.get(i++), row.get(i++), row.get(i++)),  //Address,City,State,Zip,ForeignAddress
                  row.get(i++),  //Email
                  toPhone(row.get(i++), row.get(i++)),  //HomePhone,OtherPhone
                  toAge(row.get(i++)),  //Age
                  row.get(i++)),  //Religion
              notes,         //Notes
              row.get(i++),  //Church
              row.get(i++),  //Priest
              row.get(i++),  //Engagement
              row.get(i++),  //Wedding
              row.get(i++)   //DietaryRestrictions
          );
          couples.add(couple);
        }
        catch (Exception ex) {
          System.err.println(String.format(
              "couples added(%d) - exception row input: %s", couples.size(), row));
          throw ex;
        }
      }      
    }
        
//  System.out.println(String.format("couples(%d)", weekendList.couples.size()));
//  for (Couple couple : weekendList.couples) {
//    System.out.println(couple.toString());
//  }

    return couples;
  }
  
  
  private CellStyle getBoldFont(Workbook workBook) {
    //Bold Fond
    Font boldFont = workBook.createFont();
    boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
   
    //Bold style 
    CellStyle style = workBook.createCellStyle();
    style.setFont(boldFont);
    
    return style;
  }
  
  private CellStyle getVerticalCenterStyle(Workbook workBook) {
    CellStyle style = workBook.createCellStyle();
    style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    return style;
  }
  
  private CellStyle getVerticalTopStyle(Workbook workBook) {
    CellStyle style = workBook.createCellStyle();
    style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
    return style;
  }
  
  private CellStyle getWrapTopStyle(Workbook workBook) {
    CellStyle style = workBook.createCellStyle();
    style.setWrapText(true);
    return style;
  }
  
  public void saveToFile(List<Couple> couples, File filePath)
  {
    Workbook workBook = new HSSFWorkbook();
    
    addPrintableSheet(couples, workBook);
    addDetailSheet(couples, workBook);
    
    saveToFile(workBook, filePath);
  }
  
  public void addPrintableSheet(List<Couple> couples, Workbook workBook)
  {
    Sheet sheet = createSheet(workBook, "Summary", 0.25, false);
    
    Cell cell = null;
    
    final CellStyle csBold = getBoldFont(workBook);
    final CellStyle verticalCenterStyle = getVerticalCenterStyle(workBook);
    
    int row = 0;
    int col = 1;  // start at col 1 to leave space for couple number
    
    row = addWeekendHeader(sheet, row, col);
    
    row += 2;
    setCellValue(sheet, row, col++, "Name", csBold);
    setCellValue(sheet, row, col++, "Phone", csBold);
    setCellValue(sheet, row, col++, "Age", csBold);
    setCellValue(sheet, row, col++, "Religion", csBold);
    setCellValue(sheet, row, col++, "Church/Priest", csBold);
    setCellValue(sheet, row, col++, "Dates", csBold);
    if (assignRooms) setCellValue(sheet, row, col++, "Room", csBold);
    int maxCol = col;
    
    row++;
    for (Couple couple : couples) {
      // add couple number
      setCellValue(sheet, row, 0, couple.id, verticalCenterStyle);
      sheet.addMergedRegion(new CellRangeAddress(row, row+1, 0, 0));
      
      col = setCellValue(sheet, row, 1, couple.woman, false);
      setCellValue(sheet, row, col++, couple.church);
      setCellValue(sheet, row, col++, couple.engagement);
      if (assignRooms) setCellValue(sheet, row, col++, couple.getHerRoom());
      row++;
      col = setCellValue(sheet, row, 1, couple.man, false);
      setCellValue(sheet, row, col++, couple.priest);
      setCellValue(sheet, row, col++, couple.wedding);
      if (assignRooms) setCellValue(sheet, row, col++, couple.getHisRoom());
      
      row += 2;
    }

    sheet.addMergedRegion(new CellRangeAddress(row, row, 1, maxCol-1));
    setCellValue(sheet, row++, 1, "Notes/Dietary Restrictions", csBold);
    
    final CellStyle verticalTopStyle = getVerticalTopStyle(workBook);    
    final CellStyle wrapStyle = getWrapTopStyle(workBook);
    
    for (Couple couple : couples) {
      setCellValue(sheet, row, 0, couple.id, verticalTopStyle);
      
      String cellText = couple.notes;
      String dietRestictions = couple.dietaryRestrictions;

      // conditionally include diet restrictions
      if (dietRestictions != null) {
        cellText = String.format("Dietary Restrictions: %s%n%s", dietRestictions, cellText);
      }
      
      cell = setCellValue(sheet, row, 1, cellText, wrapStyle);
      
      int numLines = StringUtils.countMatches(cellText, "\n") + 1;
      cell.getRow().setHeightInPoints(cell.getRow().getHeightInPoints() * numLines);
      sheet.addMergedRegion(new CellRangeAddress(row, row, 1, maxCol-1));
      row++;
    }
    
    autofitCols(sheet, maxCol);
  }
  
  private Sheet createSheet(Workbook workBook, String name, double margin, boolean landscape) {
    Sheet sheet = workBook.createSheet(name);
    sheet.getPrintSetup().setFitWidth((short)1);
    sheet.getPrintSetup().setLandscape(landscape);
    setMargins(sheet, margin);
    return sheet;
  }
    
  private int addWeekendHeader(Sheet sheet, int row, int col) {
    final CellStyle csBold = getBoldFont(sheet.getWorkbook());
    
    setCellValue(sheet, row, col, "Weekend", csBold);
    setCellValue(sheet, row, col+1, weekend, csBold);
    sheet.addMergedRegion(new CellRangeAddress(row, row, col+1, col+3));
    
    setCellValue(sheet, ++row, col, "Team A", csBold);
    sheet.addMergedRegion(new CellRangeAddress(row, row, col+1, col+3));
    setCellValue(sheet, ++row, col, "Team B", csBold);
    sheet.addMergedRegion(new CellRangeAddress(row, row, col+1, col+3));
    setCellValue(sheet, ++row, col, "Venue", csBold);
    sheet.addMergedRegion(new CellRangeAddress(row, row, col+1, col+3));
    
    return row;
  }
  
  private void autofitCols(Sheet sheet, int maxCol) {
    for (int i = 0; i <= maxCol; i++) {
      sheet.autoSizeColumn(i);
    }
  }
  
  private void setMargins(Sheet sheet, double margin) {
    sheet.setMargin(Sheet.LeftMargin, margin);
    sheet.setMargin(Sheet.RightMargin, margin);
    sheet.setMargin(Sheet.TopMargin, margin);
    sheet.setMargin(Sheet.BottomMargin, margin);
  }
  
  public void addDetailSheet(List<Couple> couples, Workbook workBook)
  {
    Sheet sheet = createSheet(workBook, "Details", 0.33, true);
        
    final CellStyle csBold = getBoldFont(workBook);
    
    int row = 0;
    int col = 0;
    
    setCellValue(sheet, row, col++, "Name", csBold);
    setCellValue(sheet, row, col++, "Address", csBold);
    setCellValue(sheet, row, col++, "Email", csBold);
    setCellValue(sheet, row, col++, "Phone", csBold);
    setCellValue(sheet, row, col++, "Age", csBold);
    setCellValue(sheet, row, col++, "Religion", csBold);
    setCellValue(sheet, row, col++, "Church/Priest", csBold);
    setCellValue(sheet, row, col++, "Dates", csBold);
    int maxCol = col;
    
    row++;
    for (Couple couple : couples) {
      col = setCellValue(sheet, row, 0, couple.woman, true);
      setCellValue(sheet, row, col++, couple.church);
      setCellValue(sheet, row, col++, couple.engagement);
      row++;
      col = setCellValue(sheet, row, 0, couple.man, true);
      setCellValue(sheet, row, col++, couple.priest);
      setCellValue(sheet, row, col++, couple.wedding);
      
      row += 2;
    }
    
    autofitCols(sheet, maxCol);
  }
  
  public void saveToFile(Workbook workBook, File filePath)
  {
    if (filePath.exists()) {
      createBackup(filePath);
    }
    
    try (FileOutputStream out = new FileOutputStream(filePath)) {
      workBook.write(out);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    System.out.println(String.format("file saved as [%s]", filePath.getAbsolutePath()));
  }
  
  private void createBackup(File existing)
  {
    int i = 0;
    File backup = existing;
    
    String name = existing.getName();
    String front = StringUtils.left(name, name.length()-4);
    String ext = StringUtils.right(name, 4);
    while (backup.exists()) {
      String filename = String.format("%s-pv-%d%s", front, i++, ext);
      backup = new File(existing.getParent(), filename);
    }
    if (!existing.renameTo(backup)) {
      throw new RuntimeException("Unable to rename file " + existing.getAbsolutePath());
    }
    System.out.println(String.format("file [%s] renamed to [%s]", existing, backup));
  }
  
  private Cell setCellValue(Sheet sheet, int rowIdx, int colIdx, Object value)
  {
    return setCellValue(sheet, rowIdx, colIdx, value, null);
  }
  
  private Cell setCellValue(Sheet sheet, int rowIdx, int colIdx, Object value, CellStyle style)
  {
    Row row = sheet.getRow(rowIdx);
    if (row == null) {  // create row if needed
      row = sheet.createRow(rowIdx);
    }
    Cell cell = row.createCell(colIdx);
    cell.setCellValue(ObjectUtils.toString(value));
    if (style != null) {
      cell.setCellStyle(style);
    }
    return cell;
  }
  
  private int setCellValue(Sheet sheet, int rowIdx, int colIdx, Person person, boolean includeDetails)
  {
    setCellValue(sheet, rowIdx, colIdx++, person.name);
    if (includeDetails) {
      setCellValue(sheet, rowIdx, colIdx++, person.address);
      setCellValue(sheet, rowIdx, colIdx++, person.email);
    }
    setCellValue(sheet, rowIdx, colIdx++, person.phone);
    setCellValue(sheet, rowIdx, colIdx++, person.age);
    setCellValue(sheet, rowIdx, colIdx++, person.religion);
    return colIdx;
  }
  
  private final static String toPhone(String phoneOne, String phoneTwo)
  {
    StringBuilder buf = new StringBuilder();
    append(phoneOne, buf);
    if (!StringUtils.equals(phoneOne, phoneTwo)) {  // only add 2nd phone if it's different
      append(phoneTwo, buf);
    }
    String result = trimmed(buf);
    return result;
  }
  
  private final static Integer toAge(String ageIn)
  {
    Integer age = null;
    if (ageIn != null && ageIn.length() > 0) {
      try {
        age = Integer.valueOf(ageIn);
      }
      catch (NumberFormatException e) {
        // TODO: handle exception
        System.err.println("not a number: " + ageIn);
        throw e;
      }
    }
    return age;
  }
  
  private final static String toAddress(String address, String city, String state, String zip, String foreignAddress)
  {
    if (address != null && foreignAddress != null) {
      throw new IllegalArgumentException("both addresses cannot be non-null");
    }
    
    if (foreignAddress != null) {
      return foreignAddress;
    }
    
    StringBuilder buf = new StringBuilder();
    append(address, buf);
    append(city, buf);
    append(state, buf);
    append(zip, buf);
    
    String result = trimmed(buf);
    return result;
  }
  
  public final static String trimmed(StringBuilder buf)
  {
    return StringUtils.removeEnd(buf.toString().trim(), ",");  // trim the last comma
  }
  
  public final static void append(Object value, StringBuilder buf)
  {
    if (value != null) {
      if (buf == null) {
        buf = new StringBuilder();
      }
      buf.append(value).append(", ");
    }
  }  
}

