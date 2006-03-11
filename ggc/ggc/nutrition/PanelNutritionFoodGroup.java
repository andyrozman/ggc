package ggc.nutrition;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/*
import com.atech.inf_sys.zis.data.DataAccess;
import com.atech.inf_sys.zis.data.I18nControl;
import com.atech.inf_sys.zis.ui.dialogs.DioceseCfgDialog;

import com.atech.inf_sys.zis.datalayer.Parish;
import com.atech.inf_sys.zis.datalayer.Diocese;
*/

import ggc.util.I18nControl;
import ggc.util.DataAccess;

    
public class PanelNutritionFoodGroup extends JPanel
{

    I18nControl ic = I18nControl.getInstance();
    DataAccess m_da = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label;
    JButton button;

    NutritionTreeDialog m_dialog = null;


    public PanelNutritionFoodGroup(NutritionTreeDialog dia)
    {

        super();

        m_dialog = dia;
        m_da = DataAccess.getInstance();

        font_big = m_da.getFont(DataAccess.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccess.FONT_NORMAL);

        createPanel();

    }



    public void createPanel()
    {

        this.setSize(420, 460);
        this.setLayout(null);

        Font fnt_18 = new Font("Times New Roman", Font.BOLD, 18);


        label = new JLabel(ic.getMessage("DIOCESES_CONFIGURATION"));
        label.setBounds(0, 35, 420, 40);
        label.setFont(font_big); 
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);


        label = new JLabel(ic.getMessage("ADD"));
        label.setBounds(40, 100, 100, 30);
        label.setFont(fnt_18); 
        this.add(label, null);

        label = new JLabel(ic.getMessage("ADD_DIOCESE_DESC"));
        label.setBounds(40, 130, 300, 60);
        label.setFont(font_normal); 
        this.add(label, null);



        label = new JLabel(ic.getMessage("EDIT_VIEW"));
        label.setBounds(40, 280, 300, 30);
        label.setFont(fnt_18); 
        this.add(label, null);

        label = new JLabel(ic.getMessage("EDIT_VIEW_DESC"));
        label.setBounds(40, 310, 300, 60);
        label.setFont(font_normal); 
        this.add(label, null);

        return;
    }


}
    
    

