package com.esri.geoevent.adapter.nmea;

import java.nio.ByteBuffer;

import com.esri.ges.core.geoevent.FieldException;
import com.esri.ges.core.geoevent.GeoEvent;
import com.esri.ges.spatial.Point;
import com.esri.ges.spatial.Spatial;

public class NMEAGPRMCMessageTranslator extends NMEAMessageTranslator
{
  @Override
  public void translate(String trackId, ByteBuffer buffer, GeoEvent geoEvent, Spatial spatial) throws FieldException
  {
    byte[] dst = new byte[buffer.remaining()];
    buffer.get(dst, 0, buffer.remaining());
    String[] data = new String(dst).split(",");

    int i = 0;
    geoEvent.setField(i++, trackId);
    geoEvent.setField(i++, toTime(data[1], data[9]));
    double lat_d = Double.parseDouble(data[3].substring(0, 2));
    double lat_m = Double.parseDouble(data[3].substring(2, data[3].length()));
    double lat = lat_d + (lat_m / 60.0);
    double lon_d = Double.parseDouble(data[5].substring(0, 3));
    double lon_m = Double.parseDouble(data[5].substring(3, data[5].length()));
    double lon = lon_d + (lon_m / 60.0);
    lat = (data[4].equals("N")) ? lat : -lat;
    lon = (data[6].equals("E")) ? lon : -lon;
    Point point = spatial.createPoint(lon, lat, 4326);
    geoEvent.setField(i++, point);
    geoEvent.setField(i++, data[2]);
    geoEvent.setField(i++, Double.parseDouble(data[7]));
    geoEvent.setField(i++, Double.parseDouble(data[8]));
    geoEvent.setField(i++, Double.parseDouble(data[10]));
    geoEvent.setField(i++, data[11].split("\\*")[0]);
  }
}