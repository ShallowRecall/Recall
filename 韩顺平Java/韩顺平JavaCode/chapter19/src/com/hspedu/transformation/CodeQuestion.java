package com.hspedu.transformation;

import java.io.*;

/**
 * @author ZhouYu
 * @version 1.0
 * 看一个中文乱码问题
 */
public class CodeQuestion {
    public static void main(String[] args) throws IOException {
        //读取src/textFiles/note.txt 文件到程序
        //思路
        //1. 创建字符输入流  BufferedReader [处理流]
        //2. 使用 BufferedReader 对象读取note.txt
        //3. 默认情况下，读取文件是按照 utf-8 编码读取

        String filePath = "src/textFiles/note.txt";
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String s = br.readLine();
        System.out.println("读取到的内容：" + s);
        br.close();

//        InputStreamReader
//        OutputStreamWriter
    }
}
