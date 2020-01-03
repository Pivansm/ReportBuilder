package skeleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubReportDAO extends AbstractDAO<Report> {

    public static final String SQL_FIND_ALL = "SELECT * FROM SUBREPORT";
    public static final String SQL_INSERT = "INSERT INTO SUBREPORT (IDPARENTREP, TITLES) VALUES (?, ?)";

    public SubReportDAO(Connection connection) {
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
                report.setId(rs.getInt("IDTREREP"));
                report.setNameReport(rs.getString("TITLES"));
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
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean delete(Report entity) {
        return false;
    }

    @Override
    public boolean create(Report entity) {
        return false;
    }

    @Override
    public boolean insert(Report entity) {

        boolean flag = false;
        try {

            PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getNameReport());
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
