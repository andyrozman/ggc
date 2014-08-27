package ggc.cgms.device.dexcom.receivers.data;

public enum CommandParameter
{

    None(0), //
    Byte(1), //
    Short(2), //
    Int(4), //
    Long(8), //
    ByteIntByte(6), // 
    ;

    private int lengthInPacket = 0;

    private CommandParameter(int length)
    {
        this.lengthInPacket = length;
    }

    public int getLengthInPacket()
    {
        return lengthInPacket;
    }

}
