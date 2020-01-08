package skeleton;

import tstdao.Warehouse;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO extends AbstractDAO<Report> {
    public static final String SQL_FIND_ALL = "SELECT * FROM QUREPORT";
    public static final String SQL_INSERT = "INSERT INTO QUREPORT (NAMEREP) VALUES (?)";


    public ReportDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<Report> findAll() {
        List<Report> result = new ArrayList<Report>();
        try {

            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Report report = new Report();
                report.setId(rs.getInt("IDREP"));
                report.setNameReport(rs.getString("NAMEREP"));
                result.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Report findEntityById(int id) {
        return null;
    }

    @Override
    public Report findEntityByName(String name) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean delete(Report entity) {
        return false;
    }

    @Override
    public boolean create() {
        return false;
    }

    @Override
    public boolean insert(Report entity) {
        boolean flag = false;
        try {

            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
             statement.setString(1, entity.getNameReport());
            statement.execute();
            flag = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }


    @Override
    public Report update(Report entity) {
        return null;
    }
}
