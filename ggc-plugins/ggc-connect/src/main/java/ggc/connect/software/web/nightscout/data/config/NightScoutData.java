package ggc.connect.software.web.nightscout.data.config;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by andy on 3/30/18.
 */
public class NightScoutData {

    private String firstEntry;

    private Set<String> downloadedEntries;

    private Set<String> incompleteEntries;

    private Set<String> notDownloadedEntries;

    public String getFirstEntry() {
        return firstEntry;
    }

    public void setFirstEntry(String firstEntry) {
        this.firstEntry = firstEntry;
    }

    public Set<String> getDownloadedEntries() {
        return downloadedEntries;
    }

    public void setDownloadedEntries(Set<String> downloadedEntries) {
        this.downloadedEntries = downloadedEntries;
    }

    public Set<String> getIncompleteEntries() {
        return incompleteEntries;
    }

    public void setIncompleteEntries(Set<String> incompleteEntries) {
        this.incompleteEntries = incompleteEntries;
    }

    public Set<String> getNotDownloadedEntries() {
        return notDownloadedEntries;
    }

    public void addNotDownloadedEntry(String entry)
    {
        if (notDownloadedEntries==null)
            this.notDownloadedEntries = new HashSet<String>();

        this.notDownloadedEntries.add(entry);
    }

    public void setNotDownloadedEntries(Set<String> notDownloadedEntries) {
        this.notDownloadedEntries = notDownloadedEntries;
    }


    public boolean isEntryPresent(Set<String> collection, String entry)
    {
        return  (collection!=null) && (collection.contains(entry));
    }


    public boolean isEntryPresent(String entry) {

        return isEntryPresent(this.notDownloadedEntries, entry) ||
                isEntryPresent(this.downloadedEntries, entry) ||
                isEntryPresent(this.incompleteEntries, entry);
    }
}