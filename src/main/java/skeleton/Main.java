package skeleton;

import java.io.File;
import java.sql.SQLException;

public class Main {

    /**
     * @Глпавный запуск программы
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
        //Главное рабочее окно
        MajorWindow majorWindow = new MajorWindow();
    }

}
