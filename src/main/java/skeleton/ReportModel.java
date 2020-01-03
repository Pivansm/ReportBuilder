package skeleton;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.sql.SQLException;
import java.util.Vector;

public class ReportModel  implements TreeModel {

    SQLiteJDBC sqLiteJDBC;
    ReportDAO reportDAO;
    private Vector listeners = new Vector();

    public ReportModel() throws SQLException, ClassNotFoundException {
        sqLiteJDBC = new SQLiteJDBC();
        reportDAO = new ReportDAO(sqLiteJDBC.getConn());
    }

    @Override
    public Object getRoot() {
        return reportDAO;
    }

    @Override
    public Object getChild(Object parent, int index) {
        //ReportDAO directory = (ReportDAO) parent;
        //String[] children = directory.findAll();

        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        return false;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {

    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return 0;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }
}
