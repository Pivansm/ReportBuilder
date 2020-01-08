package skeleton;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class Main {
    /**
     * Create a new table in the test database
     *
     */
    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/tests.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                + "    id integer PRIMARY KEY,\n"
                + "    name text NOT NULL,\n"
                + "    capacity real\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        //createNewTable();
        SQLiteJDBC sqLiteJDBC = new SQLiteJDBC();
        File currSqlite = new File("dbsqlrb.sqlite");
        if(!currSqlite.exists()) {
            System.out.println("В текущей папке нет БД dbsqlrb.sqlite!");
            System.out.println("создаем новую БД!");
            sqLiteJDBC.createTbl();
        }
        else
        {
            System.out.println("БД dbsqlrb.sqlite Ok!");
        }
        //sqLiteJDBC.createTbl();

    }

}
