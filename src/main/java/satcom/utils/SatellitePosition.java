package satcom.utils;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.frames.FramesFactory;
// import org.orekit.models.earth.GeoMagneticFields;
// import org.orekit.orbits.Orbit;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.bodies.OneAxisEllipsoid;
// import org.orekit.frames.TopocentricFrame;
import org.orekit.bodies.BodyShape;
// import org.orekit.bodies.CelestialBodies;
import org.orekit.frames.Frame;

public class SatellitePosition {

    public static GeodeticPoint getLatLon(TLE tle, AbsoluteDate date) {

        TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);

        // Get satellite position in TEME
        var pv = propagator.getPVCoordinates(date, FramesFactory.getTEME());

        // Convert TEME → ECEF (ITRF)
        Frame itrf = FramesFactory.getITRF(IERSConventions.IERS_2010, true);

        Vector3D positionECEF = FramesFactory.getTEME()
                .getTransformTo(itrf, date)
                .transformPosition(pv.getPosition());

        // Convert ECEF → Lat/Lon
        BodyShape earth = new OneAxisEllipsoid(
                Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                Constants.WGS84_EARTH_FLATTENING,
                itrf
            );

        return earth.transform(positionECEF, itrf, date);
    }
}
