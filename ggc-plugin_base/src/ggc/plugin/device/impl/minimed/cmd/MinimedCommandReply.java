package ggc.plugin.device.impl.minimed.cmd;

public class MinimedCommandReply
{
    
    public static final int COMMAND_REPLY_ACKNOWLEDGE = 1;
    
    public static final int COMMAND_REPLY_DATA = 2;
    
    public int data_type = 0;
    public boolean ack = false;
    public boolean success = false;
    
    public Exception exception = null;
    
    public int[] raw_data = null; 
    
    public int record_length = 64;
    
    public int max_records = 1;
    
    
    
    //public 
    
    public MinimedCommandReply(int type_, int record_length_, int max_records_)
    {
        this.data_type = type_;
        this.record_length = record_length_;
        this.max_records = max_records_;
        
        this.raw_data = new int[this.record_length * this.max_records];
    }
    
    
    
    public void clearData()
    {
        
    }
    
    
    public void setAcknowledgeReceived(boolean recv)
    {
        this.ack = recv;
        this.success = recv;
    }
    
    
    public void setException(Exception ex)
    {
        this.exception = ex;
    }
    
}
