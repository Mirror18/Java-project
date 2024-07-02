package com.mirror.TCP;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author mirror
 */
public class Client {
    @lombok.SneakyThrows
    public static void main(String[] args) {
        Socket socket = null;
        try {
            //连接指定服务器和端口
            socket = new Socket("localhost", 5555);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert socket != null;
        //获取输入流
        try (InputStream inputStream = socket.getInputStream()) {
            try (OutputStream outputStream = socket.getOutputStream()) {
                //处理输入和输出流
                handle(inputStream, outputStream);
            }
        }
        //然后釜底抽薪的，
        socket.close();
        System.out.println("disconnected.");
    }

    private static void handle(InputStream inputStream, OutputStream outputStream) {
        //初始化
        var writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        //获取输入的类
        Scanner scanner = new Scanner(System.in);
        //这里先输出，也是第一次输出，首先是缓存区内有东西
        try {
            System.out.println("[server]" + reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        这里循环输出
        while (true) {
            System.out.println(">>>");
            ///读取一行输入
            String s = scanner.nextLine();
            //把输入内容写进去，并且写进一个换行
            try {
                writer.write(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //推送上去
            try {
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String resp = null;
            try {
                resp = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("<<<" + resp);
            //如果写入bye就结束循环
            if("bye".equals(resp)){
                break;
            }
        }
    }
}
