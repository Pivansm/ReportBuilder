package skeleton;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class AbstractDAO<T extends Entity> {

    protected Connection connection;
    public AbstractDAO(Connection connection) {
        this.connection = connection;
    }
    public  abstract List<T> findAll();
    public abstract T findEntityById(int id);
    public abstract T findEntityByName(String name);
    public abstract boolean delete(int id);
    public abstract boolean delete(T entity);
    public abstract boolean create();
    public abstract boolean insert(T entity);
    public abstract T update(T entity);

    public void close(Statement st) {
        try
        {
            if (st != null)
                st.close();
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }


}
