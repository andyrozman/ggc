package ggc.nutri.panels;

import ggc.nutri.data.HomeWeightComparator;
import ggc.nutri.data.NutritionsComparator;
import ggc.nutri.db.datalayer.FoodDescription;
import ggc.nutri.db.datalayer.FoodGroup;
import ggc.nutri.db.datalayer.NutritionDefinition;
import ggc.nutri.db.datalayer.NutritionHomeWeightType;
import ggc.nutri.dialogs.FoodPartMainSelectorDialog;
import ggc.nutri.dialogs.NutritionGroupDialog;
import ggc.nutri.dialogs.NutritionTreeDialog;
import ggc.nutri.display.HomeWeightDataDisplay;
import ggc.nutri.display.NutritionDataDisplay;
import ggc.nutri.util.DataAccessNutri;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;

import com.atech.graphics.components.ATTableData;
import com.atech.graphics.components.ATTableModel;
import com.atech.graphics.components.EditableAbstractPanel;
import com.atech.graphics.layout.ZeroLayout;
import com.atech.utils.ATDataAccessAbstract;

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
 *  Filename:     PanelNutritionFoodEdit  
 *  Description:  Panel for editing nutrition Food
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class PanelNutritionFoodEdit extends GGCTreePanel implements ActionListener
{

    private static final long serialVersionUID = -5701983225229844484L;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_refuse, label_name, label_name_i18n;
    JLabel label_title;
    JButton button, button_select;
    JTextField tf_name, tf_name_i18n, tf_name_i18n_key, tf_group, tf_refuse;
    JTextArea jta_desc;

    NutritionTreeDialog m_dialog = null;
    ArrayList<NutritionDataDisplay> list_nutritions = new ArrayList<NutritionDataDisplay>();
    ArrayList<HomeWeightDataDisplay> list_hweight = new ArrayList<HomeWeightDataDisplay>();

    JTable table_1, table_2 = null;
    JScrollPane scroll_1, scroll_2 = null;

    ATTableModel model_2 = null;

    boolean was_saved = true;

    HomeWeightDataDisplay hwd = null;
    NutritionDataDisplay ndd = null;

    FoodDescription food;
    FoodGroup food_group;

    /**
     * Constructor
     * 
     * @param dia
     */
    public PanelNutritionFoodEdit(NutritionTreeDialog dia)
    {

        super(true);

        m_dialog = dia;

        this.hwd = new HomeWeightDataDisplay(ic);
        this.ndd = new NutritionDataDisplay(ic);

        font_big = m_da.getFont(ATDataAccessAbstract.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(ATDataAccessAbstract.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(ATDataAccessAbstract.FONT_NORMAL);

        createPanel();

    }

    private void createPanel()
    {

        this.setSize(520, 560);
        this.setLayout(new ZeroLayout(this.getSize()));

        Font fnt_14_bold = new Font("Times New Roman", Font.BOLD, 14);
        Font fnt_14 = new Font("Times New Roman", Font.PLAIN, 14);

        /*
         * this.setId(ch.getId()); this.setFood_group_id(ch.getFood_group_id());
         * this.setName(ch.getName()); this.setI18n_name(ch.getI18n_name());
         * this.setRefuse(ch.getRefuse());
         * this.setNutritions(ch.getNutritions());
         */

        label_title = new JLabel(ic.getMessage("MEAL_DESCRIPTION_EDIT_ADD"));
        label_title.setBounds(0, 10, 520, 40);
        label_title.setFont(font_big);
        // label.setBackground(Color.red);
        // label.setBorder(BorderFactory.createLineBorder(Color.blue));
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label_title, null);

        label = new JLabel(ic.getMessage("MEAL_NAME") + ":");
        label.setBounds(30, 65, 100, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_name = new JTextField();
        tf_name.setBounds(190, 65, 300, 25);
        // tf_name.setVerticalAlignment(JLabel.TOP);
        tf_name.setFont(fnt_14);
        tf_name.addKeyListener(new KeyListener()
        {

            /*
             * keyReleased() Creates I18n keyword and gets translation
             * @see
             * java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
             */
            public void keyReleased(KeyEvent arg0)
            {
                createKeyWord();
            }

            public void keyPressed(KeyEvent e)
            {
            }

            public void keyTyped(KeyEvent e)
            {
            }

        });

        this.add(tf_name, ZeroLayout.DYNAMIC);

        label = new JLabel(ic.getMessage("TRANSLATION_KEYWORD_MEAL") + ":");
        label.setBounds(30, 100, 300, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_name_i18n_key = new JTextField();
        tf_name_i18n_key.setBounds(190, 100, 300, 25);
        tf_name_i18n_key.setFont(fnt_14);
        tf_name_i18n_key.setEditable(false);

        this.add(tf_name_i18n_key, null);

        label = new JLabel(ic.getMessage("TRANSLATED_NAME_MEAL") + ":");
        label.setBounds(30, 135, 300, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_name_i18n = new JTextField();
        tf_name_i18n.setBounds(190, 135, 300, 25);
        tf_name_i18n.setFont(fnt_14);
        tf_name_i18n.setEditable(false);
        this.add(tf_name_i18n, null);

        label = new JLabel(ic.getMessage("DESCRIPTION") + ":");
        label.setBounds(30, 165, 300, 30);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        jta_desc = new JTextArea();
        jta_desc.setRows(2);
        // jta.setBounds(100, 190, 300, 60);

        JScrollPane scroll_3 = new JScrollPane(jta_desc);
        scroll_3.setBounds(140, 170, 350, 40);
        this.add(scroll_3, null);

        label = new JLabel(ic.getMessage("GROUP") + ":");
        label.setBounds(30, 220, 300, 30);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_group = new JTextField();
        tf_group.setBounds(140, 220, 200, 25);
        tf_group.setEditable(false);
        this.add(tf_group, null);

        this.button_select = new JButton(ic.getMessage("SELECT_GROUP"));
        button_select.setActionCommand("select_group");
        button_select.addActionListener(this);
        button_select.setBounds(350, 220, 140, 25);
        this.add(button_select, null);

        label = new JLabel(ic.getMessage("REFUSE_LBL") + ":");
        label.setBounds(30, 250, 300, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_refuse = new JTextField();
        tf_refuse.setBounds(290, 250, 50, 25);
        tf_refuse.setFont(fnt_14);
        this.add(tf_refuse, null);

        label = new JLabel(ic.getMessage("NUTRITIONS") + ":");
        label.setBounds(30, 260, 300, 60);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        table_1 = new JTable();

        this.createModel(this.list_nutritions, this.table_1, this.ndd);

        table_1.setRowSelectionAllowed(true);
        table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table_1.setDoubleBuffered(true);

        scroll_1 = new JScrollPane(table_1);
        scroll_1.setBounds(30, 305, 460, 80);
        this.add(scroll_1, ZeroLayout.DYNAMIC);
        scroll_1.repaint();

        scroll_1.updateUI();

        this.button = new JButton(m_da.getImageIcon("food_add.gif", 27, 27, this));
        // new ImageIcon(m_da.getImage("/icons/food_add.gif", this)));
        this.button.addActionListener(this);
        this.button.setActionCommand("add_nutrition");
        this.button.setToolTipText(ic.getMessage("MEAL_FOOD_ADD_DESC"));
        this.button.setBounds(370, 270, 32, 32);
        this.add(button, null);

        this.button = new JButton(m_da.getImageIcon("food_edit.gif", 27, 27, this));
        // new ImageIcon(m_da.getImage("/icons/food_edit.gif", this)));
        this.button.addActionListener(this);
        this.button.setActionCommand("edit_nutrition");
        this.button.setToolTipText(ic.getMessage("MEAL_FOOD_EDIT_DESC"));
        this.button.setBounds(410, 270, 32, 32);
        this.add(button, null);

        this.button = new JButton(m_da.getImageIcon("food_delete.gif", 27, 27, this));
        // new ImageIcon(m_da.getImage("/icons/food_delete.gif", this)));
        this.button.addActionListener(this);
        this.button.setActionCommand("remove_nutrition");
        this.button.setToolTipText(ic.getMessage("MEAL_FOOD_DELETE_DESC"));
        this.button.setBounds(450, 270, 32, 32);
        this.add(button, null);

        // home weight

        label = new JLabel(ic.getMessage("HOME_WEIGHTS") + ":");
        label.setBounds(30, 385, 300, 60);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        table_2 = new JTable();

        createModel(this.list_hweight, this.table_2, this.hwd);

        table_2.setRowSelectionAllowed(true);
        table_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table_2.setDoubleBuffered(true);

        scroll_2 = new JScrollPane(table_2);
        scroll_2.setBounds(30, 430, 460, 80);
        // scroll_2.
        this.add(scroll_2, ZeroLayout.DYNAMIC);
        scroll_2.repaint();
        scroll_2.updateUI();

        this.button = new JButton(m_da.getImageIcon("weight_add.png", 27, 27, this));
        // new ImageIcon(m_da.get.getImage("/icons/food_add.gif", this)));
        this.button.addActionListener(this);
        this.button.setActionCommand("add_home_weight");
        this.button.setToolTipText(ic.getMessage("HOME_WEIGHT_ADD_DESC"));
        this.button.setBounds(370, 395, 32, 32);
        this.add(button, null);

        this.button = new JButton(m_da.getImageIcon("weight_edit.png", 27, 27, this));
        // new ImageIcon(m_da.getImage("/icons/food_edit.gif", this)));
        this.button.addActionListener(this);
        this.button.setActionCommand("edit_home_weight");
        this.button.setToolTipText(ic.getMessage("HOME_WEIGHT_EDIT_DESC"));
        this.button.setBounds(410, 395, 32, 32);
        this.add(button, null);

        this.button = new JButton(m_da.getImageIcon("weight_delete.png", 27, 27, this));
        // new ImageIcon(m_da.getImage("/icons/food_delete.gif", this)));
        this.button.addActionListener(this);
        this.button.setActionCommand("remove_home_weight");
        this.button.setToolTipText(ic.getMessage("HOME_WEIGHT_DELETE_DESC"));
        this.button.setBounds(450, 395, 32, 32);
        this.add(button, null);

        button = new JButton(m_da.getImageIcon("disk_blue.png", 27, 27, this));
        // button.setAlignmentX(JButton.CENTER_ALIGNMENT);
        // button.setAlignmentY(JButton.CENTER_ALIGNMENT);
        // button.setIcon(m_da.getImageIcon("disk_blue.png", 30, 30, this));
        button.setBounds(440, 10, 32, 32);
        button.addActionListener(this);
        button.setActionCommand("save");
        this.add(button, null);

        this.help_button = m_da.createHelpIconByBounds(480, 10, 32, 32, this);
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

    private void createKeyWord()
    {
        String key = m_da.makeI18nKeyword(tf_name.getText());
        tf_name_i18n_key.setText(key);
        tf_name_i18n.setText(ic.getMessage(key));
    }

    /**
     * Action Listener
     */
    public void actionPerformed(ActionEvent e)
    {

        String action = e.getActionCommand();

        if (action.equals("select_group"))
        {
            NutritionGroupDialog ngd = new NutritionGroupDialog(m_dialog, this.m_dialog.m_tree_type);

            if (ngd.wasAction())
            {
                this.food_group = (FoodGroup) ngd.getSelectedObject();
                this.tf_group.setText(this.food_group.getName());
            }

        }
        else if (action.equals("add_nutrition"))
        {
            FoodPartMainSelectorDialog fpmsd = new FoodPartMainSelectorDialog(m_dialog,
                    FoodPartMainSelectorDialog.SELECTOR_NUTRITION,
                    getExistingIds(FoodPartMainSelectorDialog.SELECTOR_NUTRITION));

            if (fpmsd.wasAction())
            {
                NutritionDataDisplay ndd1 = new NutritionDataDisplay(ic,
                        (NutritionDefinition) fpmsd.getSelectedObject(), fpmsd.getAmountValue());

                this.list_nutritions.add(ndd1);
                this.createModel(this.list_nutritions, this.table_1, this.ndd);
            }

        }
        else if (action.equals("edit_nutrition"))
        {
            if (this.table_1.getSelectedRowCount() == 0)
            {
                JOptionPane.showConfirmDialog(this, ic.getMessage("SELECT_ITEM_FIRST"), ic.getMessage("ERROR"),
                    JOptionPane.CLOSED_OPTION);
                return;
            }

            NutritionDataDisplay _ndd = this.list_nutritions.get(this.table_1.getSelectedRow());

            FoodPartMainSelectorDialog fpmsd = new FoodPartMainSelectorDialog(m_dialog, _ndd);

            if (fpmsd.wasAction())
            {
                // System.out.println("Returned value: " +
                // fpmsd.getAmountValue());

                _ndd.setAmount(fpmsd.getAmountValue());
                this.createModel(this.list_nutritions, this.table_1, this.ndd);
            }

        }
        else if (action.equals("remove_nutrition"))
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
                NutritionDataDisplay _ndd = this.list_nutritions.get(this.table_1.getSelectedRow());

                this.list_nutritions.remove(_ndd);
                this.createModel(this.list_nutritions, this.table_1, this.ndd);
            }

        }
        else if (action.equals("add_home_weight"))
        {
            FoodPartMainSelectorDialog fpmsd = new FoodPartMainSelectorDialog(m_dialog,
                    FoodPartMainSelectorDialog.SELECTOR_HOME_WEIGHT,
                    getExistingIds(FoodPartMainSelectorDialog.SELECTOR_HOME_WEIGHT));

            if (fpmsd.wasAction())
            {
                HomeWeightDataDisplay _hwd = new HomeWeightDataDisplay(ic,
                        (NutritionHomeWeightType) fpmsd.getSelectedObject(), fpmsd.getAmountValue(),
                        fpmsd.getWeightValue());

                this.list_hweight.add(_hwd);
                this.createModel(this.list_hweight, this.table_2, this.hwd);
            }

        }
        else if (action.equals("edit_home_weight"))
        {
            if (this.table_2.getSelectedRowCount() == 0)
            {
                JOptionPane.showConfirmDialog(this, ic.getMessage("SELECT_ITEM_FIRST"), ic.getMessage("ERROR"),
                    JOptionPane.CLOSED_OPTION);
                return;
            }

            HomeWeightDataDisplay _hwd = this.list_hweight.get(this.table_2.getSelectedRow());

            FoodPartMainSelectorDialog fpmsd = new FoodPartMainSelectorDialog(m_dialog, _hwd);

            if (fpmsd.wasAction())
            {
                _hwd.setAmount(fpmsd.getAmountValue());
                _hwd.setWeight(fpmsd.getWeightValue());

                this.createModel(this.list_hweight, this.table_2, this.hwd);
            }

        }
        else if (action.equals("remove_home_weight"))
        {

            if (this.table_2.getSelectedRowCount() == 0)
            {
                JOptionPane.showConfirmDialog(this, ic.getMessage("SELECT_ITEM_FIRST"), ic.getMessage("ERROR"),
                    JOptionPane.CLOSED_OPTION);
                return;
            }

            int ii = JOptionPane.showConfirmDialog(this, ic.getMessage("ARE_YOU_SURE_DELETE"), ic.getMessage("ERROR"),
                JOptionPane.YES_NO_OPTION);

            if (ii == JOptionPane.YES_OPTION)
            {

                HomeWeightDataDisplay _hwd = this.list_hweight.get(this.table_2.getSelectedRow());

                this.list_hweight.remove(_hwd);
                this.createModel(this.list_hweight, this.table_2, this.hwd);
            }
        }
        else if (action.equals("save"))
        {
            if (this.hasDataChanged())
            {
                this.saveData();
            }
        }
        else
        {
            System.out.println("PanelNutritionFoodEdit::Unknown command: " + action);
        }

    }

    private String getExistingIds(int type)
    {
        if (type == FoodPartMainSelectorDialog.SELECTOR_NUTRITION)
        {

            if (this.list_nutritions.size() == 0)
                return null;
            else
            {
                StringBuffer sb = new StringBuffer(".");

                for (int i = 0; i < this.list_nutritions.size(); i++)
                {
                    sb.append(this.list_nutritions.get(i).getId());
                    sb.append(".");
                }

                String stt = sb.toString();
                return stt;
            }
        }
        else
        {
            if (this.list_hweight.size() == 0)
                return null;
            else
            {
                StringBuffer sb = new StringBuffer(".");

                for (int i = 0; i < this.list_hweight.size(); i++)
                {
                    sb.append(this.list_hweight.get(i).getId());
                    sb.append(".");
                }

                String stt = sb.toString();
                return stt;
            }
        }
    }

    /**
     * Set Parent
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setParent(java.lang.Object)
     */
    @Override
    public void setParent(Object obj)
    {
        setTypeOfAction(EditableAbstractPanel.ACTION_ADD);

        this.food = new FoodDescription(2);

        this.label_title.setText(ic.getMessage("ADD_FOOD"));

        this.food_group = (FoodGroup) obj;
        this.tf_group.setText(this.food_group.getName());
        this.button_select.setEnabled(false);

        this.tf_name.setText(ic.getMessage("NEW_FOOD"));
        this.tf_name_i18n_key.setText("");
        this.tf_name_i18n.setText("");
        this.jta_desc.setText("");

        this.was_saved = false;
        createKeyWord();

        this.list_nutritions.clear();
        this.createModel(this.list_nutritions, this.table_1, this.ndd);

        this.list_hweight.clear();
        this.createModel(this.list_hweight, this.table_2, this.hwd);

    }

    /**
     * Set Data
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setData(java.lang.Object)
     */
    @Override
    public void setData(Object obj)
    {
        this.food = (FoodDescription) obj;

        this.was_saved = false;

        setTypeOfAction(EditableAbstractPanel.ACTION_EDIT);

        this.label_title.setText(ic.getMessage("FOOD_EDIT"));
        this.tf_name.setText(this.food.getName());
        this.tf_name_i18n_key.setText(this.food.getName_i18n());
        this.tf_name_i18n.setText(ic.getMessage(this.food.getName_i18n()));

        this.jta_desc.setText(this.food.getDescription());

        if (this.food.getGroup_id() > 0)
        {
            // this.food_group = m_da.tree_roots.get("2").m_groups_ht.get("" +
            // this.food.getGroup_id());
            this.food_group = m_da.getDbCache().tree_roots.get("2").findFoodGroup(2, this.food.getGroup_id());
            this.tf_group.setText(this.food_group.getName());
        }
        else
        {
            this.food_group = null;
            this.tf_group.setText(ic.getMessage("ROOT"));
        }

        this.button_select.setEnabled(true);
        createKeyWord();

        StringTokenizer strtok = new StringTokenizer(this.food.getNutritions(), ";");
        this.list_nutritions.clear();

        while (strtok.hasMoreTokens())
        {
            NutritionDataDisplay ndd1 = new NutritionDataDisplay(ic, strtok.nextToken());

            ndd1.setNutritionDefinition(m_da.getDbCache().nutrition_defs.get(ndd1.getId()));
            list_nutritions.add(ndd1);
        }

        this.createModel(this.list_nutritions, this.table_1, this.ndd);

        this.list_hweight.clear();

        if (this.food.getHome_weights() != null && !this.food.getHome_weights().equals("")
                && !this.food.getHome_weights().equals("null"))
        {
            strtok = new StringTokenizer(this.food.getHome_weights(), ";");

            while (strtok.hasMoreTokens())
            {
                HomeWeightDataDisplay hwd1 = new HomeWeightDataDisplay(ic, strtok.nextToken());

                hwd1.setHomeWeightDefinition(m_da.getDbCache().homeweight_defs.get(hwd1.getId()).getName());
                this.list_hweight.add(hwd1);
            }
        }

        this.createModel(this.list_hweight, this.table_2, this.hwd);

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
        if (this.isAddAction())
            // add
            return ic.getMessage("WISH_TO_SAVE_NEW_FOOD");
        else
            // edit
            return ic.getMessage("WISH_TO_SAVE_EDITED_FOOD");
    }

    private String temp_nutritions;
    private String temp_home_weight;

    private void processData()
    {

        Collections.sort(this.list_nutritions, new NutritionsComparator());

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < this.list_nutritions.size(); i++)
        {
            if (i != 0)
            {
                sb.append(";");
            }

            sb.append(this.list_nutritions.get(i).getSaveData());
        }

        this.temp_nutritions = sb.toString();
        // m_da.replaceExpression(sb.toString(), ".", ",");
        // sb.toString().replaceAll(".", ",");

        Collections.sort(this.list_hweight, new HomeWeightComparator());

        sb = new StringBuffer();

        for (int i = 0; i < this.list_hweight.size(); i++)
        {
            if (i != 0)
            {
                sb.append(";");
            }

            sb.append(this.list_hweight.get(i).getSaveData());
        }

        this.temp_home_weight = sb.toString();
        // m_da.replaceExpression(sb.toString(), ".", ",");
        // sb.toString().replaceAll(".", ",");

    }

    /**
     * Was data in this panel changed.
     * 
     * @return true if data changed, false otherwise
     */
    @Override
    public boolean hasDataChanged()
    {

        if (this.was_saved)
            return false;

        processData();

        if (this.isAddAction())
            return true;
        else
        {
            if (hasChangedEntry(this.food.getName(), this.tf_name.getText())
                    || hasChangedEntry(this.food.getName_i18n(), this.tf_name_i18n_key.getText())
                    || hasChangedEntry(this.food.getDescription(), this.jta_desc.getText())
                    || hasChangedEntry(this.food.getNutritions(), this.temp_nutritions)
                    || hasChangedEntry(this.food.getHome_weights(), this.temp_home_weight)
                    || this.food.getGroup_id() != this.food_group.getId())
                return true;
            else
                return false;
        }
    }

    private boolean hasChangedEntry(String old_value, String new_value)
    {
        if (DataAccessNutri.isEmptyOrUnset(old_value) || !old_value.equals(new_value))
            return true;
        else
            return false;
    }

    /**
     * Save data in panel
     * 
     * @return true if save was successful
     */
    @Override
    public boolean saveData()
    {

        if (this.isAddAction())
        {
            this.food.setName(this.tf_name.getText());
            this.food.setName_i18n(this.tf_name_i18n_key.getText());
            this.food.setDescription(this.jta_desc.getText());
            this.food.setNutritions(this.temp_nutritions);
            this.food.setHome_weights(this.temp_home_weight);
            this.food.setGroup_id(this.food_group.getId());

            this.m_da.getNutriDb().add(this.food);
            this.was_saved = true;

            addFood2Tree(this.food);

            return true;
        }
        else
        {

            long prev_group_id = this.food.getGroup_id();

            this.food.setName(this.tf_name.getText());
            this.food.setName_i18n(this.tf_name_i18n_key.getText());
            this.food.setDescription(this.jta_desc.getText());
            this.food.setNutritions(this.temp_nutritions);
            this.food.setHome_weights(this.temp_home_weight);
            this.food.setGroup_id(this.food_group.getId());

            this.m_da.getNutriDb().edit(this.food);
            this.was_saved = true;

            if (prev_group_id != this.food.getGroup_id())
            {
                removeFoodFromTree(this.food, prev_group_id);
                addFood2Tree(this.food);
            }

            return true;
        }

        // return false;

    }

    private void addFood2Tree(FoodDescription _food)
    {
        // System.out.println(food);
        // m_da.tree_roots.get("2").m_groups_ht.get("" +
        // _food.getGroup_id()).addChild(_food);
        // m_da.tree_roots.get("2").m_foods_ht.put("" + _food.getId(), _food);

        m_da.getDbCache().tree_roots.get("2").addFood2Tree(2, _food);

        this.m_dialog.refreshTree();
    }

    private void removeFoodFromTree(FoodDescription _food, long prev_group_id)
    {
        m_da.getDbCache().tree_roots.get("2").removeFoodFromTree(2, _food, prev_group_id);
        // m_da.tree_roots.get("2").m_groups_ht.get("" +
        // prev_group_id).removeChild(_food);
    }

    public String getHelpId()
    {
        return "GGC_Food_User_Food_Edit";
    }

}
