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
 *  Filename: prefMeterConfPane.java
 *  Purpose:  Preferences Pane to configure the meter.
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */

package ggc.gui.panels.prefs;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class PrefNutritionConfPane extends AbstractPrefOptionsPanel
{
    //private JComboBox comboMeterType;
    //private JComboBox comboPortId;
    private JLabel lab_db = null;
    private JLabel lab_file = null;


    public PrefNutritionConfPane()
    {
        init();
        getLocalData();
        getDbData();
    }

    public void getDbData()
    {
        /*
    	if (MainFrame.dbH.isConnected())
    	{
    	    lab_db.setText("PROBLEM");
    	}
    	else
    	{
    	    lab_db.setText(m_ic.getMessage("DB_IS_NOT_CONNECTED"));
    	}
        */

    }


    public void getLocalData()
    {
    	File fl = new File("../data/nutrition/");
    	boolean found = false;
    
    	if (!fl.exists())
    	{
    	    found = false;
    	}
    	else
    	{
    	    String fls[] = fl.list();
    
    	    for (int i=0; i<fls.length; i++)
    	    {
        		if (fls[i].startsWith("SR"))
        		{
        		    lab_file.setText(fls[i].substring(0, fls[i].indexOf("_")));
        
        		    found = true;
        		    break;
        		}
    	    }
    
    
    
    	}

    	if (!found)
    	    lab_file.setText(m_ic.getMessage("NO_LOCAL_DATA_AVAILABLE"));

    }

    public void init()
    {
        setLayout(null);
	setBounds(0,0,300,300);

        JPanel a = new JPanel();
	a.setBounds(0,0,440,430);
	a.setLayout(null);
        a.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("NUTRITION")));

	JPanel p1 = new JPanel();
	p1.setLayout(null);
	p1.setBounds(10, 20, 420, 160);
        p1.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("ABOUT_NUT_DATA")));

	JLabel l1 = new JLabel(m_ic.getMessage("NUT_DATA_DESC"));
	l1.setBounds(20, 10, 390, 150);
	p1.add(l1);


	a.add(p1);


	JPanel p2 = new JPanel();
	p2.setBounds(10,180, 420, 100);
	p2.setLayout(null); 
	p2.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("NUTRITION_DATA_INFO")));


	JLabel lab = new JLabel();
	lab.setText(m_ic.getMessage("NUTRITION_DATA_DB"));
	lab.setBounds(40, 30, 150, 25);
	lab.setHorizontalAlignment(SwingConstants.LEFT);
	p2.add(lab);

	lab_db = new JLabel("x");
	lab_db.setBounds(220, 30, 150, 25);
	p2.add(lab_db);



	lab = new JLabel();
	lab.setText(m_ic.getMessage("NUTRITION_DATA_FILE"));
	lab.setBounds(40, 55, 150, 25);
	lab.setHorizontalAlignment(SwingConstants.LEFT);
	p2.add(lab);

	lab_file = new JLabel("x");
	lab_file.setBounds(220, 55, 150, 25);
	p2.add(lab_file);



	a.add(p2);
/*
	JPanel p3 = new JPanel();
	p3.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("NUTRITION_ACTION")));
	p3.setLayout(new GridLayout(3,2));

	JButton b889 = new JButton("Deer");
	p3.add(b889);

	JLabel ll = new JLabel("djhfjs dfhjdshf shfkjhsd fsdjk");
	p3.add(ll);

	JButton b33889 = new JButton("Deeds ffr");
	p3.add(b33889);

	JLabel ll2 = new JLabel("djhfjs dfhjdshf shfkjhsd fsdjk");
	p3.add(ll2);


	a.add(p3);
*/


        add(a, BorderLayout.NORTH);
    }

    @Override
    public void saveProps()
    {
    }
}
