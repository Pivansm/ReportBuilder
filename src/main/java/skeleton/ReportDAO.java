package skeleton;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO extends AbstractDAO<Report> {
    public static final String SQL_FIND_ALL = "SELECT * FROM QUREPORT";
    public static final String SQL_INSERT = "INSERT INTO QUREPORT (NAMEREP) VALUES (?)";
    public static final String SQL_DEL_PARENT = "DELETE FROM QUREPORT WHERE IDREP = ?";
    public static final String SQL_DEL_REPORT = "DELETE FROM SUBREPORT WHERE IDPARENTREP = ?";


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
            statement.close();
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
        boolean flag = false;
        try {

            PreparedStatement statement = connection.prepareStatement(SQL_DEL_PARENT);
            statement.setInt(1, id);
            statement.execute();
            statement.close();
            //И всей его детей
            try {
                statement = connection.prepareStatement(SQL_DEL_REPORT);
                statement.setInt(1, id);
                statement.execute();
                statement.close();
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
                System.out.println("Нечего удалаять");
            }

            flag = true;

        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
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
    public Report insert(Report entity) {

        Report report = new Report();
        try {
            report.setNameReport(entity.getNameReport());
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getNameReport());
            statement.execute();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    report.setId(rs.getInt(1));
                }
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return report;
    }


    @Override
    public Report update(Report entity) {
        return null;
    }
}
