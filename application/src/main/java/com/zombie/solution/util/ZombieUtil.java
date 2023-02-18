package com.zombie.solution.util;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@UtilityClass
public class ZombieUtil {
    private final GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);

    public static Point createPointFromCoordinate(Double lon, Double lat) {
        return factory.createPoint(new Coordinate(lon, lat));
    }
}
