package org.example;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * 思路是按照Workbook，Sheet，Row，Cell一层一层往下读取。
 */
public class ExcelParse {

    //1，初始化workbook
    public Workbook getReadWorkBook(String filePath) {
        FileInputStream is = null;
        Workbook wk = null;
        try {
            is = new FileInputStream(filePath);
            if (filePath.toLowerCase().endsWith("xls")) {
                wk = new HSSFWorkbook(is);
            } else if (filePath.toLowerCase().endsWith("xlsx")) {
                wk = new XSSFWorkbook(is);
            }
        } catch (Exception e) {
            System.out.println("流异常，error: " + e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return wk;
    }

    public static void readTxtFile(String filePath){
        try {
            String encoding="UTF-8";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    System.out.println(lineTxt);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

    }
    public static void main(String argv[]){
        String filePath = "C:\\Users\\11622\\Desktop\\code.txt";
        readTxtFile(filePath);
    }

}
