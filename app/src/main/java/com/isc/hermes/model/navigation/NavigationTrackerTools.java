package com.isc.hermes.model.navigation;

import com.isc.hermes.model.CurrentLocationModel;
import com.isc.hermes.model.graph.Node;
import com.isc.hermes.utils.CoordinatesDistanceCalculator;
import com.isc.hermes.utils.MapCoordsRecord;

import java.util.List;

public class NavigationTrackerTools {
   public static double CLOSE_ENOUGH = 10;

   public static boolean isPointReached(MapCoordsRecord target, MapCoordsRecord point){
      CoordinatesDistanceCalculator distanceCalculator = CoordinatesDistanceCalculator.getInstance();
      double distance = distanceCalculator.calculateDistance(point, target);
      return distance <= CLOSE_ENOUGH;
   }

   public static boolean isInsideSegment(RouteSegmentRecord segment, MapCoordsRecord point){
      boolean isHigherThan = (point.getLatitude() >= segment.getStart().getLatitude());
      isHigherThan &= (point.getLongitude() >= segment.getStart().getLongitude());

      boolean isLowerThan = (point.getLatitude() <= segment.getEnd().getLatitude());
      isLowerThan &= (point.getLongitude() <= segment.getEnd().getLongitude());

      return isHigherThan && isLowerThan;
   }
}
