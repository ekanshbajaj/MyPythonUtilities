import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AddRowsBetweenRowsUtilityJava {

    public static void main(String[] args) throws IOException {
        // Load the Excel file into a XSSFWorkbook object
        FileInputStream file = new FileInputStream("example.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheetAt(0);

        // Create an empty list to hold the modified rows
        List<Row> newRows = new ArrayList<>();

        // Iterate over the rows of the sheet
        Iterator<Row> rowIterator = sheet.iterator();
        Row prevRow = rowIterator.next();
        newRows.add(prevRow);
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            // Check if this row is the same as the previous row
            if (!isEqual(prevRow, row)) {
                // If the row is different, add 5 rows with the previous row's contents to the list
                for (int i = 0; i < 5; i++) {
                    newRows.add(prevRow);
                }
            }
            // Add the current row to the list
            newRows.add(row);
            prevRow = row;
        }

        // Write the modified rows back to a new Excel file
        XSSFWorkbook newWorkbook = new XSSFWorkbook();
        XSSFSheet newSheet = newWorkbook.createSheet("Sheet1");
        int rowNum = 0;
        for (Row newRow : newRows) {
            Row row = newSheet.createRow(rowNum++);
            int cellNum = 0;
            for (Cell cell : newRow) {
                Cell newCell = row.createCell(cellNum++);
                switch (cell.getCellType()) {
                case BOOLEAN:
                    newCell.setCellValue(cell.getBooleanCellValue());
                    break;
                case NUMERIC:
                    newCell.setCellValue(cell.getNumericCellValue());
                    break;
                case STRING:
                    newCell.setCellValue(cell.getStringCellValue());
                    break;
                default:
                    newCell.setCellValue("");
                }
            }
        }
        FileOutputStream outputStream = new FileOutputStream("modified123.xlsx");
        newWorkbook.write(outputStream);
        newWorkbook.close();
        outputStream.close();
        workbook.close();
        file.close();
    }

    // Check if two rows are equal by comparing their cell values
    private static boolean isEqual(Row row1, Row row2) {
        Iterator<Cell> cellIterator1 = row1.cellIterator();
        Iterator<Cell> cellIterator2 = row2.cellIterator();
        while (cellIterator1.hasNext() && cellIterator2.hasNext()) {
            Cell cell1 = cellIterator1.next();
            Cell cell2 = cellIterator2.next();
            if (!cell1.equals(cell2)) {
                return false;
            }
        }
        return !cellIterator1.hasNext() && !cellIterator2.hasNext();
    }
}
