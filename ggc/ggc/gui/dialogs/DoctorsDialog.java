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
 *  Filename: AddRowFrame.java
 *  Purpose:  Add a new row with Values to ReadMeterFrame or DailyValuesFrame.
 *
 *  Author:   schultd
 */

package ggc.gui.dialogs;

import ggc.db.hibernate.DoctorH;
import ggc.util.DataAccess;
import ggc.util.I18nControl;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;


// fix this

public class DoctorsDialog extends JDialog implements ActionListener
{

    private I18nControl m_ic = I18nControl.getInstance();
    private DataAccess m_da = DataAccess.getInstance();

//x    private boolean m_actionDone = false;

//x    private JTextField tfName;
    private JComboBox cb_template = null;
    private JTable t_doctors = null;
//x    private String[] schemes_names = null;

    GregorianCalendar gc = null;
    JSpinner sl_year = null, sl_month = null;

    public String[] filter_types = 
    {
        m_ic.getMessage("FILTER_VISIBLE"),
        m_ic.getMessage("FILTER_ALL")
    };

    public ArrayList<DoctorH> list_full;
    public ArrayList<DoctorH> active_list = new ArrayList<DoctorH>();
    
    
    
    Font font_normal, font_normal_bold;


    public DoctorsDialog(JFrame frame) 
    {
        super(frame, "", true);

        Rectangle rec = frame.getBounds();
        int x = rec.x + (rec.width/2);
        int y = rec.y + (rec.height/2);

        setBounds(x-175, y-150, 450, 380);
        this.setLayout(null);

        font_normal = m_da.getFont(DataAccess.FONT_NORMAL);
        font_normal_bold = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);

        gc = new GregorianCalendar();
        
        init();

        this.list_full = new ArrayList<DoctorH>();
        populateList();
        
        //this.cb_template.setSelectedIndex(type-1);

        this.setVisible(true);
    }



    private void init() 
    {

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 450, 350);
        panel.setLayout(null);
    
        this.getContentPane().add(panel);

        JLabel label = new JLabel(m_ic.getMessage("DOCTORS"));
        label.setFont(m_da.getFont(DataAccess.FONT_BIG_BOLD));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 20, 450, 35);
        panel.add(label);
        
    
        label = new JLabel(m_ic.getMessage("FILTER") + ":" );
        label.setFont(this.font_normal_bold);
        label.setBounds(40, 75, 100, 25);
        panel.add(label);
        
        
        
        cb_template = new JComboBox(filter_types);
        cb_template.setFont(this.font_normal);
        cb_template.setBounds(120, 75, 80, 25);
        panel.add(cb_template);
            
        this.t_doctors = new JTable(new AbstractTableModel()
            {

		@Override
		public int getColumnCount()
		{
		    // TODO Auto-generated method stub
		    return 2;
		}

		@Override
		public int getRowCount()
		{
		    active_list.size();
		    return 0;
		}

		@Override
		public Object getValueAt(int row, int column)
		{
		    // TODO Auto-generated method stub
		    DoctorH dh = active_list.get(row);
		    
		    switch(column)
		    {
			case 0:
			    return dh.getName();
			    
			case 1:
			    return m_ic.getMessage(dh.getDoctor_type().getName());
		    }
		    
		    return null;
		}
                
            })  ;  
        
        JScrollPane scp = new JScrollPane(this.t_doctors);
        scp.setBounds(40, 120, 290, 200);
        panel.add(scp);
            
        String[] names = {
        	m_ic.getMessage("ADD"),
        	m_ic.getMessage("EDIT"),
        	m_ic.getMessage("CLOSE"),
        };

        String[] cmds = {
        	m_ic.getMessage("add"),
        	m_ic.getMessage("edit"),
        	m_ic.getMessage("close"),
        };
        
        
        int[] coords = {
        	120, 150, 220, 
        };
        
        JButton button;
        
        for(int i=0; i<coords.length; i++)
        {
            button = new JButton(names[i]);
            button.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
            button.setActionCommand(cmds[i]);
            button.addActionListener(this);
            button.setBounds(340, coords[i], 80, 25);
            panel.add(button);
        }
        

    }


    public void populateList()
    {
	
	
    }
    
    
    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();
    
        if (action.equals("close"))
        {
            this.dispose();
        }
        else if (action.equals("add"))
        {
            System.out.println("Add not implemented");
        }
        else if (action.equals("edit"))
        {
            System.out.println("Edit not implemented");
        }
        else
            System.out.println("DoctorsDialog: Unknown command: " + action);

    }


}
