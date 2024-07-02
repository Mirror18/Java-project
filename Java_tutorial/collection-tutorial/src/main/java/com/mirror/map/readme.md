# map

首先先要明白，数组，列表，他们的不足之处。

那就是index和value，也就是索引和元素，也就是数组下表和内容。如果想查特定内容怎么办，这玩意儿又不是index，可以直接计算出物理位置，那么唯一的做法就是全部循环一遍，然后就是一个一个比对呗。那这玩意儿就太特么蛋疼了

也就是说，数组对于查找特定索引的还好说，直接用数组下标表示就行。对于元素内容来说就是一团乱麻。

那么这就想办法优化，给出内容就立马查找到对应的值，或许上面写的有点乱，不过对于键值对来说，本身这俩就是可以互换的，所以稍显混乱，有时候需要根据值内容查找到特定的元素，根据元素查找到特定的值

或者换句话说，数组其实就是数组下表和对应元素组成的键值对，但是有时候用不到数组下表这个东西，想换一种东西作为排序方式，放入方式。这种。

于是就有了map

即通过一个键去查询对应的值。使用`List`来实现存在效率非常低的问题，因为平均需要扫描一半的元素才能确定，而`Map`这种键值（key-value）映射表的数据结构，作用就是能高效通过`key`快速查找`value`（元素）。

`Map<K, V>`是一种键-值映射表，当我们调用`put(K key, V value)`方法时，就把`key`和`value`做了映射并放入`Map`。当我们调用`V get(K key)`时，就可以通过`key`获取到对应的`value`。如果`key`不存在，则返回`null`。和`List`类似，`Map`也是一个接口，最常用的实现类是`HashMap`。

如果只是想查询某个`key`是否存在，可以调用`boolean containsKey(K key)`方法。

如果我们在存储`Map`映射关系的时候，对同一个key调用两次`put()`方法，分别放入不同的`value`，重复放入`key-value`并不会有任何问题，但是一个`key`只能关联一个`value`。就是会覆盖，所以这个设定才会在set之前。

虽然`key`不能重复，但`value`是可以重复的：

### 遍历Map

对`Map`来说，要遍历`key`可以使用`for each`循环遍历`Map`实例的`keySet()`方法返回的`Set`集合，它包含不重复的`key`的集合：

同时遍历`key`和`value`可以使用`for each`循环遍历`Map`对象的`entrySet()`集合，它包含每一个`key-value`映射：

`Map`和`List`不同的是，`Map`存储的是`key-value`的映射关系，并且，它 *不保证顺序* 。在遍历的时候，遍历的顺序既不一定是`put()`时放入的`key`的顺序，也不一定是`key`的排序顺序。使用`Map`时，任何依赖顺序的逻辑都是不可靠的。

甚至对于不同的JDK版本，相同的代码遍历的输出顺序都是不同的！因为hashcode的方式不一样。

另外也曝出key是怎么存储的，就是通过计算哈希值来确定位置

另外map可以作为缓存，嗯，在查看一些源码的时候用的挺多。

正确使用`Map`必须保证：

1. 作为`key`的对象必须正确覆写`equals()`方法，相等的两个`key`实例调用`equals()`必须返回`true`；
2. 作为`key`的对象还必须正确覆写`hashCode()`方法，且`hashCode()`方法要严格遵循以下规范：

* 如果两个对象相等，则两个对象的`hashCode()`必须相等；
* 如果两个对象不相等，则两个对象的`hashCode()`尽量不要相等。

即对应两个实例`a`和`b`：

* 如果`a`和`b`相等，那么`a.equals(b)`一定为`true`，则`a.hashCode()`必须等于`b.hashCode()`；
* 如果`a`和`b`不相等，那么`a.equals(b)`一定为`false`，则`a.hashCode()`和`b.hashCode()`尽量不要相等。

上述第一条规范是正确性，必须保证实现，否则`HashMap`不能正常工作。

而第二条如果尽量满足，则可以保证查询效率，因为不同的对象，如果返回相同的`hashCode()`，会造成`Map`内部存储冲突，使存取的效率下降。关于这一个，则是计算hashcode的时候一个蛋疼的玩意儿。不过这里不需要自己仔细去考虑罢了

编写`equals()`和`hashCode()`遵循的原则是：

`equals()`用到的用于比较的每一个字段，都必须在`hashCode()`中用于计算；`equals()`中没有使用到的字段，绝不可放在`hashCode()`中计算。

另外注意，对于放入`HashMap`的`value`对象，没有任何要求。

`HashMap`初始化时默认的数组大小只有16，

添加超过一定数量的`key-value`时，`HashMap`会在内部自动扩容，每次扩容一倍，即长度为16的数组扩展为长度32，相应地，需要重新确定`hashCode()`计算的索引位置

由于扩容会导致重新分布已有的`key-value`，所以，频繁扩容对`HashMap`的性能影响很大。如果我们确定要使用一个容量为`10000`个`key-value`的`HashMap`，更好的方式是创建`HashMap`时就指定容量：

```
Map<String, Integer> map = new HashMap<>(10000);
```

虽然指定容量是`10000`，但`HashMap`内部的数组长度总是2 ^n^ ，因此，实际数组长度被初始化为比`10000`大的`16384`（2 ^14^ ）

## EnumMap

如果作为key的对象是`enum`类型，那么，还可以使用Java集合库提供的一种`EnumMap`，它在内部以一个非常紧凑的数组存储value，并且根据`enum`类型的key直接定位到内部数组的索引，并不需要计算`hashCode()`，不但效率最高，而且没有额外的空间浪费。

使用`EnumMap`的时候，我们总是用`Map`接口来引用它，因此，实际上把`HashMap`和`EnumMap`互换，在客户端看来没有任何区别。


## TreeMap

首先先明白的是，hashmap是一个无序的map，也就是key是无序的。因为key计算方式的问题。但是如果想要得到一个有序的sortedMap呢，那就用这个做接口，treeMap做实现类，

实话说到这里是不是觉得这很扯犊子。但其实就是数组，来哟列表这些，他们只有数字下标和内容做对应，无法建立单独的自己的关键字排序。要不然就是要两张表联合存储了。所以有了这玩意儿。

所有的map都是一样的操作。但是treeMap有个要求，那就是key的类必须实现Comparable接口。如果没有就要传入一个自定义算法，昂，跟排序写的时候一个样


在两个Key相等时，必须返回`0`

## Properties

就是配置文件，用键值对保存的，前面打算获取到当前jdk版本信息的时候干过这事，就是后续给中断了，因为获取到的类型不是整数，有其他方法进行获取，就是想要获取到整数型的时候，不行。


所以这里详细讲下什么是属性

说白了就是一些系统信息，还有一些常量之类的。用于获取当前是在什么样的环境，在不同的环境下有不同的程序执行，所以才有这个。

按理来说这个项目中也要有`setting.properties`这个文件，用于使用第三方库之类的，但是现在用不到了，一般也是xml文档。但是不存在不代表不重要，而是说我们自己的配置信息不往那里面放。但还是要学会获取和设置的

现在都是@Value注解配合着xml文档使用，spring也是这样干的。但是这个项目的目标是要做到spring cloud，中间有很多的新技术出现了


用`Properties`读取配置文件，一共有三步：

1. 创建`Properties`实例；
2. 调用`load()`读取文件；
3. 调用`getProperty()`获取配置。

调用`getProperty()`获取配置时，如果key不存在，将返回`null`。我们还可以提供一个默认值，这样，当key不存在的时候，就返回默认值。


可以从文件系统读取这个`.properties`文件：

```
String f = "setting.properties";
Properties props = new Properties();
props.load(new java.io.FileInputStream(f));

String filepath = props.getProperty("last_open_file");
String interval = props.getProperty("auto_save_interval", "120");
```


也可以从classpath读取`.properties`文件，因为`load(InputStream)`方法接收一个`InputStream`实例，表示一个字节流，它不一定是文件流，也可以是从jar包中读取的资源流：

```
Properties props = new Properties();
props.load(getClass().getResourceAsStream("/common/setting.properties"));
```


下方代码涉及到iostream，不过也是问题不大，因为这玩意儿都是和c语言的一个逻辑，所以这么多编程语言，都是用的时候找个示例代码抄着写的。

```java
import java.io.*;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException {
        String settings = "# test" + "\n" + "course=Java" + "\n" + "last_open_date=2019-08-07T12:35:01";
        ByteArrayInputStream input = new ByteArrayInputStream(settings.getBytes("UTF-8"));
        Properties props = new Properties();
        props.load(input);

        System.out.println("course: " + props.getProperty("course"));
        System.out.println("last_open_date: " + props.getProperty("last_open_date"));
        System.out.println("last_open_file: " + props.getProperty("last_open_file"));
        System.out.println("auto_save: " + props.getProperty("auto_save", "60"));
    }
}

```



`Properties`设计的目的是存储`String`类型的key－value，但`Properties`实际上是从`Hashtable`派生的，它的设计实际上是有问题的，但是为了保持兼容性，现在已经没法修改了。除了`getProperty()`和`setProperty()`方法外，还有从`Hashtable`继承下来的`get()`和`put()`方法，这些方法的参数签名是`Object`，我们在使用`Properties`的时候，不要去调用这些从`Hashtable`继承下来的方法。



### 写入配置文件

如果通过`setProperty()`修改了`Properties`实例，可以把配置写入文件，以便下次启动时获得最新配置。写入配置文件使用`store()`方法：

```
Properties props = new Properties();
props.setProperty("url", "http://www.liaoxuefeng.com");
props.setProperty("language", "Java");
props.store(new FileOutputStream("C:\\conf\\setting.properties"), "这是写入的properties注释");
```

### 编码

早期版本的Java规定`.properties`文件编码是ASCII编码（ISO8859-1），如果涉及到中文就必须用`name=\u4e2d\u6587`来表示，非常别扭。从JDK9开始，Java的`.properties`文件可以使用UTF-8编码了。

不过，需要注意的是，由于`load(InputStream)`默认总是以ASCII编码读取字节流，所以会导致读到乱码。我们需要用另一个重载方法`load(Reader)`读取：

```
Properties props = new Properties();
props.load(new FileReader("settings.properties", StandardCharsets.UTF_8));
```

就可以正常读取中文。`InputStream`和`Reader`的区别是一个是字节流，一个是字符流。字符流在内存中已经以`char`类型表示了，不涉及编码问题。



## 使用Set

我们知道，`Map`用于存储key-value的映射，对于充当key的对象，是不能重复的，并且，不但需要正确覆写`equals()`方法，还要正确覆写`hashCode()`方法。

如果我们只需要存储不重复的key，并不需要存储映射的value，那么就可以使用`Set`。

`Set`用于存储不重复的元素集合，它主要提供以下几个方法：

* 将元素添加进`Set<E>`：`boolean add(E e)`
* 将元素从`Set<E>`删除：`boolean remove(Object e)`
* 判断是否包含元素：`boolean contains(Object e)`

我们来看几个简单的例子：

```

import java.util.*;
public class Main {
    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        System.out.println(set.add("abc")); // true
        System.out.println(set.add("xyz")); // true
        System.out.println(set.add("xyz")); // false，添加失败，因为元素已存在
        System.out.println(set.contains("xyz")); // true，元素存在
        System.out.println(set.contains("XYZ")); // false，元素不存在
        System.out.println(set.remove("hello")); // false，删除失败，因为元素不存在
        System.out.println(set.size()); // 2，一共两个元素
    }
}
```

Run`Set`实际上相当于只存储key、不存储value的`Map`。我们经常用`Set`用于去除重复元素。

因为放入`Set`的元素和`Map`的key类似，都要正确实现`equals()`和`hashCode()`方法，否则该元素无法正确地放入`Set`。


最常用的`Set`实现类是`HashSet`，实际上，`HashSet`仅仅是对`HashMap`的一个简单封装，它的核心代码如下：

```
public class HashSet<E> implements Set<E> {
    // 持有一个HashMap:
    private HashMap<E, Object> map = new HashMap<>();

    // 放入HashMap的value:
    private static final Object PRESENT = new Object();

    public boolean add(E e) {
        return map.put(e, PRESENT) == null;
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public boolean remove(Object o) {
        return map.remove(o) == PRESENT;
    }
}
```

`Set`接口并不保证有序，而`SortedSet`接口则保证元素是有序的：

* `HashSet`是无序的，因为它实现了`Set`接口，并没有实现`SortedSet`接口；
* `TreeSet`是有序的，因为它实现了`SortedSet`接口。

## queue

队列（`Queue`）是一种经常使用的集合。`Queue`实际上是实现了一个先进先出（FIFO：First In First Out）的有序表。它和`List`的区别在于，`List`可以在任意位置添加和删除元素，而`Queue`只有两个操作：

* 把元素添加到队列末尾；
* 从队列头部取出元素。


在Java的标准库中，队列接口`Queue`定义了以下几个方法：

* `int size()`：获取队列长度；
* `boolean add(E)`/`boolean offer(E)`：添加元素到队尾；
* `E remove()`/`E poll()`：获取队首元素并从队列中删除；
* `E element()`/`E peek()`：获取队首元素但并不从队列中删除。

对于具体的实现类，有的Queue有最大队列长度限制，有的Queue没有。注意到添加、删除和获取队列元素总是有两个方法，这是因为在添加或获取元素失败时，这两个方法的行为是不同的。我们用一个表格总结如下：


|                    | throw Exception | 返回false或null    |
| -------------------- | ----------------- | -------------------- |
| 添加元素到队尾     | add(E e)        | boolean offer(E e) |
| 取队首元素并删除   | E remove()      | E poll()           |
| 取队首元素但不删除 | E element()     | E peek()           |


`LinkedList`即实现了`List`接口，又实现了`Queue`接口，但是，在使用的时候，如果我们把它当作List，就获取List的引用，如果我们把它当作Queue，就获取Queue的引用：

```
// 这是一个List:
List<String> list = new LinkedList<>();
// 这是一个Queue:
Queue<String> queue = new LinkedList<>();
```

始终按照面向抽象编程的原则编写代码，可以大大提高代码的质量。


所以也解释了为什么上面队列的操作会有两种方法，因为队列虽然是作为一种数据结构

但是更像是人为从其他的基础类型中加上限制条件生成的。目的就是满足一些概念划分，不管是queue，还是stack，还是堆，都是在这三种类型上添加一些条件延伸出来的概念，用于更好的交流。三种基础类型是set,map,list。数组array更像是简单的list，但是这俩还是不能放到一起比较的。不能说长得像就说是一个东西，或者一个类型的东西。


## PriorityQueue

`PriorityQueue`和`Queue`的区别在于，它的出队顺序与元素的优先级有关，对`PriorityQueue`调用`remove()`或`poll()`方法，返回的总是优先级最高的元素。


因此，放入`PriorityQueue`的元素，必须实现`Comparable`接口，`PriorityQueue`会根据元素的排序顺序决定出队的优先级。

如果我们要放入的元素并没有实现`Comparable`接口怎么办？`PriorityQueue`允许我们提供一个`Comparator`对象来判断两个元素的顺序。我们以银行排队业务为例，实现一个`PriorityQueue`：

```
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
public class Main {
    public static void main(String[] args) {
        Queue<User> q = new PriorityQueue<>(new UserComparator());
        // 添加3个元素到队列:
        q.offer(new User("Bob", "A1"));
        q.offer(new User("Alice", "A2"));
        q.offer(new User("Boss", "V1"));
        System.out.println(q.poll()); // Boss/V1
        System.out.println(q.poll()); // Bob/A1
        System.out.println(q.poll()); // Alice/A2
        System.out.println(q.poll()); // null,因为队列为空
    }
}

class UserComparator implements Comparator<User> {
    public int compare(User u1, User u2) {
        if (u1.number.charAt(0) == u2.number.charAt(0)) {
            // 如果两人的号都是A开头或者都是V开头,比较号的大小:
            return u1.number.compareTo(u2.number);
        }
        if (u1.number.charAt(0) == 'V') {
            // u1的号码是V开头,优先级高:
            return -1;
        } else {
            return 1;
        }
    }
}

class User {
    public final String name;
    public final String number;

    public User(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String toString() {
        return name + "/" + number;
    }
}

```


#### 使用Deque

我们知道，`Queue`是队列，只能一头进，另一头出。

如果把条件放松一下，允许两头都进，两头都出，这种队列叫双端队列（Double Ended Queue），学名`Deque`。

Java集合提供了接口`Deque`来实现一个双端队列，它的功能是：

* 既可以添加到队尾，也可以添加到队首；
* 既可以从队首获取，又可以从队尾获取。

我们来比较一下`Queue`和`Deque`出队和入队的方法：


|                    | Queue                  | Deque                           |
| -------------------- | ------------------------ | --------------------------------- |
| 添加元素到队尾     | add(E e) / offer(E e)  | addLast(E e) / offerLast(E e)   |
| 取队首元素并删除   | E remove() / E poll()  | E removeFirst() / E pollFirst() |
| 取队首元素但不删除 | E element() / E peek() | E getFirst() / E peekFirst()    |
| 添加元素到队首     | 无                     | addFirst(E e) / offerFirst(E e) |
| 取队尾元素并删除   | 无                     | E removeLast() / E pollLast()   |
| 取队尾元素但不删除 | 无                     | E getLast() / E peekLast()      |

对于添加元素到队尾的操作，`Queue`提供了`add()`/`offer()`方法，而`Deque`提供了`addLast()`/`offerLast()`方法。添加元素到队首、取队尾元素的操作在`Queue`中不存在，在`Deque`中由`addFirst()`/`removeLast()`等方法提供。


至于实现方式。昂，还是linkedlist.

所以猜猜看为什么会有两种方法、嗯，一个道理，让他是啥就是啥

## stack


`Stack`只有入栈和出栈的操作：

* 把元素压栈：`push(E)`；
* 把栈顶的元素“弹出”：`pop()`；
* 取栈顶元素但不弹出：`peek()`。

在Java中，我们用`Deque`可以实现`Stack`的功能：

* 把元素压栈：`push(E)`/`addFirst(E)`；
* 把栈顶的元素“弹出”：`pop()`/`removeFirst()`；
* 取栈顶元素但不弹出：`peek()`/`peekFirst()`。

为什么Java的集合类没有单独的`Stack`接口呢？因为有个遗留类名字就叫`Stack`，出于兼容性考虑，所以没办法创建`Stack`接口，只能用`Deque`接口来“模拟”一个`Stack`了。

至于为什么会有遗留类，那是因为stack的用途很多，包括机组大部分使用的就是stack，递归啊，程序计数器啊，大部分的原理就是这玩意儿。只不过给普通人留的就剩模拟的，至于为什么能模拟。昂，欢迎选修数据结构。学这玩意儿可是倒了八辈子血霉

## iterator


如果我们自己编写了一个集合类，想要使用`for each`循环，只需满足以下条件：

* 集合类实现`Iterable`接口，该接口要求返回一个`Iterator`对象；
* 用`Iterator`对象迭代集合内部数据。

这里的关键在于，集合类通过调用`iterator()`方法，返回一个`Iterator`对象，这个对象必须自己知道如何遍历该集合。

一个简单的`Iterator`示例如下，它总是以倒序遍历集合：

```
// Iterator
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ReverseList<String> rlist = new ReverseList<>();
        rlist.add("Apple");
        rlist.add("Orange");
        rlist.add("Pear");
        for (String s : rlist) {
            System.out.println(s);
        }
    }
}

class ReverseList<T> implements Iterable<T> {

    private List<T> list = new ArrayList<>();

    public void add(T t) {
        list.add(t);
    }

    @Override
    public Iterator<T> iterator() {
        return new ReverseIterator(list.size());
    }

    class ReverseIterator implements Iterator<T> {
        int index;

        ReverseIterator(int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index > 0;
        }

        @Override
        public T next() {
            index--;
            return ReverseList.this.list.get(index);
        }
    }
}

```

Run虽然`ReverseList`和`ReverseIterator`的实现类稍微比较复杂，但是，注意到这是底层集合库，只需编写一次。而调用方则完全按`for each`循环编写代码，根本不需要知道集合内部的存储逻辑和遍历逻辑。

在编写`Iterator`的时候，我们通常可以用一个内部类来实现`Iterator`接口，这个内部类可以直接访问对应的外部类的所有字段和方法。例如，上述代码中，内部类`ReverseIterator`可以用`ReverseList.this`获得当前外部类的`this`引用，然后，通过这个`this`引用就可以访问`ReverseList`的所有字段和方法

## Collections

`Collections`是JDK提供的工具类，同样位于`java.util`包中。它提供了一系列静态方法，能更方便地操作各种集合。

`addAll()`方法可以给一个`Collection`类型的集合添加若干元素。因为方法签名是`Collection`，所以我们可以传入`List`，`Set`等各种集合类型。


`Collections`提供了一系列方法来创建空集合：

* 创建空List：`List<T> emptyList()`
* 创建空Map：`Map<K, V> emptyMap()`
* 创建空Set：`Set<T> emptySet()`

要注意到返回的空集合是不可变集合，无法向其中添加或删除元素。

此外，也可以用各个集合接口提供的`of(T...)`方法创建空集合。例如，以下创建空`List`的两个方法是等价的：

```
List<String> list1 = List.of();
List<String> list2 = Collections.emptyList();
```


### 创建单元素集合

`Collections`提供了一系列方法来创建一个单元素集合：

* 创建一个元素的List：`List<T> singletonList(T o)`
* 创建一个元素的Map：`Map<K, V> singletonMap(K key, V value)`
* 创建一个元素的Set：`Set<T> singleton(T o)`

要注意到返回的单元素集合也是不可变集合，无法向其中添加或删除元素。

此外，也可以用各个集合接口提供的`of(T...)`方法创建单元素集合。例如，以下创建单元素`List`的两个方法是等价的：

```
List<String> list1 = List.of("apple");
List<String> list2 = Collections.singletonList("apple");
```

实际上，使用`List.of(T...)`更方便，因为它既可以创建空集合，也可以创建单元素集合，还可以创建任意个元素的集合：

```
List<String> list1 = List.of(); // empty list
List<String> list2 = List.of("apple"); // 1 element
List<String> list3 = List.of("apple", "pear"); // 2 elements
List<String> list4 = List.of("apple", "pear", "orange"); // 3 elements
```


### 不可变集合

`Collections`还提供了一组方法把可变集合封装成不可变集合：

* 封装成不可变List：`List<T> unmodifiableList(List<? extends T> list)`
* 封装成不可变Set：`Set<T> unmodifiableSet(Set<? extends T> set)`
* 封装成不可变Map：`Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> m)`



### 线程安全集合

`Collections`还提供了一组方法，可以把线程不安全的集合变为线程安全的集合：

* 变为线程安全的List：`List<T> synchronizedList(List<T> list)`
* 变为线程安全的Set：`Set<T> synchronizedSet(Set<T> s)`
* 变为线程安全的Map：`Map<K,V> synchronizedMap(Map<K,V> m)`

多线程的概念我们会在后面讲。因为从Java 5开始，引入了更高效的并发集合类，所以上述这几个同步方法已经没有什么用了。



在 Java 中，可变集合和不可变集合是集合的两种主要类型，它们的主要区别在于是否允许在创建后对集合进行修改。

### 可变集合 (Mutable Collection)

可变集合是指可以在创建后进行修改的集合。你可以添加、删除或更新集合中的元素。Java 中的标准集合类（如 `ArrayList`, `HashSet`, `HashMap`）都是可变集合的例子。

#### 示例代码

<pre><div class="dark bg-gray-950 rounded-md border-[0.5px] border-token-border-medium"><div class="flex items-center relative text-token-text-secondary bg-token-main-surface-secondary px-4 py-2 text-xs font-sans justify-between rounded-t-md"><span>java</span><div class="flex items-center"><span class="" data-state="closed"><button class="flex gap-1 items-center"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24" class="icon-sm"><path fill="currentColor" fill-rule="evenodd" d="M7 5a3 3 0 0 1 3-3h9a3 3 0 0 1 3 3v9a3 3 0 0 1-3 3h-2v2a3 3 0 0 1-3 3H5a3 3 0 0 1-3-3v-9a3 3 0 0 1 3-3h2zm2 2h5a3 3 0 0 1 3 3v5h2a1 1 0 0 0 1-1V5a1 1 0 0 0-1-1h-9a1 1 0 0 0-1 1zM5 9a1 1 0 0 0-1 1v9a1 1 0 0 0 1 1h9a1 1 0 0 0 1-1v-9a1 1 0 0 0-1-1z" clip-rule="evenodd"></path></svg>复制代码</button></span></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="!whitespace-pre hljs language-java">import java.util.ArrayList;
import java.util.List;

public class MutableCollectionExample {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");

        System.out.println("Initial List: " + list);

        list.remove("two");
        System.out.println("After Removal: " + list);

        list.set(1, "new three");
        System.out.println("After Update: " + list);
    }
}
</code></div></div></pre>

输出：

<pre><div class="dark bg-gray-950 rounded-md border-[0.5px] border-token-border-medium"><div class="flex items-center relative text-token-text-secondary bg-token-main-surface-secondary px-4 py-2 text-xs font-sans justify-between rounded-t-md"><span>sql</span><div class="flex items-center"><span class="" data-state="closed"><button class="flex gap-1 items-center"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24" class="icon-sm"><path fill="currentColor" fill-rule="evenodd" d="M7 5a3 3 0 0 1 3-3h9a3 3 0 0 1 3 3v9a3 3 0 0 1-3 3h-2v2a3 3 0 0 1-3 3H5a3 3 0 0 1-3-3v-9a3 3 0 0 1 3-3h2zm2 2h5a3 3 0 0 1 3 3v5h2a1 1 0 0 0 1-1V5a1 1 0 0 0-1-1h-9a1 1 0 0 0-1 1zM5 9a1 1 0 0 0-1 1v9a1 1 0 0 0 1 1h9a1 1 0 0 0 1-1v-9a1 1 0 0 0-1-1z" clip-rule="evenodd"></path></svg>复制代码</button></span></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="!whitespace-pre hljs language-sql">Initial List: [one, two, three]
After Removal: [one]
After Update: [one, new three]
</code></div></div></pre>

### 不可变集合 (Immutable Collection)

不可变集合是指在创建后不能进行修改的集合。这意味着一旦集合被创建，就不能添加、删除或更新集合中的元素。不可变集合通常用于确保数据不变性，防止在多线程环境中出现并发修改问题。

Java 9 引入了 `List.of()`, `Set.of()`, `Map.of()` 等方法来创建不可变集合。Guava 库也提供了类似的功能。

#### 示例代码

<pre><div class="dark bg-gray-950 rounded-md border-[0.5px] border-token-border-medium"><div class="flex items-center relative text-token-text-secondary bg-token-main-surface-secondary px-4 py-2 text-xs font-sans justify-between rounded-t-md"><span>java</span><div class="flex items-center"><span class="" data-state="closed"><button class="flex gap-1 items-center"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24" class="icon-sm"><path fill="currentColor" fill-rule="evenodd" d="M7 5a3 3 0 0 1 3-3h9a3 3 0 0 1 3 3v9a3 3 0 0 1-3 3h-2v2a3 3 0 0 1-3 3H5a3 3 0 0 1-3-3v-9a3 3 0 0 1 3-3h2zm2 2h5a3 3 0 0 1 3 3v5h2a1 1 0 0 0 1-1V5a1 1 0 0 0-1-1h-9a1 1 0 0 0-1 1zM5 9a1 1 0 0 0-1 1v9a1 1 0 0 0 1 1h9a1 1 0 0 0 1-1v-9a1 1 0 0 0-1-1z" clip-rule="evenodd"></path></svg>复制代码</button></span></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="!whitespace-pre hljs language-java">import java.util.List;

public class ImmutableCollectionExample {
    public static void main(String[] args) {
        List<String> list = List.of("one", "two", "three");

        System.out.println("Immutable List: " + list);

        // 尝试修改集合会抛出 UnsupportedOperationException
        try {
            list.add("four");
        } catch (UnsupportedOperationException e) {
            System.out.println("Attempted to modify immutable list: " + e);
        }
    }
}
</code></div></div></pre>

输出：

<pre><div class="dark bg-gray-950 rounded-md border-[0.5px] border-token-border-medium"><div class="flex items-center relative text-token-text-secondary bg-token-main-surface-secondary px-4 py-2 text-xs font-sans justify-between rounded-t-md"><span>yaml</span><div class="flex items-center"><span class="" data-state="closed"><button class="flex gap-1 items-center"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24" class="icon-sm"><path fill="currentColor" fill-rule="evenodd" d="M7 5a3 3 0 0 1 3-3h9a3 3 0 0 1 3 3v9a3 3 0 0 1-3 3h-2v2a3 3 0 0 1-3 3H5a3 3 0 0 1-3-3v-9a3 3 0 0 1 3-3h2zm2 2h5a3 3 0 0 1 3 3v5h2a1 1 0 0 0 1-1V5a1 1 0 0 0-1-1h-9a1 1 0 0 0-1 1zM5 9a1 1 0 0 0-1 1v9a1 1 0 0 0 1 1h9a1 1 0 0 0 1-1v-9a1 1 0 0 0-1-1z" clip-rule="evenodd"></path></svg>复制代码</button></span></div></div><div class="overflow-y-auto p-4" dir="ltr"><code class="!whitespace-pre hljs language-yaml">Immutable List: [one, two, three]
Attempted to modify immutable list: java.lang.UnsupportedOperationException
</code></div></div></pre>

### 可变集合与不可变集合的对比


| 特性     | 可变集合                              | 不可变集合                                   |
| ---------- | --------------------------------------- | ---------------------------------------------- |
| 修改元素 | 允许                                  | 不允许                                       |
| 线程安全 | 不是线程安全，需要手动同步            | 线程安全，创建后不可修改                     |
| 性能     | 一般来说性能较好，因为可以直接修改    | 性能可能较好，因为无需考虑并发修改问题       |
| 用途     | 用于需要频繁修改的集合                | 用于需要确保数据不变性或多线程访问的集合     |
| 示例     | `ArrayList`,`HashSet`,`HashMap`       | `List.of()`,`Set.of()`,`Map.of()`            |
| 创建方式 | `new ArrayList<>()`,`new HashSet<>()` | `List.of()`,`Collections.unmodifiableList()` |

### 结论

* **可变集合**适用于需要对集合进行频繁修改的场景。
* **不可变集合**适用于需要保证数据不变性或在多线程环境中共享数据的场景。

通过选择适当的集合类型，可以更好地满足不同应用场景的需求，提高代码的安全性和可维护性。
