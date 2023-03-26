package com.example.bloomFilterTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import jxl.*;

import jxl.read.biff.BiffException;

import java.io.*;
import java.util.ArrayList;

public class excelFileRead {

    public static void main(String[] args) {

        excelFileRead obj = new excelFileRead();

// 此处为我创建Excel路径：E:/zhanhj/studysrc/jxl下

        File file = new File("C:\\Users\\14353\\Desktop\\bloomtest.xls");

        obj.readExcel(file);

    }

// 去读Excel的方法readExcel，该方法的入口参数为一个File对象


    public ArrayList readExcel(File file) {
        ArrayList res=new ArrayList();

        try {

// 创建输入流，读取Excel

            InputStream is = new FileInputStream(file.getAbsolutePath());

// jxl提供的Workbook类

            Workbook wb = Workbook.getWorkbook(is);

// Excel的页签数量

            int sheet_size = wb.getNumberOfSheets();

            for (int index = 0; index < sheet_size; index++) {

// 每个页签创建一个Sheet对象

                Sheet sheet = wb.getSheet(index);

// sheet.getRows()返回该页的总行数
//                System.out.println("excel数据行数"+sheet.getRows());

                for (int i = 0; i < sheet.getRows(); i++) {

// sheet.getColumns()返回该页的总列数

                    for (int j = 0; j < sheet.getColumns(); j++) {

                        String cellinfo = sheet.getCell(j, i).getContents();
                        res.add(cellinfo);


                    }

                }

            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (BiffException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return res;
    }


}



//public class ReadExcel {
//
//    public static void main(String[] args) throws BiffException, IOException {
//
//        Workbook book = Workbook.getWorkbook(new File("Test.xls"));
//
////获得第一个工作表对象
//
//        Sheet sheet = book.getSheet("sheet_one");
//
////Sheet sheet = book.getSheet(0);
//
//        int rows = sheet.getRows();
//
//        int cols = sheet.getColumns();
//
//        System.out.println("总列数：" + cols);
//
//        System.out.println("总行数:" + rows);
//
//        System.out.println("----------------------------");
//
//        int i=0;
//
//        int j=0;
//
////循环读取数据
//
//        for(i=0;i<=rows;i++){
//
//            for(j=0;j<=cols;j++){
//
//                System.out.println("第"+j+"行，第"+i+"列为："+sheet.getCell(i, j).getContents());
//
//            }
//
//        }
//
//    }
//
//}