package org.example.biconsumer;

import java.util.function.Consumer;

public class BiConsumerTest {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("sb字符串后面将会跟随####");
        // 声明函数对象 consumer
        Consumer<StringBuilder> consumer = (str) -> str.append("sdfj");
        System.out.println(sb.toString());
        // 调用Consumer.accept()方法接收参数
        consumer.accept(sb);
        System.out.println(sb.toString());
    }
}
