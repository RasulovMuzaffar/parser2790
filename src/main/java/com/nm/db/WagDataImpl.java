package com.nm.db;

import com.nm.pojo.Operations;
import com.nm.pojo.Wagon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WagDataImpl implements WagData {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/spr2790?serverTimezone=Asia/Tashkent&zeroDateTimeBehavior=convertToNull";
    private static final String user = "test";
    private static final String password = "test";

    private static Connection conn;
    private static Statement stmt;
    private static ResultSet rs;
//    private static PreparedStatement pstm;

    protected Connection getConnection() {
//        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return conn;
    }

    protected void closeConn(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private final String SET_DATA = "INSERT INTO wagon (wag_no, from_date, to_date) " +
            "VALUES (?, ?, ?)";

    private final String GET_LAST_ID = "SELECT LAST_INSERT_ID();";

    private final String GET_DATA_BY_WAG = "SELECT operations.* FROM operations INNER JOIN wagon ON operations.id_wagon = wagon.id WHERE wag_no = ?";

    @Override
    public int persistData(Wagon w) {
        int lastId = 0;
        try (CallableStatement cstmt = getConnection().prepareCall("{call procedure1(?, ?, ?)}");) {
            cstmt.setInt(1, w.getWagNo());
            cstmt.setTimestamp(2, w.getFromDate());
            cstmt.setTimestamp(3, w.getToDate());
            ResultSet rs = cstmt.executeQuery();
            rs.next();
            lastId = rs.getInt("lastID");

        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
//        finally {
//            closeConn(conn);
//        }
        return lastId;
    }

    @Override
    public List<Operations> getOpersByWagon(int idWagon) {
        List<Operations> list = new ArrayList<>();
        try (PreparedStatement pstm = getConnection().prepareStatement(GET_DATA_BY_WAG);) {
            pstm.setLong(1, idWagon);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Operations l = new Operations();
                l.setId(rs.getInt("id"));
                l.setState(rs.getString("state"));
                l.setOperation(rs.getString("operation"));
                l.setDateTime(rs.getTimestamp("date_time"));
                l.setOperSt(rs.getInt("oper_station"));
                l.setParkPut(rs.getString("park_put"));
                l.setNPoezd(rs.getInt("number_poezd"));
                l.setIdx(rs.getString("idx"));
                l.setNWags(rs.getString("nwags"));
                l.setGolXv(rs.getString("gol_xv"));
                l.setIdWagon(rs.getInt("id_wagon"));
                list.add(l);
            }
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeConn(conn);
        }
    }
}
