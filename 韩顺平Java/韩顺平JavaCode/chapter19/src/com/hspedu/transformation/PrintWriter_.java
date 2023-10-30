package com.hspedu.transformation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Zhou Yu
 * @version 1.0
 * 演示 PrintWriter 使用方式
 */
public class PrintWriter_ {
    public static void main(String[] args) throws IOException {


//        PrintWriter printWriter = new PrintWriter(System.out);

        PrintWriter printWriter = new PrintWriter(new FileWriter("src/textFiles/f2.txt"));
        printWriter.print("hi,北京你好~");
        printWriter.close();//flush + 关闭
    }
}
