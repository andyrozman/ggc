package ggc.pump.device.insulet.data.dto.config;

import ggc.pump.device.insulet.data.dto.AbstractRecord;
import ggc.pump.device.insulet.data.enums.OmnipodDataType;

/**
 * Created by andy on 19.05.15.
 */

public class ProfileHeader extends AbstractRecord
{

    // profile_hdr: { format: 'b6.Si', fields: [
    // 'profile_idx', 'error_code', 'operation_time'
    // ] },

    Byte profileIdx;
    Short errorCode;
    Integer operationTime;


    public ProfileHeader()
    {
        super(false);
    }


    @Override
    public void customProcess(int[] data)
    {
        profileIdx = getByte(2);
        errorCode = getShort(3);
        operationTime = getIntInverted(5);
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.SubRecord;
    }


    @Override
    public String toString()
    {
        return "ProfileHeader [" + "profileIdx=" + profileIdx + ", errorCode=" + errorCode + ", operationTime="
                + operationTime + ']';
    }
}
