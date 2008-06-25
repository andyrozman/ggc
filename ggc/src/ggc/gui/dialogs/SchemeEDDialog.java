/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: SchemeEDDialog.java
 *  Purpose:  Edit or Delete color scheme.
 *
 *  Author:   andyrozman
 */

package ggc.gui.dialogs;

import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


// fix this

public class SchemeEDDialog extends JDialog implements ActionListener, ItemListener
{

    /**
     * 
     */
    private static final long serialVersionUID = 5602610719366564987L;
    private I18nControl m_ic = I18nControl.getInstance();
    private DataAccess m_da = DataAccess.getInstance();

    private boolean m_actionDone = false;
    private int m_selected_action = 0;

    private JTextField tf_name;
    private JComboBox cb_template = null;
    private String[] schemes_names = null;

    private String selected_scheme = null;

    private String[] actions = { m_ic.getMessage("SELECT"), 
	m_ic.getMessage("ACTION_EDIT"),
	m_ic.getMessage("ACTION_DELETE")
    };

    public static final int ACTION_NONE = 0;
    public static final int ACTION_EDIT = 1;
    public static final int ACTION_DELETE = 2;



    public SchemeEDDialog(JDialog dialog, String[] schemes_names, String selected) 
    {
        super(dialog, "", true);
    	this.schemes_names = schemes_names;
	this.selected_scheme = selected;
    
    	Rectangle rec = dialog.getBounds();
    	int x = rec.x + (rec.width/2);
    	int y = rec.y + (rec.height/2);
    
    	setBounds(x-175, y-150, 350, 300);
    	this.setLayout(null);






        init();
        this.setVisible(true);
    }



    private void init() 
    {

    	JPanel panel = new JPanel();
    	panel.setBounds(0, 0, 350, 250);
    	panel.setLayout(null);
    
    	this.getContentPane().add(panel);
    
    	JLabel label = new JLabel(m_ic.getMessage("EDIT_DELETE_SCHEME"));
    	label.setFont(m_da.getFont(DataAccess.FONT_BIG_BOLD));
    	label.setHorizontalAlignment(SwingConstants.CENTER);
    	label.setBounds(0, 20, 350, 35);
    	panel.add(label);

	label = new JLabel(m_ic.getMessage("SELECTED_SCHEME") +":");
	label.setFont(m_da.getFont(DataAccess.FONT_NORMAL_BOLD));
	label.setBounds(40, 90, 120, 25);
	panel.add(label);

	label = new JLabel(this.selected_scheme);
	label.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
	label.setBounds(160, 90, 200, 25);
	panel.add(label);


	label = new JLabel(m_ic.getMessage("ACTION") + ":" );
	label.setFont(m_da.getFont(DataAccess.FONT_NORMAL_BOLD));
	label.setBounds(40, 120, 280, 25);
	panel.add(label);

	cb_template = new JComboBox(this.actions);
	cb_template.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
	cb_template.addItemListener(this);
	cb_template.setBounds(160, 120, 90, 25);
	panel.add(cb_template);


    
    	label = new JLabel(m_ic.getMessage("NEW_NAME") + ":");
    	label.setFont(m_da.getFont(DataAccess.FONT_NORMAL_BOLD));
    	label.setBounds(40, 170, 80, 25);
    	panel.add(label);
    
    	tf_name = new JTextField();
    	tf_name.setBounds(140, 170, 160, 25);
    	tf_name.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
	tf_name.setEnabled(false);
    	panel.add(tf_name);
    
    
    	JButton button = new JButton(m_ic.getMessage("OK"));
    	button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
    	button.setActionCommand("ok");
    	button.addActionListener(this);
    	button.setBounds(90, 220, 80, 25);
    	panel.add(button);
    
    	button = new JButton(m_ic.getMessage("CANCEL"));
    	button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
    	button.setActionCommand("cancel");
    	button.addActionListener(this);
    	button.setBounds(180, 220, 80, 25);
    	panel.add(button);

    }


    private boolean doesSchemeNameExist()
    {
	//System.out.println("doesSchemeNameExists");

	for(int i=0; i<this.schemes_names.length; i++)
	{
	    if (this.schemes_names[i].equals(this.tf_name.getText()))
		return true;
	}

	//System.out.println("...not");

	return false;

    }


    /**
     * Invoked when an item has been selected or deselected by the user.
     * The code written for this method performs the operations
     * that need to occur when an item is selected (or deselected).
     */
    public void itemStateChanged(ItemEvent e)
    {
	if (this.cb_template.getSelectedIndex()==SchemeEDDialog.ACTION_EDIT)
	    this.tf_name.setEnabled(true);
	else
	    this.tf_name.setEnabled(false);
    }




    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
    	String action = e.getActionCommand();
    
    	if (action.equals("cancel"))
    	{
    	    m_actionDone = false;
    	    this.dispose();
    	}
    	else if (action.equals("ok"))
    	{
	    int index = this.cb_template.getSelectedIndex();

	    if (index==SchemeEDDialog.ACTION_NONE)
	    {
		JOptionPane.showMessageDialog(this, m_ic.getMessage("SELECT_ACTION_OR_CANCEL"), m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
		m_actionDone = false;
		return;
	    }
	    else if (index==SchemeEDDialog.ACTION_DELETE)
	    {
		this.m_selected_action = SchemeEDDialog.ACTION_DELETE;
	    }
	    else
	    {

		if (this.tf_name.getText().trim().equals(""))
		{
		    JOptionPane.showMessageDialog(this, m_ic.getMessage("TYPE_NAME_BEFORE"), m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
		    return;
		}

		if (doesSchemeNameExist())
		{
		    JOptionPane.showMessageDialog(this, m_ic.getMessage("SCHEME_NAME_ALREADY_USED"), m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
		    return;
		}

		this.m_selected_action = SchemeEDDialog.ACTION_EDIT;

	    }

	    m_actionDone = true;
	    this.dispose();
	    return;

    	}
    	else
    	    System.out.println("SchemeDialog: Unknown command: " + action);

    }

    public boolean actionSuccesful()
    {
        return m_actionDone;
    }


    // result is [0] = action (1,2), [1]= new name
    public String[] getActionResult()
    {
	String[] res = new String[3];

	if (m_actionDone)
	    res[0] = "" + this.m_selected_action;
	else
	    res[0] = "0";

	res[1] = this.tf_name.getText();
	//res[2] = this.cb_template.getSelectedItem().toString();

	return res;
    }


}
