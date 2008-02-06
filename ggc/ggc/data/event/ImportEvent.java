

/*
 *  GGC - GNU Gluco Control
 *
 *  A pure Java application to help you manage your diabetes.
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
 *  Filename: MonthlyValues
 *  Purpose:  This is data class for storing monthly values. 
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */

/**
 * Created on 12.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.data.event;


import gnu.io.CommPortIdentifier;
import java.util.EventObject;


/**

 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class ImportEvent extends EventObject
{

    public static final int NONE = -1;

    public static final int PORT_OPENED = 0;

    public static final int PORT_CLOSED = 1;

    public static final int IMPORT_FINISHED = 2;

    public static final int TIMEOUT = 4;

    public static final int PROGRESS = 5;

    private int type = -1;
    private int progress = 0;
    private String message = null;
    private CommPortIdentifier comPort = null;


    /**
     * Constructor for ImportEvent.
     * @param source
     */
    public ImportEvent(Object source)
    {
        super(source);
    }


    /**
     * Constructor for ImportEvent.
     * @param source
     * @param type
     */
    public ImportEvent(Object source, int type)
    {
        super(source);

        this.type = type;
    }


    public ImportEvent(Object source, int type, int progress)
    {
        super(source);

        this.type = type;
    }


    public ImportEvent(Object source, int type, CommPortIdentifier comPort)
    {
        super(source);

        this.type = type;
        this.comPort = comPort;
    }


    public ImportEvent(Object source, int type, String message)
    {
        super(source);

        this.type = type;
        this.message = message;
    }


    /**
     * Returns the type.
     * @return int
     */
    public int getType()
    {
        return type;
    }


    /**
     * Returns the progress.
     * @return int
     */
    public int getProgress()
    {
        return progress;
    }


    /**
     * Gets the abortMessage
     * @return Returns a String
     */
    public String getMessage()
    {
        return message;
    }


    /**
     * Gets the comPort
     * @return Returns a ComPortIdentifier
     */
    public CommPortIdentifier getComPort()
    {
        return comPort;
    }
}