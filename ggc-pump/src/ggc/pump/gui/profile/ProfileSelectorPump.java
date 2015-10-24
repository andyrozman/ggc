package ggc.pump.gui.profile;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;

import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.pump.manager.PumpManager;
import ggc.pump.manager.company.AbstractPumpDeviceCompany;
import ggc.pump.util.DataAccessPump;

// TODO: Auto-generated Javadoc
/**
 * The Class ProfileSelector.
 */
public class ProfileSelectorPump extends SelectorAbstractDialog
{

    private static final long serialVersionUID = -1061136628528659695L;

    private static final Logger LOG = LoggerFactory.getLogger(ProfileSelectorPump.class);

    /** 
     * The da_local. 
     */
    DataAccessPump dataAccessPump; // = DataAccessPump.getInstance();


    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public ProfileSelectorPump(DataAccessPump da, JFrame parent) // ,
                                                                 // DataAccessPump
                                                                 // da)
    {
        super(parent, da, 0, null, true);
        dataAccessPump = da;
        this.showDialog();
    }


    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public ProfileSelectorPump(DataAccessPump da, JDialog parent) // ,
                                                                  // DataAccessPump
                                                                  // da)
    {
        super(parent, da, 0, null, true);
        dataAccessPump = da;
        this.showDialog();
    }


    public static boolean isDeviceSelected()
    {
        return (DataAccessPump.getInstance().getSelectedDeviceInstance() != null);
    }


    public static boolean doesDeviceSupportProfiles()
    {
        return CollectionUtils.isNotEmpty(getList());
    }


    // /**
    // * Constructor
    // *
    // * @param parent
    // * @param da
    // */
    // public ProfileSelector(JFrame parent, ATDataAccessAbstract da)
    // {
    // super(parent, da, 0, null, true);
    // da_local = (DataAccessPump)da;
    // this.showDialog();
    // }

    /** 
     * Check And Execute Action Edit
     */
    @Override
    public void checkAndExecuteActionEdit(SelectableInterface si)
    {
        /*
         * ProfileEditor pe = new ProfileEditor(this, (PumpProfileH)si);
         * pe.setVisible(true);
         * if (pe.actionSuccessful())
         * {
         * this.filterEntries();
         * }
         */

    }


    /** 
     * Check And Execute Action New
     */
    @Override
    public void checkAndExecuteActionNew()
    {

    }


    /** 
     * Check And Execute Action Select
     */
    @Override
    public void checkAndExecuteActionSelect()
    {
    }


    /** 
     * Get Full Data
     */
    @Override
    public void getFullData()
    {
        dataAccessPump = DataAccessPump.getInstance();
        this.full = new ArrayList<SelectableInterface>();

        for (String profile : getList())
        {
            this.full.add(new PumpProfileType(profile));
        }

    }


    /**
     * Get List
     * 
     * @return
     */
    public static ArrayList<String> getList()
    {
        DataAccessPump dataAccessPump = DataAccessPump.getInstance();

        ArrayList<String> lst_out = new ArrayList<String>();
        // AbstractPumpDeviceCompany adc =
        // (AbstractPumpDeviceCompany)PumpManager.getInstance().getCompany("Minimed");
        // Vector<AbstractDeviceCompany> lst =
        // PumpManager.getInstance().getCompanies();

        try
        {
            DeviceConfigEntry de = dataAccessPump.getDeviceConfiguration().getSelectedDeviceInstance();

            System.out.println("Device Config Entry: " + de);

            AbstractPumpDeviceCompany adc = (AbstractPumpDeviceCompany) PumpManager.getInstance()
                    .getCompany(de.device_company);

            System.out.println("adc: " + adc);

            Object selectedDeviceInstance = DataAccessPump.getInstance().getSelectedDeviceInstance();

            if (selectedDeviceInstance instanceof DeviceInstanceWithHandler)
            {

            }

            System.out.println("old type: " + dataAccessPump.isSelectedDeviceOldType());

            String[] profiles = adc.getProfileNames();

            for (String profile : profiles)
            {
                lst_out.add(profile);
            }
        }
        catch (Exception ex)
        {
            LOG.error("Error getting profiles from selected pump.: {}", ex.getMessage(), ex);
        }

        return lst_out;
    }


    /**
     * Init Selector Values For Type
     */
    @Override
    public void initSelectorValuesForType()
    {
        setSelectorObject(new PumpProfileType());
        setSelectorName(ic.getMessage("PUMP_PROFILE_SELECTOR"));
        this.descriptions = new Hashtable<String, String>();
        this.descriptions.put("DESC_1", ic.getMessage("NAME_OF_PROFILE"));
        setAllowedActions(
            SelectorAbstractDialog.SELECTOR_ACTION_CANCEL | SelectorAbstractDialog.SELECTOR_ACTION_SELECT);
        this.setColumnSortingEnabled(false);
        this.setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_TEXT);
        this.setHelpStringId("PumpTool_Profile_Type_Selector");
        this.setHelpEnabled(true);
        use_generic_select = true;
        // setNewItemString(i18nControlAbstract.getMessage("NEW__PROFILE"));
    }


    /** 
     * Item State Changed
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
    }


    public static boolean isPrecheckForProfilesSucessful(JDialog component)
    {
        DataAccessPump dataAccessPump = DataAccessPump.getInstance();

        if (!isDeviceSelected())
        {
            dataAccessPump.showDialog(component, DataAccessPump.DIALOG_ERROR,
                dataAccessPump.getI18nControlInstance().getMessage("NO_DEVICE_CANT_GET_PROFILES"));
            return false;
        }

        if (!doesDeviceSupportProfiles())
        {
            dataAccessPump.showDialog(component, DataAccessPump.DIALOG_ERROR,
                dataAccessPump.getI18nControlInstance().getMessage("DEVICE_HAS_NO_PROFILES"));
            return false;
        }

        return true;
    }

    private class PumpProfileType implements SelectableInterface
    {

        String name;


        public PumpProfileType()
        {
        }


        public PumpProfileType(String name)
        {
            this.name = name;
        }


        public int compareTo(SelectableInterface o)
        {

            // TODO Auto-generated method stub
            return 0;
        }


        public int getColumnCount()
        {
            return 1;
        }


        public String getColumnName(int num)
        {
            return ic.getMessage("NAME");
        }


        public String getColumnValue(int num)
        {
            return name;
        }


        public Object getColumnValueObject(int num)
        {
            return name;
        }


        public int getColumnWidth(int num, int width)
        {
            return width;
        }


        public long getItemId()
        {
            return 0;
        }


        public String getShortDescription()
        {
            return this.name;
        }


        public boolean isFound(String text)
        {
            return name.contains(text);
        }


        public boolean isFound(int value)
        {
            return true;
        }


        public boolean isFound(int from, int till, int state)
        {
            return true;
        }


        public void setColumnSorter(ColumnSorter cs)
        {
        }


        public void setSearchContext()
        {
        }


        @Override
        public String toString()
        {
            return this.name;
        }
    }

}
