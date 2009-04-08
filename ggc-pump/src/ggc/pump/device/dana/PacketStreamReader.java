package ggc.pump.device.dana;


// TODO: Auto-generated Javadoc
/**
 * The Class PacketStreamReader.
 */
public class PacketStreamReader
{
    private byte[] buffer;
    private int cursor;
    private int offset;

    /**
     * Instantiates a new packet stream reader.
     * 
     * @param buffer the buffer
     */
    public PacketStreamReader(byte[] buffer)
    {
        this.buffer = buffer;
        this.offset = 6;
        this.reset();
    }

    /**
     * Encode int2.
     * 
     * @param buffer the buffer
     * @param offset the offset
     * @param val the val
     */
    public static void encodeInt2(byte[] buffer, int offset, int val)
    {
        buffer[offset] = (byte) ((val >> 8) & 0xff);
        buffer[offset + 1] = (byte) (val & 0xff);
    }

    /**
     * Gets the ascii.
     * 
     * @param size the size
     * 
     * @return the ascii
     */
    public String getAscii(int size)
    {
        char[] destinationArray = new char[size];

        System.arraycopy(this.buffer, this.cursor, destinationArray, 0, size);

        for (int i = 0; i < size; i++)
        {
            byte num2 = (byte) destinationArray[i];
            destinationArray[i] = (char) (num2 + 0x41);
        }
        String str = new String(destinationArray, 0, size);
        this.cursor += size;
        return str;
    }

    /**
     * Gets the byte.
     * 
     * @return the byte
     */
    public byte getByte()
    {
        return this.buffer[this.cursor++];
    }

    /**
     * Gets the command.
     * 
     * @return the command
     */
    public byte getCommand()
    {
        return this.buffer[4];
    }

    /**
     * Gets the int.
     * 
     * @return the int
     */
    public int getInt()
    {
        int num = 0;
        num += (this.buffer[this.cursor++] << 8) & 0xff00;
        return (num + (this.buffer[this.cursor++] & 0xff));
    }

    /**
     * Gets the int1.
     * 
     * @return the int1
     */
    public int getInt1()
    {
        return this.buffer[this.cursor++];
    }

    /**
     * Gets the string.
     * 
     * @param size the size
     * 
     * @return the string
     */
    public String getString(int size)
    {
        char[] destinationArray = new char[size];
        System.arraycopy(this.buffer, this.cursor, destinationArray, 0, size);
        String str = new String(destinationArray, 0, size);
        this.cursor += size;
        return str;
    }

    /**
     * Gets the sub command.
     * 
     * @return the sub command
     */
    public byte getSubCommand()
    {
        return this.buffer[5];
    }

    /**
     * Reset.
     */
    public void reset()
    {
        this.cursor = this.offset;
    }

    /**
     * Sets the buffer.
     * 
     * @param buffer the new buffer
     */
    public void setBuffer(byte[] buffer)
    {
        this.buffer = buffer;
        this.reset();
    }

    /**
     * Sets the byte.
     * 
     * @param data the new byte
     */
    public void setByte(byte data)
    {
        this.buffer[this.cursor++] = data;
    }

    /**
     * Sets the int.
     * 
     * @param data the new int
     */
    public void setInt(int data)
    {
        this.buffer[this.cursor++] = (byte) ((data >> 8) & 0xff);
        this.buffer[this.cursor++] = (byte) (data & 0xff);
    }

    /**
     * Skip.
     * 
     * @param size the size
     */
    public void skip(int size)
    {
        this.cursor += size;
    }
}

