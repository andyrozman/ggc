package ggc.cgms.data;

import java.util.Hashtable;

import com.atech.db.ext.ExtendedHandler;
import com.atech.utils.ATDataAccessAbstract;

public class ExtendedCGMSValuesExtendedEntry extends ExtendedHandler
{

    public static final int EXTENDED_SUB_TYPE = 0;
    public static final int EXTENDED_SOURCE = 1;
    @SuppressWarnings("unused")
    private static final int EXTENDED_MAX = 1;

    public ExtendedCGMSValuesExtendedEntry(ATDataAccessAbstract da)
    {
        super(da);

    }

    @Override
    public void initExtended()
    {
        ext_mapped_types = new Hashtable<Integer, String>();
        ext_mapped_types.put(ExtendedCGMSValuesExtendedEntry.EXTENDED_SUB_TYPE, "SUB_TYPE");
        ext_mapped_types.put(ExtendedCGMSValuesExtendedEntry.EXTENDED_SOURCE, "SOURCE");
    }

    @Override
    public String getExtendedObject()
    {
        return "CGMSValuesExtendedEntry";
    }

}
