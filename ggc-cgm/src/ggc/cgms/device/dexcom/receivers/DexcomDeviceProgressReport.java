package ggc.cgms.device.dexcom.receivers;

import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.plugin.data.progress.ProgressReportInterface;
import ggc.plugin.data.progress.ProgressType;

public interface DexcomDeviceProgressReport extends ProgressReportInterface
{

    // progress is static + dynamic. Static would be determinton of Pages ranges
    // (first 10% for example)
    // dynamic is then calculated with nr of pages, and 1+ for each processing
    // task

    // void setStaticProgressPercentage(int percentage);

    void addToProgressAndCheckIfCanceled(ProgressType progressType, int progressAdd) throws DexcomException;

    // void setMaxElementsForStaticProgress(int maxElements);

    // void setMaxElementsForDynamicProgress(int maxElements);

    void setDownloadCancel(boolean cancel);

    boolean isDownloadCanceled();

}
