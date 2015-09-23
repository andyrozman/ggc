package ggc.plugin.comm.cfg;

/**
 * Created by andy on 21.09.15.
 */
public class USBDevice
{

    private int vendorId;
    private int productId;
    private byte reportId = 0x0;


    public USBDevice(String description, int vendorId, int productId)
    {
        this(description, vendorId, productId, (byte) 0x00);
    }


    public USBDevice(String description, int vendorId, int productId, byte reportId)
    {
        this.vendorId = vendorId;
        this.productId = productId;
        this.reportId = reportId;
    }


    public int getVendorId()
    {
        return vendorId;
    }


    public void setVendorId(int vendorId)
    {
        this.vendorId = vendorId;
    }


    public int getProductId()
    {
        return productId;
    }


    public void setProductId(int productId)
    {
        this.productId = productId;
    }


    public byte getReportId()
    {
        return reportId;
    }


    public void setReportId(byte reportId)
    {
        this.reportId = reportId;
    }
}
