package ggc.plugin.data.progress;

public interface ProgressReportInterface
{

    // progress is static + dynamic. Static would be determinton of Pages ranges
    // (first 10% for example)
    // dynamic is then calculated with nr of pages, and 1+ for each processing
    // task

    // void setStaticProgressPercentage(int percentage);

    void addToProgress(ProgressType progressType, int progressAdd);

    // void addToProgressAndCheckIfCanceled(ProgressType progressType, int
    // progressAdd) throws DexcomException;

    // void setMaxElementsForStaticProgress(int maxElements);

    // void setMaxElementsForDynamicProgress(int maxElements);

    void configureProgressReporter(ProgressType baseProgressType, int staticProgressPercentage, int staticMaxElements,
            int dynamicMaxElements);

    void setDownloadCancel(boolean cancel);

    boolean isDownloadCanceled();

}
