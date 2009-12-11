package ggc.nutri.panels;

import ggc.nutri.db.datalayer.FoodDescription;
import ggc.nutri.db.datalayer.FoodGroup;
import ggc.nutri.dialogs.NutritionTreeDialog;
import ggc.nutri.display.HomeWeightDataDisplay;
import ggc.nutri.display.NutritionDataDisplay;
import ggc.nutri.util.DataAccessNutri;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

import com.atech.graphics.components.ATTableData;
import com.atech.graphics.components.ATTableModel;
import com.atech.graphics.layout.ZeroLayout;

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
 *  Filename:     PanelNutritionFood
 *  Description:  Panel for displaying nutrition food
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class PanelNutritionFood extends GGCTreePanel /* JPanel */implements ActionListener
{

    private static final long serialVersionUID = 4046645712360241891L;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_refuse, label_name, label_name_i18n, label_name_i18n_key, label_group;
    JButton button;
    JTextArea jta_desc;

    NutritionTreeDialog m_dialog = null;
    ArrayList<NutritionDataDisplay> list_nutrition = new ArrayList<NutritionDataDisplay>();
    ArrayList<HomeWeightDataDisplay> list_weight = new ArrayList<HomeWeightDataDisplay>();

    JTable table_1, table_2 = null;
    JScrollPane scroll_1, scroll_2 = null;

    ATTableModel model_2 = null;

    HomeWeightDataDisplay hwd = null;
    NutritionDataDisplay ndd = null;

    FoodGroup food_group = null;

    /**
     * Constructor
     * 
     * @param dia
     */
    public PanelNutritionFood(NutritionTreeDialog dia)
    {
        super(true);

        m_dialog = dia;

        this.hwd = new HomeWeightDataDisplay(ic);
        this.ndd = new NutritionDataDisplay(ic);

        font_big = m_da.getFont(DataAccessNutri.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccessNutri.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccessNutri.FONT_NORMAL);

        createPanel();

    }

    private void createPanel()
    {

        this.setSize(520, 520);
        this.setLayout(new ZeroLayout(this.getSize()));

        Font fnt_14_bold = new Font("Times New Roman", Font.BOLD, 14);
        Font fnt_14 = new Font("Times New Roman", Font.PLAIN, 14);

        /*
         * this.setId(ch.getId()); this.setFood_group_id(ch.getFood_group_id());
         * this.setName(ch.getName()); this.setI18n_name(ch.getI18n_name());
         * this.setRefuse(ch.getRefuse());
         * this.setNutritions(ch.getNutritions());
         */

        label = new JLabel(ic.getMessage("FOOD_DESCRIPTION"));
        label.setBounds(0, 25, 520, 40);
        label.setFont(font_big);
        // label.setBackground(Color.red);
        // label.setBorder(BorderFactory.createLineBorder(Color.blue));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);

        label = new JLabel(ic.getMessage("FOOD_NAME") + ":");
        label.setBounds(30, 80, 100, 25);
        // label.setBounds(30, 100, 100, 30);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        label_name = new JLabel();
        // label_name.setBounds(30, 125, 460, 50);
        label_name.setBounds(190, 80, 300, 25);
        label_name.setVerticalAlignment(JLabel.TOP);
        label_name.setFont(fnt_14);
        this.add(label_name, ZeroLayout.DYNAMIC);

        label = new JLabel(ic.getMessage("TRANSLATION_KEYWORD_MEAL") + ":");
        label.setBounds(30, 115, 300, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        label_name_i18n_key = new JLabel();
        label_name_i18n_key.setBounds(190, 115, 300, 25);
        label_name_i18n_key.setFont(fnt_14);
        // /tf_name_i18n_key.setEditable(false);

        this.add(label_name_i18n_key, null);

        label = new JLabel(ic.getMessage("TRANSLATED_NAME_MEAL") + ":");
        label.setBounds(30, 150, 300, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        label_name_i18n = new JLabel();
        label_name_i18n.setBounds(190, 150, 300, 25);
        label_name_i18n.setFont(fnt_14);
        // tf_name_i18n.setEditable(false);
        this.add(label_name_i18n, null);

        /*
         * label = new JLabel(ic.getMessage("TRANSLATED_NAME") + ":");
         * label.setBounds(30, 140, 300, 60); label.setFont(fnt_14_bold);
         * this.add(label, null);
         * 
         * 
         * this.label_name_i18n = new JLabel(); // 180
         * this.label_name_i18n.setBounds(30, 165, 460, 50);
         * label_name.setVerticalAlignment(JLabel.TOP);
         * this.label_name_i18n.setFont(fnt_14); this.add(this.label_name_i18n,
         * ZeroLayout.DYNAMIC);
         */

        label = new JLabel(ic.getMessage("DESCRIPTION") + ":");
        label.setBounds(30, 180, 300, 30);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        jta_desc = new JTextArea();
        jta_desc.setRows(2);
        jta_desc.setEditable(false);
        // jta.setBounds(100, 190, 300, 60);

        JScrollPane scroll_3 = new JScrollPane(jta_desc);
        scroll_3.setBounds(140, 185, 350, 40);
        this.add(scroll_3, null);

        label = new JLabel(ic.getMessage("GROUP") + ":");
        label.setBounds(30, 235, 300, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        label_group = new JLabel();
        label_group.setBounds(140, 235, 200, 25);
        // label_group.setEditable(false);
        label_group.setFont(fnt_14);
        this.add(label_group, null);

        label = new JLabel(ic.getMessage("REFUSE_LBL") + ":");
        label.setBounds(30, 260, 300, 30);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        label_refuse = new JLabel();
        label_refuse.setBounds(300, 260, 50, 30);
        label_refuse.setFont(fnt_14);
        this.add(label_refuse, null);

        label = new JLabel(ic.getMessage("NUTRITIONS_FOOD") + ":");
        label.setBounds(30, 270, 300, 60);
        // label.setBounds(30, 235, 300, 60);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        table_1 = new JTable();

        this.createModel(this.list_nutrition, this.table_1, this.ndd);

        table_1.setRowSelectionAllowed(true);
        table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table_1.setDoubleBuffered(true);

        scroll_1 = new JScrollPane(table_1);
        // scroll_1.setBounds(30, 280, 460, 100);
        scroll_1.setBounds(30, 315, 460, 80);
        this.add(scroll_1, ZeroLayout.DYNAMIC);
        scroll_1.repaint();

        scroll_1.updateUI();

        // home weight

        label = new JLabel(ic.getMessage("HOME_WEIGHTS") + ":");
        // label.setBounds(30, 370, 300, 60);
        label.setBounds(30, 385, 300, 60);

        label.setFont(fnt_14_bold);
        this.add(label, null);

        // HomeWeightDataDisplay hwd = new HomeWeightDataDisplay(ic);
        // System.out.println(hwd.getColumnsCount());

        table_2 = new JTable();

        createModel(this.list_weight, this.table_2, this.hwd);

        table_2.setRowSelectionAllowed(true);
        table_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table_2.setDoubleBuffered(true);

        scroll_2 = new JScrollPane(table_2);
        // scroll_2.setBounds(30, 415, 460, 80);
        scroll_2.setBounds(30, 430, 460, 80);
        // scroll_2.
        this.add(scroll_2, ZeroLayout.DYNAMIC);
        scroll_2.repaint();
        scroll_2.updateUI();

        return;
    }

    /**
     * Set Parent
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setParent(java.lang.Object)
     */
    public void setParent(Object obj)
    {

    }

    /**
     * Set Data
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setData(java.lang.Object)
     */
    public void setData(Object obj)
    {
        FoodDescription fd = (FoodDescription) obj;

        // JLabel label, label_refuse, label_name, label_name_i18n;

        // this.label_name.setText("<html><body>" + fd.getName() +
        // "</body></html>");
        // this.label_name_i18n.setText("<html><body>" + fd.getName_i18n()+
        // "</body></html>");

        this.label_name.setText(fd.getName());
        this.label_name.setToolTipText(fd.getName());
        this.label_name_i18n_key.setText(fd.getName_i18n());
        this.label_name_i18n_key.setToolTipText(fd.getName_i18n());
        this.label_name_i18n.setText(ic.getMessage(fd.getName_i18n()));
        this.label_name_i18n.setToolTipText(ic.getMessage(fd.getName_i18n()));
        this.jta_desc.setText(fd.getDescription());
        this.label_refuse.setText("" + fd.getRefuse() + " %");

        if (fd.getGroup_id() > 0)
        {
            //this.food_group = m_da.tree_roots.get("" + fd.getFoodType()).m_groups_ht.get("" + fd.getGroup_id());
            
            this.food_group = m_da.getDbCache().tree_roots.get("" + fd.getFoodType()).findFoodGroup(fd.getFoodType(), fd.getGroup_id());
            this.label_group.setText(this.food_group.getName());
        }
        else
        {
            this.food_group = null;
            this.label_group.setText(ic.getMessage("ROOT"));
        }

        // System.out.println(fd.getNutritions());
        StringTokenizer strtok = null;
        
        list_nutrition.clear();
        
        if (m_da.isValueSet(fd.getNutritions()))
        {
            strtok = new StringTokenizer(fd.getNutritions(), ";");
    
            while (strtok.hasMoreTokens())
            {
                NutritionDataDisplay ndd1 = new NutritionDataDisplay(ic, strtok.nextToken());
    
                ndd1.setNutritionDefinition(m_da.getDbCache().nutrition_defs.get(ndd1.getId()));
                list_nutrition.add(ndd1);
            }
        }

        this.createModel(this.list_nutrition, this.table_1, this.ndd);

        this.list_weight.clear();

        if (m_da.isValueSet(fd.getHome_weights()))
        {
            strtok = new StringTokenizer(fd.getHome_weights(), ";");

            while (strtok.hasMoreTokens())
            {
                HomeWeightDataDisplay hwd1 = new HomeWeightDataDisplay(ic, strtok.nextToken());
                // System.out.println("Home Weight: " + hwd1.getId());

                hwd1.setHomeWeightDefinition(m_da.getDbCache().homeweight_defs.get(hwd1.getId()).getName());
                this.list_weight.add(hwd1);
            }
        }

        this.createModel(this.list_weight, this.table_2, this.hwd);

    }

    //public static final int MODEL_NUTRITIONS = 1;
    //public static final int MODEL_HOME_WEIGHTS = 2;

    private void createModel(ArrayList<?> lst, JTable table, ATTableData object)
    {
        ATTableModel model = new ATTableModel(lst, object);
        table.setModel(model);

        // int twidth2 = this.getWidth()-50;
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        TableColumnModel cm2 = table.getColumnModel();

        for (int i = 0; i < object.getColumnsCount(); i++)
        {
            cm2.getColumn(i).setHeaderValue(object.getColumnHeader(i));
            cm2.getColumn(i).setPreferredWidth(object.getColumnWidth(i, 430));
        }

    }

    /**
     * Action Listener
     */
    public void actionPerformed(ActionEvent e)
    {

        String action = e.getActionCommand();
        System.out.println("PanelNutritionFood::Unknown command: " + action);

    }

    /**
     * Get Warning string. This method returns warning string for either add or
     * edit. If value returned is null, then no warning message box will be
     * displayed.
     * 
     * @param _action_type type of action (ACTION_ADD, ACTION_EDIT)
     * @return String value as warning string
     */
    public String getWarningString(int _action_type)
    {
        return null;
    }

    /**
     * Was data in this panel changed.
     * 
     * @return true if data changed, false otherwise
     */
    public boolean hasDataChanged()
    {
        return false;
    }

    /**
     * Save data in panel
     * 
     * @return true if save was successful
     */
    public boolean saveData()
    {
        return false;
    }

}
