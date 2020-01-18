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

class ReportDAOTest {
    @Mock
    Connection connection;
    @InjectMocks
    ReportDAO  reportDAO;

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
        PreparedStatement query = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(query);
        when(query.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true).thenReturn(false);

        when(rs.getInt(anyString())).thenReturn(1);
        when(rs.getString(anyString())).thenReturn("Report name");

        //then
        List<Report> result = reportDAO.findAll();
        Report report = result.get(0);

        Assertions.assertEquals(1, report.getId());
        Assertions.assertEquals("Report name", report.getNameReport());
    }

    @Test
    void testFindAllEmpty() {
        List<Report> result = reportDAO.findAll();

        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testFindEntityById() {
        Report result = reportDAO.findEntityById(0);

        Assertions.assertNull(result);
    }

    @Test
    void testFindEntityByName() {
        Report result = reportDAO.findEntityByName("name");

        Assertions.assertNull(result);
    }

    @Test
    void testDelete() {
        boolean result = reportDAO.delete(0);

        Assertions.assertTrue(result);
    }

    @Test
    void testDeleteByReport() {
        boolean result = reportDAO.delete(new Report(0, null));

        Assertions.assertFalse(result);
    }

    @Test
    void testCreate() {
        boolean result = reportDAO.create();

        Assertions.assertFalse(result);
    }

    @Test
    void testInsert() throws SQLException {
        //when
        PreparedStatement query = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(query);
        when(query.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(anyInt())).thenReturn(1);

        //then
        Report result = reportDAO.insert(new Report(0, "nameReport"));

        Assertions.assertEquals("nameReport", result.getNameReport());
        Assertions.assertEquals(1, result.getId());
    }

    @Test
    void testUpdate() {
        Report result = reportDAO.update(new Report(0, null));

        Assertions.assertNull(result);
    }

    @Test
    void testClose() {
        //when
        Statement st = mock(Statement.class);

        //then
        reportDAO.close(st);
    }
}
