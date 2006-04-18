/*
 * Created by IntelliJ IDEA.
 * User: schultd
 * Date: 11.07.2002
 * Time: 16:18:55
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */

package ggc.gui.dialogs;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import ggc.gui.panels.prefs.*;

import ggc.util.I18nControl;
import ggc.util.DataAccess;


public class PropertiesDialog extends JDialog implements ActionListener
{

    private I18nControl m_ic = I18nControl.getInstance();        

    private DefaultMutableTreeNode prefNode;
    private JTree prefTree;
    private JPanel prefPane;
    //private DefaultTreeModel prefTreeModel;
    //private JScrollPane prefTreePane;
    //private AbstractPrefOptionsPanel prefOptionsPane;

    public ArrayList panels = null;
    public Hashtable panel_id = null;


    public PropertiesDialog(JFrame parent)
    {
        super(parent, "", true);

	Rectangle rec = parent.getBounds();
	int x = rec.x + (rec.width/2);
	int y = rec.y + (rec.height/2);

	setBounds(x-320, y-240, 640, 480);
        setTitle(m_ic.getMessage("PREFERENCES"));

	createPanels();
        init();
	this.setResizable(false);
        this.setVisible(true);
    }


    private void init()
    {
        Dimension dim = new Dimension(80, 20);

	createNodes();

	/*
 private JTree prefTree;
 private DefaultTreeModel prefTreeModel;
     private JScrollPane prefTreePane;

	*/

        DefaultTreeModel prefTreeModel = new DefaultTreeModel(prefNode);

        prefTree = new JTree(prefTreeModel);
        prefTree.putClientProperty("JTree.lineStyle", "Angled");
        prefTree.addTreeSelectionListener(new TreeSelectionListener()
        {
            public void valueChanged(TreeSelectionEvent e)
            {
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)prefTree.getLastSelectedPathComponent();

		if (selectedNode == null)
		    return;

		selectPanel(selectedNode.toString());
            }
        });

        JScrollPane prefTreePane = new JScrollPane(prefTree, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        prefPane = new JPanel(new BorderLayout());


        //set the buttons up...
        JButton okButton = new JButton(m_ic.getMessage("OK"));
        okButton.setPreferredSize(dim);
        okButton.setActionCommand("ok");
        okButton.addActionListener(this);

        JButton cancelButton = new JButton(m_ic.getMessage("CANCEL"));
        cancelButton.setPreferredSize(dim);
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);

        JButton applyButton = new JButton(m_ic.getMessage("APPLY"));
        applyButton.setPreferredSize(dim);
        applyButton.setActionCommand("apply");
        applyButton.addActionListener(this);

        //...and align them in a row at the buttom.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);

        prefPane.add(buttonPanel, BorderLayout.SOUTH);
        prefPane.add((JPanel)panels.get(0), BorderLayout.CENTER);

        getContentPane().add(prefTreePane, BorderLayout.WEST);
        getContentPane().add(prefPane, BorderLayout.CENTER);
    }



    // ---
    // ---   NODES and PANELS
    // ---   (if you add node and panel for configuration options, please change 
    // ---    following methods and add values)
    // ---


    public void createNodes()
    {
	prefNode = new DefaultMutableTreeNode(m_ic.getMessage("PREFERENCES"));
	prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("GENERAL")));
	prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("MEDICAL_DATA")));
	prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("COLORS_AND_FONTS")));
	prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("RENDERING_QUALITY")));
	//DefaultMutableTreeNode dataNode = new DefaultMutableTreeNode(m_ic.getMessage("DATA_STORING"));
	//dataNode.add(new DefaultMutableTreeNode(m_ic.getMessage("MYSQL_SETUP")));
	//dataNode.add(new DefaultMutableTreeNode(m_ic.getMessage("TEXTFILE_SETUP")));
	//prefNode.add(dataNode);
	prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("METER_CONFIGURATION")));
	prefNode.add(new DefaultMutableTreeNode(m_ic.getMessage("NUTRITION")));
	/*DefaultMutableTreeNode meterNode = new DefaultMutableTreeNode("Meters");
	meterNode.add(new DefaultMutableTreeNode("Glucocard"));
	prefNode.add(meterNode);*/
    }



    private int PANEL_MAIN = 0;
    private int PANEL_GENERAL = 1;
    private int PANEL_MEDICAL_DATA = 2;
    private int PANEL_COLORS = 3;
    private int PANEL_RENDERING = 4;
    private int PANEL_METER = 5;
    private int PANEL_NUTRITION = 6;


    public void createPanels()
    {
	// Each node must have a panel, and panel numbers must be as they are added 
	// to. You must add panels in same order as you add, nodes.

	panels = new ArrayList();
	panel_id = new Hashtable();

	addPanel(m_ic.getMessage("PREFERENCES"), this.PANEL_MAIN, new PrefMainPane());
	addPanel(m_ic.getMessage("GENERAL"), this.PANEL_GENERAL, new PrefGeneralPane());
	addPanel(m_ic.getMessage("MEDICAL_DATA"), this.PANEL_MEDICAL_DATA, new PrefMedicalDataPane());
        addPanel(m_ic.getMessage("COLORS_AND_FONTS"), PANEL_COLORS, new PrefFontsAndColorPane(this));
	addPanel(m_ic.getMessage("RENDERING_QUALITY"), PANEL_RENDERING, new PrefRenderingQualityPane());
	addPanel(m_ic.getMessage("METER_CONFIGURATION"), PANEL_METER, new PrefMeterConfPane());
	addPanel(m_ic.getMessage("NUTRITION"), PANEL_NUTRITION, new PrefNutritionConfPane());
    }


    // ---
    // ---  End
    // ---


    private void addPanel(String name, int id, JPanel panel)
    {
	panels.add(panel);
	panel_id.put(name, ""+id);
    }


    public void selectPanel(String s)
    {

	if (!panel_id.containsKey(s))
	{
	    System.out.println("No such panel: " + s);
	    return;
	}

	String id = (String)panel_id.get(s);

	prefPane.remove(1);
	prefPane.add((JPanel)panels.get(Integer.parseInt(id)), BorderLayout.CENTER);
	prefPane.invalidate();
	prefPane.validate();
	prefPane.repaint();

    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) 
    {
        String action = e.getActionCommand();

        if (action.equals("ok")) 
        {
	    save();
            this.dispose();
        }
        else if (action.equals("cancel")) 
        {
	    reset();
            this.dispose();
        }
        else if (action.equals("apply")) 
        {
            save();
        }
        else
            System.out.println("PropertiesFrame: Unknown command: " + action);

    }

    public void save()
    {
	for (int i=0; i<panels.size(); i++)
	{
	    AbstractPrefOptionsPanel pn = (AbstractPrefOptionsPanel)panels.get(i);
	    pn.saveProps();
	}

	DataAccess.getInstance().getSettings().save();
    }

    public void reset()
    {
	DataAccess.getInstance().getSettings().reload();
    }


}
