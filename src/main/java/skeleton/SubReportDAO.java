package skeleton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubReportDAO extends AbstractDAO<SubReport> {

    public static final String SQL_FIND_ALL = "SELECT * FROM SUBREPORT";
    public static final String SQL_FIND_PARENT = "SELECT * FROM SUBREPORT WHERE IDPARENTREP = ?";
    public static final String SQL_INSERT = "INSERT INTO SUBREPORT (IDPARENTREP, TITLES) VALUES (?, ?)";

    public SubReportDAO(Connection connection) {
        super(connection);
    }


    public List<SubReport> findParentId(int id) {
        List<SubReport> result = new ArrayList<>();
        try {

            PreparedStatement statement = connection.prepareStatement(SQL_FIND_PARENT);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                SubReport subReport = new SubReport();
                subReport.setId(rs.getInt("IDSBREP"));
                subReport.setParentId(rs.getInt("IDPARENTREP"));
                subReport.setNameReport(rs.getString("TITLES"));
                subReport.setQuery(rs.getString("SKIP"));
                result.add(subReport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public List<SubReport> findAll() {
        List<SubReport> result = new ArrayList<>();
        try {

            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                SubReport subReport = new SubReport();
                subReport.setId(rs.getInt("IDSBREP"));
                subReport.setParentId(rs.getInt("IDPARENTREP"));
                subReport.setNameReport(rs.getString("TITLES"));
                subReport.setQuery(rs.getString("SKIP"));
                result.add(subReport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public SubReport findEntityById(int id) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public boolean delete(SubReport entity) {
        return false;
    }

    @Override
    public boolean create(SubReport entity) {
        return false;
    }

    @Override
    public boolean insert(SubReport entity) {

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
    public SubReport update(SubReport entity) {
        return null;
    }
}
