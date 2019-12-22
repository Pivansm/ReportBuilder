package skeleton;

import java.sql.*;

public class ConnectDb {

    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resSet;

    public ConnectDb() {

    }

    public void Conn() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("");
        System.out.println("Ok!");
    }

    public void CreateTable() throws SQLException {
        statmt = conn.createStatement();
        statmt.execute("Create table");
    }



    public void CloseConnect() throws SQLException {
        conn.close();
        statmt.close();
    }

}
