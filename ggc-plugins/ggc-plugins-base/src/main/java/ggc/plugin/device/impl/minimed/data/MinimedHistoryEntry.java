package ggc.plugin.device.impl.minimed.data;

import java.util.List;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.BitUtils;
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
 *  Filename:     MinimedHistoryRecord
 *  Description:  Minimed History Record.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class MinimedHistoryEntry
{

    protected List<Byte> rawData;

    protected int[] sizes = new int[3];

    protected int[] head;
    protected int[] datetime;
    protected int[] body;

    protected ATechDate aTechDate;

    protected static BitUtils bitUtils = new BitUtils();


    public void setData(List<Byte> listRawData, boolean doNotProcess)
    {
        this.rawData = listRawData;

        // System.out.println("Head: " + sizes[0] + ", dates: " + sizes[1] +
        // ", body=" + sizes[2]);

        if (doNotProcess)
            return;

        head = new int[getHeadLength() - 1];
        for (int i = 1; i < (getHeadLength()); i++)
        {
            head[i - 1] = listRawData.get(i);
        }

        if (getDateTimeLength() > 0)
        {
            datetime = new int[getDateTimeLength()];

            for (int i = getHeadLength(), j = 0; j < getDateTimeLength(); i++, j++)
            {
                datetime[j] = listRawData.get(i);
            }
        }

        if (getBodyLength() > 0)
        {
            body = new int[getBodyLength()];

            for (int i = (getHeadLength() + getDateTimeLength()), j = 0; j < getBodyLength(); i++, j++)
            {
                body[j] = listRawData.get(i);
            }

        }

    }


    public int getHeadLength()
    {
        return sizes[0];
    }


    public int getDateTimeLength()
    {
        return sizes[1];
    }


    public int getBodyLength()
    {
        return sizes[2];
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        sb.append(getToStringStart());
        sb.append(", length=");
        sb.append(getHeadLength());
        sb.append(",");
        sb.append(getDateTimeLength());
        sb.append(",");
        sb.append(getBodyLength());
        sb.append("(");
        sb.append((getHeadLength() + getDateTimeLength() + getBodyLength()));
        sb.append(")");

        if (head != null)
        {
            sb.append(", head=");
            sb.append(bitUtils.getDebugArrayHexValue(this.head));
        }

        if (datetime != null)
        {
            sb.append(", datetime=");
            sb.append(bitUtils.getDebugArrayHexValue(this.datetime));
        }

        if (body != null)
        {
            sb.append(", body=");
            sb.append(bitUtils.getDebugArrayHexValue(this.body));
        }

        sb.append(", rawData=");
        sb.append(bitUtils.getDebugByteListHexValue(this.rawData));

        sb.append(this.aTechDate == null ? "" : " - " + this.aTechDate.getDateTimeString());

        return sb.toString();
    }


    public abstract int getOpCode();


    public abstract String getToStringStart();


    public List<Byte> getRawData()
    {
        return rawData;
    }


    public void setRawData(List<Byte> rawData)
    {
        this.rawData = rawData;
    }


    public int[] getHead()
    {
        return head;
    }


    public void setHead(int[] head)
    {
        this.head = head;
    }


    public int[] getDatetime()
    {
        return datetime;
    }


    public void setDatetime(int[] datetime)
    {
        this.datetime = datetime;
    }


    public int[] getBody()
    {
        return body;
    }


    public void setBody(int[] body)
    {
        this.body = body;
    }


    public void setATechDate(ATechDate atdate)
    {
        this.aTechDate = atdate;
    }


    public ATechDate getATechDate()
    {
        return this.aTechDate;
    }


    public String toShortString()
    {
        if (head == null)
        {
            return "Unidentified record. ";
        }
        else
        {
            return "HistoryRecord: head=[" + bitUtils.getDebugArrayHexValue(this.head) + "]";
        }
    }


    public abstract CodeEnum getEntryType();

}
