package mvc.tree;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.io.File;

public class FileTreeFrame extends JFrame {
    private JTree fileTree;
    private FileSystemModel fileSystemModel;
    private JTextArea fileDetailsTeatArea;

    public FileTreeFrame(String directory) {
        super("JTree FileSystem");
        fileDetailsTeatArea = new JTextArea();
        fileDetailsTeatArea.setEditable(false);

        fileSystemModel = new FileSystemModel(new File(directory));
        fileTree = new JTree(fileSystemModel);
        fileTree.setEditable(true);

        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                File file = (File) fileTree.getLastSelectedPathComponent();
                fileDetailsTeatArea.setText(getFileDetails(file));
            }
        });

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, true,
                new JScrollPane(fileTree),
                new JScrollPane(fileDetailsTeatArea)
        );

        getContentPane().add(splitPane);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 480);
        setVisible(true);
    }

    private String getFileDetails(File file) {
        if(file == null)
            return  "";

        StringBuffer buffer = new StringBuffer();
        buffer.append("Name:" + file.getName() + "\n");
        buffer.append("Path:" + file.getPath() + "\n");
        buffer.append("Size:" + file.length() + "\n");

        return buffer.toString();
    }

    public static void main(String[] args) {
        //if(args.length != 1)
        //    System.err.println("Usage: ");
        //else
            new FileTreeFrame("c:\\");
    }
}
