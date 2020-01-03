package tstdao;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class ItemDaoImpl implements ItemDao {

    private DataSource dataSource;

    @Override
    public List<Item> findAll() {
        List<Item> result = new ArrayList<Item>();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getLong(Item.ID_COLUMN));
                item.setName(rs.getString(Item.NAME_COLUMN));
                result.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public Item findById(Long id) {
        Item item = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                item = new Item();
                item.setId(rs.getLong(Item.ID_COLUMN));
                item.setName(rs.getString(Item.NAME_COLUMN));
                item.setWarehouse_id(rs.getLong(Item.WAREHOUSE_ID_COLUMN));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    @Override
    public void insert(Item item) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, item.getName());
            statement.setLong(2, item.getWarehouse_id());
            statement.execute();

            ResultSet generatedkeys = statement.getGeneratedKeys();
            if (generatedkeys.next()) {
                item.setId(generatedkeys.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(Item item) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1, item.getName());
            statement.setLong(2, item.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(Item item) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(SQL_DELETE);
            statement.setLong(1, item.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
