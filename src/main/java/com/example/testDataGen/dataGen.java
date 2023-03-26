package com.example.testDataGen;


import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class dataGen {
    public static void main(String[] args) throws IOException {

        int tem_high=38,tem_low=15;
        int temp,envhum,ph,light,soilhum,processorid;
        String[] planter={"小麦","玉米","水稻","豌豆","绿豆","蚕豆","土豆","辣椒","黄瓜","丝瓜","花生","油菜","西红柿","空心菜","南瓜","白菜","蒜","姜","萝卜","茄子",};
        int nameIndex;
        int key;
        String date;
        int type=1;
        int size=2000;
        long start=System.nanoTime();
        try(Writer writer = new FileWriter("E:/testData/testData"+size+".txt")){
            for (int i=0;i<size;i++){
                temp=myRandom(tem_high,tem_low);
                nameIndex=myRandom(19,0);
                envhum=myRandom(80,40);
                ph=myRandom(9,4);
                light=myRandom(2000,15000);
                soilhum=myRandom(70,65);
                key=myRandom(150000,100000);
                date=randomDate("2023-03-13","2023-03-19");
                processorid=myRandom(6,1);
                String s = "{\"Args\":[\"save\","
                        +"\""+key+"\","
                        +"\"{"
                        + "\\\"processorid\\\":\\\"" + processorid
                        + "\\\",\\\"temperature\\\":\\\"" + temp
                        + "\\\",\\\"planterName\\\":\\\"" + planter[nameIndex]
                        + "\\\",\\\"envhumidity\\\":\\\"" + envhum
                        + "\\\",\\\"ph\\\":\\\"" + ph
                        + "\\\",\\\"light\\\":\\\"" + light
                        + "\\\",\\\"soilhumidity\\\":\\\"" + soilhum
                        +"\\\",\\\"date\\\":\\\"" + date
                        +"\\\",\\\"type\\\":\\\"" + type
                        + "\\\"}"
                        +"\"]"+"}";
//                System.out.println(s);
                writer.write(s);
                writer.write("\r\n");
            };
        }catch (IOException e){
            e.printStackTrace();
        }
        long end=System.nanoTime();
        System.out.println("数据生成结束，耗时："+(end-start)+"ns, "+(end-start)/1000+"us, "+(end-start)/1000/1000+"ms");
    }
    public static int myRandom(int high,int low){
        int ret=((int) (Math.random() * (high - low))) + low;
        return ret;
    };
    public static String randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());
            Date date1=new Date(date);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
            String result = format2.format(date1);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }
}
