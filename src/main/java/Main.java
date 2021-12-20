import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.annotation.Nonnull;


/**
 * @author zhouxinghang
 * @date 2021/12/3
 * https://openjdk.java.net/jeps/0  java 版本功能清单
 * https://javaalmanac.io/   对比查看 java 版本功能变化
 * https://dzone.com/articles/java-17-features-a-comparison-between-versions-8-a  介绍 java17 对比 java8 的变化
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("hello jdk17");
        varKeyword();
        record();
        switchExpressions();
        instanceofPatternMatching();
        sealedClass();
        textBlocks();
        optional();
        collection();
        ifacePrivateMethod();
    }

    // java 10
    private static void varKeyword() {
        var map = new HashMap<>();
        map.put("name", "java17");
        map.put("age", 17);
        map.put(11, 17); // 不加泛型限制，这里
        System.out.println(map);

        // 不能使用 var 关键字将 lambda 赋值给变量
        var nameList = new ArrayList<String>();
        nameList.add("idea");
        nameList.add("java17");

        boolean haveIdea = nameList.stream()
                .allMatch((@Nonnull var s) -> s.equals("idea")); // 可以在 lambda 表达式中使用 var
    }

    /**
     * java 14，代替 java bean，类似 Lombok’s @Value
     */
    private static void record() {
        var person = new Person("zxh", 25);
        System.out.println(person.name());
        System.out.println(person.age());
        System.out.println(person.hashCode());
        System.out.println(person.equals(new Person("zxh", 25)));
        System.out.println(person.toString());
        // todo json 序列化如何处理
    }

    /**
     * switch 表达式增强
     * java12预览，java14正式
     */
    private static void switchExpressions() {
        var dayOfWeek = LocalDateTime.now().getDayOfWeek();
        boolean freeDay = switch (dayOfWeek) {
            case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> false;
            case SATURDAY, SUNDAY -> true;
        };
        System.out.println(dayOfWeek.getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH) + " is freeDay: " + freeDay);

        // todo switch 强制转换
    }

    /**
     * java14 预览，java16 正式
     */
    private static void instanceofPatternMatching() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "java17");
        if (map instanceof HashMap hashMap && hashMap.containsKey("name")) {
            System.out.println("instanceof map:" + hashMap);
        }
    }

    // java13 不需要换行符
    private static void textBlocks() {
        String myWallOfText = """
                ______         _   _
                | ___ \\       | | (_)
                | |_/ / __ ___| |_ _ _   _ ___
                |  __/ '__/ _ \\ __| | | | / __|
                | |  | | |  __/ |_| | |_| \\__ \\
                \\_|  |_|  \\___|\\__|_|\\__,_|___/
                """;
        System.out.println(myWallOfText);

        String myPoem = """
                Roses are red, violets are blue - \
                Pretius makes the best software, that is always true
                """;
        System.out.println(myPoem);
    }

    // JDK 9/ JDK 10
    private static void optional() {
        Optional<Person> personOpt = Optional.of(new Person("devops", 20));
        /*if (personOpt.isPresent())
            System.out.println(personOpt.get());
        else
            System.out.println("Person not found");*/
        personOpt.ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Person not found")
        );

        /*
        if (personOpt.isPre sent())
            return personOpt.get();
        else
            throw new NoSuchElementException();
        */
        Optional<Person> personOpt2 = Optional.empty();
//        System.out.println(personOpt2.orElseThrow(() -> new RuntimeException("为空，抛出自定义异常")));
    }

    private static void collection() {
        // 返回不可变 list
        List<String> fruits = List.of("apple", "banana", "orange");
        // 返回不可变 map
        Map<Integer, String> numbers = Map.of(1, "one", 2,"two", 3, "three");
        System.out.println(fruits);
        System.out.println(numbers);
    }

    private static void ifacePrivateMethod() {
        ExampleInterface iface = new ExampleInterface() {
            @Override
            public void method1() {
                System.out.println("实现默认方法");
            }
        };
        iface.method1();
        iface.method2();
    }

    /**
     * java16预览，java17正式
     */
    private static void sealedClass() {
        Animal animal = new Dog();
        if (animal instanceof Cat cat) {
            cat.meow();
        } else if (animal instanceof Dog dog) {
            dog.woof();
        }
    }

    /**
     * jvm 层代码分析，让 npe 异常信息更加友好
     */
    private static void betterNpeMsg() {

    }

    /**
     * jdk self httpclient see: https://www.baeldung.com/java-9-http-client
     */
    private static void httpclient() {

    }

}

record Person(String name, Integer age) {
    public Person {
        if (name == null || age == null) {
            throw new IllegalArgumentException("invalid person properties");
        }
    }
}

// 只允许 Dog Cat 继承， 且 Dog Cat 必须继承
abstract sealed class Animal permits Dog,Cat {
}

final class Dog extends Animal {
    public void woof() {
        System.out.println("dog woof");
    }

}

final class Cat extends Animal  {
    public void meow() {
        System.out.println("cat meow");
    }
}

interface ExampleInterface {
    private void printMsg(String methodName) {
        System.out.println("Calling interface");
        System.out.println("Interface method: " + methodName);
    }

    default void method1() {
        printMsg("method1");
    }

    default void method2() {
        printMsg("method2");
    }
}