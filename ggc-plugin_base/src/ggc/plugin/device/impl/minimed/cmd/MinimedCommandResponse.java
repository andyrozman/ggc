package ggc.plugin.device.impl.minimed.cmd;

import ggc.plugin.device.impl.minimed.MinimedDeviceUtil;

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
 *  Filename:     MinimedCommandResponse  
 *  Description:  Minimed Command Response (decoder for return data)
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class MinimedCommandResponse
{

    /**
     * TBR: Rate
     */
    public static final int TBR_RATE = 0;
    
    /**
     * TBR: Duration
     */
    public static final int TBR_DURATION = 1;
    
    private MinimedDeviceUtil utils = MinimedDeviceUtil.getInstance();
    

    /**
     * Get Unsigned Short
     * 
     * @param mc
     * @param parameter
     * @return
     */
    public int getUnsignedShort(MinimedCommand mc)
    {
        return this.getUnsignedShort(mc, 0);
    }
    
    
    /**
     * Get Unsigned Short
     * 
     * @param mc
     * @param parameter
     * @return
     */
    public int getUnsignedShort(MinimedCommand mc, int parameter)
    {
        int[] dt = getDataPart(mc, parameter);
        return utils.getHexUtils().convertIntsToUnsignedShort(dt[0], dt[1]);
    }

    
    /**
     * Get String
     * 
     * @param mc
     * @return
     */
    public String getString(MinimedCommand mc)
    {
        return getString(mc, 0);
    }
    
    
    /**
     * Get String
     * 
     * @param mc
     * @param parameter 
     * @return
     */
    public String getString(MinimedCommand mc, int parameter)
    {
        return new String(utils.getHexUtils().convertIntArrayToByteArray(getDataPart(mc, parameter)));
    }
    
    
    
    
    
    private int[] getDataPart(MinimedCommand mc, int parameter)
    {
        
        switch (mc.command_code)
        {
            case MinimedCommand.COMMAND_READ_FIRMWARE_VERSION:
                return mc.raw_data;
                
            case 120: // TBR
                if (parameter==MinimedCommandResponse.TBR_DURATION)
                {
                    return utils.getParamatersArray(2, mc.raw_data[2], mc.raw_data[3]);
                }
                else
                {
//                    int i = MedicalDevice.Util.makeUnsignedShort(ai[0], ai[1]);
//                    MedicalDevice.Util.verifyDeviceValue(i, 0.0D, toBasalStrokes(35D), "Temporary Basal Rate");
//                    m_settingTempBasalRate = toBasalInsulin(i);
                    System.out.println("This parameter resolve not supported [command=" + mc.command_code + ",desc=" + mc.command_description + ",paremeter=" + parameter);
                }
        
            case 152:
                if (parameter==MinimedCommandResponse.TBR_DURATION)
                {
                    return utils.getParamatersArray(2, mc.raw_data[4], mc.raw_data[5]);
                }
                else
                {
//                    int i = MedicalDevice.Util.makeUnsignedShort(ai[2], ai[3]);
//                    MedicalDevice.Util.verifyDeviceValue(i, 0.0D, toBasalStrokes(35D), "Temporary Basal Rate");
//                    m_settingTempBasalRate = toBasalInsulin(i);
                    System.out.println("This parameter resolve not supported [command=" + mc.command_code + ",desc=" + mc.command_description + ",paremeter=" + parameter);
                }
            
        
        
            default:
                return null;
        
        }
        
       
        
    }
    
    
    
    
    
    
    
}
