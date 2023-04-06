package com.example.bloomFilterTest;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryo.serializers.MapSerializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import com.example.BPlusTest.PlanterTransient;
import org.apache.commons.codec.binary.Base64;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.*;

public class BloomFilterTest {
    private static int size = 100;
    /**
     * 预计插入的数据
     */
    private static Integer expectedInsertions = size;
    /**
     * 误判率
     */
    private static Double fpp = 0.0001;
    /**
     * 布隆过滤器
     */
    private static ArrayList<String> sz = new ArrayList();
    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), expectedInsertions, fpp);
    private static BloomFilter<String> bloomFilter2 = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), expectedInsertions, fpp);

    private static char getRandomChar() {
        String str = "";
        int hightPos; //
        int lowPos;

        Random random = new Random();

        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println("错误");
        }

        return str.charAt(0);
    }

    public static void main(String[] args) {
        // 插入 1千万数据
//        for (int i = 0; i < expectedInsertions; i++) {
//            bloomFilter.put(i);
//        }

        //获取excel表

        File file = new File("C:\\Users\\14353\\Desktop\\text" + size + ".xls");
        excelFileRead obj = new excelFileRead();
        sz = obj.readExcel(file);
//        System.out.println("数据长度： " + sz.size());
        //布隆过滤器插入
//        long bl_cr1 = System.nanoTime();
//        for (int i = 0; i < expectedInsertions; i++) {
//            bloomFilter2.put(sz.get(i));
//        }
//        long bl_cr2 = System.nanoTime();
//        System.out.println("布隆插入时间：" +(bl_cr2 - bl_cr1) + "ns,"+(bl_cr2 - bl_cr1)/1000 + "us,"+(bl_cr2 - bl_cr1)/1000000 + "ms");
        //哈希表插入----------------------------------------------------------------------------
        long time_cr1 = System.nanoTime();
        Map<String, List<String>> map = new HashMap<>();
        int count=1;
        int path_num=count;
        PlanterTransient p1=new PlanterTransient();
        String str;
        while(count>0){
            for (int i = 0; i < sz.size(); i++) {
                p1.setDate(String.valueOf(i));
                p1.setPlanterName(String.valueOf(i));
                str="else map.put(String.valueOf(sz.get(i)),l);else map.put(String.valueOf(sz.get(i)),l);";
                List<String> l=new ArrayList();
                l.add(str);
//                map.put(String.valueOf(sz.get(i)),l);
                if(map.containsKey(String.valueOf(sz.get(i))))map.get(String.valueOf(sz.get(i))).add(str);
                else map.put(String.valueOf(sz.get(i)),l);
//                if(map.containsKey(String.valueOf(sz.get(i))))map.get(String.valueOf(sz.get(i))).add(str);
//                else map.put(String.valueOf(sz.get(i)),l);
//            System.out.println("excel中的：" + sz.get(i));
//            bloomFilter2.put(String.valueOf(sz.get(i)));
            }
            count--;
        }

        long time_cr2 = System.nanoTime();
////        System.out.println("插入时间：" + (time_cr2 - time_cr1) + "ns," + (time_cr2 - time_cr1) / 1000 + "us," + (time_cr2 - time_cr1) / 1000000 + "ms");
//        System.out.println("map长度" + map.size());


//        ArrayList<String> list = new ArrayList<String>(Arrays.asList("阿著", "阿柱","阿贮","阿瞩","哎拄","哎拄","啊煮","哀拄","哀逐","皑猪"));
//        for (int j = sz.size()-1; j >0; j--) {
//            List l=new ArrayList();
//            l.add(j);
//            map.put(String.valueOf(sz.get(sz.size()-j)),l);
////            if(map.containsKey(String.valueOf(sz.get(i))))map.get(String.valueOf(sz.get(i))).add(i);
////            else map.put(String.valueOf(sz.get(i)),l);
//        }

//______________________________________________________________________________________________
//        long time_cx = System.nanoTime();
//        System.out.println(map.get("阿著"));
////        for(int i=0;i<1000;i++){
//////            System.out.println(map.get("阿著"));
////
////        }
//        long time_cx2 = System.nanoTime();
//        System.out.println("map查询耗时: " + (time_cx2 - time_cx)+"ns，"+(time_cx2 - time_cx)/1000 + "us,"+(time_cx2 - time_cx)/1000000+"ms");

        //______________________________________________________________________________________________
        //布隆过滤器检查
//        int matchCount = 0;
//        long start=System.nanoTime();
//        if (bloomFilter2.mightContain("阿著")) {
//            matchCount++;
//            System.out.println("存在匹配的值：" + matchCount);
//        }
//        long end = System.nanoTime();
//        System.out.println("布隆过滤器查询时间"+(end - start) + "ns,"+(end - start)/1000 + "us,"+(end - start)/1000000 + "ms");

        //kryo序列化hashmap----------------------------------------------------------------------------
        long serialize1 = System.nanoTime();
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setRegistrationRequired(true);
        MapSerializer serializer = new MapSerializer();
        serializer.setKeyClass(String.class, new JavaSerializer());
        serializer.setKeysCanBeNull(false);
        serializer.setValueClass(Integer.class, new JavaSerializer());
        serializer.setValuesCanBeNull(true);
        kryo.register(Integer.class, new JavaSerializer());
        kryo.register(HashMap.class, serializer);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        Output output = new Output(baos);
//        kryo.writeObject(output, map);
//        output.flush();
//        output.close();
//        byte[] b = baos.toByteArray();
//        try {
//            baos.flush();
//            baos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        long serialize2 = System.nanoTime();
//        System.out.println("序列化哈希表时间" + (serialize2 - serialize1) + "ns," + (serialize2 - serialize1) / 1000 + "us," + (serialize2 - serialize1) / 1000000 + "ms");
//        //byte数组重构哈希表
////        ByteArrayInputStream bais=new ByteArrayInputStream(b);
////        Input input=new Input(bais);
////        Map<String, List<Integer>> newmap = new HashMap<>();
////        newmap=kryo.readObject(input,HashMap.class);
//
//        map.clear();

//        哈希表写入文件，以二进制字节流形式写入----------------------------------
//        long write1 = System.nanoTime();
//        File file_map = new File("E:/Hash_index/index_test" + (path_num*size) + ".txt");
//        try {
//            //创建文件字节输出流对象，准备向d.txt文件中写出数据,true表示在原有的基础上增加内容
//            FileOutputStream fout = new FileOutputStream(file_map, true);
//            fout.write(b);
//            fout.flush();//强制刷新输出流
//            fout.close();//关闭输出流
//            System.out.println("写入完成！");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        long write2 = System.nanoTime();
//        System.out.println("写入文件时间" + (write2 - write1) + "ns," + (write2 - write1) / 1000 + "us," + (write2 - write1) / 1000000 + "ms");


        //读取哈希表文件
        long read1 = System.nanoTime();
        String test_file="E:/BPlusTree_index/data_file.txt";
        Map<String, List<String>> newmap = new HashMap<>();
        try {
            FileInputStream fin = new FileInputStream(test_file);
            int len = fin.available();
            byte[] ret = new byte[len];
            fin.read(ret);
            long read2 = System.nanoTime();
            System.out.println("读取文件时间" + (read2 - read1) + "ns," + (read2 - read1) / 1000 + "us," + (read2 - read1) / 1000000 + "ms");
            //重构哈希表
            long re_build1 = System.nanoTime();
            ByteArrayInputStream bais = new ByteArrayInputStream(ret);
            Input input = new Input(bais);
            newmap = kryo.readObject(input, HashMap.class);
            long re_build2 = System.nanoTime();
            System.out.println("重构哈希时间" + (re_build2 - re_build1) + "ns," + (re_build2 - re_build1) / 1000 + "us," + (re_build2 - re_build1) / 1000000 + "ms");
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        long re_cx1 = System.nanoTime();
        System.out.println("查询数量："+newmap.size());
//        System.out.println(newmap.size());
//        for(int i=0;i<1000;i++){
////            System.out.println(map.get("阿著"));
//
//        }
        long re_cx2 = System.nanoTime();
        System.out.println("重构哈希查询耗时: " + (re_cx2 - re_cx1) + "ns，" + (re_cx2 - re_cx1) / 1000 + "us," + (re_cx2 - re_cx1) / 1000000 + "ms");


        // 检查已在过滤器中的值，是否有匹配不上的
//        for (int i = 1; i <= expectedInsertions; i++) {
//            if (!bloomFilter.mightContain(i)) {
//                System.out.println("存在不匹配的值：" + i);
//            }
//        }
//
//        // 检查不在过滤器中的1000个值，是否有匹配上的
//        int matchCount = 0;
//        for (int i = expectedInsertions + 1; i <= expectedInsertions + 1000; i++) {
//            if (bloomFilter.mightContain(i)) {
//                matchCount++;
//            }
//        }
//        System.out.println("误判个数：" + matchCount);
        //随机生成汉字

        //生成随机汉子
//        for (int i = 1; i < 4; i++) {
//            char result = getRandomChar();
//            System.out.println("result : " + result);
//        }
//        List<String> s=new ArrayList<>();
//        s.add("我");
//        s.add("的");
//        s.add("的");
//        s.add("为");
//        s.add("他");
//        s.add("好");
//        s.add("发");
//        s.add("看");
//        s.add("吗");

    }
}
