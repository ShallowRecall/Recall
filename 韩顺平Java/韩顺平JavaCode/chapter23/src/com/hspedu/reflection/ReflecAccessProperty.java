package com.hspedu.reflection;

import java.lang.reflect.Field;

/**
 * @author ZhouYu
 * @version 1.0
 * 演示反射操作属性
 */
public class ReflecAccessProperty {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {

        //1. 得到Student类对应的 Class对象
        Class<?> stuClass = Class.forName("com.hspedu.reflection.Student");
        //2. 创建一个对象
        Object o = stuClass.newInstance();//o 的运行类型就是Student
//        System.out.println(o.getClass());//Student
        //3. 使用反射得到age 属性对象
        Field age = stuClass.getField("age");
        age.set(o,88);//通过反射来操作属性
        System.out.println(o);
        System.out.println(age.get(o));//返回age属性的值

        //4. 使用反射操作name 属性
        Field name = stuClass.getDeclaredField("name");
        //对name 进行爆破，可以操作private 属性
        name.setAccessible(true);
        //name.set(o,"老韩");
        name.set(null,"老韩~");//因为name是static属性，因此 o 也可以写成null
        System.out.println(o);
        System.out.println(name.get(o)); //获取属性值
        System.out.println(name.get(null));//获取属性值,要求name是static
    }
}


class Student {//类
    public int age;
    private static String name;

    public Student() {

    }

    public String toString() {
        return "Student [age=" + age + ", name=" + name + "]";
    }
}