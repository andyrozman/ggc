

package ggc.plugin.graph;

import ggc.core.data.GlucoValues;
import ggc.plugin.graph.panel.AxesEditorPanel;
import ggc.plugin.graph.panel.DefinitionsPanel;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import com.atech.graphics.calendar.DateRangeData;
import com.atech.graphics.calendar.DateRangeSelectionPanel2;
import com.atech.graphics.layout.ZeroLayout;
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
 *  Filename:     ###--###  
 *  Description:  ###--###
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class PlugInGraphDialog extends JDialog implements ActionListener, HelpCapable // JFrame
{

    /**
     * 
     */
    private static final long serialVersionUID = -1657001410904620188L;
    
    private DataAccessPlugInBase m_da = null;
    private I18nControlAbstract m_ic = null;

    //private CourseGraphView cGV;
    private JPanel cGV;

    private DateRangeSelectionPanel2 dRS;
    private JButton help_button = null;
    
    private JPanel main_panel = null;
    DefinitionsPanel def_panel = null;
    AxesEditorPanel axes_panel = null;

    /**
     * Constructor
     * 
     * @param da
     * @param selected_name 
     */
    public PlugInGraphDialog(DataAccessPlugInBase da, String selected_name)
    {
        super(da.getMainParent(), "", true);
        m_ic = da.getI18nControlInstance();
        setTitle(m_ic.getMessage("COURSE_GRAPH") + " [" + m_ic.getMessage("NOT_WORKING_100PRO") + "]");

        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();

        m_da.addComponent(this);
        
        setSize(800, 600);
        //this.setLayout(new ZeroLayout(800, 600));
        m_da.centerJDialog(this, da.getMainParent());

        main_panel = new JPanel();
        main_panel.setLayout(new ZeroLayout(800, 600));
        main_panel.setBounds(0, 0, 800, 600);
        
        cGV = new JPanel(); //new CourseGraphView();
        cGV.setBounds(0, 0, 800, 400);
        cGV.setBackground(Color.cyan);
        
//        main_panel.add(cGV, ZeroLayout.DYNAMIC);

        //JPanel controlPanel = 
            initControlPanel();
        //getContentPane().add(controlPanel, ZeroLayout.STATIC);

        //main_panel.add(controlPanel, ZeroLayout.STATIC);
        //m_da.enableHelp(this);
        getContentPane().add(main_panel);

        setVisible(true);
    }

    private void initControlPanel()
    {
        /*
        JPanel cPanel = new JPanel();
        cPanel.setLayout(new ZeroLayout(800, 100));
        cPanel.setSize(800, 100);
*/
    	
    	
    	
    	
        dRS = new DateRangeSelectionPanel2(m_da, DateRangeData.RANGE_TWO_WEEKS);
        dRS.setBounds(0, 450, 200, 150);
        
        main_panel.add(dRS, ZeroLayout.STATIC_ON_LOWER_EDGE);

        // axises
        
        this.axes_panel = new AxesEditorPanel(m_da);
        
        //JPanel p1 = new JPanel ();
        this.axes_panel.setBounds(200, 450, 350, 150);
        //p1.setBackground(Color.magenta);
        
        main_panel.add(this.axes_panel, ZeroLayout.STATIC_ON_LOWER_EDGE);

        // source, type
        def_panel = new DefinitionsPanel(m_da);
        def_panel.setBounds(550, 450, 250, 150);
        //p2.setBackground(Color.yellow);
        
        main_panel.add(def_panel, ZeroLayout.STATIC_ON_LOWER_EDGE);
        
        
        
        /*
        DataPlotSelectorPanel selectionPanel = new DataPlotSelectorPanel(DataPlotSelectorPanel.BG_AVG_MASK);
        selectionPanel.disableChoice(DataPlotSelectorPanel.BG_MASK | DataPlotSelectorPanel.CH_MASK
                | DataPlotSelectorPanel.INS1_MASK | DataPlotSelectorPanel.INS2_MASK
                | DataPlotSelectorPanel.INS_TOTAL_MASK);
        cGV.setData(selectionPanel.getPlotData());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        help_button = m_da.createHelpButtonBySize(120, 25, this);
        buttonPanel.add(help_button);

        // Dimension dim = new Dimension(80, 20);
        Dimension dim = new Dimension(120, 25);
        JButton drawButton = new JButton("    " + m_ic.getMessage("DRAW"));
        drawButton.setPreferredSize(dim);
        drawButton.setIcon(m_da.getImageIcon_22x22("paint.png", this));
        drawButton.setActionCommand("draw");
        drawButton.addActionListener(this);

        JButton closeButton = new JButton("    " + m_ic.getMessage("CLOSE"));
        closeButton.setPreferredSize(dim);
        closeButton.setActionCommand("close");
        closeButton.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        closeButton.addActionListener(this);
        buttonPanel.add(drawButton);
        buttonPanel.add(closeButton);

        //cPanel.add(dRS, BorderLayout.WEST);
        //cPanel.add(selectionPanel, BorderLayout.CENTER);
        //cPanel.add(buttonPanel, BorderLayout.SOUTH);
*/
        //return cPanel;
    }

    private void setNewDateRange()
    {
        //cGV.setGlucoValues(new GlucoValues(dRS.getStartCalendar(), dRS.getEndCalendar()));
    }

    private void closeDialog()
    {
        cGV = null;
        this.dispose();
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("draw"))
        {
            setNewDateRange();
            // cGV.repaint(this.getBounds());
            cGV.repaint();
        }
        else if (action.equals("close"))
        {
            closeDialog();
        }
        else
            System.out.println("CourseGraphFrame: Unknown command: " + action);
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
        return "GGC_BG_Graph_Course";
    }

}
