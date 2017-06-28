package ggc.gui.dialogs.config;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.util.DataAccess;

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
 *  Filename:     SchemeDialog  
 *  Description:  Dialog for adding Color Schemes
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// fix this

public class SchemeDialog extends JDialog implements ActionListener
{

    /**
     * 
     */
    private static final long serialVersionUID = -8843778623156001658L;
    private DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    private boolean m_actionDone = false;

    private JTextField tf_name;
    private JComboBox cb_template = null;
    private String[] schemes_names = null;


    /**
     * Constructor 
     * 
     * @param dialog
     * @param schemes_names
     */
    public SchemeDialog(JDialog dialog, String[] schemes_names)
    {
        super(dialog, "", true);
        this.schemes_names = schemes_names;

        Rectangle rec = dialog.getBounds();
        int x = rec.x + rec.width / 2;
        int y = rec.y + rec.height / 2;

        setBounds(x - 175, y - 150, 350, 300);
        this.setLayout(null);

        init();
        this.setVisible(true);
    }


    private void init()
    {
        ATSwingUtils.initLibrary();

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 350, 250);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        JLabel label = new JLabel(m_ic.getMessage("CREATE_NEW_SCHEME"));
        label.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 20, 350, 35);
        panel.add(label);

        label = new JLabel(m_ic.getMessage("NAME") + ":");
        label.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD));
        label.setBounds(40, 90, 80, 25);
        panel.add(label);

        tf_name = new JTextField();
        tf_name.setBounds(120, 90, 160, 25);
        tf_name.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        panel.add(tf_name);

        label = new JLabel(m_ic.getMessage("USE_AS_TEMPLATE") + ":");
        label.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        label.setBounds(40, 130, 280, 25);
        panel.add(label);

        cb_template = new JComboBox(this.schemes_names);
        cb_template.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        cb_template.setBounds(40, 160, 230, 25);
        panel.add(cb_template);

        JButton button = new JButton(m_ic.getMessage("OK"));
        button.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        button.setActionCommand("ok");
        button.addActionListener(this);
        button.setBounds(90, 210, 80, 25);
        panel.add(button);

        button = new JButton(m_ic.getMessage("CANCEL"));
        button.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        button.setActionCommand("cancel");
        button.addActionListener(this);
        button.setBounds(180, 210, 80, 25);
        panel.add(button);

    }


    private boolean doesSchemeNameExist()
    {
        // System.out.println("doesSchemeNameExists");

        for (String schemes_name : this.schemes_names)
        {
            if (schemes_name.equals(this.tf_name.getText()))
                return true;
        }

        // System.out.println("...not");

        return false;

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
            if (this.tf_name.getText().trim().equals(""))
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("TYPE_NAME_BEFORE"), m_ic.getMessage("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (doesSchemeNameExist())
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("SCHEME_NAME_ALREADY_USED"),
                    m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            m_actionDone = true;
            this.dispose();
        }
        else
        {
            System.out.println("SchemeDialog: Unknown command: " + action);
        }

    }


    /**
     * Was Action Successful
     * 
     * @return true if action was successful (dialog closed with OK)
     */
    public boolean actionSuccessful()
    {
        return m_actionDone;
    }


    /**
     * Get Action Results
     * 
     * @return String array of results
     */
    public String[] getActionResults()
    {
        String[] res = new String[3];

        if (m_actionDone)
        {
            res[0] = "1";
        }
        else
        {
            res[0] = "0";
        }

        res[1] = this.tf_name.getText();
        res[2] = this.cb_template.getSelectedItem().toString();

        return res;
    }

}
