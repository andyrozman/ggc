package ggc.plugin.gui;

import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.i18n.I18nControlAbstract;


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

    protected JPanel config_panel = null;
    protected Hashtable<String,String> parameters = null;
    protected String default_parameter = null;
    protected String packed_conn_parameters = null;
    private static Log log = LogFactory.getLog(DeviceSpecialConfigPanelAbstract.class);
    protected DataAccessPlugInBase m_da;
    protected I18nControlAbstract m_ic;
    protected DeviceAbstract dev_interface = null;
    
    /**
     * Delimiter for connection parameters parts
     */
    public static final String connection_part_delimiter = "#;#";
    
    /**
     * Delimiter for parameter 
     */
    public static final String parameter_delimiter = "!=";
    
    
    /**
     * Constructor
     * 
     * @param da 
     * @param di 
     */
    public DeviceSpecialConfigPanelAbstract(DataAccessPlugInBase da, DeviceAbstract di)
    {
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        this.dev_interface = di;
        this.parameters = new Hashtable<String,String>();
        this.initPanel();
        this.initParameters();
    }
    
    
    
    public abstract void initPanel();


    /**
     * Find Default Parameter
     * 
     * @param params
     * @return
     */
    public static String findDefaultParameter(String params)
    {

        if (!params.contains(connection_part_delimiter))
            return params;
        
        StringTokenizer strtok = new StringTokenizer(params, connection_part_delimiter);
        
        while(strtok.hasMoreTokens())
        {
            String tok = strtok.nextToken();
            
            if (!tok.contains(parameter_delimiter))
                return tok;
        }
        
        return "";
    }
    
    
    public void loadConnectionParameters(String param)
    {
        
        this.initParameters();
        
        if (!param.contains(connection_part_delimiter))
        {
            this.default_parameter = param;
            log.warn("Simple parameter found, while expecting extended one [param=" + param + "]");
            return;
        }
        
        
        StringTokenizer strtok = new StringTokenizer(param, connection_part_delimiter);
        
        //int count = 0;
        
        while(strtok.hasMoreTokens())
        {
            
            String tok = strtok.nextToken();
            
            if (tok.contains(parameter_delimiter))
            {
                String key = tok.substring(0, tok.indexOf(parameter_delimiter));
                String val = tok.substring(tok.indexOf(parameter_delimiter) + parameter_delimiter.length());
                
                if (this.parameters.containsKey(key))
                {
                    this.parameters.remove(key);
                    this.parameters.put(key, val);
                }
                else
                {
                    log.warn("Parameter not defined in our Config class: " + key);
                }
                
                
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
        boolean first = false;
        readParametersFromGUI();
        
        StringBuffer sb = new StringBuffer();
        
        if (this.hasDefaultParameter())
        {
            sb.append(this.default_parameter);
            first = true;
        }
//        sb.append(";");
        
        for(Enumeration<String> en=this.parameters.keys(); en.hasMoreElements(); )
        {
            String key = en.nextElement();
            
            if (first)
                sb.append(connection_part_delimiter);
            else
                first = true;
            
            sb.append(key);
            sb.append(parameter_delimiter);
            sb.append(this.parameters.get(key));
        }
        
        return sb.toString();
    }
    
    
    
    public boolean hasDefaultParameter()
    {
        return this.dev_interface.hasDefaultParameter();
    }
    
    
    public boolean areConnectionParametersValid()
    {
        boolean not_found = false;
        
        if (this.hasDefaultParameter())
        {
            if ((this.default_parameter==null) || (this.default_parameter.length()==0))
                not_found = true;
        }
        
        this.readParametersFromGUI();
        
        for(Enumeration<String> en=this.parameters.keys(); en.hasMoreElements(); )
        {
            String param = this.parameters.get(en.nextElement());

            if ((param==null) || (param.length()==0))
                not_found = true;
        }
        
        return (!not_found);
        
    }

    /**
     * Get Parameter
     * 
     * @param key
     * @return
     */
    public String getParameter(String key)
    {
        if (this.parameters.containsKey(key))
            return this.parameters.get(key);
        else
            return "";
    }
    
    
    
    public JPanel getPanel()
    {
        return this.config_panel;
    }

    

}
