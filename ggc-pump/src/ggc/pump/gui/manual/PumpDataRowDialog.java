package ggc.pump.gui.manual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;

import com.atech.graphics.components.DateTimeComponent;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.plugin.data.DeviceValuesDay;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.util.DataAccessPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     PumpDataRowDialog
 *  Description:  Data dialog for specific row (entry)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class PumpDataRowDialog extends JDialog implements ActionListener, HelpCapable
{

    private static final long serialVersionUID = 8280477836138077888L;

    private DataAccessPump m_da = DataAccessPump.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    JLabel label_title = new JLabel();
    JButton bt_item_1, bt_item_2, bt_item_3, bt_cancel, bt_ok;
    JLabel lbl_add;
    JComboBox cb_entry_type;
    JScrollPane scr_list;
    JButton help_button = null;
    JPanel main_panel = null;
    JList m_list_data = null;
    Container m_parent = null;

    PumpDataTypeComponent pdtc;
    DateTimeComponent dtc;

    String sDate = null;

    DeviceValuesDay dV = null;
    PumpValuesEntry m_dailyValuesRow = null;

    boolean in_process;

    int width = 400;
    int height = 490;

    ArrayList<PumpValuesEntryExt> list_data = new ArrayList<PumpValuesEntryExt>();
    ArrayList<PumpValuesEntryExt> delete_list_data = new ArrayList<PumpValuesEntryExt>();

    /**
     * Data (Hashtable)
     */
    public Map<PumpAdditionalDataType, PumpValuesEntryExt> ht_data = new HashMap<PumpAdditionalDataType, PumpValuesEntryExt>();


    /**
     * Constructor
     *
     * @param ndV
     * @param nDate
     * @param dialog
     */
    public PumpDataRowDialog(DeviceValuesDay ndV, String nDate, JDialog dialog)
    {
        super(dialog, "", true);

        m_parent = dialog;
        initParameters(ndV, nDate);
    }


    /**
     * Constructor
     *
     * @param ndr
     * @param dialog
     */
    public PumpDataRowDialog(PumpValuesEntry ndr, JDialog dialog)
    {
        super(dialog, "", true);
        m_parent = dialog;
        initParameters(ndr);
    }


    /**
     * Init Parameters
     *
     * @param ndV
     * @param nDate
     */
    public void initParameters(DeviceValuesDay ndV, String nDate)
    {
        setTitle(m_ic.getMessage("ADD_ROW"));
        label_title.setText(m_ic.getMessage("ADD_ROW"));

        sDate = nDate;
        dV = ndV;
        init();
        setDate();
        // load();

        if (this.m_dailyValuesRow == null)
        {
            this.m_dailyValuesRow = new PumpValuesEntry("Pump Manual");
        }

        this.setVisible(true);
    }


    /**
     * Init Parameters
     *
     * @param ndr
     */
    public void initParameters(PumpValuesEntry ndr)
    {
        setTitle(m_ic.getMessage("EDIT_ROW"));
        label_title.setText(m_ic.getMessage("EDIT_ROW"));

        // x sDate = ndr.getDateAsString();
        this.m_dailyValuesRow = ndr;

        init();
        load();

        if (this.m_dailyValuesRow.getAdditionalData().size() > 0)
        {
            for (PumpValuesEntryExt entry : this.m_dailyValuesRow.getAdditionalData().values())
            {
                this.addAddItem(entry);
            }
            this.populateJListExtended(this.list_data);
        }

        this.setVisible(true);
    }


    /**
     * Set Date
     */
    public void setDate()
    {
        StringTokenizer strtok = new StringTokenizer(sDate, ".");

        String day = strtok.nextToken();
        String month = strtok.nextToken();
        String year = strtok.nextToken();

        GregorianCalendar gc = new GregorianCalendar();

        String dt = null;
        String sg = "" + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.DAY_OF_MONTH), 2) + "."
                + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.MONTH) + 1, 2) + "." + gc.get(Calendar.YEAR);

        if (sg.equals(sDate))
        {
            dt = year + m_da.getLeadingZero(month, 2) + m_da.getLeadingZero(day, 2)
                    + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.HOUR_OF_DAY), 2)
                    + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.MINUTE), 2) + "00";
        }
        else
        {
            dt = year + m_da.getLeadingZero(month, 2) + m_da.getLeadingZero(day, 2) + "000000";
        }

        this.dtc.setDateTime(Long.parseLong(dt));

    }


    /**
     *  Populates JList component
     * @param input
     */
    public void populateJListExtended(ArrayList<?> input)
    {
        DefaultListModel newListModel = new DefaultListModel();

        for (int i = 0; i < input.size(); i++)
        {
            newListModel.addElement(input.get(i));
        }

        this.m_list_data.setModel(newListModel);
    }


    /**
     * Load
     */
    public void load()
    {
        this.dtc.setDateTime(this.m_dailyValuesRow.getDateTime());
        this.cb_entry_type.setSelectedIndex(this.m_dailyValuesRow.getBaseType().getCode());
        this.pdtc.loadData(this.m_dailyValuesRow);
    }


    private void init()
    {
        ATSwingUtils.initLibrary();

        m_da.addComponent(this);

        this.setSize(width, height);
        ATSwingUtils.centerJDialog(this, this.m_parent);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        main_panel = panel;

        this.getContentPane().add(panel);

        label_title.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD));
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        label_title.setBounds(0, 15, 400, 35);
        panel.add(label_title);

        ATSwingUtils.getLabel(m_ic.getMessage("DATE") + ":", 30, 78, 100, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        ATSwingUtils.getLabel(m_ic.getMessage("TIME") + ":", 30, 108, 100, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        this.dtc = new DateTimeComponent(this.m_da, DateTimeComponent.ALIGN_VERTICAL, 5,
                DateTimeComponent.TIME_MAXIMAL_SECOND);
        dtc.setBounds(140, 75, 100, 35);
        panel.add(dtc);

        ATSwingUtils.getLabel(m_ic.getMessage("ENTRY_TYPE") + ":", 30, 150, 100, 25, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        pdtc = new PumpDataTypeComponent(this, 175);
        pdtc.setType(PumpBaseType.None);
        panel.add(pdtc);

        cb_entry_type = new JComboBox(pdtc.getItems());
        cb_entry_type.setBounds(140, 150, 220, 25);
        cb_entry_type.addActionListener(this);
        cb_entry_type.setActionCommand("event_type");
        panel.add(cb_entry_type);

        int sy = 175 + 20;

        lbl_add = ATSwingUtils.getLabel(m_ic.getMessage("ADDITIONAL_DATA") + ":", 30, sy, 200, 25, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        // list
        this.m_list_data = new JList();
        this.m_list_data.addMouseListener(new MouseAdapter()
        {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2)
                {
                    itemEdit();
                }
            }
        });

        this.scr_list = new JScrollPane(this.m_list_data);
        this.scr_list.setBounds(30, sy + 25, 290, 150);
        panel.add(this.scr_list);

        bt_item_1 = new JButton(ATSwingUtils.getImageIcon_22x22("folder_add.png", this, m_da));
        bt_item_1.setActionCommand("item_add");
        bt_item_1.addActionListener(this);
        bt_item_1.setBounds(340, sy + 25, 30, 30);
        panel.add(bt_item_1);

        bt_item_2 = new JButton(ATSwingUtils.getImageIcon_22x22("folder_edit.png", this, m_da));
        bt_item_2.setActionCommand("item_edit");
        bt_item_2.addActionListener(this);
        bt_item_2.setBounds(340, sy + 65, 30, 30);
        panel.add(bt_item_2);

        bt_item_3 = new JButton(ATSwingUtils.getImageIcon_22x22("folder_delete.png", this, m_da));
        bt_item_3.setActionCommand("item_delete");
        bt_item_3.addActionListener(this);
        bt_item_3.setBounds(340, sy + 105, 30, 30);
        panel.add(bt_item_3);

        bt_ok = new JButton(m_ic.getMessage("OK"), ATSwingUtils.getImageIcon_22x22("ok.png", this, m_da));
        bt_ok.setActionCommand("ok");
        bt_ok.addActionListener(this);
        bt_ok.setBounds(30, sy + 205, 110, 25);
        panel.add(bt_ok);

        bt_cancel = new JButton(m_ic.getMessage("CANCEL"), ATSwingUtils.getImageIcon_22x22("cancel.png", this, m_da));
        bt_cancel.setActionCommand("cancel");
        bt_cancel.addActionListener(this);
        bt_cancel.setBounds(145, sy + 205, 110, 25);
        panel.add(bt_cancel);

        help_button = ATSwingUtils.createHelpButtonByBounds(260, sy + 205, 110, 25, this, ATSwingUtils.FONT_NORMAL,
            m_da);
        panel.add(help_button);

        m_da.enableHelp(this);

    }


    /**
     * Realign Components
     */
    public void realignComponents()
    {

        int sy = 175 + 20 + this.pdtc.getHeight();

        lbl_add.setBounds(30, sy, 200, 25);

        this.scr_list.setBounds(30, sy + 25, 290, 150);

        this.bt_item_1.setBounds(340, sy + 25, 30, 30);
        this.bt_item_2.setBounds(340, sy + 65, 30, 30);
        this.bt_item_3.setBounds(340, sy + 105, 30, 30);

        this.bt_ok.setBounds(30, sy + 205, 110, 25);
        this.bt_cancel.setBounds(145, sy + 205, 110, 25);
        help_button.setBounds(260, sy + 205, 110, 25);

        this.setSize(width, height + this.pdtc.getHeight());

    }

    private boolean was_action = false;


    // Hashtable<String, PumpDataExtendedH> add_data = new Hashtable<String,
    // PumpDataExtendedH>();

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            m_da.removeComponent(this);
            this.dispose();
        }
        else if (action.equals("ok"))
        {

            if ((this.pdtc.getBaseType() == PumpBaseType.None || this.pdtc.getBaseType() == PumpBaseType.AdditionalData)
                    && this.list_data.size() == 0 || !this.pdtc.areRequiredElementsSet())
            {
                this.warningNotSet();
            }
            else
            {
                cmdOk();
            }

            /*
             * if (this.pdtc.getBaseType()==0)
             * {
             * if (this.list_data.size() > 0)
             * this.warningNotSet();
             * else
             * cmdOk();
             * }
             * else
             * {
             * if (this.pdtc.areRequiredElementsSet())
             * {
             * cmdOk();
             * }
             * else
             * {
             * this.warningNotSet();
             * }
             * }
             */

        }
        else if (action.equals("item_add"))
        {
            PumpDataAdditionalWizardOne pdawo = new PumpDataAdditionalWizardOne(this.ht_data, this); // ,
                                                                                                     // this.m_pump_add);
            pdawo.setVisible(true);

            processAdditionalData(pdawo);
        }
        else if (action.equals("item_edit"))
        {
            if (this.m_list_data.isSelectionEmpty())
            {
                JOptionPane.showConfirmDialog(this, m_ic.getMessage("SELECT_ITEM_FIRST"), m_ic.getMessage("ERROR"),
                    JOptionPane.CLOSED_OPTION);
                return;
            }

            itemEdit();
        }
        else if (action.equals("item_delete"))
        {
            if (this.m_list_data.isSelectionEmpty())
            {
                JOptionPane.showConfirmDialog(this, m_ic.getMessage("SELECT_ITEM_FIRST"), m_ic.getMessage("ERROR"),
                    JOptionPane.CLOSED_OPTION);
                return;
            }

            int ii = JOptionPane.showConfirmDialog(this, m_ic.getMessage("ARE_YOU_SURE_DELETE"),
                m_ic.getMessage("ERROR"), JOptionPane.YES_NO_OPTION);

            if (ii == JOptionPane.YES_OPTION)
            {
                PumpValuesEntryExt pc = this.list_data.get(this.m_list_data.getSelectedIndex());

                this.deleteAddItem(pc);

                populateJListExtended(this.list_data);
            }
            else
                return;

        }
        else if (action.equals("event_type"))
        {
            this.pdtc.setType(PumpBaseType.getByCode(this.cb_entry_type.getSelectedIndex()));
            this.realignComponents();
            // System.out.println("Event changes: " +
            // this.cb_entry_type.getSelectedIndex());
        }
        /*
         * else if (action.equals("edit_food"))
         * {
         * DailyValuesMealSelectorDialog dvms = new
         * DailyValuesMealSelectorDialog(
         * dataAccess, this.m_dailyValuesRow.getMealsIds());
         * if (dvms.wasAction())
         * {
         * this.m_dailyValuesRow.setMealsIds(dvms.getStringForDb());
         * this.ftf_ch.setValue(new Float(dvms.getCHSum()
         * .replace(',', '.')));
         * if (!dvms.getStringForDb().equals(""))
         * {
         * this.cb_food_set.setSelected(true);
         * }
         * }
         * else if (action.equals("update_ch"))
         * {
         * PanelMealSelector pms = new PanelMealSelector(this, null,
         * this.m_dailyValuesRow.getMealsIds());
         * this.ftf_ch.setValue(new Float(pms.getCHSumString().replace(',',
         * '.')));
         * }
         * else if (action.equals("bolus_helper"))
         * {
         * BolusHelper bh = new BolusHelper(this,
         * dataAccess.getJFormatedTextValueFloat(ftf_bg2),
         * dataAccess.getJFormatedTextValueFloat(this.ftf_ch),
         * this.dtc.getDateTime());
         * if (bh.hasResult())
         * {
         * this.ftf_ins1.setValue(bh.getResult());
         * }
         * }
         */
        else
        {
            System.out.println("PumpDataRowDialog::unknown command: " + action);
        }

    }


    private void itemEdit()
    {
        PumpValuesEntryExt pc = this.list_data.get(this.m_list_data.getSelectedIndex());

        PumpDataAdditionalWizardTwo pdawo;

        if (pc.getTypeEnum() == PumpAdditionalDataType.FoodDb
                || pc.getTypeEnum() == PumpAdditionalDataType.FoodDescription)
        {
            PumpValuesEntryExt pc2 = null;

            if (this.ht_data.containsKey(PumpAdditionalDataType.Carbohydrates))
            {
                pc2 = this.ht_data.get(PumpAdditionalDataType.Carbohydrates);
            }

            pdawo = new PumpDataAdditionalWizardTwo(this, pc, pc2);
        }
        else
        {
            pdawo = new PumpDataAdditionalWizardTwo(this, pc);
        }

        pdawo.setVisible(true);

        processAdditionalData(pdawo);

    }


    private void warningNotSet()
    {
        JOptionPane.showMessageDialog(this, m_ic.getMessage("NOT_ALL_REQUIRED_VALUES_SET"), m_ic.getMessage("ERROR"),
            JOptionPane.ERROR_MESSAGE);
    }


    private void processAdditionalData(PumpDataAdditionalWizardOne w1)
    {
        if (w1.wasAction())
        {
            processAdditionalData(w1.getObjects());
        }

    }


    private void processAdditionalData(PumpDataAdditionalWizardTwo w2)
    {
        if (w2.wasAction())
        {
            processAdditionalData(w2.getObjects());
        }
    }


    private void processAdditionalData(PumpValuesEntryExt[] objs)
    {

        for (PumpValuesEntryExt obj : objs)
        {
            String key = PumpAdditionalDataType.getByCode(obj.getType()).getTranslation();
            if (this.ht_data.containsKey(key))
            {
                PumpValuesEntryExt o1 = this.ht_data.get(key);
                o1.setValue(obj.getValue());

                if (o1.getId() == 0)
                {
                    m_da.getDb().add(o1);
                }
                else
                {
                    m_da.getDb().edit(o1);
                }

                // deleteAddItem(this.dataAccess.getAdditionalTypes().getTypeDescription(objs[i].getType()));
            }
            else
            {
                addAddItem(obj);
            }
        }

        /*
         * for(int i=0; i<objs.length; i++)
         * {
         * if (objs[i].getObjectUniqueId().equals("0"))
         * {
         * this.dataAccess.getDb().add(objs[i]);
         * }
         * else
         * {
         * if (objs[i].hasChanged())
         * {
         * if (objs[i].getObjectUniqueId().equals("0"))
         * {
         * this.dataAccess.getDb().edit(objs[i]);
         * }
         * }
         * }
         * }
         */

        /*
         * for(int i=0; i<objs.length; i++)
         * {
         * //System.out.println("objs[]: " + objs);
         * //System.out.println("objs[].getType(): " + objs[i].getType());
         * //System.out.println("ht_data: " + ht_data);
         * //System.out.println("add types: " +
         * this.dataAccess.getAdditionalTypes());
         * if (this.ht_data.containsKey(this.dataAccess.getAdditionalTypes().
         * getTypeDescription(objs[i].getType())))
         * {
         * deleteAddItem(this.dataAccess.getAdditionalTypes().getTypeDescription(
         * objs[i
         * ].getType()));
         * }
         * addAddItem(objs[i]);
         * }
         */

        populateJListExtended(this.list_data);

    }


    private void addAddItem(PumpValuesEntryExt obj)
    {
        this.list_data.add(obj);
        this.ht_data.put(PumpAdditionalDataType.getByCode(obj.getType()), obj);
    }


    // @SuppressWarnings("unused")
    // private void deleteAddItem(String item_desc)
    // {
    // if (this.ht_data.containsKey(item_desc))
    // {
    // // PumpValuesEntryExt oe = this.ht_data.get(item_desc);
    //
    // deleteAddItem(this.ht_data.get(item_desc));
    // // this.list_data.remove(oe);
    // // this.ht_data.remove(oe);
    // }
    // }

    private void deleteAddItem(PumpValuesEntryExt oe)
    {
        m_da.getDb().delete(oe); // .add(this.m_dailyValuesRow);
        this.list_data.remove(oe);
        this.ht_data.remove(PumpAdditionalDataType.getByCode(oe.getType()));
    }


    /*
     * String button_command[] = { "update_ch",
     * m_ic.getMessage("UPDATE_FROM_FOOD"), "edit_food",
     * m_ic.getMessage("EDIT_FOOD"), "ok", m_ic.getMessage("OK"), "cancel",
     * m_ic.getMessage("CANCEL"), // "help", m_ic.getMessage("HELP")
     */

    private void cmdOk()
    {
        // System.out.println("Entry Type: " +
        // this.cb_entry_type.getSelectedIndex());

        if (this.cb_entry_type.getSelectedIndex() != PumpBaseType.AdditionalData.getCode()
                && this.cb_entry_type.getSelectedIndex() != 0)
        {
            if (this.m_dailyValuesRow == null)
            {
                this.m_dailyValuesRow = new PumpValuesEntry("Pump Manual");
            }

            this.m_dailyValuesRow.setDateTimeObject(this.dtc.getDateTimeObject());
            this.pdtc.saveData(m_dailyValuesRow);
            this.m_dailyValuesRow.setBaseType(this.cb_entry_type.getSelectedIndex());

            if (this.m_dailyValuesRow.getId() == 0)
            {
                m_da.getDb().add(this.m_dailyValuesRow);
            }
            else
            {
                m_da.getDb().edit(this.m_dailyValuesRow);
            }

            this.was_action = true;

        }

        // additional data
        long dt = this.dtc.getDateTime();

        for (int i = 0; i < this.list_data.size(); i++)
        {
            PumpValuesEntryExt ext = this.list_data.get(i);
            ext.setDt_info(dt);
            // FIXME
            m_da.getDb().commit(ext);

            this.was_action = true;
        }

        for (int i = 0; i < this.delete_list_data.size(); i++)
        {
            // PumpValuesEntryExt ext = this.delete_list_data.get(i);
            m_da.getDb().delete(this.delete_list_data.get(i));

            this.was_action = true;
        }

        m_da.removeComponent(this);
        this.dispose();

    }


    /**
     * Was Action
     *
     * @return
     */
    public boolean wasAction()
    {
        return this.was_action;
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
        return this.help_button;
    }


    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "PumpTool_Data_AddEdit";
    }

}
