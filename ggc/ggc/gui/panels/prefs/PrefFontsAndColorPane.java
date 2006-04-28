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
 *  Filename: prefFontsAndColorPane.java
 *  Purpose:  Preferences Pane to choose Fonts and Colors.
 *
 *  Author:   schultd
 *  Author:   andyrozman 
 */

package ggc.gui.panels.prefs;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.Box;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ggc.data.DailyValues;
import ggc.data.DailyValuesRow;
import ggc.db.hibernate.ColorSchemeH;
import ggc.gui.dialogs.SchemeDialog;
import ggc.gui.view.DailyGraphView;
import ggc.util.DataAccess;


public class PrefFontsAndColorPane extends AbstractPrefOptionsPanel implements MouseListener, ActionListener, ListSelectionListener, ItemListener
{
    private JList itemList;
    private JLabel lblcolor;
    private Vector items;

    private JButton btNewScheme = null;
    private JComboBox comboScheme = null;

    private Hashtable color_schemes = null;
    private ColorSchemeH selected_sheme = null;
    private String[] av_schemes_names = null;

    private DailyGraphView dgv = null;

    JDialog parent = null;

    long selected_id = 0;

    public PrefFontsAndColorPane(JDialog dialog)
    {
	this.parent = dialog;

	color_schemes = settings.getColorSchemes();
	selected_sheme = settings.getSelectedColorScheme();
	av_schemes_names = new String[color_schemes.size()];

	int i=0;
	for (Enumeration en = this.color_schemes.keys(); en.hasMoreElements(); ) 
	{
	    ColorSchemeH cs = (ColorSchemeH)color_schemes.get((String)en.nextElement());
	    av_schemes_names[i] = cs.getName();
	    i++;

	}

/*
	for (int i=1; i<=color_schemes.size(); i++)
	{
	    ColorSchemeH cs = (ColorSchemeH)color_schemes.get(""+i);
	    av_schemes_names[i-1] = cs.getName();

	    if (cs.equals(selected_sheme))
	    {
		selected_id = i-1;
	    }
	}
	*/

        items = new Vector();

	items.add(m_ic.getMessage("BG_HIGH_ZONE")); 
	items.add(m_ic.getMessage("BG_TARGET_ZONE")); 
	items.add(m_ic.getMessage("BG_LOW_ZONE")); 
	items.add(m_ic.getMessage("BG")); 
	items.add(m_ic.getMessage("BG_AVERAGE")); 
	items.add(m_ic.getMessage("BREAD_UNITS")); 
	items.add(m_ic.getMessage("INSULIN")+" " + settings.getIns1Abbr()); 
	items.add(m_ic.getMessage("INSULIN")+" " + settings.getIns2Abbr()); 
	items.add(m_ic.getMessage("INSULIN")); 
	items.add(m_ic.getMessage("INS_SLASH_BU_QUOTIENT")); 

        init();
    }

    private void init()
    {
        setLayout(new BorderLayout());
    
    	JPanel schemePanel = new JPanel(new GridLayout(1, 3));
    	schemePanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("COLOR_SCHEME_SELECT")));
    
    	schemePanel.add(new JLabel(m_ic.getMessage("SELECTED_COLOR_SCHEME")+":"));
    	schemePanel.add(comboScheme = new JComboBox(av_schemes_names));

        JPanel butPanel = new JPanel(new GridLayout(1, 3));

        schemePanel.add(new JButton(m_ic.getMessage("ADD")));
        schemePanel.add(new JButton(m_ic.getMessage("EDIT_DEL_SHORT")));
        //butPanel.add(btNewScheme = new JButton(m_ic.getMessage("ADD")));

        //butPanel.add(new JButton(m_ic.getMessage("EDIT_DEL")));
        //butPanel.add(new JButton(m_ic.getMessage("-")));

    	//schemePanel.add(btNewScheme = new JButton(m_ic.getMessage("NEW")));
        //schemePanel.add(butPanel);
    	//btNewScheme.addActionListener(this);
        //btNewScheme.setFont(m_da.getFont(DataAccess.FONT_NORMAL));

    	//System.out.println("SS: " + this.selected_sheme.getName());
    
    	comboScheme.setSelectedItem(this.selected_sheme.getName());
    	comboScheme.addItemListener(this);

        itemList = new JList(items);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.addListSelectionListener(this); 

        JPanel colorPanel = new JPanel(new BorderLayout());
        colorPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("COLORS")));

        JPanel a = new JPanel(new GridLayout(1, 1));
        a.add(lblcolor = new JLabel(" "));

        lblcolor.setOpaque(true);
        lblcolor.setBorder(BorderFactory.createLineBorder(Color.black));

        lblcolor.addMouseListener(this); 

        colorPanel.add(itemList, BorderLayout.WEST);
        colorPanel.add(a, BorderLayout.CENTER);


    	JPanel testingPanel = new JPanel(new BorderLayout());
    	dgv = new DailyGraphView(this.selected_sheme, createDailyGraphValues());
    	testingPanel.setPreferredSize(new Dimension(150, 170));
    	testingPanel.add(dgv, BorderLayout.CENTER);
    
    
    	Box box = Box.createVerticalBox();
    	box.add(schemePanel);
    	box.add(colorPanel);
    	box.add(testingPanel);

        add(box);

        itemList.setSelectedIndex(0);
    }

    public DailyValues createDailyGraphValues()
    {
    	DailyValues dv = new DailyValues(); 
    
    	dv.addRow(new DailyValuesRow("04.04.2006", "7:30", "4.3", "6", "", "8", "", ""));
    	dv.addRow(new DailyValuesRow("04.04.2006", "9:35", "5.9", "", "", "", "", ""));
    	dv.addRow(new DailyValuesRow("04.04.2006", "12:15", "8.1", "10", "", "12", "", ""));
    	dv.addRow(new DailyValuesRow("04.04.2006", "14:15", "6.7", "", "", "", "", ""));
    	dv.addRow(new DailyValuesRow("04.04.2006", "19:30", "7.8", "12", "", "14", "", ""));
    	dv.addRow(new DailyValuesRow("04.04.2006", "21:15", "5.9", "", "", "", "", ""));
    	dv.addRow(new DailyValuesRow("04.04.2006", "23:30", "", "", "36", "", "", ""));
    
    	return dv;
    }



    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     */
    public void mouseClicked(MouseEvent e)
    {
	if (this.selected_sheme.getCustom_type()==0)
	{
	    JOptionPane.showMessageDialog(this, m_ic.getMessage("COLOR_SCHEME_PREDEFINED"), m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
	    return;
	}

	Color col = JColorChooser.showDialog(null, m_ic.getMessage("CHOOSE_A_COLOR"), Color.black);
	if (col != null) 
	{
	    setColor(itemList.getSelectedValue().toString(), col);
	    lblcolor.setBackground(col);
	    changed = true;
	    dgv.redraw();
	}
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}


    public Color getColor(String name)
    {
	int color = 0;

	if (name.equals(m_ic.getMessage("BG_HIGH_ZONE")))
	    color = this.selected_sheme.getColor_bg_high();
	else if (name.equals(m_ic.getMessage("BG_TARGET_ZONE")))
	    color = this.selected_sheme.getColor_bg_target();
	else if (name.equals(m_ic.getMessage("BG_LOW_ZONE")))
	    color = this.selected_sheme.getColor_bg_low(); 
	else if (name.equals(m_ic.getMessage("BG")))
	    color = this.selected_sheme.getColor_bg(); 
	else if (name.equals(m_ic.getMessage("BG_AVERAGE")))
	    color = this.selected_sheme.getColor_bg_avg(); 
	else if (name.equals(m_ic.getMessage("BREAD_UNITS")))
	    color = this.selected_sheme.getColor_ch();
	else if (name.equals(m_ic.getMessage("INSULIN")+" " + settings.getIns1Abbr()))
	    color = this.selected_sheme.getColor_ins1();
	else if (name.equals(m_ic.getMessage("INSULIN")+" " + settings.getIns2Abbr()))
	    color = this.selected_sheme.getColor_ins2(); 
	else if (name.equals(m_ic.getMessage("INSULIN")))
	    color = this.selected_sheme.getColor_ins();
	else if (name.equals(m_ic.getMessage("INS_SLASH_BU_QUOTIENT")))
	    color = this.selected_sheme.getColor_ins_perbu();

	return m_da.getColor(color);

    }


    public void setColor(String name, Color clr)
    {
    	int color = clr.getRGB();
    
    	if (name.equals(m_ic.getMessage("BG_HIGH_ZONE")))
    	    this.selected_sheme.setColor_bg_high(color);
    	else if (name.equals(m_ic.getMessage("BG_TARGET_ZONE")))
    	    this.selected_sheme.setColor_bg_target(color);
    	else if (name.equals(m_ic.getMessage("BG_LOW_ZONE")))
    	    this.selected_sheme.setColor_bg_low(color); 
    	else if (name.equals(m_ic.getMessage("BG")))
    	    this.selected_sheme.setColor_bg(color); 
    	else if (name.equals(m_ic.getMessage("BG_AVERAGE")))
    	    this.selected_sheme.setColor_bg_avg(color); 
    	else if (name.equals(m_ic.getMessage("BREAD_UNITS")))
    	    this.selected_sheme.setColor_ch(color);
    	else if (name.equals(m_ic.getMessage("INSULIN")+" " + settings.getIns1Abbr()))
    	    this.selected_sheme.setColor_ins1(color);
    	else if (name.equals(m_ic.getMessage("INSULIN")+" " + settings.getIns2Abbr()))
    	    this.selected_sheme.setColor_ins2(color); 
    	else if (name.equals(m_ic.getMessage("INSULIN")))
    	    this.selected_sheme.setColor_ins(color);
    	else if (name.equals(m_ic.getMessage("INS_SLASH_BU_QUOTIENT")))
    	    this.selected_sheme.setColor_ins_perbu(color);

    }


    private int findIndex(String col)
    {
    	for (int i=0; i<this.av_schemes_names.length; i++)
    	{
    	    if (this.av_schemes_names[i].equals(col))
    	    {
                return i;
    	    }
    	}
    
    	return -1;
    }


    /**
     * Invoked when an item has been selected or deselected by the user.
     * The code written for this method performs the operations
     * that need to occur when an item is selected (or deselected).
     */
    public void itemStateChanged(ItemEvent e)
    {
        this.selected_sheme = (ColorSchemeH)this.color_schemes.get(this.comboScheme.getSelectedItem().toString());
        dgv.setScheme(this.selected_sheme);
    }


    public void valueChanged(ListSelectionEvent e)
    {
        lblcolor.setBackground(getColor(itemList.getSelectedValue().toString()));
    }


    public void actionPerformed(ActionEvent e)
    {
    	SchemeDialog sd = new SchemeDialog(this.parent, this.av_schemes_names);
    
    	if (sd.actionSuccesful())
    	{
    	    String[] str = sd.getActionResult();
    
    	    ColorSchemeH cs = (ColorSchemeH)this.color_schemes.get(str[2]);

            //System.out.println("As Template: " + str[2]);
    
    	    this.selected_sheme = new ColorSchemeH(str[1], 1, cs.getColor_bg(),
    				cs.getColor_bg_avg(), cs.getColor_bg_low(), cs.getColor_bg_high(),
    				cs.getColor_bg_target(), cs.getColor_ins(), cs.getColor_ins1(),
    				cs.getColor_ins2(), cs.getColor_ins_perbu(), cs.getColor_ch());
    
    	    this.color_schemes.put(this.selected_sheme.getName(), selected_sheme);
    
    	    String[] strs = new String[(this.av_schemes_names.length +1)];
    
    	    int i=0;
    
    	    for ( ; i<this.av_schemes_names.length; i++)
    	    {
                strs[i] = this.av_schemes_names[i];
    	    }
    
    	    strs[i] = str[1];
    
    	    this.av_schemes_names = strs;
    
    	    this.comboScheme.addItem(str[1]);
    	    this.comboScheme.setSelectedIndex(av_schemes_names.length-1);
    	    // sel
    	    dgv.setScheme(this.selected_sheme);
    
    	}

    }


    public void saveProps()
    {

    	for (Enumeration en = this.color_schemes.keys(); en.hasMoreElements(); ) 
    	{
    	    String key = (String)en.nextElement();
    	    ColorSchemeH cs = (ColorSchemeH)this.color_schemes.get(key);
    
    	    if (cs.getCustom_type()==1)
    	    {
        		if (cs.getId()==0)
        		{
        		    cs.setId(m_da.getDb().addHibernate(cs));
        		}
        		else
        		{
        		    m_da.getDb().editHibernate(cs);
        		}
    	    }
    	}
    
    	settings.setColorSchemes(this.color_schemes, false);
    	settings.setColorSchemeObject(comboScheme.getSelectedItem().toString());

    }
}