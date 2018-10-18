package com.zeling.wa.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

/**
 * h2连接池
 * 
 * @author chenbd 2018年10月11日
 */
public class ConnectionPool {
	
    private JdbcConnectionPool jdbcCP = null;

    private ConnectionPool() {
        String dbPath ="./zeling/h2";
        jdbcCP = JdbcConnectionPool.create("jdbc:h2:" + dbPath, "zeling", "zeling");
        jdbcCP.setMaxConnections(50);
    }
    
    private static class Singleton {
    	private final static ConnectionPool INSTANCE = new ConnectionPool();
    }

    public static ConnectionPool getInstance() {
    	return Singleton.INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return jdbcCP.getConnection();
    }
}