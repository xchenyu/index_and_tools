package com.example;

import java.io.IOException;
import com.google.protobuf.ByteString;
public class test {
    public static void main(String[] args) throws IOException {
        ByteString bs=ByteString.copyFrom("hi".getBytes());
        System.out.println(bs.toString());

    }
}
