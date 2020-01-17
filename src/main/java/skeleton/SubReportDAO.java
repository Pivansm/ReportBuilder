package skeleton;

import java.sql.*;
import java.util.*;

public class SubReportDAO extends AbstractDAO<SubReport> {

    public static final String SQL_FIND_ALL = "SELECT * FROM SUBREPORT";
    public static final String SQL_FIND_PARENT = "SELECT * FROM SUBREPORT WHERE IDPARENTREP = ?";
    public static final String SQL_INSERT = "INSERT INTO SUBREPORT (IDPARENTREP, TITLES, SKIP) VALUES (?, ?, ?)";
    public static final String SQL_UPDATE = "UPDATE SUBREPORT SET SKIP = ? WHERE IDSBREP = ?";
    public static final String SQL_DEL_REPORT = "DELETE FROM SUBREPORT WHERE IDSBREP = ?";

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
    public SubReport findEntityByName(String name) {
        return null;
    }

    @Override
    public boolean delete(int id) {

        boolean flag = false;
        try {

            PreparedStatement statement = connection.prepareStatement(SQL_DEL_REPORT);
            statement.setInt(1, id);
            statement.execute();

            flag = true;

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean delete(SubReport entity) {
        return false;
    }

    @Override
    public boolean create() {
        return false;
    }

    @Override
    public SubReport insert(SubReport entity) {
        SubReport subReport = new SubReport();
        try {

            subReport.setNameReport(entity.getNameReport());
            subReport.setParentId(entity.getParentId());
            subReport.setQuery(entity.getQuery());
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getParentId());
            statement.setString(2, entity.getNameReport());
            statement.setString(3, entity.getQuery());
            statement.execute();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    subReport.setId(rs.getInt(1));
                }
            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return subReport;

    }

    @Override
    public SubReport update(SubReport entity)
    {
        try {

            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1, entity.getQuery());
            statement.setInt(2, entity.getId());
            statement.execute();

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
