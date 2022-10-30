package com.vroomvroom.safemobis.util;

import com.vroomvroom.safemobis.domain.enumerate.TrafficCode;

import java.util.HashMap;
import java.util.Map;

import static com.vroomvroom.safemobis.domain.enumerate.TrafficCode.*;

public class CircleUtil {

    private static final Map<TrafficCode, Double> radiusMap = new HashMap<>() {{
        put(CAR, 5.0);
        put(PEDESTRIAN, 1.0);
        put(CHILD, 1.0);
        put(KICK_BOARD, 1.0);
        put(BICYCLE, 2.0);
        put(MOTORCYCLE, 2.0);
    }};

    public static Map<TrafficCode, Double> getRadiusMap() {
        return radiusMap;
    }
}
