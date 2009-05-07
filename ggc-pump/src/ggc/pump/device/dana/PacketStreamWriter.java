package ggc.pump.device.dana;

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
 *  Filename:     PacketStreamWriter  
 *  Description:  Packet Stream Writer
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// OLD Implementation

public class PacketStreamWriter
{
    private byte[] buffer;
    private int cursor;

    /**
     * Instantiates a new packet stream writer.
     * 
     * @param buffer
     *            the buffer
     */
    public PacketStreamWriter(byte[] buffer)
    {
        this.buffer = buffer;
        this.cursor = 0;
    }

    /**
     * Sets the byte.
     * 
     * @param data
     *            the new byte
     */
    public void setByte(byte data)
    {
        this.buffer[this.cursor++] = data;
    }

    /**
     * Sets the int.
     * 
     * @param data
     *            the new int
     */
    public void setInt(int data)
    {
        this.buffer[this.cursor++] = (byte) ((data >> 8) & 0xff);
        this.buffer[this.cursor++] = (byte) (data & 0xff);
    }
}

