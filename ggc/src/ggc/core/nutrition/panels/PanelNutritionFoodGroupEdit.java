package ggc.core.nutrition.panels;

import ggc.core.db.datalayer.FoodGroup;
import ggc.core.db.datalayer.MealGroup;
import ggc.core.nutrition.GGCTreeRoot;
import ggc.core.nutrition.NutritionTreeDialog;
import ggc.core.nutrition.dialogs.NutritionGroupDialog;
import ggc.core.util.DataAccess;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.atech.graphics.components.EditableAbstractPanel;

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
 *  Filename:     PanelNutritionFoodGroupEdit  
 *  Description:  Panel for editing food/meal group
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class PanelNutritionFoodGroupEdit extends GGCTreePanel implements ActionListener 
{

    private static final long serialVersionUID = -824948577643991763L;

    Font font_big, font_normal, font_normal_b;
    JLabel label, label_name, label_name_i18n, label_parent, label_title;
    JButton button, button_select;

    JTextField tf_parent, tf_name, tf_name_i18n, tf_name_i18n_key;

    NutritionTreeDialog m_dialog = null;
    int group_type = 2;

    FoodGroup parent_food_group = null;
    MealGroup parent_meal_group = null;

    FoodGroup food_group = null;
    MealGroup meal_group = null;

    boolean was_saved = true;


    /**
     * Constructor
     * 
     * @param dia
     * @param type
     */
    public PanelNutritionFoodGroupEdit(NutritionTreeDialog dia, int type)
    {

        super(true, dia.ic);

        this.group_type = type;

        m_dialog = dia;

        font_big = m_da.getFont(DataAccess.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccess.FONT_NORMAL);

        createPanel();

    }

    private void createPanel()
    {

        this.setSize(520, 520);
        // this.setBackground(Color.blue);
        this.setLayout(null);

        Font fnt_14_bold = new Font("Times New Roman", Font.BOLD, 14);
        Font fnt_14 = new Font("Times New Roman", Font.PLAIN, 14);

        label_title = new JLabel(ic.getMessage("FOOD_GROUP_EDIT_ADD"));
        label_title.setBounds(0, 35, 520, 40);
        label_title.setFont(font_big);
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label_title, null);

        // this.setDescription(ch.getDescription());
        // this.setDescription_i18n(ch.getDescription_i18n());

        label = new JLabel(ic.getMessage("GROUP_NAME") + ":");
        label.setBounds(80, 100, 100, 30);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_name = new JTextField();
        tf_name.setBounds(80, 140, 320, 25);
        tf_name.setFont(fnt_14);
        tf_name.addKeyListener(new KeyListener()
        {

            /*
             * keyReleased() Creates I18n keyword and gets translation
             * 
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
        this.add(tf_name, null);

        // new KeyListener();

        label = new JLabel(ic.getMessage("TRANSLATION_KEYWORD") + ":");
        label.setBounds(80, 180, 320, 60);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_name_i18n_key = new JTextField();
        tf_name_i18n_key.setBounds(80, 230, 320, 25);
        tf_name_i18n_key.setFont(fnt_14);
        tf_name_i18n_key.setEditable(false);

        this.add(tf_name_i18n_key, null);

        label = new JLabel(ic.getMessage("TRANSLATED_NAME") + ":");
        label.setBounds(80, 265, 320, 60);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_name_i18n = new JTextField();
        tf_name_i18n.setBounds(80, 315, 320, 25);
        tf_name_i18n.setFont(fnt_14);
        tf_name_i18n.setEditable(false);

        this.add(tf_name_i18n, null);

        label = new JLabel(ic.getMessage("PARENT_GROUP"));
        label.setBounds(80, 365, 300, 30);
        label.setFont(fnt_14_bold);
        this.add(label, null);

        tf_parent = new JTextField();
        tf_parent.setBounds(80, 400, 320, 25);
        tf_parent.setEditable(false);
        this.add(tf_parent, null);

        button_select = new JButton(ic.getMessage("SELECT_GROUP"));
        button_select.setActionCommand("select_group");
        button_select.addActionListener(this);
        button_select.setBounds(260, 430, 140, 25);
        this.add(button_select, null);

        button = new JButton();
        button.setBounds(470, 15, 32, 32);
        button.setAlignmentX(JButton.CENTER_ALIGNMENT);
        button.setAlignmentY(JButton.CENTER_ALIGNMENT);
        button.setIcon(m_da.getImageIcon("disk_blue.png", 26, 26, this));
        button.addActionListener(this);
        button.setActionCommand("save");
        this.add(button, null);

        /*
         * label_parent = new JLabel(ic.getMessage("Desc"));
         * label_parent.setBounds(40, 370, 300, 60);
         * label_parent.setFont(font_normal); this.add(label_parent, null);
         */

        createKeyWord();

        return;
    }

    private void createKeyWord()
    {
        String key = m_da.makeI18nKeyword(tf_name.getText());
        tf_name_i18n_key.setText(key);
        tf_name_i18n.setText(ic.getMessage(key));
    }


    private boolean checkRecursiveGroup(FoodGroup fg, long target)
    {
        if (fg.getId() == target)
            return true;
        else
        {
            for (int i = 0; i < fg.getGroupChildrenCount(); i++)
            {
                boolean check = checkRecursiveGroup((FoodGroup) fg.getGroupChild(i), target);
                if (check == true)
                    return true;
            }

            return false;
        }
    }

    private boolean checkRecursiveGroup(MealGroup fg, long target)
    {
        if (fg.getId() == target)
            return true;
        else
        {
            for (int i = 0; i < fg.getGroupChildrenCount(); i++)
            {
                boolean check = checkRecursiveGroup((MealGroup) fg.getGroupChild(i), target);
                if (check == true)
                    return true;
            }

            return false;
        }
    }

    FoodGroup parent_group;

    /**
     * Set Parent
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setParent(java.lang.Object)
     */
    public void setParent(Object obj)
    {
        this.was_saved = false;
        this.button_select.setEnabled(false);

//        System.out.println("setParent: " + obj);

        if (group_type == GGCTreeRoot.TREE_USER_NUTRITION)
        {
            setTypeOfAction(EditableAbstractPanel.ACTION_ADD);
            tf_name.setText(ic.getMessage("NEW_GROUP"));
            this.label_title.setText(ic.getMessage("ADD_FOOD_GROUP"));
            this.parent_food_group = (FoodGroup) obj;
            this.tf_parent.setText(this.parent_food_group.getName());
            createKeyWord();
        }
        else
        {
            setTypeOfAction(EditableAbstractPanel.ACTION_ADD);
            tf_name.setText(ic.getMessage("NEW_GROUP"));
            this.label_title.setText(ic.getMessage("ADD_MEAL_GROUP"));
            this.parent_meal_group = (MealGroup) obj;
            this.tf_parent.setText(this.parent_meal_group.getName());
            createKeyWord();
        }
    }

    /**
     * Set Parent Root
     * 
     * @see ggc.core.nutrition.panels.GGCTreePanel#setParentRoot(java.lang.Object)
     */
    public void setParentRoot(Object obj)
    {
        this.was_saved = false;
        this.button_select.setEnabled(false);

//        System.out.println("setParentRoot: " + obj);

        if (group_type == GGCTreeRoot.TREE_USER_NUTRITION)
        {
            setTypeOfAction(EditableAbstractPanel.ACTION_ADD);
            tf_name.setText(ic.getMessage("NEW_GROUP"));
            this.label_title.setText(ic.getMessage("ADD_FOOD_GROUP"));
            this.parent_food_group = new FoodGroup(2);
            this.parent_food_group.setId(0L);
            this.tf_parent.setText(ic.getMessage("ROOT"));
            createKeyWord();
        }
        else
        {
            setTypeOfAction(EditableAbstractPanel.ACTION_ADD);
            tf_name.setText(ic.getMessage("NEW_GROUP"));
            this.label_title.setText(ic.getMessage("ADD_MEAL_GROUP"));
            this.parent_meal_group = new MealGroup();
            this.parent_meal_group.setId(0L);
            this.tf_parent.setText(ic.getMessage("ROOT"));
            createKeyWord();
        }
    }

    /**
     * Set Data
     * 
     * @see com.atech.graphics.components.EditableAbstractPanel#setData(java.lang.Object)
     */
    public void setData(Object obj)
    {
        this.was_saved = false;
        this.button_select.setEnabled(true);

        if ((group_type == GGCTreeRoot.TREE_USDA_NUTRITION) || (group_type == GGCTreeRoot.TREE_USER_NUTRITION))
        {
            setTypeOfAction(EditableAbstractPanel.ACTION_EDIT);
            this.label_title.setText(ic.getMessage("EDIT_FOOD_GROUP"));
            this.food_group = (FoodGroup) obj;

            tf_name.setText(this.food_group.getName());
            tf_name_i18n.setText(this.food_group.getName_i18n());

            if (this.food_group.getParentId() > 0)
            {
                //this.parent_food_group = m_da.tree_roots.get("2").m_groups_ht.get("" + this.food_group.getParentId());
                this.parent_food_group = m_da.tree_roots.get("2").findFoodGroup(2, this.food_group.getParentId());
                this.tf_parent.setText(this.parent_food_group.getName());
            }
            else
            {
                this.parent_food_group = null;
                this.tf_parent.setText(ic.getMessage("ROOT"));
            }

            createKeyWord();

        }
        else
        {
            setTypeOfAction(EditableAbstractPanel.ACTION_EDIT);
            this.label_title.setText(ic.getMessage("EDIT_MEAL_GROUP"));
            this.meal_group = (MealGroup) obj;

            tf_name.setText(this.meal_group.getName());
            tf_name_i18n.setText(this.meal_group.getName_i18n());

            if (this.meal_group.getParent_id() > 0)
            {
                //this.parent_meal_group = m_da.tree_roots.get("3").m_meal_groups_ht.get("" + this.meal_group.getParent_id());
                this.parent_meal_group = m_da.tree_roots.get("3").findMealGroup(3, this.meal_group.getParent_id());
                this.tf_parent.setText(this.parent_meal_group.getName());
            }
            else
            {
                this.parent_meal_group = null;
                this.tf_parent.setText(ic.getMessage("ROOT"));
            }

            createKeyWord();

        }

        /*
         * this.label_title.setText(ic.getMessage("EDIT_FOOD_GROUP")); FoodGroup
         * group = (FoodGroup)obj;
         * 
         * label_name.setText(group.getName());
         * label_name_i18n.setText(group.getName_i18n());
         */
    }

    /**
     * Action Performed
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {

        String command = e.getActionCommand();

        if (command.equals("select_group"))
        {
            NutritionGroupDialog ngd = new NutritionGroupDialog(m_da, this.m_dialog.m_tree_type);

            if (ngd.wasAction())
            {
                if (this.m_dialog.m_tree_type == GGCTreeRoot.TREE_USER_NUTRITION)
                {
                    FoodGroup fg = (FoodGroup) ngd.getSelectedObject();

                    boolean rec = this.checkRecursiveGroup(this.food_group, fg.getId());

                    if (rec)
                    {
                        JOptionPane.showMessageDialog(this, ic.getMessage("CANT_SELECT_CHILD_OR_GROUP"), ic
                                .getMessage("WARNING"), JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    else
                    {
                        this.parent_food_group = fg;
                        this.tf_parent.setText(this.parent_food_group.getName());
                    }
                }
                else
                {
                    MealGroup fg = (MealGroup) ngd.getSelectedObject();

                    boolean rec = this.checkRecursiveGroup(this.meal_group, fg.getId());

                    if (rec)
                    {
                        JOptionPane.showMessageDialog(this, ic.getMessage("CANT_SELECT_CHILD_OR_GROUP"), ic
                                .getMessage("WARNING"), JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    else
                    {
                        this.parent_meal_group = fg;
                        this.tf_parent.setText(this.parent_meal_group.getName());
                    }
                }

            }

        }
        else if (command.equals("save"))
        {
            if (this.hasDataChanged())
            {
                this.saveData();
            }
        }

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
            return ic.getMessage("WISH_TO_SAVE_NEW_GROUP");
        }
        else
        {
            // edit
            return ic.getMessage("WISH_TO_SAVE_EDITED_GROUP");
        }

    }

    /**
     * Was data in this panel changed.
     * 
     * @return true if data changed, false otherwise
     */
    public boolean hasDataChanged()
    {

        if (this.was_saved)
            return false;

        if (this.isAddAction())
        {
            return true;
        }
        else
        {
            if (this.m_dialog.m_tree_type == GGCTreeRoot.TREE_USER_NUTRITION)
            {
                if ((this.getParentId() != this.food_group.getParentId())
                        || (!this.food_group.getName().equals(this.tf_name.getText()))
                        || (!this.food_group.getName_i18n().equals(this.tf_name_i18n_key.getText())))
                    return true;
            }
            else
            {
                if ((this.getParentId() != this.meal_group.getParent_id())
                        || (!this.meal_group.getName().equals(this.tf_name.getText()))
                        || (!this.meal_group.getName_i18n().equals(this.tf_name_i18n_key.getText())))
                    return true;
            }

            return false;
        }
    }

    private void debug(String msg)
    {
        // System.out.println("PanelNutritionFoodGroupEdit: " + msg);
        System.out.println(msg);
    }

    /**
     * Save data in panel
     * 
     * @return true if save was successful
     */
    public boolean saveData()
    {
        debug("saveData");
        if (this.isAddAction())
        {
            debug("addAction");
            if (this.m_dialog.m_tree_type == GGCTreeRoot.TREE_USER_NUTRITION)
            {
                FoodGroup fg = new FoodGroup(2);

                fg.setParentId(getParentId());
                fg.setName(tf_name.getText().trim());
                fg.setName_i18n(this.tf_name_i18n_key.getText());

                this.m_da.getDb().add(fg);
                this.was_saved = true;

//                System.out.println(fg.getLongDescription());
                
                this.addFoodGroup2Tree(fg);

            }
            else
            {
                MealGroup fg = new MealGroup();

                fg.setParent_id(getParentId());
                fg.setName(tf_name.getText().trim());
                fg.setName_i18n(this.tf_name_i18n_key.getText());

                this.m_da.getDb().add(fg);
                this.was_saved = true;

                //System.out.println(fg.getLongDescription());
                
                this.addMealGroup2Tree(fg);

            }

            return true;
        }
        else
        {

            debug("EditAction");

            if (this.m_dialog.m_tree_type == GGCTreeRoot.TREE_USER_NUTRITION)
            {
                long prev_parent_id = this.food_group.getParentId();

                this.food_group.setParentId(getParentId());
                this.food_group.setName(tf_name.getText().trim());
                this.food_group.setName_i18n(this.tf_name_i18n_key.getText());

                this.m_da.getDb().edit(this.food_group);

                if (prev_parent_id != this.food_group.getParentId())
                {
                    this.removeFoodGroupFromTree(this.food_group, prev_parent_id);
                    this.addFoodGroup2Tree(this.food_group);
                }

            }
            else
            {

                long prev_parent_id = this.meal_group.getParent_id();

                this.meal_group.setParent_id(getParentId());
                this.meal_group.setName(tf_name.getText().trim());
                this.meal_group.setName_i18n(this.tf_name_i18n_key.getText());

                this.m_da.getDb().edit(this.meal_group);

                if (prev_parent_id != this.meal_group.getParent_id())
                {
                    this.removeMealGroupFromTree(this.meal_group, prev_parent_id);
                    this.addMealGroup2Tree(this.meal_group);
                }
            }

            return false;
        }
    }

    private void removeFoodGroupFromTree(FoodGroup fg, long prev_parent_id)
    {
        m_da.tree_roots.get("2").removeFoodGroupFromTree(2, fg, prev_parent_id);
        /*if (prev_parent_id == 0)
        {
            m_da.tree_roots.get("2").m_groups_tree.remove(fg);
        }
        else
        {
            m_da.tree_roots.get("2").m_groups_ht.get("" + prev_parent_id).removeChild(fg);
        }*/
    }

    private void removeMealGroupFromTree(MealGroup fg, long prev_parent_id)
    {
        m_da.tree_roots.get("3").removeMealGroupFromTree(3, fg, prev_parent_id);
        /*
        if (prev_parent_id == 0)
        {
            m_da.tree_roots.get("3").m_meal_groups_tree.remove(fg);
        }
        else
        {
            m_da.tree_roots.get("3").m_meal_groups_ht.get("" + prev_parent_id).removeChild(fg);
        }*/
    }

    private void addFoodGroup2Tree(FoodGroup fg)
    {
        m_da.tree_roots.get("2").addFoodGroup2Tree(2, fg);
        //m_da.tree_roots.get("2").addFoodGroup(fg);
        m_dialog.refreshTree();

        /*
         * if (fg.getParentId()==0) { m_da.tree_roots.get("2").addFoodGroup(fg);
         * m_dialog.refreshTree(); } else {
         * m_da.tree_roots.get("2").m_groups_ht.get("" +
         * fg.getParentId()).addChild(fg);
         * //m_da.tree_roots.get("2").m_groups_ht.put("" + fg.getParentId(),
         * fg);
         * 
         * m_dialog.refreshTree(); }
         */
    }

    private void addMealGroup2Tree(MealGroup fg)
    {
        m_da.tree_roots.get("3").addMealGroup2Tree(3, fg);
        //m_da.tree_roots.get("3").addMealGroup(fg);
        m_dialog.refreshTree();

        /*
         * if (fg.getParent_id()==0) {
         * m_da.tree_roots.get("3").addMealGroup(fg); m_dialog.refreshTree(); }
         * else { m_da.tree_roots.get("3").m_meal_groups_ht.get("" +
         * fg.getParent_id()).addChild(fg); m_dialog.refreshTree(); }
         */
    }

    private long getParentId()
    {
        if (this.m_dialog.m_tree_type == GGCTreeRoot.TREE_USER_NUTRITION)
        {
            if (this.parent_food_group == null)
                return 0L;
            else
                return this.parent_food_group.getId();
        }
        else
        {
            if (this.parent_meal_group == null)
                return 0L;
            else
                return this.parent_meal_group.getId();
        }

    }

}
