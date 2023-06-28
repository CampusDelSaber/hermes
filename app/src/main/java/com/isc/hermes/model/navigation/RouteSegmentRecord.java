package com.isc.hermes.model.navigation;

import com.isc.hermes.utils.MapCoordsRecord;

public class RouteSegmentRecord {
    private final MapCoordsRecord start;
    private final MapCoordsRecord end;

    public RouteSegmentRecord(MapCoordsRecord start, MapCoordsRecord end) {
        this.start = start;
        this.end = end;
    }

    public MapCoordsRecord getStart() {
        return start;
    }

    public MapCoordsRecord getEnd() {
        return end;
    }

}
