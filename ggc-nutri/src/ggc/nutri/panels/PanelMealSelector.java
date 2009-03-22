package ggc.nutri.panels;

import ggc.core.util.DataAccess;
import ggc.nutri.db.datalayer.DailyFoodEntries;
import ggc.nutri.db.datalayer.DailyFoodEntry;
import ggc.nutri.db.datalayer.MealNutrition;
import ggc.nutri.db.datalayer.NutritionDefinition;
import ggc.nutri.dialogs.MealSpecialSelectorDialog;
import ggc.nutri.display.DailyFoodEntryDisplay;
import ggc.nutri.display.MealNutritionsDisplay;
import ggc.nutri.util.DataAccessNutri;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

import com.atech.graphics.components.ATTableData;
import com.atech.graphics.components.ATTableModel;
import com.atech.graphics.layout.ZeroLayout;
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
 *  Filename:     PanelMealSelector  
 *  Description:  Panel Meal Selector
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class PanelMealSelector extends /* GGCTreePanel */JPanel implements ActionListener
{

    private static final long serialVersionUID = 568309027874088901L;

    DataAccessNutri m_da = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_refuse, label_name, label_name_i18n;
    JLabel label_title;
    JButton button, button_select;
    JTextField tf_name, tf_name_i18n, tf_name_i18n_key, tf_group;
    JTextArea jta_desc;
    JTable table_1, table_2 = null;
    JScrollPane scroll_1, scroll_2 = null;
    JDialog m_dialog;
    ActionListener action_listener;

    ATTableModel model_2 = null;
    ArrayList<DailyFoodEntryDisplay> list_food_entries = new ArrayList<DailyFoodEntryDisplay>();
    ArrayList<MealNutritionsDisplay> list_nutritions = new ArrayList<MealNutritionsDisplay>();

    boolean was_saved = true;

    DailyFoodEntryDisplay dfed = null;
    MealNutritionsDisplay mnd = null;

    I18nControlAbstract ic = null;
    String meals_ids;

    /**
     * Constructor
     * 
     * @param dialog
     * @param list
     * @param meals_ids
     */
    public PanelMealSelector(JDialog dialog, ActionListener list, String meals_ids)
    {
        super();

        this.action_listener = list;
        // super(true, dia.ic);

        m_dialog = dialog;
        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();

        // this.mpd = new MealFoodDisplay(ic);
        this.dfed = new DailyFoodEntryDisplay(ic);
        this.mnd = new MealNutritionsDisplay(ic);

        font_big = m_da.getFont(DataAccess.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccess.FONT_NORMAL);

        this.meals_ids = meals_ids;

        createPanel();

        loadFoodParts();

    }

    private void loadFoodParts()
    {
        //System.out.println("Food parts: " + this.meals_ids);

        // System.out.println("Load Food Parts N/A");
        if ((this.meals_ids != null) && (this.meals_ids.length() != 0))
        {
            String[] arrays = m_da.splitString(this.meals_ids, ";");

            for (int i = 0; i < arrays.length; i++)
            {
                DailyFoodEntry dfe = new DailyFoodEntry(arrays[i]);

                addFoodPart(new DailyFoodEntryDisplay(ic, dfe), false);
            }

            this.refreshFoodParts();
        }

    }

    private void addFoodPart(DailyFoodEntryDisplay _dfed)
    {
        this.addFoodPart(_dfed, true);
    }

    private void addFoodPart(DailyFoodEntryDisplay _dfed, boolean refresh)
    {
        if (_dfed.getDailyFoodEntry() != null)
            this.list_food_entries.add(_dfed);

        if (refresh)
            this.refreshFoodParts();
    }


    /**
     * Get String For Db 
     * 
     * @return
     */
    public String getStringForDb()
    {
        int last = this.list_food_entries.size() - 1;
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i <= last; i++)
        {
            sb.append(this.list_food_entries.get(i).getStringForDb());

            if (i != last)
                sb.append(";");
        }

        return sb.toString();
    }

    /**
     * Get CH Sum String
     * @return
     */
    public String getCHSumString()
    {
        // 205
        // ArrayList<MealNutritionsDisplay>
        for (int i = 0; i < this.list_nutritions.size(); i++)
        {
            MealNutritionsDisplay mnd1 = this.list_nutritions.get(i);

            // System.out.println(mnd1);

            if (mnd1.getId().equals("205"))
            {
                // System.out.println("Found !!!!!!!!!!!" + mnd1);
                return mnd1.getValue();
            }

        }

        return "0,0";
    }

    private void createPanel()
    {

        this.setSize(520, 560);
        this.setLayout(null); // new ZeroLayout(this.getSize()));

        Font fnt_14_bold = new Font("Times New Roman", Font.BOLD, 14);
        // Font fnt_14 = new Font("Times New Roman", Font.PLAIN, 14);

        /*
         * this.setId(ch.getId()); this.setFood_group_id(ch.getFood_group_id());
         * this.setName(ch.getName()); this.setI18n_name(ch.getI18n_name());
         * this.setRefuse(ch.getRefuse());
         * this.setNutritions(ch.getNutritions());
         */

        label_title = new JLabel(ic.getMessage("MEALS_FOODS_SELECTOR_DAILY"));
        label_title.setBounds(0, 25, 520, 40);
        label_title.setFont(font_big);
        // label.setBackground(Color.red);
        // label.setBorder(BorderFactory.createLineBorder(Color.blue));
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label_title, null);

        label = new JLabel(ic.getMessage("FOODS_MEALS_NUTRITIONS") + ":");
        label.setBounds(30, 90, 300, 60);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        table_1 = new JTable();

        this.createModel(this.list_food_entries, this.table_1, this.dfed);

        table_1.setRowSelectionAllowed(true);
        table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table_1.setDoubleBuffered(true);

        scroll_1 = new JScrollPane(table_1);
        scroll_1.setBounds(30, 135, 460, 110);
        this.add(scroll_1, ZeroLayout.DYNAMIC);
        scroll_1.repaint();

        scroll_1.updateUI();

        this.button = new JButton(new ImageIcon(m_da.getImage("/icons/food_add.gif", this)));
        // this.button.addActionListener(this);
        this.button.addActionListener(this);
        this.button.setActionCommand("add_food");
        this.button.setToolTipText(ic.getMessage("MEAL_FOOD_ADD_DESC"));
        this.button.setBounds(370, 95, 32, 32);
        this.add(button, null);

        this.button = new JButton(new ImageIcon(m_da.getImage("/icons/food_edit.gif", this)));
        this.button.addActionListener(this);
        // this.button.addActionListener(this);
        this.button.setActionCommand("edit_food");
        this.button.setToolTipText(ic.getMessage("MEAL_FOOD_EDIT_DESC"));
        this.button.setBounds(410, 95, 32, 32);
        this.add(button, null);

        this.button = new JButton(new ImageIcon(m_da.getImage("/icons/food_delete.gif", this)));
        this.button.addActionListener(this);
        // this.button.addActionListener(this);
        this.button.setActionCommand("remove_food");
        this.button.setToolTipText(ic.getMessage("MEAL_FOOD_DELETE_DESC"));
        this.button.setBounds(450, 95, 32, 32);
        this.add(button, null);

        // home weight

        label = new JLabel(ic.getMessage("AVAILABLE_NUTRITIONS") + ":");
        label.setBounds(30, 270, 300, 30);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        table_2 = new JTable();

        createModel(this.list_nutritions, this.table_2, this.mnd);

        table_2.setRowSelectionAllowed(true);
        table_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table_2.setDoubleBuffered(true);

        scroll_2 = new JScrollPane(table_2);
        scroll_2.setBounds(30, 300, 460, 90);
        // scroll_2.
        this.add(scroll_2, ZeroLayout.DYNAMIC);
        scroll_2.repaint();
        scroll_2.updateUI();

        JButton button1 = new JButton("  " + ic.getMessage("OK"));
        button1.setActionCommand("ok");
        button1.setIcon(m_da.getImageIcon_22x22("ok.png", this));
        button1.setBounds(55, 410, 120, 25);
        button1.addActionListener(this.action_listener);
        this.add(button1, null);

        button1 = new JButton("  " + ic.getMessage("CANCEL"));
        button1.setActionCommand("cancel");
        button1.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        button1.setBounds(190, 410, 120, 25);
        button1.addActionListener(this.action_listener);
        this.add(button1);

        button1 = m_da.createHelpButtonByBounds(380, 410, 110, 25, this);
        this.add(button1);

        return;
    }

    //private static final int MODEL_MEAL_PARTS = 1;
    //private static final int MODEL_MEALS_NUTRITIONS = 2;

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
     * m_da.makeI18nKeyword(tf_name.getText()); tf_name_i18n_key.setText(key);
     * tf_name_i18n.setText(ic.getMessage(key)); }
     */

    private void refreshFoodParts()
    {
        this.createModel(this.list_food_entries, this.table_1, this.dfed);

        this.refreshNutritions();
    }

    /*
    private JTable getFoodTable()
    {
        return this.table_1;
    }*/

    /**
     * Action Listener
     */
    public void actionPerformed(ActionEvent e)
    {

        String action = e.getActionCommand();

        if (action.equals("add_food"))
        {
            MealSpecialSelectorDialog mssd = new MealSpecialSelectorDialog(m_dialog, 0L);

            System.out.println(mssd.getDailyFoodEntry());
            
            DailyFoodEntryDisplay _dfed = new DailyFoodEntryDisplay(ic, mssd.getDailyFoodEntry());

            this.addFoodPart(_dfed);
        }
        else if (action.equals("edit_food"))
        {
            out("edit");

            // TODO: fix edit food

            if (this.table_1.getSelectedRowCount() == 0)
            {
                JOptionPane.showConfirmDialog(this, ic.getMessage("SELECT_ITEM_FIRST"), ic.getMessage("ERROR"),
                    JOptionPane.CLOSED_OPTION);
                return;
            }

            DailyFoodEntryDisplay _dfed = this.list_food_entries.get(this.table_1.getSelectedRow());
            MealSpecialSelectorDialog mssd = new MealSpecialSelectorDialog(m_dialog, _dfed.getDailyFoodEntry());

            if (mssd.wasAction())
            {
                //System.out.println(dfed);
                DailyFoodEntry dfed_new = mssd.getDailyFoodEntry();

                System.out.println(dfed_new);

                // dfed.resetWeightValues(mssd.getDailyFoodEntry());

                //System.out.println(dfed);

                this.refreshFoodParts();
            }
            else
            {
                //System.out.println("NO Action !");
            }
        }
        else if (action.equals("remove_food"))
        {

            if (this.table_1.getSelectedRowCount() == 0)
            {
                JOptionPane.showConfirmDialog(this, ic.getMessage("SELECT_ITEM_FIRST"), ic.getMessage("ERROR"),
                    JOptionPane.CLOSED_OPTION);
                return;
            }

            int ii = JOptionPane.showConfirmDialog(this, ic.getMessage("ARE_YOU_SURE_DELETE"), ic.getMessage("ERROR"),
                JOptionPane.YES_NO_OPTION);

            if (ii == JOptionPane.YES_OPTION)
            {
                DailyFoodEntryDisplay _dfed = this.list_food_entries.get(this.table_1.getSelectedRow());

                this.list_food_entries.remove(_dfed);
                this.refreshFoodParts();
            }

        }
        else
            System.out.println("PanelMealSelector::Unknown command: " + action);

        /*
         * if (action.equals("add_meal")) { System.out.println("Add Meal");
         * MealSelectorDialog msd = new MealSelectorDialog(m_da,
         * this.meal.getId());
         * 
         * if (msd.wasAction()) { MealPart mp = new
         * MealPart(msd.getSelectedObjectType(), msd.getSelectedObject(),
         * msd.getAmountValue()); this.list_parts.add(new MealPartsDisplay(ic,
         * mp));
         * 
         * this.createModel(this.list_parts, this.table_1, this.mpd);
         * refreshNutritions(); }
         * 
         * } else if (action.equals("edit_meal")) {
         * System.out.println("Edit Meal");
         * 
         * 
         * if (this.table_1.getSelectedRowCount()==0) {
         * JOptionPane.showConfirmDialog(this,
         * ic.getMessage("SELECT_ITEM_FIRST"), ic.getMessage("ERROR"),
         * JOptionPane.CLOSED_OPTION); return; }
         * 
         * MealPartsDisplay mnd =
         * this.list_parts.get(this.table_1.getSelectedRow());
         * MealSelectorDialog msd = new MealSelectorDialog(m_da,
         * mnd.getMealPart());
         * 
         * if (msd.wasAction()) { mnd.setAmount(msd.getAmountValue());
         * this.createModel(this.list_parts, this.table_1, this.mpd);
         * refreshNutritions(); }
         * 
         * 
         * 
         * } else if (action.equals("remove_meal")) {
         * System.out.println("Remove Meal");
         * 
         * 
         * if (this.table_1.getSelectedRowCount()==0) {
         * JOptionPane.showConfirmDialog(this,
         * ic.getMessage("SELECT_ITEM_FIRST"), ic.getMessage("ERROR"),
         * JOptionPane.CLOSED_OPTION); return; }
         * 
         * int ii = JOptionPane.showConfirmDialog(this,
         * ic.getMessage("ARE_YOU_SURE_DELETE"), ic.getMessage("ERROR"),
         * JOptionPane.YES_NO_OPTION);
         * 
         * if (ii==JOptionPane.YES_OPTION) { MealPartsDisplay mnd =
         * this.list_parts.get(this.table_1.getSelectedRow()); //PersonContact
         * pc = m_contact_data.get(list_contact.getSelectedIndex());
         * 
         * this.list_parts.remove(mnd);
         * 
         * this.createModel(this.list_parts, this.table_1, this.mpd);
         * refreshNutritions(); }
         * 
         * }
         */

    }

    private void out(String text)
    {
        System.out.println(text);
    }

    /*
     * proc v1 private void refreshNutritions() {
     * System.out.println("Refresh Nutritions");
     * 
     * ArrayList<MealNutrition> nut_list = new ArrayList<MealNutrition>();
     * 
     * //Hashtable<String,MealNutrition> nutres = new
     * Hashtable<String,MealNutrition>();
     * 
     * //loadGI_GL(nutres);
     * 
     * 
     * DailyFoodEntry dfe_main = new DailyFoodEntry();
     * 
     * for(int i=0; i< this.list_food_entries.size(); i++) {
     * 
     * DailyFoodEntry dfe = this.list_food_entries.get(i).getDailyFoodEntry();
     * 
     * if (dfe == null) { // XXX: damage control: item does not contain a valid
     * DailyFoodEntry, so delete it? // this.list_food_entries.remove(i);
     * DailyFoodEntryDisplay dfed = this.list_food_entries.get(i);
     * System.out.println("Item doesn't contain DailiyFoodEntry: " +
     * dfed.getColumnValue(0)); continue; }
     * 
     * out("getNutritions for food: " + i );
     * 
     * 
     * ArrayList<MealNutrition> lst = dfe.getNutrientsCalculated();
     * //ArrayList<MealNutrition> lst =
     * this.list_food_entries.get(i).getDailyFoodEntry().getNutrients();
     * 
     * out("before merge: " + i ); dfe_main.displayNutritions(lst);
     * 
     * out("merge: " + i ); dfe_main.mergeNutrientsData(lst);
     * dfe_main.mergeGlycemicData(dfe);
     * 
     * out("fter merge: " + i ); dfe_main.displayNutritions();
     * 
     * //dfe_main.displayNutritions();
     * 
     * / float amount =
     * this.list_food_entries.get(i).getDailyFoodEntry().getMultiplier();
     * 
     * System.out.println("Multiplier: " + amount);
     * 
     * for(int j=0; j<lst.size(); j++) { MealNutrition mn = lst.get(j);
     * 
     * if ((mn.getId()>=4000)) { // TODO: Fix this with usage of new
     * GlycemicIndexLoad class
     * 
     * // GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, GL_MIN = 4004,
     * GL_MAX = 4005
     * 
     * if (mn.getId() == 4000) { checkGI_GL(nutres, mn, true); } else if
     * (mn.getId()==4001) { checkGI_GL(nutres, mn, false); } else if
     * (mn.getId()==4002) { checkGI_GL_Min(nutres, mn, true); } else if
     * (mn.getId()==4003) { checkGI_GL_Max(nutres, mn, true); } else if
     * (mn.getId()==4004) { checkGI_GL_Min(nutres, mn, false); } else if
     * (mn.getId()==4005) { checkGI_GL_Max(nutres, mn, false); }
     * 
     * } else {
     * 
     * if (!nutres.containsKey("" + mn.getId())) { MealNutrition mmn = new
     * MealNutrition(mn); nutres.put("" + mmn.getId(), mmn); }
     * 
     * nutres.get("" +
     * mn.getId()).addToCalculatedAmount((mn.getCalculatedAmount() amount));
     * 
     * }
     * 
     * 
     * } // for (j)
     */
    /*
     * } // for (i)
     * 
     * 
     * //nut_list.addAll(dfe_main.getNutrientsCalculated());
     * 
     * //nut_list.addAll(dfe_main.getNutrientsCalculated());
     * 
     * nut_list.addAll(dfe_main.getNutrients());
     * 
     * this.list_nutritions.clear();
     * 
     * //for(Enumeration<String> en = nutres.keys(); en.hasMoreElements(); )
     * for(int i=0; i<nut_list.size(); i++ ) {
     * 
     * MealNutrition meal_nut = nut_list.get(i); //en.nextElement());
     * 
     * System.out.println(meal_nut.getCalculatedAmount());
     * 
     * if (meal_nut.getAmount() > 0) { MealNutritionsDisplay mnd = new
     * MealNutritionsDisplay(ic, meal_nut);
     * 
     * NutritionDefinition nd =
     * this.m_da.getDb().nutrition_defs.get(mnd.getId());
     * mnd.setNutritionDefinition(nd);
     * 
     * this.list_nutritions.add(mnd); } }
     * 
     * java.util.Collections.sort(this.list_nutritions, new
     * MealNutritionsDisplay(ic)); this.createModel(this.list_nutritions,
     * this.table_2, this.mnd);
     * 
     * }
     */

    private void refreshNutritions()
    {
//        System.out.println("Refresh Nutritions. Entries: " + this.list_food_entries.size() );

        ArrayList<MealNutrition> nut_list = new ArrayList<MealNutrition>();

        DailyFoodEntries dfe_main = new DailyFoodEntries();

        for (int i = 0; i < this.list_food_entries.size(); i++)
        {
            DailyFoodEntry dfe = this.list_food_entries.get(i).getDailyFoodEntry();

            //System.out.println(dfe);

            dfe_main.addDailyFoodEntry(dfe);
        }

        // nut_list.addAll(dfe_main.getCalculatedNutrients());
        nut_list.addAll(dfe_main.getCalculatedNutrients());

        this.list_nutritions.clear();
        
        //System.out.println("Nutrients: " + nut_list.size());        

        // for(Enumeration<String> en = nutres.keys(); en.hasMoreElements(); )
        for (int i = 0; i < nut_list.size(); i++)
        {

            MealNutrition meal_nut = nut_list.get(i); // en.nextElement());

            //System.out.println(meal_nut.getCalculatedAmount()); // proc v1
            //System.out.println("Meal Nut: " + meal_nut);
           
            
            //if (meal_nut.getAmount() > 0)
            if ((meal_nut.getAmount() > 0) || (meal_nut.getAmountSum() > 0))
            {
                MealNutritionsDisplay _mnd = new MealNutritionsDisplay(ic, meal_nut);

                NutritionDefinition nd = this.m_da.getDbCache().nutrition_defs.get(_mnd.getId());
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

        if ((nutres.get(gi_gl_min).getAmount() == 0))
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

        if ((nutres.get(gi_gl_max).getAmount() == 0))
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
            par = "4003";
        else
            par = "4005";

        if ((nutres.get(par).getAmount() == 0))
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
            par = "4002";
        else
            par = "4004";

        if ((nutres.get(par).getAmount() == 0))
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

    /*
     * public void loadMealsParts() { this.list_parts.clear();
     * 
     * 
     * String parts = this.meal.getParts();
     * 
     * if ((parts==null) || (parts.length()==0)) {
     * this.createModel(this.list_parts, this.table_1, this.mpd);
     * this.list_nutritions.clear(); this.createModel(this.list_nutritions,
     * this.table_2, this.mnd); return; }
     * 
     * StringTokenizer strtok = new StringTokenizer(parts, ";");
     * 
     * while (strtok.hasMoreTokens()) { MealPart mp = new
     * MealPart(strtok.nextToken()); this.list_parts.add(new
     * MealPartsDisplay(ic, mp)); }
     * 
     * this.createModel(this.list_parts, this.table_1, this.mpd);
     * refreshNutritions();
     * 
     * / MealPart mp = new MealPart(msd.getSelectedObjectType(),
     * msd.getSelectedObject(), msd.getAmountValue()); this.list_parts.add(new
     * MealPartsDispay(ic, mp));
     * 
     * this.createModel(this.list_parts, this.table_1, this.mpd);
     * refreshNutritions();
     */

    // }

    /*
     * public void setParent(Object obj) {
     * setTypeOfAction(EditableAbstractPanel.ACTION_ADD);
     * 
     * this.meal = new Meal();
     * 
     * this.label_title.setText(ic.getMessage("ADD_MEAL"));
     * 
     * this.meal_group = (MealGroup)obj;
     * this.tf_group.setText(this.meal_group.getName());
     * this.button_select.setEnabled(false);
     * 
     * this.tf_name.setText(ic.getMessage("NEW_MEAL"));
     * this.tf_name_i18n_key.setText(""); this.tf_name_i18n.setText("");
     * this.jta_desc.setText("");
     * 
     * this.loadMealsParts();
     * 
     * this.was_saved = false; createKeyWord(); }
     * 
     * 
     * public void setData(Object obj) { this.meal = (Meal)obj;
     * 
     * this.was_saved = false;
     * 
     * setTypeOfAction(EditableAbstractPanel.ACTION_EDIT);
     * 
     * this.label_title.setText(ic.getMessage("MEAL_EDIT"));
     * this.tf_name.setText(this.meal.getName());
     * this.tf_name_i18n_key.setText(this.meal.getName_i18n());
     * this.tf_name_i18n.setText(ic.getMessage(this.meal.getName_i18n()));
     * 
     * this.jta_desc.setText(this.meal.getDescription());
     * 
     * 
     * if (this.meal.getGroup_id()>0) { this.meal_group =
     * m_da.tree_roots.get("3").m_meal_groups_ht.get("" +
     * this.meal.getGroup_id());
     * this.tf_group.setText(this.meal_group.getName()); } else {
     * this.meal_group = null; this.tf_group.setText(ic.getMessage("ROOT")); }
     * 
     * 
     * this.loadMealsParts(); this.button_select.setEnabled(true);
     * 
     * createKeyWord();
     * 
     * }
     */

}
