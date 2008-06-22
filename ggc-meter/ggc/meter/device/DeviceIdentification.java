package ggc.meter.device;

public class DeviceIdentification
{
    public String company = null;
    public String device_selected = null;
    
    public String device_identified = null;
    public String device_family = null;
    
    public String device_software_version = null;
    public String device_hardware_version = null;
    public String device_serial_number = null;
    
    
    public String getInformation(String prefix)
    {
        StringBuffer sb = new StringBuffer();
        
        sb.append(prefix);
        sb.append(" Meter Company: " + this.company);
        sb.append("  Selected Device: " + this.device_selected);
        sb.append("\n");
        
        sb.append(prefix + " ");
        appendParameter(this.device_identified, "Device identified", sb);
        appendParameter(this.device_family, "Device family", sb);
        appendParameter(this.device_serial_number, "S/N", sb);
        sb.append("\n");

        if ((this.device_hardware_version!=null) || (this.device_software_version!=null))
        {
            sb.append(prefix + " ");
            appendParameter(this.device_software_version, "Sw version", sb);
            appendParameter(this.device_hardware_version, "Hw version", sb);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void appendParameter(String param, String text, StringBuffer sb)
    {
        if (param!=null)
            sb.append(text + ": " + param + "  ");
        
    }
    
    
    
}
