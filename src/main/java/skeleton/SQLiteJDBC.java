package skeleton;

import java.sql.*;


public class SQLiteJDBC {

    Connection conn;
    Statement statement;

    public SQLiteJDBC() throws SQLException, ClassNotFoundException {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dbsqlrb.sqlite");
            statement = conn.createStatement();
    }

    public void createTbl() throws SQLException {
        String strSql;
        strSql = "CREATE TABLE IF NOT EXISTS UNOTHOPR (IDREP INTEGER PRIMARY KEY AUTOINCREMENT, ";
        strSql += "NAMEREP varchar(250) NOT NULL, ";
        strSql += "SKIP text not null, IDRESB integer)";
        statement.executeUpdate(strSql);

        strSql = "CREATE UNIQUE INDEX IF NOT EXISTS UI_REP_ID ON UNOTHOPR(IDREP, NAMEREP) ";
        statement.executeUpdate(strSql);

        strSql = "CREATE TABLE IF NOT EXISTS TREUNOTH (IDTREREP INTEGER PRIMARY KEY AUTOINCREMENT, ";
        strSql += "IDPARENUO integer not null, TITLESB varchar(250) NOT NULL,";
        strSql += "PRZSRT integer)";
        statement.executeUpdate(strSql);
    }


}
