package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.example.BPlusTest.PlanterTransient;
import com.google.protobuf.ByteString;

public class test {
    public static void main(String[] args) throws IOException {
//        ByteString bs=ByteString.copyFrom("hi".getBytes());
//        System.out.println(bs.toString());
        List<String> list=new ArrayList<>();
        PlanterTransient p1=new PlanterTransient();
        int size=10;
        int low1=500;
        int high1=10000;

        p1.setDate(String.valueOf(2));
        p1.setPlanterName(String.valueOf(1));
        String str=p1.toString();
        System.out.println(str);
        long time1 = System.nanoTime();
        JSON.toJSON(p1);
//        for (int i = 1; i<size; i++) {
//            int n = ((int) (Math.random() * (high1 - low1))) + low1;
////            System.out.println("i:"+i+" "+" n:"+n);
////            Product p = new Product(n, "test", 1.0 * i);
//            p1.setDate(String.valueOf(n));
//            p1.setPlanterName(String.valueOf(i));
//            String json= JSON.toJSON(p1).toString();
//        }

        long time2 = System.nanoTime();
        System.out.println("插入"+size+"个数据耗时: " + (time2 - time1)+"纳秒，"+(time2 - time1)/1000+"微秒，"+(time2 - time1)/1000000+"ms");

    }
}
