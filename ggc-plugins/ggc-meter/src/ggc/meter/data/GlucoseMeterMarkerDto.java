package ggc.meter.data;

import ggc.meter.defs.GlucoseMeterMarker;

/**
 * Created by andy on 12/11/16.
 */
public class GlucoseMeterMarkerDto
{

    GlucoseMeterMarker markerType;
    String data;


    public GlucoseMeterMarkerDto(GlucoseMeterMarker markerType)
    {
        this(markerType, null);
    }


    public GlucoseMeterMarkerDto(GlucoseMeterMarker markerType, String data)
    {
        this.markerType = markerType;
        this.data = data;
    }


    public static GlucoseMeterMarkerDto createMarker(GlucoseMeterMarker markerType)
    {
        return new GlucoseMeterMarkerDto(markerType);
    }


    public static GlucoseMeterMarkerDto createMarker(GlucoseMeterMarker markerType, String data)
    {
        return new GlucoseMeterMarkerDto(markerType, data);
    }

}
