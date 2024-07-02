package com.mirror.TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;


/**
 * @author mirror
 */
public class Server {
    public static void main(String[] args) {
        //源码中是继承了自动关闭，所以这里可以用try包裹，但我们本身就是要无限循环的
        //实现对指定IP和指定端口进行接听
        ServerSocket ss = null;
        try {
            //监听5555端口，没有IP地址，表示对计算机所有的网络接口上进行监听
             ss = new ServerSocket(5555);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //这是服务器初始化输出的信息
        System.out.println("server is running...");
//然后就是无限循环
        while(true){
            Socket sock = null;
            try {
                assert ss != null;
                //表示每当新的客户端连接进来后，就返回一个Socket实例
                //这个实例是用来和刚链接的客户端进行通信的
                sock = ss.accept();
                System.out.println("connected from " + sock.getRemoteSocketAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
            //因为客户端很多，所以要实现并发处理
            //如果没有客户端连接进来，这个会一直阻塞并一直等待
            Thread t = new Handler(sock);
            t.start();
        }
    }
    /*
    主线程的作用就是接受新的连接
    每当收到新连接之后
    就创建一个新的线程进行处理。
    这里转到线程的处理

    所以这里也可用线程池
     */
}
class Handler extends Thread{
    Socket sock;
    //进行初始化
    public Handler(Socket sock){
        this.sock = sock;
    }

    @Override
    public void run() {
        try (InputStream input = this.sock.getInputStream()) {
            try (OutputStream output = this.sock.getOutputStream()) {
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {
            }
            System.out.println("client disconnected.");
        }
    }

    private void handle(InputStream input, OutputStream output) throws IOException{
        //设置缓存区的读写
        var writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        var reader = new BufferedReader(new InputStreamReader(input,StandardCharsets.UTF_8));
        //这玩意儿是默认的，归属到初始化中
        writer.write("hello\n");
        writer.flush();
        for(;;){
            //读取缓存区内容，按照行读写
            String s = reader.readLine();
            if("bye".equals(s)){
                writer.write("bye\n");
                writer.flush();
                break;
            }
            //返回，并把缓存区的给推送
            writer.write("ok: " + s + "\n");
            writer.flush();
        }
    }


}