package ggc.nutri.panels;

import ggc.nutri.data.MealNutritionsComparator;
import ggc.nutri.data.MealPartsComparator;
import ggc.nutri.db.datalayer.Meal;
import ggc.nutri.db.datalayer.MealGroup;
import ggc.nutri.db.datalayer.MealNutrition;
import ggc.nutri.db.datalayer.MealPart;
import ggc.nutri.db.datalayer.NutritionDefinition;
import ggc.nutri.dialogs.MealSelectorDialog;
import ggc.nutri.dialogs.NutritionGroupDialog;
import ggc.nutri.dialogs.NutritionTreeDialog;
import ggc.nutri.display.MealNutritionsDisplay;
import ggc.nutri.display.MealPartsDisplay;
import ggc.nutri.util.DataAccessNutri;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
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
 *  Filename:     PanelNutritionMealEdit
 *  Description:  Panel for editing nutrition Meal
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class PanelNutritionMealEdit extends GGCTreePanel /* JPanel */implements ActionListener
{

    private static final long serialVersionUID = 1091537198268076868L;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_refuse, label_name, label_name_i18n;
    JLabel label_title;
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

    MealPartsDisplay mpd = null;
    MealNutritionsDisplay mnd = null;

    Meal meal;
    MealGroup meal_group;

    
    /**
     * Constructor
     * 
     * @param dia
     */
    public PanelNutritionMealEdit(NutritionTreeDialog dia)
    {
        super(true);

        m_dialog = dia;

        this.mpd = new MealPartsDisplay(ic);
        this.mnd = new MealNutritionsDisplay(ic);

        font_big = m_da.getFont(DataAccessNutri.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccessNutri.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccessNutri.FONT_NORMAL);

        createPanel();
    }

    private void createPanel()
    {

        this.setSize(520, 560);
        this.setLayout(new ZeroLayout(this.getSize()));

        Font fnt_14_bold = new Font("Times New Roman", Font.BOLD, 14);
        Font fnt_14 = new Font("Times New Roman", Font.PLAIN, 14);

        label_title = new JLabel(ic.getMessage("MEAL_DESCRIPTION_EDIT_ADD"));
        label_title.setBounds(0, 25, 520, 40);
        label_title.setFont(font_big);
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label_title, null);

        label = new JLabel(ic.getMessage("MEAL_NAME") + ":");
        label.setBounds(30, 80, 100, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_name = new JTextField();
        tf_name.setBounds(190, 80, 300, 25);
        tf_name.setFont(fnt_14);
        tf_name.addKeyListener(new KeyListener()
        {

            /*
             * keyReleased() Creates I18n keyword and gets translation
             * 
             * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
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
        label.setBounds(30, 115, 300, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_name_i18n_key = new JTextField();
        tf_name_i18n_key.setBounds(190, 115, 300, 25);
        tf_name_i18n_key.setFont(fnt_14);
        tf_name_i18n_key.setEditable(false);

        this.add(tf_name_i18n_key, null);

        label = new JLabel(ic.getMessage("TRANSLATED_NAME_MEAL") + ":");
        label.setBounds(30, 150, 300, 25);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_name_i18n = new JTextField();
        tf_name_i18n.setBounds(190, 150, 300, 25);
        tf_name_i18n.setFont(fnt_14);
        tf_name_i18n.setEditable(false);
        this.add(tf_name_i18n, null);

        label = new JLabel(ic.getMessage("DESCRIPTION") + ":");
        label.setBounds(30, 180, 300, 30);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        jta_desc = new JTextArea();
        jta_desc.setRows(2);
        // jta.setBounds(100, 190, 300, 60);

        JScrollPane scroll_3 = new JScrollPane(jta_desc);
        scroll_3.setBounds(140, 185, 350, 40);
        this.add(scroll_3, null);

        label = new JLabel(ic.getMessage("GROUP") + ":");
        label.setBounds(30, 235, 300, 30);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_group = new JTextField();
        tf_group.setBounds(140, 235, 200, 25);
        tf_group.setEditable(false);
        this.add(tf_group, null);

        this.button_select = new JButton(ic.getMessage("SELECT_GROUP"));
        button_select.setActionCommand("select_group");
        button_select.addActionListener(this);
        button_select.setBounds(350, 235, 140, 25);
        this.add(button_select, null);

        // scroll_3.setBounds(140, 180, 350, 45);

        // jta.set

        /*
         * label_refuse = new JLabel(); label_refuse.setBounds(300, 230, 50,
         * 30); label_refuse.setFont(fnt_14); this.add(label_refuse, null);
         * 
         * 
         * 
         * / this.label_name_i18n = new JLabel(); // 180
         * this.label_name_i18n.setBounds(30, 165, 460, 50);
         * label_name.setVerticalAlignment(JLabel.TOP);
         * this.label_name_i18n.setFont(fnt_14); this.add(this.label_name_i18n,
         * ZeroLayout.DYNAMIC);
         * 
         * 
         * 
         * label = new JLabel(ic.getMessage("REFUSE") + ":");
         * label.setBounds(30, 230, 300, 30); label.setFont(fnt_14_bold);
         * this.add(label, null);
         */

        /*
         * label_refuse = new JLabel(); label_refuse.setBounds(300, 230, 50,
         * 30); label_refuse.setFont(fnt_14); this.add(label_refuse, null);
         */

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

        this.button = new JButton(new ImageIcon(m_da.getImage("/icons/food_add.gif", this)));
        this.button.addActionListener(this);
        this.button.setActionCommand("add_meal");
        this.button.setToolTipText(ic.getMessage("MEAL_FOOD_ADD_DESC"));
        this.button.setBounds(370, 275, 32, 32);
        this.add(button, null);

        this.button = new JButton(new ImageIcon(m_da.getImage("/icons/food_edit.gif", this)));
        this.button.addActionListener(this);
        this.button.setActionCommand("edit_meal");
        this.button.setToolTipText(ic.getMessage("MEAL_FOOD_EDIT_DESC"));
        this.button.setBounds(410, 275, 32, 32);
        this.add(button, null);

        this.button = new JButton(new ImageIcon(m_da.getImage("/icons/food_delete.gif", this)));
        this.button.addActionListener(this);
        this.button.setActionCommand("remove_meal");
        this.button.setToolTipText(ic.getMessage("MEAL_FOOD_DELETE_DESC"));
        this.button.setBounds(450, 275, 32, 32);
        this.add(button, null);

        // home weight

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

        
        button = new JButton();
        button.setBounds(470, 5, 32, 32);
        button.setAlignmentX(JButton.CENTER_ALIGNMENT);
        button.setAlignmentY(JButton.CENTER_ALIGNMENT);
        button.setIcon(m_da.getImageIcon("disk_blue.png", 26, 26, this));
        button.addActionListener(this);
        button.setActionCommand("save");
        this.add(button, null);
        
        
        return;
    }

    //public static final int MODEL_MEAL_PARTS = 1;
    //public static final int MODEL_MEALS_NUTRITIONS = 2;

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

        if (action.equals("add_meal"))
        {
            //System.out.println("Add Meal");
            MealSelectorDialog msd = new MealSelectorDialog(m_dialog, this.meal.getId());

            if (msd.wasAction())
            {
                MealPart mp = new MealPart(msd.getSelectedObjectType(), msd.getSelectedObject(), msd.getAmountValue());
                this.list_parts.add(new MealPartsDisplay(ic, mp));

                this.createModel(this.list_parts, this.table_1, this.mpd);
                refreshNutritions();
            }

        }
        else if (action.equals("edit_meal"))
        {
            //System.out.println("Edit Meal");

            if (this.table_1.getSelectedRowCount() == 0)
            {
                JOptionPane.showConfirmDialog(this, ic.getMessage("SELECT_ITEM_FIRST"), ic.getMessage("ERROR"),
                    JOptionPane.CLOSED_OPTION);
                return;
            }

            MealPartsDisplay _mnd = this.list_parts.get(this.table_1.getSelectedRow());
            MealSelectorDialog msd = new MealSelectorDialog(m_dialog, _mnd.getMealPart());

            if (msd.wasAction())
            {
                _mnd.setAmount(msd.getAmountValue());
                this.createModel(this.list_parts, this.table_1, this.mpd);
                refreshNutritions();
            }

        }
        else if (action.equals("remove_meal"))
        {
            //System.out.println("Remove Meal");

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
                MealPartsDisplay _mnd = this.list_parts.get(this.table_1.getSelectedRow());
                // PersonContact pc =
                // m_contact_data.get(list_contact.getSelectedIndex());

                this.list_parts.remove(_mnd);

                this.createModel(this.list_parts, this.table_1, this.mpd);
                refreshNutritions();
            }

        }
        else if (action.equals("select_group"))
        {
            NutritionGroupDialog ngd = new NutritionGroupDialog(m_dialog, this.m_dialog.m_tree_type);

            if (ngd.wasAction())
            {
                this.meal_group = (MealGroup) ngd.getSelectedObject();
                this.tf_group.setText(this.meal_group.getName());
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
            System.out.println("PanelNutritionMealEdit::Unknown command: " + action);

    }

    private void refreshNutritions()
    {
        Hashtable<String, MealNutrition> nutres = new Hashtable<String, MealNutrition>();

        loadGI_GL(nutres);

        for (int i = 0; i < this.list_parts.size(); i++)
        {
            ArrayList<MealNutrition> lst = this.list_parts.get(i).getMealPart().getNutrients();

            float amount = this.list_parts.get(i).getMealPart().getAmount();

            for (int j = 0; j < lst.size(); j++)
            {
                MealNutrition mn = lst.get(j);

                if ((mn.getId() >= 4000))
                {
                    // GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003,
                    // GL_MIN = 4004, GL_MAX = 4005

                    if (mn.getId() == 4000)
                    {
                        checkGI_GL(nutres, mn, true);
                    }
                    else if (mn.getId() == 4001)
                    {
                        checkGI_GL(nutres, mn, false);
                    }
                    else if (mn.getId() == 4002)
                    {
                        checkGI_GL_Min(nutres, mn, true);
                    }
                    else if (mn.getId() == 4003)
                    {
                        checkGI_GL_Max(nutres, mn, true);
                    }
                    else if (mn.getId() == 4004)
                    {
                        checkGI_GL_Min(nutres, mn, false);
                    }
                    else if (mn.getId() == 4005)
                    {
                        checkGI_GL_Max(nutres, mn, false);
                    }

                }
                else
                {

                    if (!nutres.containsKey("" + mn.getId()))
                    {
                        MealNutrition mmn = new MealNutrition(mn);
                        nutres.put("" + mmn.getId(), mmn);
                    }

                    // nutres.get("" + mn.getId()).addToAmount((mn.getAmount() *
                    // amount));
                    nutres.get("" + mn.getId()).addToCalculatedAmount(((mn.getAmount() / 100.0f) * amount));

                }

            } // for (j)
        } // for (i)

        this.list_nutritions.clear();

        for (Enumeration<String> en = nutres.keys(); en.hasMoreElements();)
        {

            MealNutrition meal_nut = nutres.get(en.nextElement());

            if (meal_nut.getAmount() > 0)
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

    private void loadGI_GL(Hashtable<String, MealNutrition> nutres)
    {
        // GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, GL_MIN = 4004,
        // GL_MAX = 4005
        nutres.put("4002", new MealNutrition(4002, 0.0f, "GI Min"));
        nutres.put("4003", new MealNutrition(4003, 0.0f, "GI Max"));
        nutres.put("4004", new MealNutrition(4004, 0.0f, "GL Min"));
        nutres.put("4005", new MealNutrition(4005, 0.0f, "GL Max"));
    }

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

    private void loadMealsParts()
    {
        this.list_parts.clear();

        String parts = this.meal.getParts();

        if ((parts == null) || (parts.length() == 0))
        {
            this.createModel(this.list_parts, this.table_1, this.mpd);
            this.list_nutritions.clear();
            this.createModel(this.list_nutritions, this.table_2, this.mnd);
            return;
        }

        StringTokenizer strtok = new StringTokenizer(parts, ";");

        while (strtok.hasMoreTokens())
        {
            MealPart mp = new MealPart(strtok.nextToken());
            this.list_parts.add(new MealPartsDisplay(ic, mp));
        }

        this.createModel(this.list_parts, this.table_1, this.mpd);
        refreshNutritions();

        /*
         * MealPart mp = new MealPart(msd.getSelectedObjectType(),
         * msd.getSelectedObject(), msd.getAmountValue());
         * this.list_parts.add(new MealPartsDispay(ic, mp));
         * 
         * this.createModel(this.list_parts, this.table_1, this.mpd);
         * refreshNutritions();
         */

    }

    /**
     * Set Parent
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setParent(java.lang.Object)
     */
    public void setParent(Object obj)
    {
        setTypeOfAction(EditableAbstractPanel.ACTION_ADD);

        this.meal = new Meal();

        this.label_title.setText(ic.getMessage("ADD_MEAL"));

        this.meal_group = (MealGroup) obj;
        this.tf_group.setText(this.meal_group.getName());
        this.button_select.setEnabled(false);

        this.tf_name.setText(ic.getMessage("NEW_MEAL"));
        this.tf_name_i18n_key.setText("");
        this.tf_name_i18n.setText("");
        this.jta_desc.setText("");
        this.meal.setGroup_id(this.meal_group.getId());

        this.loadMealsParts();

        this.was_saved = false;
        createKeyWord();
    }

    /**
     * Set Data
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setData(java.lang.Object)
     */
    public void setData(Object obj)
    {
        this.meal = (Meal) obj;

        this.was_saved = false;

        setTypeOfAction(EditableAbstractPanel.ACTION_EDIT);

        this.label_title.setText(ic.getMessage("MEAL_EDIT"));
        this.tf_name.setText(this.meal.getName());
        this.tf_name_i18n_key.setText(this.meal.getName_i18n());
        this.tf_name_i18n.setText(ic.getMessage(this.meal.getName_i18n()));

        this.jta_desc.setText(this.meal.getDescription());

        if (this.meal.getGroup_id() > 0)
        {
            //this.meal_group = m_da.tree_roots.get("3").m_meal_groups_ht.get("" + this.meal.getGroup_id());
            this.meal_group = m_da.getDbCache().tree_roots.get("3").findMealGroup(3, this.meal.getGroup_id());
            this.tf_group.setText(this.meal_group.getName());
        }
        else
        {
            this.meal_group = null;
            this.tf_group.setText(ic.getMessage("ROOT"));
        }

        this.loadMealsParts();
        this.button_select.setEnabled(true);

        createKeyWord();

    }

    /**
     * Get Warning string. This method returns warning string for either add or
     * edit. If value returned is null, then no warning message box will be
     * displayed.
     * 
     * @param _action_type type of action (ACTION_ADD, ACTION_EDIT)
     * 
     * @return String value as warning string
     */
    public String getWarningString(int _action_type)
    {
        if (this.isAddAction())
        {
            // add
            return ic.getMessage("WISH_TO_SAVE_NEW_MEAL");
        }
        else
        {
            // edit
            return ic.getMessage("WISH_TO_SAVE_EDITED_MEAL");
        }
    }

    private String temp_parts;
    private String temp_nutritions;

    private void processData()
    {
        Collections.sort(this.list_parts, new MealPartsComparator());

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < this.list_parts.size(); i++)
        {
            if (i != 0)
                sb.append(";");

            sb.append(this.list_parts.get(i).getSaveData());
        }

        //System.out.println("Sb: " + sb.toString());
        //System.out.println("Sb Replace all: " + sb.toString().replaceAll(".", ","));
        
        //System.out.println("Sb ReplaceExpr: " + m_da.replaceExpression(sb.toString(), ".", ","));
        
        
        this.temp_parts = sb.toString();// m_da.replaceExpression(sb.toString(), ".", ","); 
            //sb.toString().replaceAll(".", ",");

        Collections.sort(this.list_nutritions, new MealNutritionsComparator());

        sb = new StringBuffer();

        for (int i = 0; i < this.list_nutritions.size(); i++)
        {
            if (i != 0)
                sb.append(";");

            sb.append(this.list_nutritions.get(i).getSaveData());
        }

        this.temp_nutritions = sb.toString(); // m_da.replaceExpression(sb.toString(), ".", ","); 
            //sb.toString().replaceAll(".", ",");

    }

    /**
     * Was data in this panel changed.
     * 
     * @return true if data changed, false otherwise
     */
    public boolean hasDataChanged()
    {

        //System.out.println("hasDataChanged");

        if (this.was_saved)
            return false;

        processData();

        if (this.isAddAction())
        {
            return true;
        }
        else
        {
            if ((hasChangedEntry(this.meal.getName(), this.tf_name.getText()))
                    || (hasChangedEntry(this.meal.getName_i18n(), this.tf_name_i18n_key.getText()))
                    || (hasChangedEntry(this.meal.getDescription(), this.jta_desc.getText()))
                    || (hasChangedEntry(this.meal.getParts(), this.temp_parts))
                    || (hasChangedEntry(this.meal.getNutritions(), this.temp_nutritions))
                    || (this.meal.getGroup_id() != this.meal_group.getId()))
                return true;
            else
                return false;
        }
    }

    private boolean hasChangedEntry(String old_value, String new_value)
    {
        System.out.println("hasChangedEntry [old=" + old_value + ",new=" + new_value + "]");

        if ((DataAccessNutri.isEmptyOrUnset(old_value)) || (!old_value.equals(new_value)))
            return true;
        else
            return false;

    }

    /**
     * Save data in panel
     * 
     * @return true if save was successful
     */
    public boolean saveData()
    {
        //System.out.println("in save");
        
        if (this.isAddAction())
        {
            //System.out.println("in add");

            //System.out.println(this.meal_group);
            
            this.meal.setName(this.tf_name.getText());
            this.meal.setName_i18n(this.tf_name_i18n_key.getText());
            this.meal.setDescription(this.jta_desc.getText());
            this.meal.setParts(this.temp_parts);
            this.meal.setNutritions(this.temp_nutritions);
            this.meal.setGroup_id(this.meal_group.getId());

            this.m_da.getNutriDb().add(this.meal);
            this.was_saved = true;

            addMeal2Tree(this.meal);

            return true;
        }
        else
        {
            System.out.println("in edit");

            long prev_group_id = this.meal.getGroup_id();

            this.meal.setName(this.tf_name.getText());

            this.meal.setName_i18n(this.tf_name_i18n_key.getText());
            this.meal.setDescription(this.jta_desc.getText());
            this.meal.setParts(this.temp_parts);
            this.meal.setNutritions(this.temp_nutritions);
            this.meal.setGroup_id(this.meal_group.getId());

            this.m_da.getNutriDb().edit(this.meal);
            this.was_saved = true;

            if (prev_group_id != this.meal.getGroup_id())
            {
                removeMealFromTree(this.meal, prev_group_id);
                addMeal2Tree(this.meal);
            }

            return true;
        }

    }

    private void addMeal2Tree(Meal _meal)
    {
        m_da.getDbCache().tree_roots.get("3").addMeal2Tree(3, _meal); 
        //m_da.tree_roots.get("3").m_meal_groups_ht.get("" + _meal.getGroup_id()).addChild(_meal);
        //m_da.tree_roots.get("3").m_meals_ht.put("" + _meal.getId(), _meal);
        this.m_dialog.refreshTree();
    }

    private void removeMealFromTree(Meal _meal, long prev_group_id)
    {
        m_da.getDbCache().tree_roots.get("3").removeMealFromTree(3, _meal, prev_group_id);
        //m_da.tree_roots.get("3").m_meal_groups_ht.get("" + prev_group_id).removeChild(_meal);
    }

}
