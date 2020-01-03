package skeleton;

import java.sql.*;


public class SQLiteJDBC {

    private Connection conn;
    public Statement statement;

    public SQLiteJDBC() throws SQLException, ClassNotFoundException {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:dbsqlrb.sqlite");
            statement = conn.createStatement();
    }

    public void createTbl() throws SQLException {
        String strSql;
        strSql = "CREATE TABLE IF NOT EXISTS QUREPORT (IDREP INTEGER PRIMARY KEY AUTOINCREMENT, ";
        strSql += "NAMEREP varchar(250) NOT NULL) ";
        statement.executeUpdate(strSql);

        strSql = "CREATE UNIQUE INDEX IF NOT EXISTS UI_REP_ID ON QUREPORT(IDREP, NAMEREP) ";
        statement.executeUpdate(strSql);

        strSql = "CREATE TABLE IF NOT EXISTS SUBREPORT(IDSBREP INTEGER PRIMARY KEY AUTOINCREMENT, ";
        strSql += "IDPARENTREP integer not null, TITLES varchar(250) NOT NULL,";
        strSql += "SKIP text)";
        statement.executeUpdate(strSql);
    }

    public Connection getConn() {
        return conn;
    }
}
