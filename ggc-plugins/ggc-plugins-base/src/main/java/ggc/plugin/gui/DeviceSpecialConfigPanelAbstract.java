package ggc.plugin.gui;

import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;

import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.util.DataAccessPlugInBase;

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

    private static final Logger LOG = LoggerFactory.getLogger(DeviceSpecialConfigPanelAbstract.class);

    protected JPanel configPanel = null;
    protected Map<String, String> parameters = null;
    protected String defaultParameter = null;
    protected DataAccessPlugInBase dataAccess;
    protected I18nControlAbstract i18nControl;

    protected DeviceAbstract deviceInterfaceV1 = null;
    protected DeviceInstanceWithHandler deviceInterfaceV2 = null;

    /**
     * Delimiter for connection parameters parts
     */
    public static final String CONNECTION_PART_DELIMITER = "#;#";

    /**
     * Delimiter for parameter 
     */
    public static final String PARAMETER_DELIMITER = "!=";


    /**
     * Constructor
     * 
     * @param da 
     * @param deviceInterfaceV1
     *
     * @deprecated You should use Constructor with DeviceInstanceWithHandler (all future devices need to be V2)
     */
    @Deprecated
    public DeviceSpecialConfigPanelAbstract(DataAccessPlugInBase da, DeviceAbstract deviceInterfaceV1)
    {
        this.dataAccess = da;
        this.i18nControl = da.getI18nControlInstance();
        this.deviceInterfaceV1 = deviceInterfaceV1;
        this.parameters = new Hashtable<String, String>();
        this.initPanel();
        this.initParameters();
    }


    /**
     * Constructor
     *
     * @param da
     * @param deviceInterfaceV2
     */
    public DeviceSpecialConfigPanelAbstract(DataAccessPlugInBase da, DeviceInstanceWithHandler deviceInterfaceV2)
    {
        this.dataAccess = da;
        this.i18nControl = da.getI18nControlInstance();
        this.deviceInterfaceV2 = deviceInterfaceV2;
        this.parameters = new Hashtable<String, String>();
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

        if (!params.contains(CONNECTION_PART_DELIMITER))
            return params;

        StringTokenizer strtok = new StringTokenizer(params, CONNECTION_PART_DELIMITER);

        while (strtok.hasMoreTokens())
        {
            String tok = strtok.nextToken();

            if (!tok.contains(PARAMETER_DELIMITER))
                return tok;
        }

        return "";
    }


    public void loadConnectionParameters(String param)
    {

        this.initParameters();

        if (!param.contains(CONNECTION_PART_DELIMITER))
        {
            this.defaultParameter = param;
            LOG.warn("Simple parameter found, while expecting extended one [param=" + param + "]");
            return;
        }

        StringTokenizer strtok = new StringTokenizer(param, CONNECTION_PART_DELIMITER);

        while (strtok.hasMoreTokens())
        {

            String tok = strtok.nextToken();

            if (tok.contains(PARAMETER_DELIMITER))
            {
                String key = tok.substring(0, tok.indexOf(PARAMETER_DELIMITER));
                String val = tok.substring(tok.indexOf(PARAMETER_DELIMITER) + PARAMETER_DELIMITER.length());

                // if (this.parameters.containsKey(key))
                // {
                // this.parameters.remove(key);
                // this.parameters.put(key, val);
                // }
                // else
                // {
                // LOG.warn("Parameter not defined in our Config class: " +
                // key);
                // }

                this.parameters.put(key, val);
            }
            else
            {
                defaultParameter = tok;
            }

        }

        this.loadParametersToGUI();

    }


    public String getDefaultParameter()
    {
        return this.defaultParameter;
    }


    public void setDefaultParameter(String par)
    {
        this.defaultParameter = par;
    }


    public String saveConnectionParameters()
    {
        readParametersFromGUI();

        StringBuilder sb = new StringBuilder();

        if (this.hasDefaultParameter())
        {
            DataAccessPlugInBase.appendToStringBuilder(sb, this.defaultParameter, CONNECTION_PART_DELIMITER);
        }

        for (Map.Entry<String, String> entry : this.parameters.entrySet())
        {
            DataAccessPlugInBase.appendToStringBuilder(sb, entry.getKey() + PARAMETER_DELIMITER + entry.getValue(),
                CONNECTION_PART_DELIMITER);
        }

        return sb.toString();
    }


    public boolean hasDefaultParameter()
    {
        if (this.deviceInterfaceV1 != null)
            return this.deviceInterfaceV1.hasDefaultParameter();
        else
            return this.deviceInterfaceV2.hasDefaultParameter();
    }


    public boolean areConnectionParametersValid()
    {
        if (this.hasDefaultParameter() && (StringUtils.isBlank(this.defaultParameter)))
        {
            return false;
        }

        return areCustomParametersValid();
    }


    public abstract boolean areCustomParametersValid();


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
        return this.configPanel;
    }


    public void setParameter(String key, String value)
    {
        this.parameters.put(key, value);
    }


    public String getCustomErrorMessage()
    {
        return null;
    }

}
