package ggc.nutri.panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.table.TableColumnModel;

import com.atech.graphics.components.ATTableData;
import com.atech.graphics.components.ATTableModel;
import com.atech.graphics.layout.ZeroLayout;
import com.atech.utils.ATSwingUtils;

import ggc.nutri.db.datalayer.*;
import ggc.nutri.dialogs.NutritionTreeDialog;
import ggc.nutri.display.MealNutritionsDisplay;
import ggc.nutri.display.MealPartsDisplay;

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
 *  Filename:     PanelNutritionMeal
 *  Description:  Panel for displaying nutrition Meal
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class PanelNutritionMeal extends GGCTreePanel implements ActionListener
{

    private static final long serialVersionUID = 6648128901412564424L;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_refuse, label_name, label_name_i18n, label_name_i18n_key;
    JLabel label_title, label_group;
    JButton button, button_select;
    JTextField tf_name, tf_name_i18n, tf_name_i18n_key, tf_group;
    JTextArea jta_desc;

    NutritionTreeDialog m_dialog = null;
    ArrayList<MealPartsDisplay> list_parts = new ArrayList<MealPartsDisplay>();
    ArrayList<MealNutritionsDisplay> list_nutritions = new ArrayList<MealNutritionsDisplay>();

    JTable table_1, table_2 = null;
    JScrollPane scroll_1, scroll_2 = null;

    ATTableModel model_2 = null;

    boolean was_saved = true;

    // HomeWeightDataDisplay hwd = null;
    // NutritionDataDisplay ndd = null;
    MealPartsDisplay mpd = null;
    MealNutritionsDisplay mnd = null;

    Meal meal;
    MealGroup meal_group;


    /**
     * Constructor
     * 
     * @param dia
     */
    public PanelNutritionMeal(NutritionTreeDialog dia)
    {

        super(true);

        m_dialog = dia;

        this.mpd = new MealPartsDisplay(ic);
        this.mnd = new MealNutritionsDisplay(ic);

        ATSwingUtils.initLibrary();

        font_big = ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD);
        font_normal_b = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);
        font_normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);

        createPanel();

    }


    private void createPanel()
    {

        this.setSize(520, 560);
        this.setLayout(new ZeroLayout(this.getSize()));

        Font fnt_14_bold = new Font("Times New Roman", Font.BOLD, 14);
        Font fnt_14 = new Font("Times New Roman", Font.PLAIN, 14);

        label_title = new JLabel(ic.getMessage("MEAL_VIEW"));
        label_title.setBounds(0, 25, 520, 40);
        label_title.setFont(font_big);
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label_title, null);

        label = new JLabel(ic.getMessage("MEAL_NAME") + ":");
        label.setBounds(30, 80, 100, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        label_name = new JLabel();
        label_name.setBounds(190, 80, 300, 25);
        // tf_name.setVerticalAlignment(JLabel.TOP);
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

        label = new JLabel(ic.getMessage("FOODS_MEALS_NUTRITIONS") + ":");
        label.setBounds(30, 270, 300, 60);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        table_1 = new JTable();

        this.createModel(this.list_parts, this.table_1, this.mpd);

        table_1.setRowSelectionAllowed(true);
        table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table_1.setDoubleBuffered(true);

        scroll_1 = new JScrollPane(table_1);
        scroll_1.setBounds(30, 315, 460, 80);
        this.add(scroll_1, ZeroLayout.DYNAMIC);
        scroll_1.repaint();

        scroll_1.updateUI();

        // nutritions

        label = new JLabel(ic.getMessage("AVAILABLE_NUTRITIONS") + ":");
        label.setBounds(30, 385, 300, 60);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        table_2 = new JTable();

        createModel(this.list_nutritions, this.table_2, this.mnd);

        table_2.setRowSelectionAllowed(true);
        table_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table_2.setDoubleBuffered(true);

        scroll_2 = new JScrollPane(table_2);
        scroll_2.setBounds(30, 430, 460, 80);
        // scroll_2.
        this.add(scroll_2, ZeroLayout.DYNAMIC);
        scroll_2.repaint();
        scroll_2.updateUI();

        this.help_button = ATSwingUtils.createHelpIconByBounds(470, 10, 35, 35, this, ATSwingUtils.FONT_NORMAL, m_da);
        this.add(help_button);

        m_da.enableHelp(this);

        return;
    }


    // public static final int MODEL_MEAL_PARTS = 1;
    // public static final int MODEL_MEALS_NUTRITIONS = 2;

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


    /*
     * private void createKeyWord() { String key =
     * dataAccess.makeI18nKeyword(tf_name.getText());
     * tf_name_i18n_key.setText(key);
     * tf_name_i18n.setText(i18nControlAbstract.getMessage(key)); }
     */

    /**
     * Action Listener
     */
    public void actionPerformed(ActionEvent e)
    {

        String action = e.getActionCommand();
        System.out.println("PanelNutritionMeal::Unknown command: " + action);

    }


    private void refreshNutritions()
    {

        // Hashtable<String,MealNutrition> nutres = new
        // Hashtable<String,MealNutrition>();

        // loadGI_GL(nutres);

        // DailyFoodEntries dfes = new DailyFoodEntries();
        /*
         * MealParts mpts = new MealParts();
         * for(int i=0; i< this.list_parts.size(); i++) {
         * mpts.addMealPart(this.list_parts.get(i).getMealPart());
         * //dfes.addDailyFoodEntry(new
         * DailyFoodEntry(this.list_parts.get(i).getMealPart()));
         * //ArrayList<MealNutrition> lst =
         * this.list_parts.get(i).getMealPart().getNutritions(); }
         */

        /*
         * for(int i=0; i< this.list_parts.size(); i++) {
         * ArrayList<MealNutrition> lst =
         * this.list_parts.get(i).getMealPart().getNutritions();
         * float amount = this.list_parts.get(i).getMealPart().getAmount();
         * for(int j=0; j<lst.size(); j++) { MealNutrition mn = lst.get(j);
         * if ((mn.getId()>=4000)) { // GI = 4000, GL = 4001, GI_MIN = 4002,
         * GI_MAX = 4003, GL_MIN = 4004, GL_MAX = 4005
         * if (mn.getId() == 4000) { checkGI_GL(nutres, mn, true); } else if
         * (mn.getId()==4001) { checkGI_GL(nutres, mn, false); } else if
         * (mn.getId()==4002) { checkGI_GL_Min(nutres, mn, true); } else if
         * (mn.getId()==4003) { checkGI_GL_Max(nutres, mn, true); } else if
         * (mn.getId()==4004) { checkGI_GL_Min(nutres, mn, false); } else if
         * (mn.getId()==4005) { checkGI_GL_Max(nutres, mn, false); }
         * } else {
         * if (!nutres.containsKey("" + mn.getId())) { MealNutrition mmn = new
         * MealNutrition(mn); nutres.put("" + mmn.getId(), mmn); }
         * //nutres.get("" + mn.getId()).addToAmount((mn.getAmount() amount));
         * //nutres.get("" +
         * mn.getId()).addToCalculatedAmount(((mn.getAmount()/100.0f) amount));
         * nutres.get("" + mn.getId()).addAmountToSum(mn.getCalculatedAmount());
         * }
         * } // for (j) } // for (i)
         */

        // System.out.println("Refresh Nutritions");
        ArrayList<MealNutrition> nut_list = new ArrayList<MealNutrition>();

        DailyFoodEntries dfe_main = new DailyFoodEntries();
        dfe_main.addDailyFoodEntry(new DailyFoodEntry(this.meal, 1.0f));

        /*
         * for(int i=0; i< this.list_parts.size(); i++) { DailyFoodEntry dfe =
         * this.list_parts.get(i).getDailyFoodEntry();
         * System.out.println("DFE: " + dfe);
         * dfe_main.addDailyFoodEntry(dfe); }
         */

        // nut_list.addAll(dfe_main.getCalculatedNutrients());
        nut_list.addAll(dfe_main.getCalculatedNutrients());

        this.list_nutritions.clear();

        // ArrayList<MealNutrition> lst = mpts.getCalculatedNutrients();

        // this.list_nutritions.clear();

        // for(Enumeration<String> en = nutres.keys(); en.hasMoreElements(); )
        for (int i = 0; i < nut_list.size(); i++)
        {

            MealNutrition meal_nut = nut_list.get(i);

            if (meal_nut.getAmount() > 0)
            {
                MealNutritionsDisplay _mnd = new MealNutritionsDisplay(ic, meal_nut);

                NutritionDefinition nd = m_da.getDbCache().nutrition_defs.get(_mnd.getId());
                _mnd.setNutritionDefinition(nd);

                this.list_nutritions.add(_mnd);
            }
        }

        java.util.Collections.sort(this.list_nutritions, new MealNutritionsDisplay(ic));
        this.createModel(this.list_nutritions, this.table_2, this.mnd);

    }


    // TODO: fix GI/GL handling
    @SuppressWarnings("unused")
    private void loadGI_GL(Hashtable<String, MealNutrition> nutres)
    {
        // GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, GL_MIN = 4004,
        // GL_MAX = 4005
        nutres.put("4002", new MealNutrition(4002, 0.0f, "GI Min"));
        nutres.put("4003", new MealNutrition(4003, 0.0f, "GI Max"));
        nutres.put("4004", new MealNutrition(4004, 0.0f, "GL Min"));
        nutres.put("4005", new MealNutrition(4005, 0.0f, "GL Max"));
    }


    // TODO: fix GI/GL handling
    @SuppressWarnings("unused")
    private void checkGI_GL(Hashtable<String, MealNutrition> nutres, MealNutrition mn, boolean GI)
    {
        // GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, GL_MIN = 4004,
        // GL_MAX = 4005

        String gi_gl_min;
        String gi_gl_max;

        if (GI)
        {
            gi_gl_min = "4002";
            gi_gl_max = "4003";
        }
        else
        {
            gi_gl_min = "4004";
            gi_gl_max = "4005";
        }

        if (nutres.get(gi_gl_min).getAmount() == 0)
        {
            nutres.get(gi_gl_min).setAmount(mn.getAmount());
        }
        else
        {
            if (nutres.get(gi_gl_min).getAmount() > mn.getAmount())
            {
                nutres.get(gi_gl_min).setAmount(mn.getAmount());
            }
        }

        if (nutres.get(gi_gl_max).getAmount() == 0)
        {
            nutres.get(gi_gl_max).setAmount(mn.getAmount());
        }
        else
        {
            if (nutres.get(gi_gl_max).getAmount() < mn.getAmount())
            {
                nutres.get(gi_gl_max).setAmount(mn.getAmount());
            }
        }

    }


    // TODO: fix GI/GL handling
    @SuppressWarnings("unused")
    private void checkGI_GL_Max(Hashtable<String, MealNutrition> nutres, MealNutrition mn, boolean GI)
    {
        // GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, GL_MIN = 4004,
        // GL_MAX = 4005

        String par;

        if (GI)
        {
            par = "4003";
        }
        else
        {
            par = "4005";
        }

        if (nutres.get(par).getAmount() == 0)
        {
            nutres.get(par).setAmount(mn.getAmount());
        }
        else
        {
            if (nutres.get(par).getAmount() < mn.getAmount())
            {
                nutres.get(par).setAmount(mn.getAmount());
            }
        }

    }


    // TODO: fix GI/GL handling
    @SuppressWarnings("unused")
    private void checkGI_GL_Min(Hashtable<String, MealNutrition> nutres, MealNutrition mn, boolean GI)
    {
        // GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, GL_MIN = 4004,
        // GL_MAX = 4005

        String par;

        if (GI)
        {
            par = "4002";
        }
        else
        {
            par = "4004";
        }

        if (nutres.get(par).getAmount() == 0)
        {
            nutres.get(par).setAmount(mn.getAmount());
        }
        else
        {
            if (nutres.get(par).getAmount() > mn.getAmount())
            {
                nutres.get(par).setAmount(mn.getAmount());
            }
        }

    }


    private void loadMealsParts()
    {
        this.list_parts.clear();

        String parts = this.meal.getParts();

        if (parts == null || parts.length() == 0)
        {
            this.createModel(this.list_parts, this.table_1, this.mpd);
            this.list_nutritions.clear();
            this.createModel(this.list_nutritions, this.table_2, this.mnd);
            return;
        }

        StringTokenizer strtok = new StringTokenizer(parts, ";");

        while (strtok.hasMoreTokens())
        {
            // MealPart mp = new MealPart(strtok.nextToken());
            DailyFoodEntry dfe = new DailyFoodEntry(strtok.nextToken(), 1);
            this.list_parts.add(new MealPartsDisplay(ic, dfe));
        }

        this.createModel(this.list_parts, this.table_1, this.mpd);
        refreshNutritions();

        /*
         * MealPart mp = new MealPart(msd.getSelectedObjectType(),
         * msd.getSelectedObject(), msd.getAmountValue());
         * this.list_parts.add(new MealPartsDispay(i18nControlAbstract, mp));
         * this.createModel(this.list_parts, this.table_1, this.mpd);
         * refreshNutritions();
         */

    }


    /**
     * Set Parent
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setParent(java.lang.Object)
     */
    @Override
    public void setParent(Object obj)
    {
    }


    /**
     * Set Data
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setData(java.lang.Object)
     */
    @Override
    public void setData(Object obj)
    {
        this.meal = (Meal) obj;

        // setTypeOfAction(EditableAbstractPanel.ACTION_EDIT);

        // this.label_title.setText(i18nControlAbstract.getMessage("MEAL_EDIT"));
        this.label_name.setText(this.meal.getName());
        this.label_name.setToolTipText(this.meal.getName());
        this.label_name_i18n_key.setText(this.meal.getNameI18n());
        this.label_name_i18n_key.setToolTipText(this.meal.getNameI18n());
        this.label_name_i18n.setText(ic.getMessage(this.meal.getNameI18n()));
        this.label_name_i18n.setToolTipText(ic.getMessage(this.meal.getNameI18n()));

        this.jta_desc.setText(this.meal.getDescription());

        if (this.meal.getGroupId() > 0)
        {
            // this.meal_group =
            // dataAccess.tree_roots.get("3").m_meal_groups_ht.get("" +
            // this.meal.getGroupId());
            this.meal_group = m_da.getDbCache().tree_roots.get("3").findMealGroup(3, this.meal.getGroupId());
            this.label_group.setText(this.meal_group.getName());
        }
        else
        {
            this.meal_group = null;
            this.label_group.setText(ic.getMessage("ROOT"));
        }

        this.loadMealsParts();
        // this.button_select.setEnabled(true);

    }


    /**
     * Get Warning string. This method returns warning string for either add or
     * edit. If value returned is null, then no warning message box will be
     * displayed.
     * 
     * @param _action_type
     *            type of action (ACTION_ADD, ACTION_EDIT)
     * @return String value as warning string
     */
    @Override
    public String getWarningString(int _action_type)
    {
        return null;
    }


    /*
     * private String temp_parts; private String temp_nutritions;
     * private void processData() { Collections.sort(this.list_parts, new
     * MealPartsComparator());
     * StringBuffer sb = new StringBuffer();
     * for(int i=0; i<this.list_parts.size(); i++) { if (i!=0) sb.append(";");
     * sb.append(this.list_parts.get(i).getSaveData()); }
     * this.temp_parts = sb.toString();
     * Collections.sort(this.list_nutritions, new MealNutritionsComparator());
     * sb = new StringBuffer();
     * for(int i=0; i<this.list_nutritions.size(); i++) { if (i!=0)
     * sb.append(";");
     * sb.append(this.list_nutritions.get(i).getSaveData()); }
     * this.temp_nutritions = sb.toString();
     * }
     */

    /**
     * Was data in this panel changed.
     * 
     * @return true if data changed, false otherwise
     */
    @Override
    public boolean hasDataChanged()
    {

        return false;
        /*
         * System.out.println("hasDataChanged");
         * if (this.was_saved) return false;
         * processData();
         * if (this.isAddAction()) { return true; } else { if
         * ((hasChangedEntry(this.meal.getName(), this.tf_name.getText())) ||
         * (hasChangedEntry(this.meal.getNameI18n(),
         * this.tf_name_i18n_key.getText())) ||
         * (hasChangedEntry(this.meal.getDescription(),
         * this.jta_desc.getText())) || (hasChangedEntry(this.meal.getParts(),
         * this.temp_parts)) || (hasChangedEntry(this.meal.getNutritions(),
         * this.temp_nutritions)) || (this.meal.getGroupId()!=
         * this.meal_group.getId()) ) return true; else return false; }
         */
    }


    /**
     * Save data in panel
     * 
     * @return true if save was successful
     */
    @Override
    public boolean saveData()
    {

        return false;

        /*
         * if (this.isAddAction()) { this.meal.setName(this.tf_name.getText());
         * this.meal.setNameI18n(this.tf_name_i18n_key.getText());
         * this.meal.setDescription(this.jta_desc.getText());
         * this.meal.setParts(this.temp_parts);
         * this.meal.setNutritions(this.temp_nutritions);
         * this.meal.setGroupId(this.meal_group.getId());
         * this.dataAccess.getDb().add(this.meal); this.was_saved = true;
         * addMeal2Tree(this.meal);
         * return true; } else {
         * long prev_group_id = this.meal.getGroupId();
         * this.meal.setName(this.tf_name.getText());
         * this.meal.setNameI18n(this.tf_name_i18n_key.getText());
         * this.meal.setDescription(this.jta_desc.getText());
         * this.meal.setParts(this.temp_parts);
         * this.meal.setNutritions(this.temp_nutritions);
         * this.meal.setGroupId(this.meal_group.getId());
         * this.dataAccess.getDb().edit(this.meal); this.was_saved = true;
         * if (prev_group_id != this.meal.getGroupId()) {
         * removeMealFromTree(this.meal, prev_group_id);
         * addMeal2Tree(this.meal); }
         * return true; }
         */

    }


    /*
     * private void addMeal2Tree(Meal meal) {
     * dataAccess.tree_roots.get("3").m_meal_groups_ht.get("" +
     * meal.getGroupId()).addChild(meal); }
     * public void removeMealFromTree(Meal meal, long prev_group_id) {
     * dataAccess.tree_roots.get("3").m_meal_groups_ht.get("" +
     * prev_group_id).removeChild(meal); }
     */

    public String getHelpId()
    {
        return "GGC_Food_Meal_Meal_View";
    }

}
