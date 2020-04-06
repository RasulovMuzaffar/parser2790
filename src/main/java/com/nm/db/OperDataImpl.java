package com.nm.db;

import com.nm.pojo.Operations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperDataImpl implements OperData {
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

    private final String SET_DATA = "INSERT INTO operations (state, operation, date_time, oper_station, park_put, number_poezd, idx, nwags, gol_xv, id_wagon) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?)";

    private final String GET_DATA_BY_WAG = "select * from operations o where o.id_wagon = ?";

    @Override
    public boolean persistOpers(List<Operations> oList, int idWagon) {
        try (PreparedStatement pstm = getConnection().prepareStatement(SET_DATA);) {
            for (Operations o : oList) {

                pstm.setString(1, o.getState());
                pstm.setString(2, o.getOperation());
                pstm.setTimestamp(3, o.getDateTime());
                pstm.setInt(4, o.getOperSt());
                pstm.setString(5, o.getParkPut());
                pstm.setInt(6, o.getNPoezd());
                pstm.setString(7, o.getIdx());
                pstm.setString(8, o.getNWags());
                pstm.setString(9, o.getGolXv());
                pstm.setInt(10, idWagon);

                pstm.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeConn(conn);
        }
        return true;
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
                l.setOperSt(rs.getInt("operSt"));
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
