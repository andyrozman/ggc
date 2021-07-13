package ggc.plugin.device.impl.abbott.hid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.plugin.device.PlugInBaseException;

public class AbbottHidReportDto
{

    private static final Logger LOG = LoggerFactory.getLogger(AbbottHidReportDto.class);

    private final byte payloadLength;
    private final byte[] payload;

    private byte[] rawData;

    private byte messageTypeRaw;
    static BitUtils bitUtils;


    public AbbottHidReportDto(byte[] data)
    {
        this.rawData = data;

        this.messageTypeRaw = data[0];
        this.payloadLength = data[1];

        if (bitUtils == null)
        {
            bitUtils = AbbottUtil.getBitUtils();
        }

        this.payload = bitUtils.getByteSubArray(data, 2, this.payloadLength);

        if (AbbottUtil.getCurrentDebug())
        {
            System.out.println("MessageType: " + messageTypeRaw + ", payloadLength=" + this.payloadLength);

            if (this.payloadLength > 0)
            {
                System.out.println("Value: " + bitUtils.getDebugByteArray(this.payload));
            }
        }
    }


    public AbbottHidBaseCommand getMessageType()
    {
        return AbbottHidBaseCommand.getByResponseCode(this.messageTypeRaw);
    }


    public String extractText() throws PlugInBaseException
    {
        String outText = AbbottUtil.getBitUtils().getString(this.payload, 0, this.payload.length);

        if (outText.contains("CMD"))
        {
            outText = AbbottUtil.validateTextResponse(outText, true, false);
        }

        return outText;
    }


    public String extractTextNoValidation()
    {
        return bitUtils.getString(this.payload, 0, this.payload.length);
    }


    public byte getMessageTypeRaw()
    {
        return messageTypeRaw;
    }


    public byte[] extractByteResponse()
    {
        return this.payload;
    }


    public static void main(String[] args)
    {
        String checksum = "00000175";

        String chk = checksum;

        // while (chk.startsWith("0"))
        // {
        // chk = chk.substring(1);
        // }

        long targetChecksum = Long.decode("0x" + chk);

        System.out.println(String.format("checksum: got: %s, expected: %s", 373, targetChecksum));

    }

}
