/*
 * Created by IntelliJ IDEA.
 * User: schultd
 * Date: 11.07.2002
 * Time: 16:18:55
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */

package gui;


import gui.prefPane.*;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class PropertiesFrame extends JFrame
{
    static PropertiesFrame singleton = null;
    private DefaultMutableTreeNode prefNode;
    private DefaultTreeModel prefTreeModel;
    private JTree prefTree;
    private JScrollPane prefTreePane;
    private JPanel prefPane;
    private AbstractPrefOptionsPanel prefOptionsPane;

    public PropertiesFrame()
    {
        super("Preferences");
        init();
    }

    public static PropertiesFrame getInstance()
    {
        if (singleton == null)
            singleton = new PropertiesFrame();
        return singleton;
    }

    public static void showMe()
    {
        if (singleton == null)
            singleton = new PropertiesFrame();
        singleton.show();
    }

    private void init()
    {
        setBounds(200, 200, 600, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());
        Dimension dim = new Dimension(80, 20);

        prefNode = new DefaultMutableTreeNode("Preferences");
        prefNode.add(new DefaultMutableTreeNode("General"));
        prefNode.add(new DefaultMutableTreeNode("Medical Data"));
        prefNode.add(new DefaultMutableTreeNode("Colors & Fonts"));
        prefNode.add(new DefaultMutableTreeNode("Rendering Quality"));
        DefaultMutableTreeNode dataNode = new DefaultMutableTreeNode("Data Storing");
        dataNode.add(new DefaultMutableTreeNode("MySQL Setup"));
        dataNode.add(new DefaultMutableTreeNode("TextFile Setup"));
        prefNode.add(dataNode);
        /*DefaultMutableTreeNode meterNode = new DefaultMutableTreeNode("Meters");
        meterNode.add(new DefaultMutableTreeNode("Glucocard"));
        prefNode.add(meterNode);*/

        prefTreeModel = new DefaultTreeModel(prefNode);

        prefTree = new JTree(prefTreeModel);
        prefTree.putClientProperty("JTree.lineStyle", "Angled");
        prefTree.addTreeSelectionListener(new TreeSelectionListener()
        {
            public void valueChanged(TreeSelectionEvent e)
            {
                AbstractPrefOptionsPanel old = prefOptionsPane;
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)prefTree.getLastSelectedPathComponent();
                if (selectedNode == null)
                    return;
                String s = selectedNode.toString();

                if (s.equals("General")) {
                    prefOptionsPane = new prefGeneralPane();
                } else if (s.equals("Medical Data")) {
                    prefOptionsPane = new prefMedicalDataPane();
                } else if (s.equals("Colors & Fonts")) {
                    prefOptionsPane = new prefFontsAndColorPane();
                } else if (s.equals("Data Storing")) {
                    prefOptionsPane = new prefDataStoringPane();
                } else if (s.equals("MySQL Setup")) {
                    prefOptionsPane = new prefMySQLSetupPane();
                } else if (s.equals("TextFile Setup")) {
                    prefOptionsPane = new prefTextFileSetupPane();
                } else if (s.equals("Rendering Quality")) {
                    prefOptionsPane = new prefRenderingQualityPane();
                }

                old.kill();
                old = null;

                prefPane.remove(1);
                prefPane.add(prefOptionsPane, BorderLayout.CENTER);
                prefPane.invalidate();
                prefPane.validate();
                prefPane.repaint();
            }
        });

        prefTreePane = new JScrollPane(prefTree, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        prefPane = new JPanel(new BorderLayout());


        //set the buttons up...
        JButton okButton = new JButton("OK");
        okButton.setPreferredSize(dim);
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                prefOptionsPane.saveProps();
                dispose();
                singleton = null;
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(dim);
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                close();
            }
        });

        JButton applyButton = new JButton("Apply");
        applyButton.setPreferredSize(dim);
        applyButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                prefOptionsPane.saveProps();
            }
        });

        //...and align them in a row at the buttom.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        prefOptionsPane = new prefGeneralPane();

        prefPane.add(buttonPanel, BorderLayout.SOUTH);
        prefPane.add(prefOptionsPane, BorderLayout.CENTER);


        getContentPane().add(prefTreePane, BorderLayout.WEST);
        getContentPane().add(prefPane, BorderLayout.CENTER);
    }

    private void close()
    {
        prefOptionsPane.kill();
        dispose();
        singleton = null;
    }

    private class CloseListener extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            close();
        }
    }
}
