package ggc.pump.gui.profile;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Calendar;

import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;
import com.atech.utils.ATSwingUtils;
import com.atech.utils.data.ATechDate;

import ggc.core.db.hibernate.pump.PumpProfileH;
import ggc.pump.db.PumpProfile;
import ggc.pump.util.DataAccessPump;

// TODO: Auto-generated Javadoc
/**
 * The Class ProfileSelector.
 */
public class ProfileSelector extends SelectorAbstractDialog
{

    private static final long serialVersionUID = -1061136628528659695L;

    /** 
     * The da_local. 
     */
    DataAccessPump da_local = DataAccessPump.getInstance();
    boolean m_select = false;


    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public ProfileSelector(DataAccessPump da, Component parent) // ,
                                                                // DataAccessPump
                                                                // da)
    {
        super(da.getMainParent(), da, 0, null, true);
        da_local = da;

        ATSwingUtils.centerJDialog(this, parent);
        this.setResizable(false);

        this.showDialog();
    }


    /**
     * Constructor
     * 
     * @param da
     * @param parent
     * @param select
     */
    public ProfileSelector(DataAccessPump da, Component parent, boolean select) // ,
                                                                                // DataAccessPump
                                                                                // da)
    {
        super(da.getMainParent(), da, 0, null, false);
        this.m_select = select;
        da_local = da;
        init();

        ATSwingUtils.centerJDialog(this, parent);
        this.setResizable(false);

        this.showDialog();
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
        ProfileEditor pe = new ProfileEditor(this, (PumpProfileH) si);
        pe.setVisible(true);

        if (pe.actionSuccessful())
        {
            this.filterEntries();
        }

    }


    /** 
     * Check And Execute Action New
     */
    @Override
    public void checkAndExecuteActionNew()
    {
        ProfileEditor pe = new ProfileEditor(this);
        pe.setVisible(true);

        if (pe.actionSuccessful())
        {
            PumpProfileH pr = pe.getResult();

            // System.out.println("PumpProfile: " + pr);
            // System.out.println("PumpProfile(till=" + pr.getActiveTill());

            boolean added = false;

            if (pr.getActiveTill() == -1)
            {
                // if this one should be last, we need to close previous last

                PumpProfile pr_other = getOpenProfile(pr.getName());

                // System.out.println("Other: " + pr_other);

                if (pr_other != null)
                {
                    if (pr_other.getActiveFrom() < pr.getActiveFrom())
                    {
                        ATechDate at = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_MIN, pr.getActiveFrom());
                        at.add(Calendar.MINUTE, -1);

                        pr_other.setActiveTill(at.getATDateTimeAsLong());
                    }
                    else
                    {
                        pr.setActiveTill(pr_other.getActiveFrom());
                    }

                    this.full.add(new PumpProfile(pr));
                    added = true;

                }
            }

            if (!added)
            {
                this.full.add(new PumpProfile(pr));
            }

            // System.out.println("Success: ");
            // this.full.add(new PumpProfile(pe.getResult()));
            this.filterEntries();
            // System.out.println("Success: " + this.full);
        }
    }


    private PumpProfile getOpenProfile(String name)
    {
        for (int i = 0; i < this.full.size(); i++)
        {
            PumpProfile pp = (PumpProfile) this.full.get(i);

            if (pp.getActiveTill() == -1 && pp.getName().equals(name))
                return pp;
        }
        return null;
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
        da_local = DataAccessPump.getInstance();
        this.full = new ArrayList<SelectableInterface>();
        this.full.addAll(da_local.getDb().getProfiles());
    }


    /**
     * Init Selector Values For Type
     */
    @Override
    public void initSelectorValuesForType()
    {
        setSelectorObject(new PumpProfile(DataAccessPump.getInstance().getI18nControlInstance()));
        setSelectorName(i18nControl.getMessage("PROFILE_SELECTOR"));

        if (this.m_select)
        {
            setAllowedActions(
                SelectorAbstractDialog.SELECTOR_ACTION_SELECT | SelectorAbstractDialog.SELECTOR_ACTION_CANCEL);
            this.use_generic_select = true;
        }
        else
        {
            setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_EDIT | SelectorAbstractDialog.SELECTOR_ACTION_NEW
                    | SelectorAbstractDialog.SELECTOR_ACTION_CANCEL);
        }
        this.setColumnSortingEnabled(false);
        this.setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_DATE_BOTH);
        this.setHelpStringId("PumpTool_Profile_Selector");
        this.setHelpEnabled(true);
        setNewItemString(i18nControl.getMessage("NEW__PROFILE"));
    }


    /** 
     * Item State Changed
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
    }

}
