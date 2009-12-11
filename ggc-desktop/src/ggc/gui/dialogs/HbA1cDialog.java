package ggc.gui.dialogs;

import ggc.core.data.HbA1cValues;
import ggc.core.data.graph.GraphViewHbA1c;
import ggc.core.util.DataAccess;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.atech.graphics.graphs.GraphViewerPanel;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;


/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     HbA1cDialog2  
 *  Description:  Dialog for HbA1c, using new graph framework
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class HbA1cDialog extends JDialog implements ActionListener, HelpCapable
{

    private static final long serialVersionUID = 8918250955552579544L;

    private JButton help_button;
    private JLabel lblHbA1c;
    private JLabel lblExp;
    private JLabel lblBGAvg;
    private JLabel lblReadings;
    private JLabel lblReadingsPerDay;

    private DataAccess m_da = null;
    private I18nControlAbstract m_ic = null;
    GraphViewHbA1c gv; // = new GraphViewHbA1c();
    private HbA1cValues hbValues;

    /**
     * Constructor
     * 
     * @param da
     */
    public HbA1cDialog(DataAccess da)
    {
        super(da.getMainParent(), "HbA1c", true);
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        
        gv = new GraphViewHbA1c();
        this.hbValues = gv.getDataObject();
        
        init();
        updateLabels();

        this.m_da.enableHelp(this);
        this.setTitle(m_ic.getMessage("CALCULATED_HBA1C"));
        
        m_da.addComponent(this);

        this.setVisible(true);
    }

    private void updateLabels()
    {
        lblExp.setText(hbValues.getValuation());
        lblHbA1c.setText(DataAccess.Decimal2Format.format(hbValues.getHbA1c_Method3()) + " %");
        lblBGAvg.setText(DataAccess.Decimal2Format.format(hbValues.getAvgBG()));
        lblReadings.setText(hbValues.getReadings() + "");
        lblReadingsPerDay.setText(DataAccess.Decimal2Format.format(hbValues.getReadingsPerDay()));
    }

    private void init()
    {

        getContentPane().setLayout(new BorderLayout());
        setSize(700, 460);

        // left panel;
/*        hbView = new HbA1cView(this.hbValues);
        hbView.setMinimumSize(new Dimension(450, 460));
        hbView.setPreferredSize(hbView.getMinimumSize());
        getContentPane().add(hbView, BorderLayout.CENTER); */
        
        GraphViewerPanel gvp = new GraphViewerPanel(gv);
        gvp.setMinimumSize(new Dimension(450, 440)); // 450, 460
        gvp.setPreferredSize(gvp.getMinimumSize());
        getContentPane().add(gvp, BorderLayout.CENTER);
        

        // right panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setMinimumSize(new Dimension(250, 460));
        rightPanel.setPreferredSize(rightPanel.getMinimumSize());

        JPanel bottomRightPanel = new JPanel(new GridLayout(1, 2, 15, 2));
        bottomRightPanel.setMinimumSize(new Dimension(250, 30));
        bottomRightPanel.setPreferredSize(bottomRightPanel.getMinimumSize());
        bottomRightPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        JPanel infoPanel = new JPanel((LayoutManager) null);
        infoPanel.setMinimumSize(new Dimension(250, 430));
        infoPanel.setPreferredSize(infoPanel.getMinimumSize());

        getContentPane().add(rightPanel, BorderLayout.EAST);

        JLabel label = new JLabel(m_ic.getMessage("CALCULATED_HBA1C"));
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setBounds(0, 20, 250, 35);
        label.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(label); // , what)

        // current hba1c
        label = new JLabel(m_ic.getMessage("YOUR_CURRENT_HBA1C") + ":");
        label.setBounds(0, 90, 250, 25);
        label.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(label);

        lblHbA1c = new JLabel();
        lblHbA1c.setFont(new Font("Dialog", Font.BOLD, 16));
        // lblHbA1c.setBounds(100, 130, 50, 30);
        lblHbA1c.setBounds(0, 110, 250, 30);
        lblHbA1c.setHorizontalAlignment(JLabel.CENTER);
        infoPanel.add(lblHbA1c);

        // valuation
        label = new JLabel(m_ic.getMessage("VALUATION") + ":");
        label.setBounds(20, 180, 100, 25);
        infoPanel.add(label);

        lblExp = new JLabel();
        lblExp.setBounds(100, 180, 150, 25);
        infoPanel.add(lblExp);

        // bg avg.
        label = new JLabel(m_ic.getMessage("BG") + " " + m_ic.getMessage("AVG") + ":");
        label.setBounds(20, 230, 100, 25);
        infoPanel.add(label);

        lblBGAvg = new JLabel();
        lblBGAvg.setBounds(170, 230, 50, 25);
        infoPanel.add(lblBGAvg);

        // readings
        label = new JLabel(m_ic.getMessage("READINGS") + ":");
        label.setBounds(20, 260, 100, 25);
        infoPanel.add(label);

        lblReadings = new JLabel();
        lblReadings.setBounds(170, 260, 50, 25);
        infoPanel.add(lblReadings);

        // reading / day
        label = new JLabel(m_ic.getMessage("READINGS_SLASH_DAY") + ":");
        label.setBounds(20, 290, 100, 25);
        infoPanel.add(label);

        lblReadingsPerDay = new JLabel();
        lblReadingsPerDay.setBounds(170, 290, 50, 25);
        infoPanel.add(lblReadingsPerDay);

        JButton closeButton = new JButton("   " + m_ic.getMessage("CLOSE"));
        // closeButton.setPreferredSize(new Dimension(100, 25));
        closeButton.setBounds(5, 395, 110, 25);
        closeButton.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        closeButton.addActionListener(this);
        closeButton.setActionCommand("close");
        bottomRightPanel.add(closeButton);

        this.help_button = this.m_da.createHelpButtonByBounds(125, 395, 110, 25, this);
        bottomRightPanel.add(this.help_button);
        // buttonPanel.add(help_button);

        rightPanel.add(infoPanel, BorderLayout.CENTER);
        rightPanel.add(bottomRightPanel, BorderLayout.SOUTH);
        
    }

    private void closeDialog()
    {
        gv = null;
        this.dispose();
        m_da.removeComponent(this);
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
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /**
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }

    /**
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
        return this.help_button;
    }

    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return gv.getHelpId();
    }

}
