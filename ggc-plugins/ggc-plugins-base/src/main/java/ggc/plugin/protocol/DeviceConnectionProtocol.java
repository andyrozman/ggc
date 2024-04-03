package ggc.plugin.protocol;

import com.atech.utils.data.CodeEnum;

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
 *  Filename:     DeviceConnectionProtocol
 *  Description:  Connection Protocols definitions (for V2 DeviceInterface).
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum DeviceConnectionProtocol implements CodeEnum
{

    None(0, "PROT_NONE"), //
    Serial_USBBridge(1, "PROT_SERIAL_BRIDGE"), //
    USB_Hid(2, "PROT_USB_HID"), //
    MassStorageXML(3, "PROT_MASS_STORAGE_XML"), //
    BlueTooth_Serial(4, "PROT_BLUETOOTH_SERIAL"), //
    FileImport(5, "PROT_FILE_IMPORT"), // ?
    Database(6, "PROT_DATABASE"), //
    Multiple(7, "PROT_MULTIPLE") //
    ;

    int code;
    String i18nKey;
    String translation;


    private DeviceConnectionProtocol(int code, String descriptionKey)
    {
        this.code = code;
        this.i18nKey = descriptionKey;
    }


    public String getTranslation()
    {
        return translation;
    }


    public void setTranslation(String translation)
    {
        this.translation = translation;
    }


    public int getCode()
    {
        return code;
    }


    public String getI18nKey()
    {
        return i18nKey;
    }

}
