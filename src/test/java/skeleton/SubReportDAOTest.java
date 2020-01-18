package skeleton;

import java.sql.Connection;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SubReportDAOTest {
    
    private static final String TEST_STRING = "Test any sub report string";

    @Mock
    Connection   connection;
    @InjectMocks
    SubReportDAO subReportDAO;

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
    void testFindParentId() {
        List<SubReport> result = subReportDAO.findParentId(0);

        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testFindAll() throws SQLException {
        //when
        PreparedStatement query = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(query);
        when(query.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);

        when(rs.getInt(anyString())).thenReturn(1);
        when(rs.getString(anyString())).thenReturn(TEST_STRING);

        //then
        List<SubReport> result = subReportDAO.findAll();
        SubReport subReport = result.get(0);

        Assertions.assertEquals(1, subReport.getId());
        Assertions.assertEquals(1, subReport.getParentId());
        Assertions.assertEquals(TEST_STRING, subReport.getNameReport());
        Assertions.assertEquals(TEST_STRING, subReport.getQuery());
    }

    @Test
    void testFindAllEmpty() {
        List<SubReport> result = subReportDAO.findAll();

        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testFindEntityById() {
        SubReport result = subReportDAO.findEntityById(0);

        Assertions.assertNull(result);
    }

    @Test
    void testFindEntityByName() {
        SubReport result = subReportDAO.findEntityByName("name");

        Assertions.assertNull(result);
    }

    @Test
    void testDelete() {
        boolean result = subReportDAO.delete(0);

        Assertions.assertTrue(result);
    }

    @Test
    void testDeleteBySubReport() {
        boolean result = subReportDAO.delete(new SubReport());

        Assertions.assertFalse(result);
    }

    @Test
    void testCreate() {
        boolean result = subReportDAO.create();

        Assertions.assertFalse(result);
    }

    @Test
    void testInsert() throws SQLException {
        //when
        SubReport report = new SubReport() {{
            setNameReport("Report name");
            setParentId(1);
            setQuery("Test query string");
        }};
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(anyInt())).thenReturn(1);

        //then
        SubReport result = subReportDAO.insert(report);

        Assertions.assertEquals("Report name", result.getNameReport());
        Assertions.assertEquals(1, result.getParentId());
        Assertions.assertEquals("Test query string", result.getQuery());
        Assertions.assertEquals(1, result.getId());
    }

    @Test
    void testUpdate() {
        SubReport result = subReportDAO.update(new SubReport());

        Assertions.assertNull(result);
    }

    @Test
    void testClose() {
        //when
        Statement st = mock(Statement.class);

        //then
        subReportDAO.close(st);
    }
}
