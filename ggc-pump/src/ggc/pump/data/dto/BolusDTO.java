package ggc.pump.data.dto;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.DecimalPrecission;

import ggc.core.util.DataAccess;
import ggc.pump.data.defs.PumpBolusType;

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
 *  Filename:     BolusDTO
 *  Description:  Bolus DTO
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class BolusDTO
{

    private Double requestedAmount;
    private Double deliveredAmount;
    private Double immediateAmount; // when Multiwave this is used
    private Integer duration;
    private PumpBolusType bolusType;
    private Double insulinOnBoard;
    private ATechDate aTechDate;

    private DecimalPrecission precission = DecimalPrecission.TwoDecimals;


    public Double getRequestedAmount()
    {
        return requestedAmount;
    }


    public void setRequestedAmount(Double requestedAmount)
    {
        this.requestedAmount = requestedAmount;
    }


    public Double getDeliveredAmount()
    {
        return deliveredAmount;
    }


    public void setDeliveredAmount(Double deliveredAmount)
    {
        this.deliveredAmount = deliveredAmount;
    }


    public Integer getDuration()
    {
        return duration;
    }


    public void setDuration(Integer duration)
    {
        this.duration = duration;
    }


    public PumpBolusType getBolusType()
    {
        return bolusType;
    }


    public void setBolusType(PumpBolusType bolusType)
    {
        this.bolusType = bolusType;
    }


    public Double getInsulinOnBoard()
    {
        return insulinOnBoard;
    }


    public void setInsulinOnBoard(Double insulinOnBoard)
    {
        this.insulinOnBoard = insulinOnBoard;
    }


    public void setATechDate(ATechDate ATechDate)
    {
        this.aTechDate = ATechDate;
    }


    public ATechDate getATechDate()
    {
        return aTechDate;
    }


    private String getDurationString()
    {
        int minutes = this.duration.intValue();

        int h = minutes / 60;

        minutes -= (h * 60);

        return DataAccess.getLeadingZero(h, 2) + ":" + DataAccess.getLeadingZero(minutes, 2);
    }


    public String getValue()
    {
        if ((bolusType == PumpBolusType.Normal) || (bolusType == PumpBolusType.Audio))
        {
            return getFormattedDecimal(this.deliveredAmount);
        }
        else if (bolusType == PumpBolusType.Extended)
        {
            return String.format("AMOUNT_SQUARE=%s;DURATION=%s", getFormattedDecimal(this.deliveredAmount),
                getDurationString());
        }
        else
        {
            return String.format("AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s", getFormattedDecimal(this.immediateAmount),
                getFormattedDecimal(this.deliveredAmount), getDurationString());
        }
    }


    public Double getImmediateAmount()
    {
        return immediateAmount;
    }


    public void setImmediateAmount(Double immediateAmount)
    {
        this.immediateAmount = immediateAmount;
    }


    public String getFormattedDecimal(double value)
    {
        String val = String.format(this.precission.getFormatString(), value);
        return val.replaceAll(",", ".");
    }


    public String getBolusKey()
    {
        return "Bolus_" + this.bolusType.name();

    }

}
