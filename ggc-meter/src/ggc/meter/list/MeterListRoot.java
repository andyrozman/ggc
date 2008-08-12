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
 *  Filename: GGCTreeRoot
 *  Purpose:  Used for holding tree information for nutrition and meals
 *
 *  Author:   andyrozman
 */

package ggc.meter.list;

import ggc.meter.util.DataAccessMeter;

import java.util.ArrayList;

public class MeterListRoot 
{

    public ArrayList<String> m_meterCompanies = null;
    

    public boolean debug = true;
    public boolean dev = false;
    


    public MeterListRoot() 
    {
    	createData();
    }


    

    
    public void createData()
    {
    	m_meterCompanies = new ArrayList<String>();
    	
    	m_meterCompanies.add("Abbott Diabetes Care");
    	m_meterCompanies.add("Bayer Diagnostics");
    	m_meterCompanies.add("Diabetic Supply of Suncoast");
    	m_meterCompanies.add("Diagnostic Devices");
    	m_meterCompanies.add("Arkray USA (formerly Hypoguard)");
    	m_meterCompanies.add("HealthPia America");
    	m_meterCompanies.add("Home Diagnostics");
    	m_meterCompanies.add("Lifescan");
    	m_meterCompanies.add("Nova Biomedical");
    	m_meterCompanies.add("Roche Diagnostics");
    	m_meterCompanies.add("Sanvita");
    	m_meterCompanies.add("U.S. Diagnostics");
    	m_meterCompanies.add("WaveSense");
    	
    }
    
    
    
    
    
    
    
    @Override
    public String toString()
    {
    	return DataAccessMeter.getInstance().getI18nControlInstance().getMessage("METERS_LIST");
    }


}
