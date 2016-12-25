package ggc.gui.panels.prefs;

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.PropertiesDialog;
import ggc.gui.dialogs.SchemeDialog;
import ggc.gui.dialogs.SchemeEDDialog;
import ggc.gui.graphs.DailyGraphView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.atech.help.HelpCapable;

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


public class PrefFontsAndColorPane extends AbstractPrefOptionsPanel implements MouseListener, ActionListener,
        ListSelectionListener, ItemListener, HelpCapable
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

    private JPanel schemePanel;//Moved declarations out of the init() to be able to use them later in updateGraphView.
    private JPanel colorPanel;
    private JPanel testingPanel;
    private Box box;           //End Moved declarations out of the init() to be able to use them later in updateGraphView.

    /**
     * Constructor
     * 
     * @param dia
     */
    public PrefFontsAndColorPane(PropertiesDialog dia)
    {
        super(dia);

        color_schemes = settings.getColorSchemes();
        selected_sheme = settings.getSelectedColorScheme();

        refreshColorSchemeList(false, false);

        this.deleted_schemes = new ArrayList<ColorSchemeH>();

        items = new Vector<String>();

        items.add(m_ic.getMessage("BG_HIGH_ZONE"));
        items.add(m_ic.getMessage("BG_TARGET_ZONE"));
        items.add(m_ic.getMessage("BG_LOW_ZONE"));
        items.add(m_ic.getMessage("BG"));
        items.add(m_ic.getMessage("BG_AVERAGE"));
        items.add(m_ic.getMessage("BREAD_UNITS"));
        items.add(m_ic.getMessage("INSULIN") + " " + settings.getIns1Abbr());
        items.add(m_ic.getMessage("INSULIN") + " " + settings.getIns2Abbr());
        items.add(m_ic.getMessage("INSULIN"));
        items.add(m_ic.getMessage("INS_SLASH_BU_QUOTIENT"));

        init();
        // m_da.enableHelp(this);
    }

    
    private void init()
    {
        setLayout(new BorderLayout());

        schemePanel = new JPanel(new GridLayout(1, 3));
        schemePanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("COLOR_SCHEME_SELECT")));

        schemePanel.add(new JLabel(m_ic.getMessage("SELECTED_COLOR_SCHEME") + ":"));
        schemePanel.add(cb_scheme = new JComboBox(av_schemes_names));

        schemePanel.add(bt_new_scheme = new JButton(m_ic.getMessage("ADD")));
        schemePanel.add(bt_edit_scheme = new JButton(m_ic.getMessage("EDIT_DEL_SHORT")));
        bt_new_scheme.addActionListener(this);
        bt_new_scheme.setActionCommand("add");

        bt_edit_scheme.addActionListener(this);
        bt_edit_scheme.setActionCommand("edit");

        cb_scheme.setSelectedItem(this.selected_sheme.getName());
        cb_scheme.addItemListener(this);

        itemList = new JList(items);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.addListSelectionListener(this);

        colorPanel = new JPanel(new BorderLayout());
        colorPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("COLORS")));

        JPanel a = new JPanel(new GridLayout(1, 1));
        a.add(lblcolor = new JLabel(" "));

        lblcolor.setOpaque(true);
        lblcolor.setBorder(BorderFactory.createLineBorder(Color.black));

        lblcolor.addMouseListener(this);

        colorPanel.add(itemList, BorderLayout.WEST);
        colorPanel.add(a, BorderLayout.CENTER);

        testingPanel = new JPanel(new BorderLayout());
        testingPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("COLOR_PREVIEW")));
        dgv = new DailyGraphView(selected_sheme, createDailyGraphValues());
        testingPanel.setPreferredSize(new Dimension(150, 170));
        testingPanel.add(dgv, BorderLayout.CENTER);

        box = Box.createVerticalBox();
        box.add(schemePanel);
        box.add(colorPanel);
        box.add(testingPanel);

        add(box);

        itemList.setSelectedIndex(0);
    }
    
    /**
     * updateGraphView() updates the graphs in the preferences section to reflect
     * changes in mmol/l or mg/dl and the other values(high/low BG etc).
     * @author henrik
     */
    
   public void updateGraphView()
   {
       
        box.remove(testingPanel);
        testingPanel = new JPanel(new BorderLayout());
        testingPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("COLOR_PREVIEW")));
        dgv = new DailyGraphView(selected_sheme, createDailyGraphValues());
        testingPanel.setPreferredSize(new Dimension(150, 170));
        testingPanel.add(dgv, BorderLayout.CENTER);
        box.add(testingPanel);
        box.validate();
        box.repaint();
        
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

        dv.addRow(new DailyValuesRow(200604040600L, 100, 0, 20, 0f, "", ""));
        dv.addRow(new DailyValuesRow(200604040700L, 40, 0, 0, 2f, "", ""));
        dv.addRow(new DailyValuesRow(200604040800L, 200, 3, 0, 0f, "", ""));
        dv.addRow(new DailyValuesRow(200604040900L, (int) da.getSettings().getBG1_High(), 4, 0, 0f, "", ""));
        dv.addRow(new DailyValuesRow(200604041000L, (int) da.getSettings().getBG1_Low(), 0, 0, 2f, "", ""));
        dv.addRow(new DailyValuesRow(200604041100L, 90, 0, 0, 0f, "", ""));
        dv.addRow(new DailyValuesRow(200604041200L, 120, 11, 0, 5f, "", ""));
        dv.addRow(new DailyValuesRow(200604041300L, 150, 1, 10, 0f, "", ""));

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
            JOptionPane.showMessageDialog(this, m_ic.getMessage("COLOR_SCHEME_PREDEFINED"), m_ic.getMessage("ERROR"),
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        Color col = JColorChooser.showDialog(null, m_ic.getMessage("CHOOSE_A_COLOR"), Color.black);
        if (col != null)
        {
            setColor(itemList.getSelectedValue().toString(), col);
            lblcolor.setBackground(col);
            changed = true;
            dgv.applyColorScheme(this.selected_sheme);
        }
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) { }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) { }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) { }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) { }

    /**
     * Get Color
     * 
     * @param name
     * @return
     */
    public Color getColor(String name)
    {
        int color = 0;

        if (name.equals(m_ic.getMessage("BG_HIGH_ZONE")))
            color = this.selected_sheme.getColor_bg_high();
        else if (name.equals(m_ic.getMessage("BG_TARGET_ZONE")))
            color = this.selected_sheme.getColor_bg_target();
        else if (name.equals(m_ic.getMessage("BG_LOW_ZONE")))
            color = this.selected_sheme.getColor_bg_low();
        else if (name.equals(m_ic.getMessage("BG")))
            color = this.selected_sheme.getColor_bg();
        else if (name.equals(m_ic.getMessage("BG_AVERAGE")))
            color = this.selected_sheme.getColor_bg_avg();
        else if (name.equals(m_ic.getMessage("BREAD_UNITS")))
            color = this.selected_sheme.getColor_ch();
        else if (name.equals(m_ic.getMessage("INSULIN") + " " + settings.getIns1Abbr()))
            color = this.selected_sheme.getColor_ins1();
        else if (name.equals(m_ic.getMessage("INSULIN") + " " + settings.getIns2Abbr()))
            color = this.selected_sheme.getColor_ins2();
        else if (name.equals(m_ic.getMessage("INSULIN")))
            color = this.selected_sheme.getColor_ins();
        else if (name.equals(m_ic.getMessage("INS_SLASH_BU_QUOTIENT")))
            color = this.selected_sheme.getColor_ins_perbu();

        return m_da.getColor(color);

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

        if (name.equals(m_ic.getMessage("BG_HIGH_ZONE")))
            this.selected_sheme.setColor_bg_high(color);
        else if (name.equals(m_ic.getMessage("BG_TARGET_ZONE")))
            this.selected_sheme.setColor_bg_target(color);
        else if (name.equals(m_ic.getMessage("BG_LOW_ZONE")))
            this.selected_sheme.setColor_bg_low(color);
        else if (name.equals(m_ic.getMessage("BG")))
            this.selected_sheme.setColor_bg(color);
        else if (name.equals(m_ic.getMessage("BG_AVERAGE")))
            this.selected_sheme.setColor_bg_avg(color);
        else if (name.equals(m_ic.getMessage("BREAD_UNITS")))
            this.selected_sheme.setColor_ch(color);
        else if (name.equals(m_ic.getMessage("INSULIN") + " " + settings.getIns1Abbr()))
            this.selected_sheme.setColor_ins1(color);
        else if (name.equals(m_ic.getMessage("INSULIN") + " " + settings.getIns2Abbr()))
            this.selected_sheme.setColor_ins2(color);
        else if (name.equals(m_ic.getMessage("INSULIN")))
            this.selected_sheme.setColor_ins(color);
        else if (name.equals(m_ic.getMessage("INS_SLASH_BU_QUOTIENT")))
            this.selected_sheme.setColor_ins_perbu(color);
    }

    /*
     * x private int findIndex(String col) { for (int i=0;
     * i<this.av_schemes_names.length; i++) { if
     * (this.av_schemes_names[i].equals(col)) { return i; } }
     * 
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
     * Value Changed
     * 
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e)
    {
        lblcolor.setBackground(getColor(itemList.getSelectedValue().toString()));
    }

    /**
     * Action Performed
     * 
     * @see ggc.gui.panels.prefs.AbstractPrefOptionsPanel#actionPerformed(java.awt.event.ActionEvent)
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

                selected_sheme = new ColorSchemeH(str[1], 1, cs.getColor_bg(), cs.getColor_bg_avg(), cs
                        .getColor_bg_low(), cs.getColor_bg_high(), cs.getColor_bg_target(), cs.getColor_ins(), cs
                        .getColor_ins1(), cs.getColor_ins2(), cs.getColor_ins_perbu(), cs.getColor_ch());

                this.color_schemes.put(selected_sheme.getName(), selected_sheme);

                String[] strs = new String[(this.av_schemes_names.length + 1)];

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
                JOptionPane.showMessageDialog(this, m_ic.getMessage("PREDEFINED_SCHEME_CANT_BE_CHANGED"), m_ic
                        .getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
                return;
            }
            else
            {
                // DataAccess.notImplemented(
                // "PrefFonts...:actionPerformed.edit Non Custom");
                SchemeEDDialog sed = new SchemeEDDialog(this.parent, av_schemes_names, (String) this.cb_scheme
                        .getSelectedItem());

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

            for (int j = 0; j < this.av_schemes_names.length; j++)
            {
                this.cb_scheme.addItem(this.av_schemes_names[j]);
            }

            item_changed_status = true;

            if (action_delete)
                this.cb_scheme.setSelectedIndex(0);
            else
                this.cb_scheme.setSelectedIndex(sel_item);
        }

    }

    /**
     * Save Properties
     * 
     * @see ggc.gui.panels.prefs.AbstractPrefOptionsPanel#saveProps()
     */
    @Override
    public void saveProps()
    {

        for (Enumeration<String> en = this.color_schemes.keys(); en.hasMoreElements();)
        {
            String key = (String) en.nextElement();
            ColorSchemeH cs = this.color_schemes.get(key);

            if (cs.getCustom_type() == 1)
            {
                if (cs.getId() == 0)
                {
                    cs.setId(m_da.getDb().addHibernate(cs));
                }
                else
                {
                    m_da.getDb().editHibernate(cs);
                }
            }
        }

        settings.setColorSchemes(this.color_schemes, false);

        // System.out.println("Selected color: " +
        // cb_scheme.getSelectedItem().toString());
        settings.setColorSchemeObject(cb_scheme.getSelectedItem().toString());

        // remove deleted schemes
        for (int i = 0; i < this.deleted_schemes.size(); i++)
        {
            m_da.getDb().deleteHibernate(this.deleted_schemes.get(i));
        }

    }

    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /**
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }

    /**
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
        return this.parent.getHelpButton();
    }

    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "GGC_Prefs_Colors_Fonts";
    }

}
