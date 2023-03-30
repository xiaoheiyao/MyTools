package com.lqz.gcs.app.map;

import com.topxgun.imap.model.ILatLng;
import com.topxgun.imap.utils.IMapUtils;

import java.util.List;

/**
 * Class to convert lat/lon values to MercatorProjection
 * 墨卡托投影
 */
public final class MercatorProjection {
    public static final double DEGREES_PER_RADIANS = 180.0 / Math.PI;
    public static final double RADIANS_PER_DEGREES = Math.PI / 180.0;
    public static final double PI_OVER_2 = Math.PI / 2.0;
    public static final double RADIUS = 6378137.0;
    public static final double RADIUS_2 = RADIUS * 0.5;
    public static final double RAD_RAD = RADIANS_PER_DEGREES * RADIUS;


    public static MercatorPoint toMercatorPoint(ILatLng iLatLng) {
        MercatorPoint mercatorPoint = new MercatorPoint();
        mercatorPoint.x = longitudeToX(iLatLng.longitude);
        mercatorPoint.y = latitudeToY(iLatLng.latitude);
        return mercatorPoint;
    }

    public static ILatLng toLatLng(MercatorPoint mercatorPoint) {
        ILatLng latLng = new ILatLng(yToLatitude(mercatorPoint.y), xToLongitude(mercatorPoint.x));
        return latLng;
    }

    /**
     * Convert geo lat to vertical distance in meters.
     *
     * @param latitude the latitude in decimal degrees.
     * @return the vertical distance in meters.
     */
    public static double latitudeToY(double latitude) {
        final double rad = latitude * RADIANS_PER_DEGREES;
        final double sin = Math.sin(rad);
        return RADIUS_2 * Math.log((1.0d + sin) / (1.0d - sin));
    }

    /**
     * Convert geo lon to horizontal distance in meters.
     *
     * @param longitude the longitude in decimal degrees.
     * @return the horizontal distance in meters.
     */
    public static double longitudeToX(double longitude) {
        return longitude * RAD_RAD;
    }

    /**
     * Convert horizontal distance in meters to longitude in decimal degress.
     *
     * @param x the horizontal distance in meters.
     * @return the longitude in decimal degrees.
     */
    public static double xToLongitude(final double x) {
        return xToLongitude(x, true);
    }

    /**
     * Convert horizontal distance in meters to longitude in decimal degress.
     *
     * @param x      the horizontal distance in meters.
     * @param linear if using continuous pan.
     * @return the longitude in decimal degrees.
     */
    public static double xToLongitude(
            final double x,
            final boolean linear) {
        final double rad = x / RADIUS;
        final double deg = rad * DEGREES_PER_RADIANS;
        if (linear) {
            return deg;
        }
        final double rotations = Math.floor((deg + 180.0) / 360.0);
        return deg - (rotations * 360.0);
    }

    /**
     * Convert vertical distance in meters to latitude in decimal degress.
     *
     * @param y the vertical distance in meters.
     * @return the latitude in decimal degrees.
     */
    public static double yToLatitude(final double y) {
        final double rad = PI_OVER_2 - (2.0 * Math.atan(Math.exp(-1.0 * y / RADIUS)));
        return rad * DEGREES_PER_RADIANS;
    }

    public static double getMercatorDistance(List<ILatLng> latLngList, double distance) {
        double totolDistance = 0;
        for (int i = 0; i < latLngList.size(); i++) {
            int j = i + 1;
            if (j == latLngList.size()) {
                j = 0;
            }
            ILatLng point1 = latLngList.get(i);
            ILatLng point2 = latLngList.get(j);
            double twoPointRealDistance = IMapUtils.calculateLineDistance(new ILatLng(point1.latitude, point1.longitude), new ILatLng(point2.latitude, point2.longitude));
            double twoPointMeratorDistance = getTwoPointMeratorDistance(point1, point2);
            double meratorDistance = distance * twoPointMeratorDistance / twoPointRealDistance;
            totolDistance = totolDistance + meratorDistance;
        }
        return totolDistance / latLngList.size();
    }

    public static double getRawDistance(List<ILatLng> latLngList, double meratorDistance) {
        double totolDistance = meratorDistance * latLngList.size();
        double scale = 0;
        for (int i = 0; i < latLngList.size(); i++) {
            int j = i + 1;
            if (j == latLngList.size()) {
                j = 0;
            }
            ILatLng point1 = latLngList.get(i);
            ILatLng point2 = latLngList.get(j);
            double twoPointRealDistance = IMapUtils.calculateLineDistance(new ILatLng(point1.latitude, point1.longitude), new ILatLng(point2.latitude, point2.longitude));
            double twoPointMeratorDistance = getTwoPointMeratorDistance(point1, point2);
            scale += twoPointMeratorDistance / twoPointRealDistance;
        }
        return totolDistance / scale;
    }

    private static double getTwoPointMeratorDistance(ILatLng point1, ILatLng point2) {
        double x1 = longitudeToX(point1.longitude);
        double y1 = latitudeToY(point1.latitude);
        double x2 = longitudeToX(point2.longitude);
        double y2 = latitudeToY(point2.latitude);
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow((y2 - y1), 2));
    }
}
