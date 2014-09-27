package ggc.gui.dialogs.stock;

import ggc.core.db.datalayer.StockBaseType;
import ggc.core.util.DataAccess;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

/**
 * 
 * @author andy
 *
 */
public class StockTypeDialog extends JDialog implements ActionListener
{
    // private JTextField jTextField0;
    private JPanel panel;
    // private JLabel lblIdValue;

    private JLabel lbl_title;
    private JLabel lbl_id_data;

    private JTextField tf_name;
    private JTextField tf_name_i18n;

    // private JTextField txtNamelocdata;

    private JButton btnOk;
    private JButton btnCancel;
    private JButton btnHelp;

    DataAccess m_da = DataAccess.getInstance();
    I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    /**
     * @wbp.parser.constructor
     */
    public StockTypeDialog(JDialog parent)
    {
        super(parent, "", true);
        initGUI();
    }

    public StockTypeDialog(JDialog parent, StockBaseType si)
    {
        super(parent, "", true);
        initGUI();
    }

    private void initGUI()
    {

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 450, 350);

        getContentPane().setLayout(null);
        this.getContentPane().add(panel, null);

        setSize(450, 350);

        ATSwingUtils.initLibrary();

        // JLabel label = null;

        JLabel label = ATSwingUtils.getTitleLabel(m_ic.getMessage("NAME"), 0, 20, 350, 34, panel,
            ATSwingUtils.FONT_BIG_BOLD);

        // ID
        ATSwingUtils.getLabel(m_ic.getMessage("STOCK_ID"), 40, 90, 100, 24, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        this.lbl_id_data = ATSwingUtils.getLabel("", 160, 90, 100, 24, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        // Name
        ATSwingUtils.getLabel(m_ic.getMessage("NAME"), 40, 130, 100, 24, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        this.tf_name = ATSwingUtils.getTextField("", 160, 130, 140, 20, panel, ATSwingUtils.FONT_NORMAL);

        // Name Localized
        ATSwingUtils
                .getLabel(m_ic.getMessage("NAME_LOCALIZED"), 40, 170, 100, 24, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        this.tf_name_i18n = ATSwingUtils.getTextField("", 160, 170, 140, 20, panel, ATSwingUtils.FONT_NORMAL);

        /*
         * this.lblNameLocalized = new JLabel("Name Localized");
         * this.lblNameLocalized.setFont(new Font("SansSerif", Font.BOLD, 12));
         * this.lblNameLocalized.setBounds(38, 133, 103, 14);
         * this.jPanel0.add(this.lblNameLocalized);
         */

        // ATSwingUtils.getLabel(m_ic.getMessage("NAME"), 40, 100, 100, 24,
        // panel, ATSwingUtils.FONT_NORMAL_BOLD);

        /*
         * this.txtNamelocdata = new JTextField();
         * this.txtNamelocdata.setText("nameLocData");
         * this.txtNamelocdata.setBounds(160, 130, 140, 20);
         * this.jPanel0.add(this.txtNamelocdata);
         */

        int[] s = { 16, 16 };

        ATSwingUtils.getButton("  " + m_ic.getMessage("OK"), 36, 220, 110, 25, panel, ATSwingUtils.FONT_NORMAL,
            "ok.png", "ok", this, m_da, s);

        ATSwingUtils.getButton("  " + m_ic.getMessage("CANCEL"), 135, 220, 110, 25, panel, ATSwingUtils.FONT_NORMAL,
            "cancel.png", "ok", this, m_da, s);

        ATSwingUtils.getButton("  " + m_ic.getMessage("HELP"), 220, 220, 110, 25, panel, ATSwingUtils.FONT_NORMAL,
            "help.png", "ok", this, m_da, s);

        /*
         * this.btnOk = new JButton("OK");
         * this.btnOk.setBounds(36, 175, 89, 23);
         * this.jPanel0.add(this.btnOk);
         * this.btnCancel = new JButton("Cancel");
         * this.btnCancel.setBounds(135, 175, 89, 23);
         * this.jPanel0.add(this.btnCancel);
         * this.btnHelp = new JButton("Help");
         * this.btnHelp.setBounds(225, 175, 89, 23);
         * this.jPanel0.add(this.btnHelp);
         * // getContentPane().add(this.lblId);
         */
    }

    /**
     * Was Action Successful
     * 
     * @return true if action was successful (dialog closed with OK)
     */
    public boolean actionSuccessful()
    {
        return false; // m_actionDone;
    }

    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getActionCommand().equals("ok"))
        {
            this.dispose();
        }
        else if (ae.getActionCommand().equals("cancel"))
        {
            this.dispose();
        }
        // TODO Auto-generated method stub

    }

}
