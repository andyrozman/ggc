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
 *  Filename: GGCPropertiesHelper.java
 *  Purpose:  A class with supporting methods for the GGCProperties.
 *
 *  Author:   schultd
 */

package util;


import java.io.*;
import java.util.Properties;


public class GGCPropertiesHelper
{
    private Properties props = new Properties();
    //private static GGCProperties singleton = null;
    private File propsFile;

    public GGCPropertiesHelper()
    {
        propsFile = new File(System.getProperty("user.home") + "/.ggc.prefs");
    }

    /*
	public static GGCPropertiesHelper getInstance()
	{
		if(singleton == null)
			singleton = new GGCPropertiesHelper();
		return singleton;
	}
	*/

    public String getFileName()
    {
        return propsFile.getAbsolutePath();
    }

    public void read()
    {
        FileInputStream in = null;
        try {
            in = new FileInputStream(propsFile);
            props.load(in);
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public void write()
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(propsFile);
            props.store(out, "This file was automatically generated.");
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public boolean getBoolean(String key)
    {
        String val = props.getProperty(key, "false");
        if (val.equalsIgnoreCase("true"))
            return true;
        else
            return false;
    }

    public int getInt(String key)
    {
        try {
            return Integer.parseInt(props.getProperty(key, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public long getLong(String key)
    {
        try {
            return Long.parseLong(props.getProperty(key, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    public float getFloat(String key)
    {
        try {
            return Float.parseFloat(props.getProperty(key, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String get(String key)
    {
        return props.getProperty(key, "");
    }

    public void set(String key, String newValue)
    {
        props.setProperty(key, newValue);
    }

    public void set(String key, boolean newValue)
    {
        props.setProperty(key, newValue + "");
    }

    public void set(String key, int newValue)
    {
        props.setProperty(key, newValue + "");
    }

    public void set(String key, long newValue)
    {
        props.setProperty(key, newValue + "");
    }

    public void set(String key, float newValue)
    {
        props.setProperty(key, newValue + "");
    }

    public void setDefault(String key, String defValue)
    {
        if (props.getProperty(key, null) == null)
            set(key, defValue);
    }
}
