package com.example.BPlusTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.example.BPlusTest.hashIndexTest.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreeTest {
    @Test
    public static void main(String[] args) throws IOException {
        BPlusTreePlanter<PlanterTransient, Integer> tree = new BPlusTreePlanter<>(50);
        int low1 = 1000;
        int high1 = 10000;
        long time1 = System.nanoTime();
        int size = 4000000;
        String fileName = "E:/BPlusTree_index/index_" + size + ".txt";
        String data_file = "E:/BPlusTree_index/data_file" + ".txt";
        PlanterTransient p1 = new PlanterTransient();
        HashMap<String, List<Object[]>> path_map = new HashMap<>();
        int[] tt = {219, 743, 793, 95};
        for (int i = 0; i < size; i++) {
            int n = ((int) (Math.random() * (high1 - low1))) + low1;
//            System.out.println("n:"+n);
            p1.setDate(String.valueOf(n));
            p1.setPlanterName(String.valueOf(n));
            tree.insert(p1, p1.getDate(), path_map);
        }

        data_To_file(path_map);
        long time2 = System.nanoTime();
//        p1.setDate(String.valueOf(37749));
//        p1.setPlanterName("test");
//        tree.insert(p1, p1.getDate(), path_map);
//        data_To_file(path_map);

        System.out.println("插入" + size + "个数据耗时: " + (time2 - time1) + "纳秒，" + (time2 - time1) / 1000 + "微秒，" + (time2 - time1) / 1000000 + "ms");
//        序列化
        long ser1 = System.nanoTime();
        String ser_str = serialize(tree);
        long ser2 = System.nanoTime();
        System.out.println("序列化" + size + "个数据耗时: " + (ser2 - ser1) + "纳秒，" + (ser2 - ser1) / 1000 + "微秒，" + (ser2 - ser1) / 1000000 + "ms");

        //树序列写入文件
        write_file(fileName, ser_str, size);

        //读取文件
        String tree_str = read_file(fileName);

        //反序列化文件内容
        long deser1 = System.nanoTime();
        BPlusTreePlanter new_tree = deserialize(tree, tree_str);
        long deser2 = System.nanoTime();
        System.out.println("反序列化" + size + "个数据耗时: " + (deser2 - deser1) + "纳秒，" + (deser2 - deser1) / 1000 + "微秒，" + (deser2 - deser1)/ 1000000 + "ms");

        long time10 = System.nanoTime();
        String ret = new_tree.find(9857);
//        System.out.println(ret);
        long time11 = System.nanoTime();
        System.out.println("查找耗时：" + (time11 - time10) / 1000000);

    }

    static String serialize(BPlusTreePlanter tree) {
        long time4 = System.nanoTime();
        String str = tree.tree_serialize(tree);
        long time5 = System.nanoTime();
        System.out.println("序列化树耗时: " + (time5 - time4) + "纳秒，" + (time5 - time4) / 1000000 + "ms");
        return str;
    }

    static void write_file(String fileName, String str, int size) {
        long time_read1 = System.nanoTime();
        try (Writer writer = new FileWriter(fileName)) {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long time_read2 = System.nanoTime();
        System.out.println(size + "个数的B+树写入文件耗时: " + (time_read2 - time_read1) + "纳秒，" + (time_read2 - time_read1) / 1000000 + "ms");
    }

    static String read_file(String fileName) throws IOException {
        long time6 = System.nanoTime();
        File file = new File(fileName);
        FileInputStream inputStream = new FileInputStream(file);
        int length = inputStream.available();
        byte bytes[] = new byte[length];
        inputStream.read(bytes);
        inputStream.close();
        String tree_str = new String(bytes, StandardCharsets.UTF_8);
        long time7 = System.nanoTime();
        System.out.println("读取" + fileName + "索引文件耗时: " + (time7 - time6) + "纳秒，" + (time7 - time6) / 1000000 + "ms");
        return tree_str;
    }

    static BPlusTreePlanter deserialize(BPlusTreePlanter tree, String tree_str) {
        long time8 = System.nanoTime();
        BPlusTreePlanter<PlanterTransient, Integer> new_tree = tree.tree_deserialize(tree_str);
        long time9 = System.nanoTime();
        System.out.println("读取文件反序列化树耗时: " + (time9 - time8) + "纳秒，" + (time9 - time8) / 1000000 + "ms");
        return new_tree;
    }

    static void data_To_file(HashMap path_map) {
        //获取path——map的迭代器，遍历元素，元素内的entry为要插入的文件path和插入的（key：value）对
        Iterator<Map.Entry<String, List<Object[]>>> iterator = path_map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<Object[]>> entry = iterator.next();
            String path = entry.getKey();
            List<Object[]> data = entry.getValue();
            //读取文件，并反序列化文件为hashmap
            HashMap<String, List<String>> map = new HashMap<>();
            byte[] before = map_read(path);
            if (before.length != 0) map = map_deserialize(before);
            //把data逐个插入
            for (Object[] arr : data) {
                String key = String.valueOf(arr[0]);
                String value = String.valueOf(arr[1]);
                List<String> l = new ArrayList<>();
                l.add(value);
                if (map.containsKey(key)) map.get(key).add(String.valueOf(value));
                else map.put(key, l);
            }
//            将hashmap序列化后存入文件
            byte[] after = map_serialize(map);
            map_write(after, path);
        }
    }
}


