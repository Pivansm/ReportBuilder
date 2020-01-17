package skeleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SettingsDAO extends AbstractDAO<Setting> {

    public static final String SQL_FIND_ALL = "SELECT * FROM SETTING";
    public static final String SQL_FIND_TYPE = "SELECT * FROM SETTING WHERE VTYPEJDBC = ?";
    public static final String SQL_FIND_CURRENT = "SELECT * FROM SETTING WHERE BCURR = 1";
    public static final String SQL_CREATE_TBL = "CREATE TABLE IF NOT EXISTS SETTING(VTYPEJDBC varchar(100), VSERVER varchar(250), VBASE varchar(200), VUSERNAME varchar(150), VPASSVORD varchar(200), BCURR INT)";
    public static final String SQL_CREATE_UI = "CREATE UNIQUE INDEX IF NOT EXISTS UI_TYPEJDBC ON SETTING(VTYPEJDBC) ";
    public static final String SQL_INSERT = "INSERT INTO SETTING (VTYPEJDBC, VSERVER, VBASE) VALUES (?, ?, ?)";
    public static final String SQL_UPDATE = "UPDATE SETTING SET VSERVER = ?, VBASE = ?, VUSERNAME = ?, VPASSVORD = ?, BCURR = 1 WHERE VTYPEJDBC = ?";
    public static final String SQL_UPDATE_CURR_ALL = "UPDATE SETTING SET BCURR = 0";

    public SettingsDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<Setting> findAll() {
        List<Setting> result = new ArrayList<>();
        try {

            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Setting setting = new Setting();
                setting.setTypeJDBC(rs.getString("VTYPEJDBC"));
                setting.setServerName(rs.getString("VSERVER"));
                setting.setBaseName(rs.getString("VBASE"));
                setting.setUserName(rs.getString("VUSERNAME"));
                setting.setPassword(rs.getString("VPASSVORD"));
                result.add(setting);
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public Setting findEntityById(int id) {
        return null;
    }

    @Override
    public Setting findEntityByName(String typeJDBC) {

        Setting setting = new Setting();
        try {

            PreparedStatement statement = connection.prepareStatement(SQL_FIND_TYPE);
            statement.setString(1, typeJDBC);
            ResultSet rs = statement.executeQuery();

            //while (rs.next()) {
            if(!rs.next()) return null;

                setting = new Setting();
                setting.setTypeJDBC(rs.getString("VTYPEJDBC"));
                setting.setServerName(rs.getString("VSERVER"));
                setting.setBaseName(rs.getString("VBASE"));
                setting.setUserName(rs.getString("VUSERNAME"));
                setting.setPassword(rs.getString("VPASSVORD"));
            //}

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return setting;
    }


    public Setting findEntityByCurrent() {

        Setting setting = new Setting();
        try {

            PreparedStatement statement = connection.prepareStatement(SQL_FIND_CURRENT);
            ResultSet rs = statement.executeQuery();

            if(!rs.next()) return null;

            setting = new Setting();
            setting.setTypeJDBC(rs.getString("VTYPEJDBC"));
            setting.setServerName(rs.getString("VSERVER"));
            setting.setBaseName(rs.getString("VBASE"));
            setting.setUserName(rs.getString("VUSERNAME"));
            setting.setPassword(rs.getString("VPASSVORD"));

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return setting;
    }


    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean delete(Setting entity) {
        return false;
    }

    @Override
    public boolean create() {

        boolean flag = false;
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_CREATE_TBL);
            statement.execute();
            statement.execute(SQL_CREATE_UI);
            statement.close();
            flag = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public Setting insert(Setting entity) {

        Setting setting = new Setting();
        try {

            setting.setTypeJDBC(entity.getTypeJDBC());

            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
            statement.setString(1, entity.getTypeJDBC());
            statement.setString(2, entity.getServerName());
            statement.setString(3, entity.getBaseName());
            //statement.setString(3, entity.getBaseName());
            statement.execute();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return setting;
    }

    @Override
    public Setting update(Setting entity) {

        try {

            updateCurrent();
            //Основные изменения
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1, entity.getServerName());
            statement.setString(2, entity.getBaseName());
            statement.setString(3, entity.getUserName());
            statement.setString(4, entity.getPassword());
            //По фильтру
            statement.setString(5, entity.getTypeJDBC());
            statement.execute();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }


    public boolean updateCurrent() {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_CURR_ALL);
            statement.execute();
            statement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isTable(String nameTbl) {

        boolean tExists = false;

        try (ResultSet rs = connection.getMetaData().getTables(null, null, nameTbl, null)) {
            while (rs.next()) {
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(nameTbl)) {
                    tExists = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tExists;
    }

}
