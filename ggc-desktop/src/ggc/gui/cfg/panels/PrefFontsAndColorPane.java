package ggc.gui.cfg.panels;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.data.cfg.ConfigurationManagerWrapper;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.util.DataAccess;
import ggc.gui.cfg.PropertiesDialog;
import ggc.gui.cfg.SchemeDialog;
import ggc.gui.cfg.SchemeEDDialog;
import ggc.gui.graphs.DailyGraphView;

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
 *  Filename:     PrefFontsAndColorPane
 *  Description:  Preferences Panel to choose Fonts and Colors.
 * 
 *  Author: schultd
 *          andyrozman {andy@atech-software.com}  
 */

public class PrefFontsAndColorPane extends AbstractPrefOptionsPanel
        implements MouseListener, ActionListener, ListSelectionListener, ItemListener
{

    private static final long serialVersionUID = -4053935746376957308L;
    private JList itemList;
    private JLabel lblcolor;
    private Vector<String> items;

    private JButton bt_new_scheme, bt_edit_scheme;
    private JComboBox cb_scheme = null;

    private Hashtable<String, ColorSchemeH> color_schemes = null;
    private ColorSchemeH selected_sheme = null;
    private String[] av_schemes_names = null;

    private ArrayList<ColorSchemeH> deleted_schemes = null;
    private boolean item_changed_status = false;

    private DailyGraphView dgv = null;
    long selected_id = 0;

    private JPanel schemePanel;// Moved declarations out of the init() to be
                               // able to use them later in updateGraphView.
    private JPanel colorPanel;
    private JPanel testingPanel;
    private Box box; // End Moved declarations out of the init() to be able to
    private GlucoseUnitType glucoseUnitType;
    // use them later in updateGraphView.


    /**
     * Constructor
     * 
     * @param dia
     */
    public PrefFontsAndColorPane(PropertiesDialog dia)
    {
        super(dia);

        color_schemes = ggcProperties.getColorSchemes();
        selected_sheme = ggcProperties.getSelectedColorScheme();
        glucoseUnitType = configurationManagerWrapper.getGlucoseUnit();

        refreshColorSchemeList(false, false);

        this.deleted_schemes = new ArrayList<ColorSchemeH>();

        items = new Vector<String>();

        items.add(i18nControl.getMessage("BG_HIGH_ZONE"));
        items.add(i18nControl.getMessage("BG_TARGET_ZONE"));
        items.add(i18nControl.getMessage("BG_LOW_ZONE"));
        items.add(i18nControl.getMessage("BG"));
        items.add(i18nControl.getMessage("BG_AVERAGE"));
        items.add(i18nControl.getMessage("BREAD_UNITS"));
        items.add(i18nControl.getMessage("INSULIN") + " " + configurationManagerWrapper.getIns1Abbr());
        items.add(i18nControl.getMessage("INSULIN") + " " + configurationManagerWrapper.getIns2Abbr());
        items.add(i18nControl.getMessage("INSULIN"));
        items.add(i18nControl.getMessage("INS_SLASH_BU_QUOTIENT"));

        init();
        // dataAccess.enableHelp(this);
    }


    private void init()
    {
        setLayout(null);

        schemePanel = new JPanel();
        schemePanel.setLayout(null);
        schemePanel.setBorder(BorderFactory.createTitledBorder(i18nControl.getMessage("COLOR_SCHEME_SELECT")));
        schemePanel.setBounds(0, 0, 515, 50);

        JLabel label = new JLabel(i18nControl.getMessage("SELECTED_COLOR_SCHEME") + ":");
        label.setBounds(10, 13, 150, 30);

        schemePanel.add(label);

        cb_scheme = new JComboBox(av_schemes_names);
        cb_scheme.setBounds(170, 15, 140, 25);
        schemePanel.add(cb_scheme);

        bt_new_scheme = new JButton(i18nControl.getMessage("ADD"));
        bt_new_scheme.setBounds(315, 15, 90, 25);
        schemePanel.add(bt_new_scheme);

        bt_edit_scheme = new JButton(i18nControl.getMessage("EDIT_DEL_SHORT"));
        bt_edit_scheme.setBounds(410, 15, 90, 25);
        schemePanel.add(bt_edit_scheme);

        this.add(schemePanel);

        bt_new_scheme.addActionListener(this);
        bt_new_scheme.setActionCommand("add");

        bt_edit_scheme.addActionListener(this);
        bt_edit_scheme.setActionCommand("edit");

        cb_scheme.setSelectedItem(this.selected_sheme.getName());
        cb_scheme.addItemListener(this);

        // Color panel

        colorPanel = new JPanel();
        colorPanel.setLayout(null);
        colorPanel.setBorder(BorderFactory.createTitledBorder(i18nControl.getMessage("COLORS")));
        colorPanel.setBounds(0, 50, 515, 105); // 45 110
        this.add(colorPanel);

        itemList = new JList(items);
        itemList.setBorder(new LineBorder(Color.black, 1));
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.addListSelectionListener(this);

        // itemList.s

        JScrollPane scp = new JScrollPane(itemList);
        scp.setBounds(10, 20, 200, 75); // 80
        colorPanel.add(scp);

        // JPanel a = new JPanel(null);
        // a.add(lblcolor = new JLabel(" "));

        lblcolor = new JLabel(" ");
        lblcolor.setBounds(215, 20, 290, 75); // 80
        lblcolor.setOpaque(true);
        lblcolor.setBorder(BorderFactory.createLineBorder(Color.black));
        lblcolor.addMouseListener(this);

        // colorPanel.add(itemList, BorderLayout.WEST);
        colorPanel.add(lblcolor); // a, BorderLayout.CENTER);

        // if (true)
        // return;

        testingPanel = new JPanel();
        testingPanel.setLayout(new BorderLayout());
        testingPanel.setBorder(BorderFactory.createTitledBorder(i18nControl.getMessage("COLOR_PREVIEW")));
        testingPanel.setBounds(0, 152, 515, 269);

        dgv = new DailyGraphView(selected_sheme, createDailyGraphValues(), glucoseUnitType);
        dgv.setBounds(10, 17, 500, 50);

        // testingPanel.setPreferredSize(new Dimension(150, 170));
        testingPanel.add(dgv);

        this.add(testingPanel);

        // colorPanel.setBounds(0, 50, 515, 130);

        /*
         * box = Box.createVerticalBox();
         * box.add(schemePanel);
         * box.add(colorPanel);
         * box.add(testingPanel);
         * add(box);
         */
        itemList.setSelectedIndex(0);
    }


    public void updateGlucoseUnitType(GlucoseUnitType unitType)
    {
        this.glucoseUnitType = unitType;
    }


    /**
     * updateGraphView() updates the graphs in the preferences section to reflect
     * changes in mmol/l or mg/dl and the other values(high/low BG etc).
     * Changed by Andy, after changing this panel.
     * 
     * @author henrik
     */
    public void updateGraphView()
    {
        // if (true)
        // return;

        // box.remove(testingPanel);
        // testingPanel = new JPanel(new BorderLayout());
        // testingPanel.setBorder(BorderFactory.createTitledBorder(i18nControl.getMessage("COLOR_PREVIEW")));
        testingPanel.removeAll();

        dgv = null;
        dgv = new DailyGraphView(selected_sheme, createDailyGraphValues(), glucoseUnitType);
        dgv.setBounds(8, 20, 500, 245);
        // dgv.setBounds(0, 0, 510, 210);

        testingPanel.add(dgv);

        // testingPanel.setPreferredSize(new Dimension(150, 170));
        // testingPanel.add(dgv, BorderLayout.CENTER);
        // box.add(testingPanel);
        // box.validate();
        // box.repaint();

    }


    /**
     * Creates a sample piece of <code>{@link DailyValues data}</code> for the
     * sample <code>{@link DailyGraphView graph}</code>.
     * 
     * @return The <code>{@link DailyValues sample data}</code>.
     */
    public static DailyValues createDailyGraphValues()
    {
        DailyValues dv = new DailyValues();
        DataAccess da = DataAccess.getInstance();
        ConfigurationManagerWrapper configurationManagerWrapper = da.getConfigurationManagerWrapper();

        dv.addRow(new DailyValuesRow(200604040400L, 100, 0, 20, 0f, "", ""));
        dv.addRow(new DailyValuesRow(200604040700L, 40, 0, 0, 2f, "", ""));
        dv.addRow(new DailyValuesRow(200604041000L, 200, 3, 0, 0f, "", ""));
        dv.addRow(new DailyValuesRow(200604041200L, (int) configurationManagerWrapper.getBG1High(), 4, 0, 0f, "", ""));
        dv.addRow(new DailyValuesRow(200604041500L, (int) configurationManagerWrapper.getBG1Low(), 0, 0, 2f, "", ""));
        dv.addRow(new DailyValuesRow(200604041700L, 90, 0, 0, 0f, "", ""));
        dv.addRow(new DailyValuesRow(200604042000L, 120, 11, 0, 5f, "", ""));
        dv.addRow(new DailyValuesRow(200604042200L, 150, 1, 10, 0f, "", ""));

        return dv;
    }


    /**
     * Invoked when the mouse button has been clicked (pressed and released) on
     * a component.
     */
    public void mouseClicked(MouseEvent e)
    {
        if (this.selected_sheme.getCustom_type() == 0)
        {
            JOptionPane.showMessageDialog(this, i18nControl.getMessage("COLOR_SCHEME_PREDEFINED"),
                i18nControl.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        Color col = JColorChooser.showDialog(null, i18nControl.getMessage("CHOOSE_A_COLOR"), Color.black);
        if (col != null)
        {
            setColor(itemList.getSelectedValue().toString(), col);
            lblcolor.setBackground(col);
            changed = true;
            dgv.applyColorScheme(this.selected_sheme);
        }
    }


    /**
     * {@inheritDoc}
     */
    public void mousePressed(MouseEvent e)
    {
    }


    /**
     * {@inheritDoc}
     */
    public void mouseReleased(MouseEvent e)
    {
    }


    /**
     * {@inheritDoc}
     */
    public void mouseEntered(MouseEvent e)
    {
    }


    /**
     * {@inheritDoc}
     */
    public void mouseExited(MouseEvent e)
    {
    }


    /**
     * Get Color
     * 
     * @param name
     * @return
     */
    public Color getColor(String name)
    {
        int color = 0;

        if (name.equals(i18nControl.getMessage("BG_HIGH_ZONE")))
        {
            color = this.selected_sheme.getColor_bg_high();
        }
        else if (name.equals(i18nControl.getMessage("BG_TARGET_ZONE")))
        {
            color = this.selected_sheme.getColor_bg_target();
        }
        else if (name.equals(i18nControl.getMessage("BG_LOW_ZONE")))
        {
            color = this.selected_sheme.getColor_bg_low();
        }
        else if (name.equals(i18nControl.getMessage("BG")))
        {
            color = this.selected_sheme.getColor_bg();
        }
        else if (name.equals(i18nControl.getMessage("BG_AVERAGE")))
        {
            color = this.selected_sheme.getColor_bg_avg();
        }
        else if (name.equals(i18nControl.getMessage("BREAD_UNITS")))
        {
            color = this.selected_sheme.getColor_ch();
        }
        else if (name.equals(i18nControl.getMessage("INSULIN") + " " + configurationManagerWrapper.getIns1Abbr()))
        {
            color = this.selected_sheme.getColor_ins1();
        }
        else if (name.equals(i18nControl.getMessage("INSULIN") + " " + configurationManagerWrapper.getIns2Abbr()))
        {
            color = this.selected_sheme.getColor_ins2();
        }
        else if (name.equals(i18nControl.getMessage("INSULIN")))
        {
            color = this.selected_sheme.getColor_ins();
        }
        else if (name.equals(i18nControl.getMessage("INS_SLASH_BU_QUOTIENT")))
        {
            color = this.selected_sheme.getColor_ins_perbu();
        }

        return dataAccess.getColor(color);

    }


    /**
     * Set Color
     * 
     * @param name
     * @param clr
     */
    public void setColor(String name, Color clr)
    {
        int color = clr.getRGB();

        if (name.equals(i18nControl.getMessage("BG_HIGH_ZONE")))
        {
            this.selected_sheme.setColor_bg_high(color);
        }
        else if (name.equals(i18nControl.getMessage("BG_TARGET_ZONE")))
        {
            this.selected_sheme.setColor_bg_target(color);
        }
        else if (name.equals(i18nControl.getMessage("BG_LOW_ZONE")))
        {
            this.selected_sheme.setColor_bg_low(color);
        }
        else if (name.equals(i18nControl.getMessage("BG")))
        {
            this.selected_sheme.setColor_bg(color);
        }
        else if (name.equals(i18nControl.getMessage("BG_AVERAGE")))
        {
            this.selected_sheme.setColor_bg_avg(color);
        }
        else if (name.equals(i18nControl.getMessage("BREAD_UNITS")))
        {
            this.selected_sheme.setColor_ch(color);
        }
        else if (name.equals(i18nControl.getMessage("INSULIN") + " " + configurationManagerWrapper.getIns1Abbr()))
        {
            this.selected_sheme.setColor_ins1(color);
        }
        else if (name.equals(i18nControl.getMessage("INSULIN") + " " + configurationManagerWrapper.getIns2Abbr()))
        {
            this.selected_sheme.setColor_ins2(color);
        }
        else if (name.equals(i18nControl.getMessage("INSULIN")))
        {
            this.selected_sheme.setColor_ins(color);
        }
        else if (name.equals(i18nControl.getMessage("INS_SLASH_BU_QUOTIENT")))
        {
            this.selected_sheme.setColor_ins_perbu(color);
        }
    }


    /*
     * x private int findIndex(String col) { for (int i=0;
     * i<this.av_schemes_names.length; i++) { if
     * (this.av_schemes_names[i].equals(col)) { return i; } }
     * return -1; }
     */

    /**
     * Invoked when an item has been selected or deselected by the user. The
     * code written for this method performs the operations that need to occur
     * when an item is selected (or deselected).
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        if (this.item_changed_status)
            return;

        this.selected_sheme = this.color_schemes.get(this.cb_scheme.getSelectedItem().toString());
        dgv.applyColorScheme(this.selected_sheme);
        lblcolor.setBackground(getColor(itemList.getSelectedValue().toString()));
    }


    /**
     * {@inheritDoc}
     */
    public void valueChanged(ListSelectionEvent e)
    {
        lblcolor.setBackground(getColor(itemList.getSelectedValue().toString()));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (e.getActionCommand().equals("add"))
        {
            SchemeDialog sd = new SchemeDialog(this.parent, this.av_schemes_names);

            if (sd.actionSuccessful())
            {
                String[] str = sd.getActionResults();

                ColorSchemeH cs = this.color_schemes.get(str[2]);

                // System.out.println("As Template: " + str[2]);

                selected_sheme = new ColorSchemeH(str[1], 1, cs.getColor_bg(), cs.getColor_bg_avg(),
                        cs.getColor_bg_low(), cs.getColor_bg_high(), cs.getColor_bg_target(), cs.getColor_ins(),
                        cs.getColor_ins1(), cs.getColor_ins2(), cs.getColor_ins_perbu(), cs.getColor_ch());

                this.color_schemes.put(selected_sheme.getName(), selected_sheme);

                String[] strs = new String[this.av_schemes_names.length + 1];

                int i = 0;

                for (; i < this.av_schemes_names.length; i++)
                {
                    strs[i] = this.av_schemes_names[i];
                }

                strs[i] = str[1];

                this.av_schemes_names = strs;

                this.cb_scheme.addItem(str[1]);
                this.cb_scheme.setSelectedIndex(av_schemes_names.length - 1);
                // sel
                dgv.applyColorScheme(selected_sheme);

            }
        }
        else
        {

            ColorSchemeH cs = this.color_schemes.get(this.cb_scheme.getSelectedItem());

            if (cs.getCustom_type() == 0)
            {
                JOptionPane.showMessageDialog(this, i18nControl.getMessage("PREDEFINED_SCHEME_CANT_BE_CHANGED"),
                    i18nControl.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            else
            {
                // DataAccess.notImplemented(
                // "PrefFonts...:actionPerformed.edit Non Custom");
                SchemeEDDialog sed = new SchemeEDDialog(this.parent, av_schemes_names,
                        (String) this.cb_scheme.getSelectedItem());

                if (sed.actionSuccessful())
                {
                    String[] res = sed.getActionResults();

                    System.out.println("Action: " + res[0] + " " + res[1]);

                    if (res[0].equals("1"))
                    {
                        this.color_schemes.remove(cs.getName());
                        cs.setName(res[1]);
                        this.color_schemes.put(res[1], cs);

                        refreshColorSchemeList(true, false);
                    }
                    else if (res[0].equals("2"))
                    {
                        // add scheme to deleted
                        this.deleted_schemes.add(cs);

                        // remove scheme from active color schemes
                        this.color_schemes.remove(this.cb_scheme.getSelectedItem());

                        refreshColorSchemeList(true, true);
                    }

                }
            }

        }

    }


    private void refreshColorSchemeList(boolean force_update_to_combo, boolean action_delete)
    {

        item_changed_status = true;

        av_schemes_names = new String[color_schemes.size()];

        int i = 0;
        for (Enumeration<String> en = this.color_schemes.keys(); en.hasMoreElements();)
        {
            ColorSchemeH cs = color_schemes.get(en.nextElement());
            av_schemes_names[i] = cs.getName();
            i++;
        }

        if (force_update_to_combo)
        {
            int sel_item = this.cb_scheme.getSelectedIndex();

            this.cb_scheme.removeAllItems();

            for (String av_schemes_name : this.av_schemes_names)
            {
                this.cb_scheme.addItem(av_schemes_name);
            }

            item_changed_status = true;

            if (action_delete)
            {
                this.cb_scheme.setSelectedIndex(0);
            }
            else
            {
                this.cb_scheme.setSelectedIndex(sel_item);
            }
        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProps()
    {

        for (Enumeration<String> en = this.color_schemes.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();
            ColorSchemeH cs = this.color_schemes.get(key);

            if (cs.getCustom_type() == 1)
            {
                if (cs.getId() == 0)
                {
                    cs.setId(dataAccess.getDb().addHibernate(cs));
                }
                else
                {
                    dataAccess.getDb().editHibernate(cs);
                }
            }
        }

        ggcProperties.setColorSchemes(this.color_schemes, false);

        // System.out.println("Selected color: " +
        // cb_scheme.getSelectedItem().toString());
        ggcProperties.setColorSchemeObject(cb_scheme.getSelectedItem().toString());

        // remove deleted schemes
        for (int i = 0; i < this.deleted_schemes.size(); i++)
        {
            dataAccess.getDb().deleteHibernate(this.deleted_schemes.get(i));
        }

    }


    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        return "GGC_Prefs_Colors_Fonts";
    }

}
