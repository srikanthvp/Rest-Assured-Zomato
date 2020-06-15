package org.framework.Utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReadUtils {

    public List<String> getDataFromXls(String filePath, int cellNumber) {
        FileInputStream fileInputStream = null;
        List<String> datalList = new ArrayList<String>();
        Workbook workbook = null;

        try {
            fileInputStream = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("File not found at specified location: " + filePath);
            e.printStackTrace();
        }

        try {
            if (filePath.endsWith("xls")) {
                workbook = new HSSFWorkbook(fileInputStream);
            }
            if (filePath.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(fileInputStream);
            } else {
                System.out.println("File extension is not as expected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sheet sheet = workbook.getSheet("products");

        int lastRowNum = sheet.getLastRowNum();

        for (int i = 1; i < lastRowNum; i++) {

            datalList.add(sheet.getRow(i).getCell(cellNumber).getStringCellValue());
        }

        return datalList;
    }
}

