package ggc.meter.util;

import java.util.List;

import ggc.meter.defs.GlucoseMeterMarker;

/**
 * Created by andy on 12/11/16.
 */
public class GlucoseMeterMarkerHandler
{

    // FIXME
    public String t(List<GlucoseMeterMarker> markers)
    {
        if (markers.size() == 0)
            return null;
        else
            return DataAccessMeter.getInstance().createStringRepresentationOfCollection(markers, ",");
    }

}
