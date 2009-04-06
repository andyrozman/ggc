package ggc.pump.device.dana;

// TODO: Auto-generated Javadoc
/**
 * The Class PacketStreamWriter.
 */
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
