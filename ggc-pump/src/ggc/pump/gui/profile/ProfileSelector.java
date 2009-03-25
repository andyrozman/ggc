package ggc.pump.gui.profile;

import ggc.core.db.hibernate.pump.PumpProfileH;
import ggc.pump.data.db.PumpProfile;
import ggc.pump.util.DataAccessPump;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collection;

import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;



public class ProfileSelector extends SelectorAbstractDialog
{

    private static final long serialVersionUID = -1061136628528659695L;
    DataAccessPump da_local = DataAccessPump.getInstance();
    
    
    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public ProfileSelector(DataAccessPump da, Component parent) //, DataAccessPump da) 
    {
        super(da.getMainParent(), da, 0, null, true);
        da_local = (DataAccessPump)da; 
        this.showDialog();
    }

//    /**
//     * Constructor
//     * 
//     * @param parent
//     * @param da
//     */
//    public ProfileSelector(JFrame parent, ATDataAccessAbstract da) 
//    {
//        super(parent, da, 0, null, true);
//        da_local = (DataAccessPump)da; 
//        this.showDialog();
//    }
    
    
    
    @Override
    public void checkAndExecuteActionEdit(SelectableInterface si)
    {
        ProfileEditor pe = new ProfileEditor(this, (PumpProfileH)si);
        pe.setVisible(true);
        
        if (pe.actionSuccessful())
        {
            this.filterEntries();
        }
        
    }

    @Override
    public void checkAndExecuteActionNew()
    {
        ProfileEditor pe = new ProfileEditor(this);
        pe.setVisible(true);
        
        if (pe.actionSuccessful())
        {
            System.out.println("Success: ");
            this.full.add(new PumpProfile(pe.getResult()));
            this.filterEntries();
            System.out.println("Success: " + this.full);
        }
    }

    @Override
    public void checkAndExecuteActionSelect()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void getFullData()
    {
        da_local = DataAccessPump.getInstance();
        System.out.println("da_local: " + da_local);
        System.out.println("da_local.getDb(): " + da_local.getDb());
        System.out.println("da_local.getDb().getProfiles(): " + da_local.getDb().getProfiles());
        
        
        this.full = new ArrayList<SelectableInterface>();
        this.full.addAll((Collection<? extends SelectableInterface>) da_local.getDb().getProfiles());
        
    }

    @Override
    public void initSelectorValuesForType()
    {
        setSelectorObject(new PumpProfile(DataAccessPump.getInstance().getI18nControlInstance()));
        setSelectorName(ic.getMessage("PROFILE_SELECTOR"));
        setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_EDIT|SelectorAbstractDialog.SELECTOR_ACTION_NEW|SelectorAbstractDialog.SELECTOR_ACTION_CANCEL);
        this.setColumnSortingEnabled(false);
        this.setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_DATE_BOTH);
        this.setHelpEnabled(false);
    }

    @Override
    public void itemStateChanged(ItemEvent e)
    {
        // TODO Auto-generated method stub
        
    }
    
    
    
    
}
