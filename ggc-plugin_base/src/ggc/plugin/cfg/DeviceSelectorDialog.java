package ggc.plugin.cfg;

/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: NutritionTreeDialog
 *  Purpose:  Main class for displaying nutrition information.
 *
 *  Author:   andyrozman
 */

 
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JDialog;

import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;
import com.atech.utils.ATDataAccessAbstract;


/**
 * Selector component for Meters (Simple Configuration)...
 * @author arozman
 *
 */

public class DeviceSelectorDialog extends SelectorAbstractDialog
{

    //private DataAccessMeter m_da = null;
    
    
    /**
     * 
     */
    private static final long serialVersionUID = -391406165609998040L;
    DeviceConfigurationDefinition dcd = null; 
 


    public DeviceSelectorDialog(JDialog parent, ATDataAccessAbstract da) 
    {
        super(parent, da, 0, null, true);
        this.showDialog();
    }

  
    public DeviceConfigurationDefinition getDeviceConfigurationDefinition()
    {
        if (this.dcd == null)
            this.dcd = ((DataAccessPlugInBase)m_da).getDeviceConfigurationDefinition();
        
        return this.dcd;
    }
    
    
    public void initSelectorValuesForType()
    {
        setSelectorObject((SelectableInterface)this.getDeviceConfigurationDefinition().getDummyObject());
        setSelectorName(ic.getMessage("SELECTOR_" + this.dcd.getDevicePrefix()));
	    setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_CANCEL_AND_SELECT);
	    this.setColumnSortingEnabled(false);
	    this.setHelpEnabled(false);
    }

     
    


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectorAbstractDialog#getFullData()
     */
    public void getFullData()
    {
        this.full = new ArrayList<SelectableInterface>();
        this.full.addAll((Collection<? extends SelectableInterface>) this.getDeviceConfigurationDefinition().getSupportedDevices());
    }
    
    
    public void setHelpContext()
    {
//	m_da.getHelpContext().getMainHelpBroker().enableHelpOnButton(this.getHelpButton(), this.getHelpId(), null);
//	m_da.getHelpContext().getMainHelpBroker().enableHelpKey(this.getRootPane(), this.getHelpId(), null);
    }
    


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectorAbstractDialog#checkAndExecuteActionEdit()
     */
    @Override
    public void checkAndExecuteActionEdit()
    {
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectorAbstractDialog#checkAndExecuteActionNew()
     */
    @Override
    public void checkAndExecuteActionNew()
    {
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectorAbstractDialog#checkAndExecuteActionSelect()
     */
    @Override
    public void checkAndExecuteActionSelect()
    {
        if (table!=null)
        {
//            System.out.println("in select action");
            if (table.getSelectedRow()==-1)
                return;

            this.selected_object = list.get(table.getSelectedRow());
//            System.out.println("in select action: selected:" + this.selected_object);
            
            this.dispose();
        }
	
    }



    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectorAbstractDialog#itemStateChanged(java.awt.event.ItemEvent)
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
    }
    
    
    
    
}
