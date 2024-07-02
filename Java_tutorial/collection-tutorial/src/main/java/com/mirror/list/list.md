# list

是一种有序列表

> list的行为和数组几乎完全相同。`List`内部按照放入元素的先后顺序存放，每个元素都可以通过索引确定自己的位置，`List`的索引和数组一样，从`0`开始。

数组和列表类似，也是有序结构。

但是数组有麻烦点，就在于增删上。因为位置确定，所以每次修改都是需要多次复制和删除

## ArrayList

`ArrayList`在内部使用了数组来存储所有元素。

虽然时间上跟数组的一样，但是好在我们不用自己写方法了，有现成的接口

* 在末尾添加一个元素：`boolean add(E e)`
* 在指定索引添加一个元素：`boolean add(int index, E e)`
* 删除指定索引的元素：`E remove(int index)`
* 删除某个元素：`boolean remove(Object e)`
* 获取指定索引的元素：`E get(int index)`
* 获取链表大小（包含元素的个数）：`int size()`


但学过数据结构就知道，不单单只有这种存储方式，还有一种链表的方式存储数组

一种`LinkedList`通过“链表”也实现了List接口。在`LinkedList`中，它的内部每个元素都指向下一个元素：

比较一下`ArrayList`和`LinkedList`：


|                     | ArrayList    | LinkedList           |
| --------------------- | -------------- | ---------------------- |
| 获取指定元素        | 速度很快     | 需要从头开始查找元素 |
| 添加元素到末尾      | 速度很快     | 速度很快             |
| 在指定位置添加/删除 | 需要移动元素 | 不需要移动元素       |
| 内存占用            | 少           | 较大                 |

通常情况下，我们总是优先使用`ArrayList`。

## linkedList

所以通过以上的介绍，昂，因为都是通过实现list接口，所以调用方法也是一样的。只不过内部的实现方式不同，所需时间也不同。

那么这种list集合和其他有什么区别呢，首先就是list接口允许我们添加重复的元素，list还允许添加null。

## 创建list

除了ArrayList ,LinkedList，还可以通过List接口提供的of()方法，根据给定的元素快速创建List.

```java
List<Integer> list = List.of(1, 2, 5);
```

就是这里面不接受null值。

## 遍历list

除了简单的for循环，也就是get的方法来进行遍历。

还可以使用迭代器访问List.

`Iterator`本身也是一个对象，但它是由`List`的实例调用`iterator()`方法的时候创建的。`Iterator`对象知道如何遍历一个`List`，并且不同的`List`类型，返回的`Iterator`对象实现也是不同的，但总是具有最高的访问效率。

### iterator


`Iterator`对象有两个方法：`boolean hasNext()`判断是否有下一个元素，`E next()`返回下一个元素。因此，使用`Iterator`遍历`List`代码如下：

```java

import java.util.Iterator;
import java.util.List;
public class Main {
    public static void main(String[] args) {
        List<String> list = List.of("apple", "pear", "banana");
        for (Iterator<String> it = list.iterator(); it.hasNext(); ) {
            String s = it.next();
            System.out.println(s);
        }
    }
}
```

虽然但是，这是标准写法。

但前面使用ArrayList的时候，我们一般用的是 for each，当担这玩意儿内部也是调用的iterator。迭代器永远的神

只要实现了`Iterable`接口的集合类都可以直接用`for each`循环来遍历，Java编译器本身并不知道如何遍历集合对象，但它会自动把`for each`循环变成`Iterator`的调用，原因就在于`Iterable`接口定义了一个`Iterator<E> iterator()`方法，强迫集合类必须返回一个`Iterator`实例。

## List和Array的转换

前面也用不少了，静态方法，内部方法，都有

就是列表转换为数组

```java



public class Main {
    public static void main(String[] args) {
        List<String> list = List.of("apple", "pear", "banana");
        Object[] array = list.toArray();//会丢失类型信息
        for (Object s : array) {
            System.out.println(s);
        }

List<Integer> list = List.of(12, 34, 56);
        Integer[] array = list.toArray(new Integer[3]);
        for (Integer n : array) {
            System.out.println(n);
        }
Integer[] array = list.toArray(Integer[]::new);//这种方法说是是上面的一种优化也成
    }
}

```


反过来，把`Array`变为`List`就简单多了，通过`List.of(T...)`方法最简单：

```java
Integer[] array = { 1, 2, 3 };
List<Integer> list = List.of(array);
```

需要注意什么呢，那就是返回的List鬼知道是哪个实现子类，所以返回的是一个只读list，无法添加的那种


`List`还提供了`boolean contains(Object o)`方法来判断`List`是否包含某个指定元素。

此外，`int indexOf(Object o)`方法可以返回某个元素的索引，如果元素不存在，就返回`-1`。

### 编写equals

如何正确编写`equals()`方法？`equals()`方法要求我们必须满足以下条件：

* 自反性（Reflexive）：对于非`null`的`x`来说，`x.equals(x)`必须返回`true`；
* 对称性（Symmetric）：对于非`null`的`x`和`y`来说，如果`x.equals(y)`为`true`，则`y.equals(x)`也必须为`true`；
* 传递性（Transitive）：对于非`null`的`x`、`y`和`z`来说，如果`x.equals(y)`为`true`，`y.equals(z)`也为`true`，那么`x.equals(z)`也必须为`true`；
* 一致性（Consistent）：对于非`null`的`x`和`y`来说，只要`x`和`y`状态不变，则`x.equals(y)`总是一致地返回`true`或者`false`；
* 对`null`的比较：即`x.equals(null)`永远返回`false`。


总结一下`equals()`方法的正确编写方法：

1. 先确定实例“相等”的逻辑，即哪些字段相等，就认为实例相等；
2. 用`instanceof`判断传入的待比较的`Object`是不是当前类型，如果是，继续比较，否则，返回`false`；
3. 对引用类型用`Objects.equals()`比较，对基本类型直接用`==`比较。

使用`Objects.equals()`比较两个引用类型是否相等的目的是省去了判断`null`的麻烦。两个引用类型都是`null`时它们也是相等的。

如果不调用`List`的`contains()`、`indexOf()`这些方法，那么放入的元素就不需要实现`equals()`方法。

```
