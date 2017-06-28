package ggc.plugin.data.enums;

/**
 * Created by andy on 13.06.17.
 */
public enum ASCII
{

    NUL(0x00, 0), //
    SOH(0x01, 1), //
    STX(0x02, 2), //
    ETX(0x03, 3), //
    EOT(0x04, 4), //
    ENQ(0x05, 5), //
    ACK(0x06, 6), //
    BEL(0x07, 7), //
    BS(0x08, 8), //
    TAB(0x09, 9), //
    LF(0x0A, 10), //
    VT(0x0B, 11), //
    FF(0x0C, 12), //
    CR(0x0D, 13), //
    SO(0x0E, 14), //
    SI(0x0F, 15), //

    DLE(0x10, 16), //
    DC1(0x11, 17), //
    DC2(0x12, 18), //
    DC3(0x13, 19), //
    DC4(0x14, 20), //
    NAK(0x15, 21), //
    SYN(0x16, 22), //
    ETB(0x17, 23), //
    CAN(0x18, 24), //
    EM(0x19, 25), //
    SUB(0x1A, 26), //
    ESC(0x1B, 27), //
    FS(0x1C, 28), //
    GS(0x1D, 29), //
    RS(0x1E, 30), //
    US(0x1F, 31), //
    SPC(0x20, 32), //
    ;

    // public static final char STX = '\002';
    // public static final char ETX = '\003';
    // public static final char EOT = '\004';
    // public static final char ENQ = '\005';
    // public static final char ACK = '\006';
    // public static final char ETB = '\027';
    // public static final char LF = '\n';
    // public static final char CR = '\r';
    // public static final char NAK = '\025';
    // public static final char CAN = '\030';

    private byte value;


    ASCII(int code)
    {
        this.value = (byte) code;
    }


    ASCII(int code, int code2)
    {
        this.value = (byte) code;
    }


    public byte getValue()
    {
        return value;
    }
}
