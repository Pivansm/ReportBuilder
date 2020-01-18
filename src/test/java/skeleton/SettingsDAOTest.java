package skeleton;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SettingsDAOTest {

    private static final String TEST_STRING = "Test any setting string";

    @Mock
    Connection  connection;
    @InjectMocks
    SettingsDAO settingsDAO;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);

        PreparedStatement query = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(query);
        when(query.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);
    }

    @Test
    void testFindAll() throws SQLException {
        //when
        PreparedStatement query = mock(PreparedStatement.class);  //заглушка
        ResultSet rs = mock(ResultSet.class);  //заглушка

        when(connection.prepareStatement(anyString())).thenReturn(query);
        when(query.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false); //Для правильного выхода из while цикла

        when(rs.getString(anyString())).thenReturn(TEST_STRING);

        //then
        List<Setting> result = settingsDAO.findAll();
        Setting setting = result.get(0);

        Assertions.assertEquals(TEST_STRING, setting.getTypeJDBC());
        Assertions.assertEquals(TEST_STRING, setting.getServerName());
        Assertions.assertEquals(TEST_STRING, setting.getBaseName());
        Assertions.assertEquals(TEST_STRING, setting.getUserName());
        Assertions.assertEquals(TEST_STRING, setting.getPassword());
    }

    @Test
    void testFindAllEmpty() {
        List<Setting> result = settingsDAO.findAll();

        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testFindEntityById() {
        Setting result = settingsDAO.findEntityById(0);

        Assertions.assertNull(result);
    }

    @Test
    void testFindEntityByName() {
        Setting result = settingsDAO.findEntityByName("typeJDBC");

        Assertions.assertNull(result);
    }

    @Test
    void testFindEntityByCurrent() {
        Setting result = settingsDAO.findEntityByCurrent();

        Assertions.assertNull(result);
    }

    @Test
    void testDelete() {
        boolean result = settingsDAO.delete(0);

        Assertions.assertFalse(result);
    }

    @Test
    void testDeleteBySettings() {
        boolean result = settingsDAO.delete(new Setting());

        Assertions.assertFalse(result);
    }

    @Test
    void testCreate() {
        boolean result = settingsDAO.create();

        Assertions.assertTrue(result);
    }

    @Test
    void testInsert() {
        Setting result = settingsDAO.insert(new Setting() {{
            setTypeJDBC("JDBCType");
        }});

        Assertions.assertEquals("JDBCType", result.getTypeJDBC());
    }

    @Test
    void testUpdate() {
        Setting result = settingsDAO.update(new Setting());

        Assertions.assertNull(result);
    }

    @Test
    void testUpdateCurrent() {
        boolean result = settingsDAO.updateCurrent();

        Assertions.assertTrue(result);
    }

    @Test
    void testIsTable() throws SQLException {
        //when
        DatabaseMetaData md = mock(DatabaseMetaData.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.getMetaData()).thenReturn(md);
        when(md.getTables(anyString(), anyString(), anyString(), any())).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString(anyString())).thenReturn("Test table name");

        //then
        //boolean result = settingsDAO.isTable("Test table name");

        //Assertions.assertTrue(result);
    }

    @Test
    void testClose() {
        //when
        Statement st = mock(Statement.class);

        //then
        settingsDAO.close(st);
    }
}
