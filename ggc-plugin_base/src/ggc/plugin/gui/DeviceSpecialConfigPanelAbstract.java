package ggc.plugin.gui;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     DeviceSpecialConfigPanelAbstract
 *  Description:  
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class DeviceSpecialConfigPanelAbstract implements DeviceSpecialConfigPanelInterface
{

    protected Hashtable<String,String> parameters = null;
    protected String default_parameter = null;
    protected String packed_conn_parameters = null;
    
    
    /**
     * Constructor
     */
    public DeviceSpecialConfigPanelAbstract()
    {
        this.parameters = new Hashtable<String,String>();
        this.initParameters();
        this.initPanel();
    }
    
    
    
    public void initPanel()
    {
    }


    public void loadConnectionParameters(String param)
    {
        
        this.initParameters();
        
        StringTokenizer strtok = new StringTokenizer(param, ";");
        
        //int count = 0;
        
        while(strtok.hasMoreTokens())
        {
            
            String tok = strtok.nextToken();
            
            if (tok.contains("!="))
            {
                String key = tok.substring(0, tok.indexOf("!="));
                String val = tok.substring(tok.indexOf("!=") + 2);
                
                this.parameters.put(key, val);
            }
            else
                default_parameter = tok;
            
        }
        
        this.loadParametersToGUI();

    }

    
    public String getDefaultParameter()
    {
        return this.default_parameter;
    }
    

    public void setDefaultParameter(String par)
    {
        this.default_parameter = par;
    }
    
    
    public String saveConnectionParameters()
    {
        readParametersFromGUI();
        
        StringBuffer sb = new StringBuffer();
        sb.append(this.default_parameter);
//        sb.append(";");
        
        for(Enumeration<String> en=this.parameters.keys(); en.hasMoreElements(); )
        {
            String key = en.nextElement();
            sb.append(";");
            sb.append(key);
            sb.append("!=");
            sb.append(this.parameters.get(key));
        }
        
        return sb.toString();
    }
    
    
    
    public boolean hasDefaultParameter()
    {
        return true;
    }
    
    
    public boolean areConnectionParametersValid()
    {
        boolean not_found = false;
        
        if (this.hasDefaultParameter())
        {
            if ((this.default_parameter==null) || (this.default_parameter.length()==0))
                not_found = true;
        }
        
        for(Enumeration<String> en=this.parameters.keys(); en.hasMoreElements(); )
        {
            String param = this.parameters.get(en.nextElement());

            if ((param==null) || (param.length()==0))
                not_found = true;
        }
        
        return (!not_found);
        
    }

    
    

}
