package com.example.BPlusTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeTest {
    @Test
    public static void main(String[] args) throws IOException {

//        BPlusTree<Product, Integer> tree = new BPlusTree<>(50);
//        int low1=500;
//        int high1=1000;
//        long time1 = System.nanoTime();
//        for (int i = 1; i<29500; i++) {
//            int n = ((int) (Math.random() * (high1 - low1))) + low1;
////            System.out.println("i:"+i+" "+" n:"+n);
//            Product p = new Product(n, "test", 1.0 * i);
//                tree.insert(p, p.getId());
//        }
//        long time2 = System.nanoTime();
//        System.out.println("插入耗时: " + (time2 - time1)+"纳秒，"+(time2 - time1)/1000+"微秒，"+(time2 - time1)/1000000+"ms");
//        for (int i = 1; i<3000; i++) {
//            int n = ((int) (Math.random() * (high1 - low1))) + low1;
////            System.out.println("i:"+i+" "+" n:"+n);
//            Product p = new Product(i, "test", 1.0 * i);
//            tree.insert(p, p.getId());
//        }
//        for (int i = 1; i<4000; i++) {
//            int n = ((int) (Math.random() * (high1 - low1))) + low1;
////            System.out.println("i:"+i+" "+" n:"+n);
//            Product p = new Product(i, "test", 1.0 * i);
//            tree.insert(p, p.getId());
//        }

//        Product p1 = new Product(25, "test", 1.0 * 25);
//        tree.insert(p1, p1.getId());

//        List<Product> list=tree.Rangefind(7,false,10,false);
//        Product p1 = new Product(3, "item", 1.0 * 100);
//        tree.insert(p1,p1.getId());
//        tree.insert(p1,p1.getId());
//        tree.insert(p1,p1.getId());
//        tree.insert(p1,p1.getId());
//
//        long time2 = System.nanoTime();
//        System.out.println("插入耗时: " + (time2 - time1)+"纳秒，"+(time2 - time1)/1000000+"ms");

        //查询
//        long sum = 0;
//        int low=0;
//        int high=4001;
//        for (int j = 1; j<10001; j++){
//            long time_cx = System.nanoTime();
//            int n = ((int) (Math.random() * (high - low))) + low;
//            List<Product> ret=tree.find(n);
//            long time_cx2 = System.nanoTime();
//            sum+=time_cx2 - time_cx;
//            System.out.println(n+":查询耗时: " + (time_cx2 - time_cx)+"纳秒，"+(time_cx2 - time_cx)/1000+"微秒，"+(time_cx2 - time_cx)/1000000+"ms");
//        }
//        long avg=sum/100;
//        System.out.println("查询总耗时: " + sum+"纳秒，"+sum/1000+"微秒，"+sum/1000000+"ms");
//        System.out.println("查询总耗时: " + avg+"纳秒，"+avg/1000+"微秒，"+sum/1000000+"ms");
//        long time_cx = System.nanoTime();
//        List<Product> ret=tree.find(602);
//        int count=0;
//        for (Product p:ret) {
//            count++;
//        }
//        System.out.println("出现"+count+"次");
//        long time_cx2 = System.nanoTime();
//        System.out.println("直接查询耗时: " + (time_cx2 - time_cx)+"纳秒，"+(time_cx2 - time_cx)/1000+"微秒，"+(time_cx2 - time_cx)/1000000+"ms");

        //范围查询
//        long time_fwcx = System.nanoTime();
//        List<Product> list=tree.Rangefind(1,false,50,false);
//        long time_fwcx2 = System.nanoTime();
//        System.out.println("范围查询耗时: " + (time_fwcx2 - time_fwcx)+"纳秒，"+(time_fwcx2 - time_fwcx)/1000000+"ms");


        //序列化树
//        long time4 = System.nanoTime();
//        String test_str=tree.serialize(tree);
//        long time5 = System.nanoTime();
//        System.out.println("序列化树耗时: " + (time5 - time4)+"纳秒，"+(time5 - time4)/1000000+"ms");

        //反序列化树
//        long timed1 = System.nanoTime();
//        BPlusTree<Product, Integer> tree2=tree.deserialize(test_str);
//        long timed2 = System.nanoTime();
//        System.out.println("反序列化树耗时: " + (timed2 - timed1)+"纳秒，"+(timed2 - timed1)/1000000+"ms");

        //重构树查询
//        long timecs = System.nanoTime();
//        List<Product> list2=tree.Rangefind(1,false,3,true);
//        long timecs2 = System.nanoTime();
//        System.out.println("重构树查询耗时: " + (timecs2 - timecs)+"纳秒，"+(timecs2 - timecs)/1000+"微秒，"+(timecs2 - timecs)/1000000+"ms");

        //B+树序列写入文件
//        long time_read1 = System.nanoTime();
//        try(Writer writer = new FileWriter("E:/BPlusTree_index/index_test2.txt")){
//            writer.write(test_str);
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//        long time_read2 = System.nanoTime();
//        System.out.println("B+树写入文件耗时: " + (time_read2 - time_read1)+"纳秒，"+(time_read2 - time_read1)/1000000+"ms");
        BPlusTreePlanter<PlanterTransient, Integer> tree2 = new BPlusTreePlanter<>(10);
//        //读取索引序列文件
        long time6 = System.nanoTime();
        File file=new File("E:/BlockIndex/Planter/DataIndex/block199.txt");
        FileInputStream inputStream = new FileInputStream(file);
        int length = inputStream.available();
        byte bytes[] = new byte[length];
        inputStream.read(bytes);
        inputStream.close();
        String str =new String(bytes, StandardCharsets.UTF_8);
        long time7 = System.nanoTime();
        System.out.println("读取索引文件耗时: " + (time7 - time6)+"纳秒，"+(time7 - time6)/1000000+"ms");
//
        long time8 = System.nanoTime();
        BPlusTreePlanter<PlanterTransient, Integer> new_tree=tree2.deserialize(str);
        long time9 = System.nanoTime();
        System.out.println("读取文件反序列化树耗时: " + (time9 - time8)+"纳秒，"+(time9 - time8)/1000000+"ms");
        PlanterTransient p=new PlanterTransient();
        p.setDate("20231203");
        p.setPlanterName("test");
        new_tree.insert(p,p.getDate());

        String planter_str=new_tree.serialize(new_tree);
        BPlusTreePlanter cs=new_tree.deserialize(planter_str);
        List<PlanterTransient> res=new_tree.find(20231203);
        System.out.println(res.get(0).getPlanterName());


//        //重构树查询
//        long time10 = System.nanoTime();
////        new_tree.find(100);
//        List<PlanterTransient> p=new_tree.find(20230316);
//        for (int i = 0; i < p.size(); i++) {
//            System.out.println(p.get(i).getPlantertxID());
//        }
////        new_tree.Rangefind(30,false,100,false);
//        long time11 = System.nanoTime();
//        System.out.println("读取序列文件重构树查询耗时: " + (time11 - time10)+"纳秒，"+(time11 - time10)/1000000+"ms");

    }

}


