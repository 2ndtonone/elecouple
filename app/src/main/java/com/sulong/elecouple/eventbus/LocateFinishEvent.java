package com.sulong.elecouple.eventbus;

import com.sulong.elecouple.entity.LocationInfo;

/**
 * Created by ydh on 2015/11/6.
 */
public class LocateFinishEvent {
    public boolean isAcquiredLocation;
    public LocationInfo newestLocationInfo;

    public LocateFinishEvent(boolean isAcquiredLocation, LocationInfo newestLocationInfo) {
        this.isAcquiredLocation = isAcquiredLocation;
        this.newestLocationInfo = newestLocationInfo;
    }
}