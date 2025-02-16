package com.hspedu.jdbc;

import com.mysql.cj.jdbc.Driver;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Zhou Yu
 * @version 1.0
 * 分析java 连接mysql的5种方式
 */
public class JdbcConn {

    //方式一
    @Test
    public void connect01() throws SQLException {
        Driver driver = new com.mysql.cj.jdbc.Driver();

        String url = "jdbc:mysql://localhost:3306/hsp_db02?serverTimezone=UTC";

        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }

    //方式二
    @Test
    public void connect02() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        //使用反射加载Driver类 , 动态加载，更加的灵活，减少依赖性
        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        String url = "jdbc:mysql://localhost:3306/hsp_db02?serverTimezone=UTC";
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "123456");

        Connection connect = driver.connect(url, info);
        System.out.println("方式二=" + connect);

    }

    //方式三 使用DriverManager 替代 Driver 进行统一管理
    @Test
    public void connect03() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        //使用反射加载Driver
        Class<?> aClass = Class.forName("com.mysql.cj.jdbc.Driver");
        Driver driver = (Driver) aClass.newInstance();

        //创建url 和 user 和 password
        String url = "jdbc:mysql://localhost:3306/hsp_db02?serverTimezone=UTC";
        String user = "root";
        String password = "123456";

        DriverManager.registerDriver(driver);//注册Driver驱动

        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println("第三种方式=" + connection);

    }

    //方式四：使用Class.forName 自动完成注册驱动，简化代码
    //这种方式获取连接是使用的最多，推荐使用
    @Test
    public void connect04() throws ClassNotFoundException, SQLException {
        //使用反射加载Driver
        //在加载 Driver类时，完成注册
        /*
        源码:1. 静态代码块，在类加载时，会执行一次.
        2. DriverManager.registerDriver(new Driver());
        3. 因此注册driver的工作已经完成
        static {
                try {
                    DriverManager.registerDriver(new Driver());
                } catch (SQLException var1) {
                    throw new RuntimeException("Can't register driver!");
                }
            }
         */
        Class.forName("com.mysql.cj.jdbc.Driver");

        //创建url 和 user 和 password
        String url = "jdbc:mysql://localhost:3306/hsp_db02?serverTimezone=UTC";
        String user = "root";
        String password = "123456";

        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println("第四种方式=" + connection);
    }

    //方式5，在方式4的基础上改进，增加配置文件，让连接mysql更加灵活
    @Test
    public void connect05() throws IOException, ClassNotFoundException, SQLException {

        //通过Properties对象获取配置文件的信息
        Properties properties = new Properties();
        properties.load(new FileInputStream("src\\mysql.properties"));

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String driver = properties.getProperty("driver");
        String url = properties.getProperty("url");

        Class.forName(driver);//建议写上

        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println("方式五=" + connection);
    }
}
