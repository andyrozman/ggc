
namespace GGCMobileNET.Data.Tools
{

// this one should be extended, we have several variables that need to be filled

public interface BackupRestoreWorkGiver //extends BackupRestoreBase 
{

    
	//public String getTargetName();
    
    void setStatus(int procents);
	
	void setTask(string task_name);
    
    
	// old
	//public String getObjectClassName();
	
	//public String getObjectHeader();
	
	//public String getObjectValues();
	
}
}