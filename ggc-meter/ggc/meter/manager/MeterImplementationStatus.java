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
 *  Filename: MeterManager.java
 *  Purpose:  This class contains all definitions for Meters. This includes:
 *        meter names, classes that handle meter and all other relevant data.
 *
 *  Author:   andyrozman
 */


package ggc.meter.manager; 

import ggc.meter.util.DataAccess;
import ggc.meter.util.I18nControl;

import java.util.Hashtable;

import javax.swing.ImageIcon;

public class MeterImplementationStatus
{


    public static final int IMPLEMENTATION_NOT_AVAILABLE = 0;
    public static final int IMPLEMENTATION_NOT_PLANNED = 1;
    public static final int IMPLEMENTATION_PLANNED = 2;
    public static final int IMPLEMENTATION_PARTITIAL = 3;
    public static final int IMPLEMENTATION_FULL = 4;
    public static final int IMPLEMENTATION_TESTED = 5;
    public static final int IMPLEMENTATION_DONE = 6;
    
    
    
    public static final int FUNCTIONALITY_READ_DATA_FULL = 1;
    public static final int FUNCTIONALITY_READ_DATA_PARTITIAL = 2;
    public static final int FUNCTIONALITY_CLEAR_DATA = 4;
    public static final int FUNCTIONALITY_READ_INFO = 8;
    public static final int FUNCTIONALITY_READ_CONFIG = 16;
    
    
    




}
