package com.lucasbpaixao.csvconsumer.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DaoFactory {
    @Value("#{environment['spring.datasource.url']}")
    private String url;
    @Value("#{environment['spring.datasource.username']}")
    private String username;
    @Value("#{environment['spring.datasource.password']}")
    private String password;

    public Connection connection() throws SQLException {
        System.out.println(this.url);
        System.out.println(this.username);
        System.out.println(this.password);

        return DriverManager.getConnection(url , username, password);
    }
}
