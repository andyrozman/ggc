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
 *  Filename: HbA1cFrame.java
 *  Purpose:  gives a "guess" about the current HbA1c
 *
 *  Author:   schultd
 */

package ggc.gui.dialogs;


import ggc.core.data.HbA1cValues;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;
import ggc.gui.view.HbA1cView;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.atech.help.HelpCapable;


public class HbA1cDialog extends JDialog implements ActionListener, HelpCapable
{
    
    JButton help_button;    

    private I18nControl m_ic = I18nControl.getInstance();        

    //private static HbA1cFrame singleton = null;
    private HbA1cView hbView;
    private HbA1cValues hbValues;

    private JLabel lblHbA1c;
    private JLabel lblExp;
    private JLabel lblBGAvg;
    private JLabel lblReadings;
    private JLabel lblReadingsPerDay;

    private DataAccess m_da = null;


    public HbA1cDialog(DataAccess da)
    {
        super(da.getMainParent(), "HbA1c", true);
        this.m_da = da;
        init();

        //hbValues = this.m_da.getHbA1c(new GregorianCalendar());
        hbValues = this.m_da.getDb().getHbA1c(new GregorianCalendar(), false);
        updateLabels();

        hbView.setHbA1cValues(hbValues);
        
        this.m_da.enableHelp(this);
        this.setTitle(m_ic.getMessage("CALCULATED_HBA1C"));
        
	this.setVisible(true);
    }

    public void updateLabels()
    {
        lblExp.setText(hbValues.getValuation());
        lblHbA1c.setText(DataAccess.Decimal2Format.format(hbValues.getHbA1c_Method3()) + " %");
        lblBGAvg.setText(DataAccess.Decimal2Format.format(hbValues.getAvgBG()));
        lblReadings.setText(hbValues.getReadings() + "");
        lblReadingsPerDay.setText(DataAccess.Decimal2Format.format(hbValues.getReadingsPerDay()));
    }

    private void init()
    {
	
        getContentPane().setLayout(null);
        setBounds(100, 100, 550, 460);

        // left panel;
        hbView = new HbA1cView();
        hbView.setBounds(0, 0, 300, 460);
        getContentPane().add(hbView, null);
        
        // right panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        infoPanel.setBounds(300, 0, 250, 460);
        getContentPane().add(infoPanel, null);

        JLabel label = new JLabel(m_ic.getMessage("CALCULATED_HBA1C"));
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setBounds(0, 20, 250, 35);
        label.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(label); //, what)

        // current hba1c
        label = new JLabel(m_ic.getMessage("YOUR_CURRENT_HBA1C")+":");
        label.setBounds(0, 90, 250, 25);
        label.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(label);
        
        lblHbA1c = new JLabel();
        lblHbA1c.setFont(new Font("Dialog", Font.BOLD, 16));
        //lblHbA1c.setBounds(100, 130, 50, 30);
        lblHbA1c.setBounds(0, 110, 250, 30);
        lblHbA1c.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(lblHbA1c);
        
        // valuation
        label = new JLabel(m_ic.getMessage("VALUATION")+":");
        label.setBounds(20, 180, 100, 25);
        infoPanel.add(label);
        
        lblExp = new JLabel();
        lblExp.setBounds(100, 180, 150, 25);
        infoPanel.add(lblExp);

        
        // bg avg.
        label = new JLabel(m_ic.getMessage("BG")+" "+ m_ic.getMessage("AVG")+":");
        label.setBounds(20, 230, 100, 25);
        infoPanel.add(label);

        lblBGAvg = new JLabel();
        lblBGAvg.setBounds(170, 230, 50, 25);
        infoPanel.add(lblBGAvg);

        // readings
        label = new JLabel(m_ic.getMessage("READINGS")+":");
        label.setBounds(20, 260, 100, 25);
        infoPanel.add(label);

        lblReadings = new JLabel();
        lblReadings.setBounds(170, 260, 50, 25);
        infoPanel.add(lblReadings);

        // reading / day
        label = new JLabel(m_ic.getMessage("READINGS_SLASH_DAY")+":");
        label.setBounds(20, 290, 100, 25);
        infoPanel.add(label);

        lblReadingsPerDay = new JLabel();
        lblReadingsPerDay.setBounds(170, 290, 50, 25);
        infoPanel.add(lblReadingsPerDay);
        
        JButton closeButton = new JButton("   " + m_ic.getMessage("CLOSE"));
        //closeButton.setPreferredSize(new Dimension(100, 25));
        closeButton.setBounds(5, 395, 110, 25);
        closeButton.setIcon(m_da.getImageIcon_22x22("cancel.png", this));        
	closeButton.addActionListener(this);
	closeButton.setActionCommand("close");
	infoPanel.add(closeButton);
	
        
        this.help_button = this.m_da.createHelpButtonByBounds(125, 395, 110, 25, this);
        infoPanel.add(this.help_button);
        //buttonPanel.add(help_button);

    }

    
    
    
    public void setHbA1cText(String s)
    {
        lblHbA1c.setText(s);
    }

    public void setBGAvgText(String s)
    {
        lblBGAvg.setText(s);
    }

    public void setReadingsText(String s)
    {
        lblReadings.setText(s);
    }

    public void setReadingsPerDayText(String s)
    {
        lblReadingsPerDay.setText(s);
    }

    public void setExpressivnessText(String s)
    {
        lblExp.setText(s);
    }


    private void closeDialog()
    {
        hbView = null;
        this.dispose();
    }



    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) 
    {
        String action = e.getActionCommand();

        if (action.equals("close")) 
        {
            this.closeDialog();
        }
        else
            System.out.println("HbA1cDialog:Unknown command: " + action);
    }
    
    
    // ****************************************************************
    // ******              HelpCapable Implementation             *****
    // ****************************************************************
    
    /* 
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
	return this.getRootPane();
    }

    /* 
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
	return this.help_button;
    }

    /* 
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
	return "pages.GGC_BG_HbA1c";
    }

    
}
