package com.mirror.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author mirror
 */
public class NumberOfBig {

    public static void main(String[] args) {
        BigInteger bigInteger = new BigInteger("123456789");
        System.out.println(bigInteger.pow(5));

        BigInteger b1 = new BigInteger("1234567");
        BigInteger b2 = new BigInteger("2498642");
        BigInteger sum = b1.add(b2);
        System.out.println(sum);
        System.out.println(sum.intValue());
        /*
        上面是整数
        下面这个是浮点数
        主要针对于大的值。而且也可以转换为基本类
         */
        BigDecimal bigDecimal = new BigDecimal("123.456");
        System.out.println(bigDecimal.multiply(bigDecimal));
        //这个输出小数位数
        System.out.println(bigDecimal.scale());
        //这个转换为一个想等的，但是去掉末尾0的
        System.out.println(bigDecimal.stripTrailingZeros());
        //然后还能四舍五入和直接截断
//        BigDecimal d1 = new BigDecimal("123.456789");
//        BigDecimal d2 = d1.setScale(4, RoundingMode.HALF_UP);
//        // 四舍五入，123.4568
//         BigDecimal d3 = d1.setScale(4, RoundingMode.DOWN);
//         // 直接截断，123.4567
//         System.out.println(d2);
//         System.out.println(d3);
        /*
        关于bigdecimal的演示这里就不做过多使用了
        反正用的时候也需要重新查
        这里好像源码出了点问题懒得修了
         */

        /*
        另外就是常用工具类了
        但实话说
        已经做了很多演示了
        因为前面求随机数啥之类的
         */

        /*
        唯一需要注意的可能就是随机数
         */
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            sr = new SecureRandom();
        }
        byte[] bytes = new byte[16];
        sr.nextBytes(bytes);
        System.out.println(Arrays.toString(bytes));

        /*
        至于进制转换，虽然一般还是要查
        不过可以记下这个
        HexFormat.of();
         */
    }
}
