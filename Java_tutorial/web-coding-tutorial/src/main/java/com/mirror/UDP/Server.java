package com.mirror.UDP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

/**
 * @author mirror
 */
public class Server {
    public static void main(String[] args) {
        DatagramSocket ds = null;
        try {
            //监听指定端口
            ds = new DatagramSocket(5555);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        System.out.println("UDP server is running...");
        //无限循环
        while (true) {
            //数据缓存区
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                assert ds != null;
                //收到一个UDP数据包
                ds.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 收取到的数据存储在buffer中，由packet.getOffset(), packet.getLength()指定起始位置和长度
            // 将其按UTF-8编码转换为String:
            String s = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
            System.out.println(s);
            //发送数据
            byte[] data = "ACK".getBytes(StandardCharsets.UTF_8);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                outputStream.write(data);
                outputStream.write(packet.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }


            byte[] result = outputStream.toByteArray();

            packet.setData(result);
            try {
                ds.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

class Handle extends Thread{
    DatagramSocket ds;
    public Handle(DatagramSocket datagramSocket){
        ds=datagramSocket;
    }
    @Override
    public void run(){
        
    }
}