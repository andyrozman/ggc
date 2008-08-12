package ggc.plugin.list;

//package ggc.gui.nutrition.panels;

//import ggc.gui.nutrition.NutritionTreeDialog;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.atech.i18n.I18nControlAbstract;


// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman


public class BaseListMainPanel extends BaseListAbstractPanel //JPanel
{
	static final long serialVersionUID = 0L;


    I18nControlAbstract m_ic = null;
    DataAccessPlugInBase m_da = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label;
    JButton button;

    BaseListDialog m_dialog = null;

    

    public BaseListMainPanel(BaseListDialog dia)
    {

        super();

        m_dialog = dia;
        m_da = dia.m_da;
        m_ic = m_da.getI18nControlInstance();

        font_big = m_da.getFont(DataAccessPlugInBase.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccessPlugInBase.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccessPlugInBase.FONT_NORMAL);

        createPanel();

    }


    public void createPanel()
    {

        this.setSize(460, 520);
        this.setLayout(null);

        Font fnt_18 = new Font("Times New Roman", Font.PLAIN, 14);

        label = new JLabel(m_da.getWebListerTitle());
        label.setBounds(0, 35, 520, 40);
        label.setFont(font_big); 
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);


        label = new JLabel(m_da.getWebListerDescription());
        label.setBounds(40, 120, 400, 250);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(fnt_18); 
        this.add(label, null);

        return;
    }


    public void setData(Object obj)
    {
    }


}
    
    

