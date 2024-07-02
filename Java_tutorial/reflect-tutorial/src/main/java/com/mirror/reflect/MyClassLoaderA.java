package com.mirror.reflect;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class MyClassLoaderA {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = String.class.getClassLoader();
        System.out.println(classLoader);
        //创建一个2s执行一次的定时任务
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //设置当前环境
                String swapPath = MyClassLoader.class.getResource("").getPath();
//                System.out.println(swapPath);
//                /target/classes/com/mirror/reflect/
                String className = "com.mirror.reflect.Test";
//                System.out.println(Set.of(className));
//                [com.mirror.reflect.Test]
                //每次都实例化一个classloader
                MyClassLoader myClassLoader = new MyClassLoader(swapPath,Set.of(className));


                try{
                    //使用自定义的加载类
                    Object o = myClassLoader.loadClass(className).getDeclaredConstructor().newInstance();
                    //调用printVersion
                    o.getClass().getMethod("printVersion").invoke(o);
                    /*
                    为什么要通过反射加载方法。
                    Test.class会隐性的被加载当前类的ClassLoader加载，当前Main方法默认的ClassLoader为AppClassLoader，而不是我们自定义的MyClassLoader
                     */
                }catch(InstantiationException | IllegalAccessException | ClassNotFoundException|NoSuchMethodException| InvocationTargetException ignored){}
            }
        },0,2000);
    }
}

abstract class ClassLoaderA {

    private final ClassLoaderA parent;

    private ClassLoaderA(Void unused, String name, ClassLoaderA parent) {

        this.parent = parent;

    }
//当然这个类是压根不会用，只是解释下如何加载类的，原文几百行的代码，看的我头大
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        synchronized (getClassLoadingLock(name)) {
            //查看是否已经加载过类，加载过后的类会有缓存
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                try {
                    //如果父类不为空，啧让父类加载
                    if (parent != null) {
                        c = parent.loadClass(name, false);
                    } else {
                        //父类是null及时BootStapCassLoader，使用启动类类加载器加载
                        c = findBootStrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }
                //如果父类没有加载该类
                if (c == null) {
                    //模板方法模式，没加载成功就findClass
                    long t1 = System.nanoTime();
                    //就让当前类加载器加载
                    c = findClass(name);
                }
            }
            return c;
        }

    }
//子类只需实现findClass，关心类从哪里加载即可
    protected abstract Class<?> findClass(String name);

    protected abstract Class<?> findBootStrapClassOrNull(String name);

    protected abstract Class<?> findLoadedClass(String name);

    protected abstract Object getClassLoadingLock(String name);

}

 class MyClassLoader extends ClassLoader{
    //用于读取.class文件的路径
    private final String swapPath;
    //用于标记这些name的类是先由自身加载的
    private final Set<String> userMyClassLoaderLoad;

     /**
      * 构造器
      * @param swapPath
      * @param userMyClassLoaderLoad
      */
    public MyClassLoader(String swapPath,Set<String> userMyClassLoaderLoad){
        this.swapPath=swapPath;
        this.userMyClassLoaderLoad = userMyClassLoaderLoad;
    }

     /**
      *
      * @param name
      * @return
      * @throws ClassNotFoundException
      */
    @Override
     public Class<?> loadClass(String name) throws ClassNotFoundException{
        //先查找
        Class<?> c = findLoadedClass(name);
        //特殊的类让自身加载
        if(c == null && userMyClassLoaderLoad.contains(name)){
            c = findClass(name);
            if(c != null){
                return c;
            }
        }
        return super.loadClass(name);
    }
    @Override
     protected Class<?> findClass(String name){
        //根据文件系统路径加载class文件，并返回byte[]值
        byte[] classBytes = getClassByte(name);
        //调用classloader提供的方法，将二进制数组转换为class类的实例
        return defineClass(name,classBytes,0,classBytes.length);
    }

     private byte[] getClassByte(String name) {
        //Test.class
         String className = name.substring(name.lastIndexOf(".")+1)+".class";
         //完整路径
         try (FileInputStream fileInputStream = new FileInputStream(swapPath + className);
              ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
             byte[] buffer = new byte[1024];
             int length;
             while ((length = fileInputStream.read(buffer)) != -1) {
                 byteArrayOutputStream.write(buffer, 0, length);
             }
             return byteArrayOutputStream.toByteArray();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return null;
     }
}

