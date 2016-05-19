package ggc.gui.dialogs.stock;

import java.awt.*;

import javax.swing.*;

import com.atech.graphics.dialogs.StandardDialogForObject;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.hibernate.StockH;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;

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
 *  Filename:     zzz  
 *  Description:  zzz
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// fix this
// for defining stock entry

// REUSED

/**
 * This dialog is in use by StocktakingListDef and StockListDef
 *
 * @author andy
 *
 */
// FIXME addComponent

public class StockDialog extends StandardDialogForObject
{

    // TO DO
    // object gui
    // load
    // save

    // edit value
    // edit stock

    // s boolean editValue;

    // OLD
    private DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();
    private GGCProperties props = m_da.getSettings();

    private boolean m_actionDone = false;

    // private long last_change = 0;

    // static AddRowFrame singleton = null;

    JLabel label_title = new JLabel();

    boolean in_process;
    boolean debug = false;

    // private boolean m_add_action = true;
    private Container m_parent = null;
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;
    private JLabel lblAmount;
    private JComboBox comboBox;
    private JLabel lblEntryType;
    private JButton btnOk;
    private JButton btnCancel;
    private JFormattedTextField formattedTextField;
    private JLabel lblStockType;
    private JLabel lblStockSubType;
    private JButton btnSelectstocktype;
    // private JButton btnHelp;


    public StockDialog(JDialog dialog)
    {
        super(dialog, DataAccess.getInstance());
    }


    public StockDialog(JDialog dialog, StockH subTypeObject, boolean editValue)
    {
        super(dialog, DataAccess.getInstance(), subTypeObject, false);

        this.editValue = editValue;

        init(subTypeObject);
    }


    @Override
    public void loadData(Object dataObject)
    {
        // editing (changes value only)
    }


    @Override
    public boolean saveData()
    {
        // versioning

        return false;
    }


    public void initGUI()
    {
        int x = 0;
        int y = 0;
        int width = 400;
        int height = 480;

        Rectangle bnd = m_parent.getBounds();

        x = bnd.width / 2 + bnd.x - width / 2;
        y = bnd.height / 2 + bnd.y - height / 2;

        this.setBounds(x, y, 400, 360);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 392, 329);
        panel.setLayout(null);

        getContentPane().setLayout(null);

        this.getContentPane().add(panel);

        label_title.setFont(new Font("SansSerif", Font.BOLD, 22));
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        label_title.setBounds(0, 15, 392, 35);
        label_title.setText("TITLE");
        panel.add(label_title);

        this.lblNewLabel = new JLabel("Stock subtype");
        this.lblNewLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        this.lblNewLabel.setBounds(40, 109, 91, 14);
        panel.add(this.lblNewLabel);

        this.lblNewLabel_1 = new JLabel("Date Time");
        this.lblNewLabel_1.setFont(new Font("SansSerif", Font.BOLD, 12));
        this.lblNewLabel_1.setBounds(40, 148, 91, 14);
        panel.add(this.lblNewLabel_1);

        this.lblNewLabel_2 = new JLabel("Stock Type");
        this.lblNewLabel_2.setFont(new Font("SansSerif", Font.BOLD, 12));
        this.lblNewLabel_2.setBounds(40, 73, 93, 14);
        panel.add(this.lblNewLabel_2);

        this.lblAmount = new JLabel("Amount:");
        this.lblAmount.setFont(new Font("SansSerif", Font.BOLD, 12));
        this.lblAmount.setBounds(40, 202, 91, 14);
        panel.add(this.lblAmount);

        this.comboBox = new JComboBox();
        this.comboBox.setBounds(156, 231, 106, 20);
        panel.add(this.comboBox);

        this.lblEntryType = new JLabel("Entry type:");
        this.lblEntryType.setFont(new Font("SansSerif", Font.BOLD, 12));
        this.lblEntryType.setBounds(40, 233, 91, 14);
        panel.add(this.lblEntryType);

        this.btnOk = new JButton("OK");
        this.btnOk.setBounds(74, 280, 89, 23);
        panel.add(this.btnOk);

        this.btnCancel = new JButton("Cancel");
        this.btnCancel.setBounds(173, 280, 89, 23);
        panel.add(this.btnCancel);

        this.formattedTextField = new JFormattedTextField();
        this.formattedTextField.setBounds(156, 200, 103, 20);
        panel.add(this.formattedTextField);

        this.lblStockType = new JLabel("Stock Type");
        this.lblStockType.setFont(new Font("SansSerif", Font.PLAIN, 12));
        this.lblStockType.setBounds(156, 73, 144, 14);
        panel.add(this.lblStockType);

        this.lblStockSubType = new JLabel("stock sub type");
        this.lblStockSubType.setFont(new Font("SansSerif", Font.PLAIN, 12));
        this.lblStockSubType.setBounds(156, 109, 140, 14);
        panel.add(this.lblStockSubType);

        this.btnSelectstocktype = new JButton("selectStockType");
        this.btnSelectstocktype.setBounds(315, 73, 46, 53);
        panel.add(this.btnSelectstocktype);

        this.btnHelp = new JButton("Help");
        this.btnHelp.setBounds(272, 280, 89, 23);
        panel.add(this.btnHelp);

    }


    @Override
    public String getHelpId()
    {
        return null;
    }

}
