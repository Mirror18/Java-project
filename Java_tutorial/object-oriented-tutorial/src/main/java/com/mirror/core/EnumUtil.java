package com.mirror.core;

import lombok.Getter;

import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author mirror
 */
public class EnumUtil {
    //创建一个线程安全的并发set
    private static final Map<Object, Object> key2EnumMap = new ConcurrentHashMap<>();
    //创建一个并发的set
    private static final Set<Class<?>> enumSet = ConcurrentHashMap.newKeySet();

    /**
     * 获取枚举（不缓存）
     *
     * @param enumType          枚举class类型
     * @param enumToValueMapper 抽取字段（按照什么字段搜索）
     * @param key               待匹配的字段值
     * @param <T>               方法的入参
     * @param <R>               方法的出参
     * @return 返回一个容器类对象，
     */
    public static <T extends Enum<T>, R> Optional<T> getEnumWithCache(Class<T> enumType, Function<T, R> enumToValueMapper, Object key) {
        //如果enumSet这个列表中不存在这个类
        if (!enumSet.contains(enumType)) {
            //这个关键字用在多线程环境下实现同步没确保同一时刻只有一个线程可以执行到这个方法
            //就是一个同步锁
            synchronized (enumType) {
                //至于为什么这里再写一遍，其实就是因为锁的位置
                if (!enumSet.contains(enumType)) {
                    //向这个set中添加元素
                    enumSet.add(enumType);
                    //这是获取枚举值，也或者说枚举的实例，对象。
                    for (T enumConstant : enumType.getEnumConstants()) {
                        //第二个参数有意思，不过也相当于把T类型转换为R类型，在函数中又转为字符串
                        //返回的是枚举类名+枚举值名

                        String mapKey = getKey(enumType, enumToValueMapper.apply(enumConstant));
                        key2EnumMap.put(mapKey, enumConstant);
                    }
                }
            }
        }
        //一个容器嘞，可能包含也可能不包含非空值的对象
        //这里句返回了一个可能为空也可能不为空的值，最后强转了
        return Optional.ofNullable((T) key2EnumMap.get(getKey(enumType, key)));

    }

    /**
     * 获取key
     * 带来枚举类型作为前缀，避免不同枚举的key重复
     *
     * @param enumTpe
     * @param key
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T extends Enum<T>, R> String getKey(Class<T> enumTpe, R key) {
        return enumTpe.getName().concat(key.toString());
    }

    /**
     * 获取枚举（不缓存）
     *
     * @param enumType
     * @param enumToValueMapper
     * @param key
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T extends Enum<T>, R> Optional<T> getEnum(Class<T> enumType, Function<T, R> enumToValueMapper, Object key) {
        for (T enumThis : enumType.getEnumConstants()) {
            if (enumToValueMapper.apply(enumThis).equals(key)) {
                //包含非空值
                return Optional.of(enumThis);
            }
        }
        //创建一个空的对象
        return Optional.empty();
    }


    // ----------- 测试工具类 ------------
    public static void main(String[] args) {
        // 按code搜索，寻找code=2的FruitEnum对象
        Optional<FruitEnum> fruitEnum = EnumUtil.getEnumWithCache(FruitEnum.class, FruitEnum::getCode, 2);
        //如果这个容器类对象包含值，就对这个值进行操作
        fruitEnum.ifPresent(fruit -> System.out.println(fruit.getName()));
        /*
        这里详细讲下发生了什么
        主要就省略一步，根据传入的类型获取枚举类，虽然我也不知道到底是那个麻烦，
        但最后要强转
         */
    }

    @Getter
    enum FruitEnum {
        //测试用的
        APPLE(1, "Apple"), BANANA(2, "banana"), ORANGE(3, "origin"),
        ;

        FruitEnum(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        private final Integer code;
        private final String name;
    }
}
