/*
 * Created by IntelliJ IDEA.
 * User: schultd
 * Date: 11.07.2002
 * Time: 16:18:55
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */

package ggc.gui;


import ggc.gui.prefPane.*;
import ggc.util.I18nControl;

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


public class PropertiesFrame extends JDialog
{

    private I18nControl m_ic = I18nControl.getInstance();        

    static PropertiesFrame singleton = null;
    private DefaultMutableTreeNode prefNode;
    private DefaultTreeModel prefTreeModel;
    private JTree prefTree;
    private JScrollPane prefTreePane;
    private JPanel prefPane;
    private AbstractPrefOptionsPanel prefOptionsPane;

    public PropertiesFrame(MainFrame main)
    {
        super();

	Toolkit theKit = main.getToolkit();
	//Toolkit theKit = JFrame.getToolkit();
	Dimension wndSize = theKit.getScreenSize();

	int x, y; 
	

	x = wndSize.width/2 - 320;
	y = wndSize.height/2 - 240;

	setBounds(x, y, 640, 480);
        setTitle(m_ic.getMessage("PREFERENCES"));

        init();
    }

    public static PropertiesFrame getInstance(MainFrame main)
    {
        if (singleton == null)
            singleton = new PropertiesFrame(main);
        return singleton;
    }

    public static void showMe(MainFrame main)
    {
        if (singleton == null)
            singleton = new PropertiesFrame(main);
        singleton.show();
    }

    private void init()
    {
        setBounds(200, 200, 600, 500);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new CloseListener());
        Dimension dim = new Dimension(80, 20);

        prefNode = new DefaultMutableTreeNode(m_ic.getMessage("PREFERENCES"));
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("GENERAL")));
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("MEDICAL_DATA")));
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("COLORS_AND_FONTS")));
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("RENDERING_QUALITY")));
        DefaultMutableTreeNode dataNode = new DefaultMutableTreeNode(m_ic.getMessage("DATA_STORING"));
        dataNode.add(new DefaultMutableTreeNode(m_ic.getMessage("MYSQL_SETUP")));
        dataNode.add(new DefaultMutableTreeNode(m_ic.getMessage("TEXTFILE_SETUP")));
        prefNode.add(dataNode);
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("METER_CONFIGURATION")));
        prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("NUTRITION")));
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

                if (s.equals(m_ic.getMessage("GENERAL"))) 
                    prefOptionsPane = new prefGeneralPane();
		else if (s.equals(m_ic.getMessage("MEDICAL_DATA"))) 
                    prefOptionsPane = new prefMedicalDataPane();
                else if (s.equals(m_ic.getMessage("COLORS_AND_FONTS")))
                    prefOptionsPane = new prefFontsAndColorPane();
                else if (s.equals(m_ic.getMessage("DATA_STORING")))
                    prefOptionsPane = new prefDataStoringPane();
		else if (s.equals(m_ic.getMessage("MYSQL_SETUP"))) 
                    prefOptionsPane = new prefMySQLSetupPane();
		else if (s.equals(m_ic.getMessage("TEXTFILE_SETUP"))) 
                    prefOptionsPane = new prefTextFileSetupPane();
		else if (s.equals(m_ic.getMessage("RENDERING_QUALITY"))) 
                    prefOptionsPane = new prefRenderingQualityPane();
		else if (s.equals(m_ic.getMessage("METER_CONFIGURATION"))) 
                    prefOptionsPane = new prefMeterConfPane();
		else if (s.equals(m_ic.getMessage("NUTRITION"))) 
		    prefOptionsPane = new prefNutritionConfPane();


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
        JButton okButton = new JButton(m_ic.getMessage("OK"));
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

        JButton cancelButton = new JButton(m_ic.getMessage("CANCEL"));
        cancelButton.setPreferredSize(dim);
        cancelButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                close();
            }
        });

        JButton applyButton = new JButton(m_ic.getMessage("APPLY"));
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
