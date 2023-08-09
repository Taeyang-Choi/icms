package com.ogp.icms.global.util;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

@Slf4j
@Repository
public class SqlUtil {
    private final HikariDataSource dataSource;

    public SqlUtil(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String pQuery(String...args) {
        return pQuery(false, args);
    }

    public String pQuery(boolean returnNull, String... args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(args[0]);

            for (int i = 1; i < args.length; i++) {
                pstmt.setString(i, ((String)args[i]));
            }

            rs = pstmt.executeQuery();

            if (!rs.next()) return (returnNull)? null :  "{}";
            return rs.getString(1);

        } catch(SQLException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    public String pQueryList(String...args) {
        return pQueryList(1, args);
    }

    public String pQueryList(int size, String...args) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(args[0]);

            for (int i = 1; i < args.length; i++) {
                pstmt.setString(i, ((String)args[i]));
            }
            rs = pstmt.executeQuery();

            String list = "";
            int cnt = 0;
            while (rs.next()) {
                if (cnt++ > 0) list += ",";

                if(size == 1) {
                    list += rs.getString(1);
                }
                else {
                    String temp = "";
                    for (int i = 1; i <= size; i++) {
                        if(i > 1) temp += ",";
                        temp += rs.getString(i);
                    }
                    temp = "[" + temp + "]";
                    list += temp;
                }
            }
            return "[" + list + "]";
        } catch(SQLException ex) {
            ex.printStackTrace();
            return "[]";
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    public void pUpdate(String...args) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(args[0]);

            for (int i = 1; i < args.length; i++) {
                pstmt.setString(i, ((String)args[i]));
            }

            pstmt.executeUpdate();

        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally {
            JdbcUtils.closeStatement(pstmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    public Connection getConnection() {
        try {
            Connection connection = dataSource.getConnection();
            //log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
