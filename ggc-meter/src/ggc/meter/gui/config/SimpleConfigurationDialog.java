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
 *  Filename: PropertiesDialog
 *
 *  Purpose:  Dialog for setting properties for application.
 *
 *  Author:   andyrozman {andy@atech-software.com}
 *
 */
package ggc.meter.gui.config;


import ggc.meter.data.cfg.MeterConfiguration;
import ggc.meter.device.MeterInterface;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.atech.utils.ATDataAccessAbstract;


public class SimpleConfigurationDialog extends JDialog implements ActionListener //, HelpCapable
{

    /**
     * 
     */
    private static final long serialVersionUID = -1218534990235155505L;
    private I18nControl m_ic = I18nControl.getInstance();        
    private ATDataAccessAbstract m_da; // = DataAccessMeter.getInstance();

    JTextArea editArea;
    private JPanel prefPane;
    JButton help_button;

    MeterConfiguration config = null;
    


    public SimpleConfigurationDialog(ATDataAccessAbstract da)
    {
        super(da.getMainParent(), "", true);
        
        m_da = da;
        
        m_da.addComponent(this);

        setSize(650,400);
        setTitle(m_ic.getMessage("SIMPLE_PREFERENCES"));
        m_da.centerJDialog(this, m_da.getCurrentComponentParent());

        help_button = m_da.createHelpButtonBySize(120, 25, this);
        
        init();
        this.loadConfiguration();
        this.setResizable(false);
        this.setVisible(true);
    }


    private void init()
    {
        //Dimension dim = new Dimension(120, 25);



//	prefPane = new JPanel(new BorderLayout());
	
        prefPane = new JPanel(null);
        
        JLabel label = new JLabel(m_ic.getMessage("SIMPLE_PREFERENCES"));
        label.setBounds(0,20, 650, 30);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(m_da.getFont(DataAccessMeter.FONT_BIG_BOLD));
        prefPane.add(label);
        
        
        editArea = new JTextArea(); //15, 60);
        editArea.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
        editArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        JScrollPane scrollingText = new JScrollPane(editArea);        
        scrollingText.setBounds(30,80,430,230);
        
        prefPane.add(scrollingText);

        
        help_button.setBounds(30, 320, 110, 25);
        help_button.addActionListener(this);
        help_button.setActionCommand("help");
        prefPane.add(help_button);
        
        
        
        //set the buttons up...
        JButton button = new JButton("  " + m_ic.getMessage("SAVE"));
        //okButton.setPreferredSize(dim);
        button.setIcon(m_da.getImageIcon_22x22("disk_blue.png", this));
        button.setActionCommand("save");
        button.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        button.setBounds(220, 320, 110, 25);
        button.addActionListener(this);
        prefPane.add(button);

        button = new JButton("  " +m_ic.getMessage("CLOSE"));
        //cancelButton.setPreferredSize(dim);
        button.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        button.setActionCommand("close");
        button.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        button.setBounds(350, 320, 110, 25);
        button.addActionListener(this);
        prefPane.add(button);

        
        button = new JButton(m_ic.getMessage("INSERT_METER"));
        //cancelButton.setPreferredSize(dim);
        //button.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        button.setActionCommand("select_meter");
        button.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        button.setBounds(480, 180, 140, 25);
        button.addActionListener(this);
        prefPane.add(button);

        button = new JButton(m_ic.getMessage("INSERT_TZ"));
        //cancelButton.setPreferredSize(dim);
        //button.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        button.setActionCommand("insert_tz");
        button.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        button.setBounds(480, 220, 140, 25);
        button.addActionListener(this);
        prefPane.add(button);
        
        
/*
        button = new JButton(m_ic.getMessage("COPY"));
        button.setActionCommand("edit_copy");
        button.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        button.setBounds(520, 60, 110, 25);
        button.addActionListener(this);
        prefPane.add(button);
        
        button = new JButton(m_ic.getMessage("CUT"));
        button.setActionCommand("edit_copy");
        button.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        button.setBounds(520, 90, 110, 25);
        button.addActionListener(this);
        prefPane.add(button);
        
        
        button = new JButton(m_ic.getMessage("PASTE"));
        button.setActionCommand("edit_copy");
        button.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        button.setBounds(520, 120, 110, 25);
        button.addActionListener(this);
        prefPane.add(button);
        
        
        button = new JButton(m_ic.getMessage("DELETE"));
        button.setActionCommand("edit_delete");
        button.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        button.setBounds(520, 150, 110, 25);
        button.addActionListener(this);
        prefPane.add(button);
  */      
        //this.editArea.
        //this.editArea.c
        
/*        
        JButton applyButton = new JButton("  " +m_ic.getMessage("APPLY"));
        applyButton.setPreferredSize(dim);
        applyButton.setIcon(m_da.getImageIcon_22x22("flash.png", this));
        applyButton.setActionCommand("apply");
        applyButton.addActionListener(this);

        //...and align them in a row at the buttom.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBounds(20, 280, 400, 30);
        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        
        buttonPanel.add(help_button);
*/
        //prefPane.add(buttonPanel, BorderLayout.SOUTH);
        //prefPane.add(this.prefPane, BorderLayout.CENTER);

        //getContentPane().add(prefTreePane, BorderLayout.WEST);
	//getContentPane().add(list, BorderLayout.WEST);
        getContentPane().add(prefPane, BorderLayout.CENTER);
    }


    
    public void loadConfiguration()
    {
        this.config = new MeterConfiguration(true);
        this.editArea.setText(this.config.getConfigText());
    }
    


    private void saveConfiguration()
    {
        System.out.println("saveConfiguration");
        this.config.setConfigText(this.editArea.getText());
        this.config.saveConfig();
    }

    
    
    
    

    // ---
    // ---  End
    // ---


    



    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) 
    {
        String action = e.getActionCommand();

        if (action.equals("save")) 
        {
            saveConfiguration();
            //System.out.println("Ok Action not implemented");
            
//            save();
//            ok_action = true;
//            this.dispose();
        }
        else if (action.equals("select_meter"))
        {
            MeterDeviceSelectorDialog mdsd = new MeterDeviceSelectorDialog(this, m_da);
            
            if (mdsd.wasAction())
            {
            
            MeterInterface mi = (MeterInterface)mdsd.getSelectedObject();
            
            StringBuffer sb = new StringBuffer();
            sb.append("#\n");
            sb.append("#  Meter Device #X\n");
            sb.append("#\n");
            sb.append("METER_X_NAME=My meter\n");
            sb.append("METER_X_COMPANY=" + mi.getDeviceCompany().getName() + "\n");
            sb.append("METER_X_DEVICE=" + mi.getName() + "\n");
            sb.append("METER_X_CONNECTION_PARAMETER=COM9\n");
            
            
            this.editArea.insert(sb.toString(), this.editArea.getCaretPosition());
            }
        }
        else if (action.equals("close")) 
        {
            this.dispose();
        }
        else if (action.equals("help")) 
        {
            new SimpleConfigurationHelp(this);
        }
        else if (action.equals("insert_tz")) 
        {
            SimpleConfigurationTZDialog sc = new SimpleConfigurationTZDialog(this, m_da);
            
            if (sc.wasAction())
            {
                this.editArea.insert(sc.getData(), this.editArea.getCaretPosition());
            }
            
        }
        else if (action.equals("edit_cut"))
        {
            this.editArea.cut();
        }
        else if (action.equals("edit_copy"))
        {
            this.editArea.copy();
        }
        else if (action.equals("edit_paste"))
        {
            this.editArea.paste();
        }
        else if (action.equals("edit_delete"))
        {
            this.editArea.cut();
        }
        else
            System.out.println("PropertiesFrame: Unknown command: " + action);

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
        //return ((HelpCapable)this.selected_panel).getHelpId();
        return "";
    }
    
    
    public static void main(String[] args)
    {
        JFrame fr = new JFrame();
        fr.setBounds(0,0,800,600);
        
        DataAccessMeter da = DataAccessMeter.createInstance(fr);
        da.setMainParent(fr);
        
        new SimpleConfigurationDialog(da);
        
        
    }
    
    
}
