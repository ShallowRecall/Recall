package com.hspedu.jdbc.datasource;

import com.hspedu.jdbc.utils.JDBCUtils;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

/**
 * @author Zhou Yu
 * @version 1.0
 */
public class ConQuestion {

    //代码 连接数据库5000次

    @Test
    public void testCon() {
        //看看连接-关闭 connection 会耗用多久
        long start = System.currentTimeMillis();
        System.out.println("开始连接...");
        for (int i = 0; i < 5000; i++) {
            //使用传统的jdbc方式，得到连接
            Connection connection = JDBCUtils.getConnection();
            //....

            //关闭
            JDBCUtils.close(null, null, connection);
        }
        long end = System.currentTimeMillis();

        System.out.println("传统方式5000次 耗时：" + (end - start));
    }
}
