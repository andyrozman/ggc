package ggc.core.update;

import com.atech.update.v4.data.UpdateFileEntryV4;
import com.atech.update.v4.data.UpdateFileV4;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by andy on 09.04.2024.
 */
public class UpdateV4FileUTest {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void prepareV4UpdateFile() {

        List<UpdateFileEntryV4> list = new ArrayList<>();
        addToList(15, "0.7",
                "",
                7, null, list);
        addToList(14, "0.6.1",
                "Core 0.6.0, Desktop 0.6.0, Meter Tool 2.2.0, Pump Tool 1.5.0, CGMS Tool 1.3.5",
                7, null, list);
        addToList(13, "0.6",
                "Core 0.6.0, Desktop 0.6.0, Meter Tool 2.2.0, Pump Tool 1.5.0, CGMS Tool 1.3.5",
                7, null, list);
        addToList(12, "0.5.0.3-2",
                "Core 0.5.0.3, Desktop 0.5.0.3, Meter Tool 2.0.3, Pump Tool 1.3.4, CGMS Tool 1.0.3",
                7, null, list);
        addToList(11, "0.5.0.3",
                "Core 0.5.0.3, Desktop 0.5.0.3, Meter Tool 2.0.3, Pump Tool 1.3.4, CGMS Tool 1.0.3",
                7, null, list);
        addToList(10, "0.5",
                "Core 0.5, Desktop 0.4.9, Main 0.4.11, Meter Tool 2.0.3, Pump Tool 1.3.2, CGMS Tool 1.0.3",
                7, null, list);

        Collections.sort(list);

        UpdateFileV4 updateFileV4 = UpdateFileV4.builder()
                .update_list(list)
                .build();

        System.out.println("UpdateFile:\n" + gson.toJson(updateFileV4));

    }

    private void addToList(int id, String version, String description, int dbVersion, String dbChecksum, List<UpdateFileEntryV4> list) {
        list.add(UpdateFileEntryV4.builder()
                .id(id)
                .version(version)
                .description(description)
                .dbVersion(dbVersion)
                .dbChecksum(dbChecksum)
                .build());
    }

}
