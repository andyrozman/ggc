package ggc.pump.gui.bre;

import ggc.pump.util.DataAccessPump;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import com.atech.utils.ATSwingUtils;

public class BasalRateEstimator extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 3285623806907044947L;
    DataAccessPump m_da = DataAccessPump.getInstance();
    JList lst_bgs, lst_basals_old, lst_basals_new, lst_ratios;
    
    public BasalRateEstimator()
    {
        ATSwingUtils.initLibrary();
        init();
        this.setSize(800, 600);
    }

    private void init()
    {
        //this.setLayout(null);
        this.setTitle("Basal Rate Estimator");
        JLabel label;
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        this.add(panel);
        
        ATSwingUtils.getTitleLabel("Basal Rate Estimator", 0, 20, 800, 35, panel, ATSwingUtils.FONT_BIG_BOLD); 
        
        ATSwingUtils.getLabel("List of BGs:", 50, 85, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
            
        lst_bgs = new JList();
        JScrollPane scr = new JScrollPane(lst_bgs);
        scr.setBounds(40, 110, 160, 160);
        panel.add(scr);
        
        ATSwingUtils.getLabel("List of Old Basals:", 220, 85, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 

        this.lst_basals_old = new JList();
        scr = new JScrollPane(this.lst_basals_old);
        scr.setBounds(210, 110, 160, 160);
        panel.add(scr);

        ATSwingUtils.getLabel("List of Ratios:", 390, 85, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
        
        this.lst_ratios = new JList();
        scr = new JScrollPane(this.lst_ratios);
        scr.setBounds(380, 110, 160, 160);
        panel.add(scr);

        ATSwingUtils.getLabel("List of New Basals:", 50, 280, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
        
        this.lst_basals_new = new JList();
        scr = new JScrollPane(this.lst_basals_new);
        scr.setBounds(40, 310, 160, 240);
        panel.add(scr);
        
        JPanel panel_graph = new JPanel();
        panel_graph.setBorder(new TitledBorder("Basal Rate Display"));
        panel_graph.setBounds(210, 280, 560, 270);
        panel_graph.setBackground(new Color(0, 191, 255));
        
        panel.add(panel_graph);

        ATSwingUtils.getLabel("Date of display: ", 570, 120, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
        

        
        ATSwingUtils.getLabel("02/02/2009", 690, 120, 200, 25, panel, ATSwingUtils.FONT_NORMAL); 
        
        ATSwingUtils.getButton("Change Date", 570, 170, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_date", this, m_da);
        
        JButton b = ATSwingUtils.getButton("Configure Ratios", 570, 210, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_ratios", this, m_da);
        b.setEnabled(false);

        ATSwingUtils.getButton("Algorithm", 570, 250, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "algorithm", this, m_da);
        
        //ATSwingUtils.getButton("Change Date", 500, 50, 150, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_date", this, m_da);
        
    }
    
    
    public static void main(String args[])
    {
        BasalRateEstimator bre = new BasalRateEstimator();
        
        bre.setVisible(true);
        
        
        
    }

    public void actionPerformed(ActionEvent e)
    {
        System.out.println(e.getActionCommand() + " is currently not supported.");
    }
    
    
    
    
    
    
    
}

