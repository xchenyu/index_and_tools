package com.example.demo;

import com.example.demo.service.IpfsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IpfsServiceTest {
    @Autowired
    private IpfsService ipfsService;

    // 上传
    @Test
    public void uploadToIpfs() throws IOException {
        byte[] data = "hello world!!".getBytes();
        String hash = ipfsService.uploadToIpfs(data);
        System.out.println(hash);
    }

    // 下载
    @Test
    public void downloadIpfs() {
        String hash = "QmVwRS3oRTcdf4DR7GwLpbNngESru5uqWKquhLziAkLaaa";
        byte[] data = ipfsService.downFromIpfs(hash);
        System.out.println(new String(data));
    }
    @Test
    public void test(){
        System.out.println("1");
    }
}
