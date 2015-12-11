package ggc.pump.defs.device;

/**
 * Created by andy on 21.10.15.
 */
public enum PumpProfileDefinition
{
    AnimasProfile(12, "1", "2", "3", "4"), //
    MinimedProfile(-1, "STD", "A", "B"), // FIXME
    RocheProfile(24, "1", "2", "3", "4", "5"), //
    AsanteProfile(10, "A", "B", "C", "D"), //
    TandemProfile(16, "1", "2", "3", "4", "5", "6"), //
    DanaProfile(48, "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15");

    int maxTimeSlots;
    String[] profiles;


    PumpProfileDefinition(int maxTimeSlots, String... profiles)
    {
        this.maxTimeSlots = maxTimeSlots;
        this.profiles = profiles;
    }

}
