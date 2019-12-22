package mvc.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PhilosophersJTree extends JFrame {
    private JTree tree;
    private DefaultTreeModel philosophers;
    private DefaultMutableTreeNode rootNode;

    public PhilosophersJTree() {
        super("Demo Tree Philosophers");
        DefaultMutableTreeNode philosophersNode = getPhilosopherTree();

        philosophers = new DefaultTreeModel(philosophersNode);
        tree = new JTree(philosophers);

        JButton addButton = new JButton("Add Philosopher");
        addButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                addPhilosopher();
            }
        });

        JButton removeButton = new JButton("Remove Selected Philosopher");
        removeButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                removeSelectedPhilosopher();
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.add(addButton);
        inputPanel.add(removeButton);

        Container container = getContentPane();
        container.add(new JScrollPane(tree), BorderLayout.CENTER);
        container.add(inputPanel, BorderLayout.NORTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    private void addPhilosopher() {

        DefaultMutableTreeNode parent = getSelectedNode();
        if(parent == null) {
            JOptionPane.showMessageDialog(PhilosophersJTree.this, "Select ar era.","Error", JOptionPane.ERROR_MESSAGE);

            return;
        }

        String name = JOptionPane.showInputDialog(PhilosophersJTree.this, "Enter Name:");
        philosophers.insertNodeInto(new DefaultMutableTreeNode(name), parent, parent.getChildCount());

    }

    private void removeSelectedPhilosopher() {
        DefaultMutableTreeNode selectedNode = getSelectedNode();
        if(selectedNode != null)
            philosophers.removeNodeFromParent(selectedNode);

    }

    private DefaultMutableTreeNode getSelectedNode() {
        return (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    }

    private DefaultMutableTreeNode getPhilosopherTree() {

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Philosophers");
        DefaultMutableTreeNode ancient = new DefaultMutableTreeNode("Ancient");
        rootNode.add(ancient);

        ancient.add(new DefaultMutableTreeNode("Socrates"));
        ancient.add(new DefaultMutableTreeNode("Plato"));
        ancient.add(new DefaultMutableTreeNode("Aristotle"));

        return rootNode;

    }

    public static void main(String[] args) {
        new PhilosophersJTree();
    }

}
