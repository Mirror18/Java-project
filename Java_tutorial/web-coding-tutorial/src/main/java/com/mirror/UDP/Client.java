package com.mirror.UDP;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * @author mirror
 */
public class Client {
    public static void main(String[] args) {
        DatagramSocket ds = null;
        try {
            //连接指定服务器端口
            ds = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            assert ds != null;
            //设置超市时间
            ds.setSoTimeout(1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            //连接
            ds.connect(InetAddress.getByName("localhost"),5555);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        //发送，可以用scanner，然后打包为datagramPacket在发送，
        byte[] data = "hello".getBytes(StandardCharsets.UTF_8);

        DatagramPacket packet = new DatagramPacket(data,data.length);

        try {
            ds.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //接收返回的内容

        byte[] buffer = new byte[1024];
        packet =new DatagramPacket(buffer,buffer.length);
        try {
            ds.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String resp = new String(packet.getData(),packet.getOffset(),packet.getLength());
        System.out.println(resp);
        ds.disconnect();
        ds.close();
    }
}
