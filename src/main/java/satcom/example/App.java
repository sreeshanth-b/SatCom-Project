package satcom.example;

import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.TopocentricFrame;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.propagation.SpacecraftState;
import org.orekit.utils.PVCoordinates;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.data.*;

import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class App {

    public static void main(String[] args) throws Exception {

    
        File orekitData = new File("orekit-data");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));


   
        String line1 = "1 00900U 64063C   25336.76317516  .00000919  00000+0  92992-3 0  9995";
        String line2 = "2 00900  90.2215  67.2257 0027050 164.4617 317.9484 13.76346416 44401";

   

        TLE tle = new TLE(line1, line2);
        TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);

     
        double latitude  = 12.9716;
        double longitude = 77.5946;
        double altitude  = 920;  

        GeodeticPoint observer = new GeodeticPoint(Math.toRadians(latitude),
         Math.toRadians(longitude),
          altitude);

        OneAxisEllipsoid earth = new OneAxisEllipsoid(
                Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                Constants.WGS84_EARTH_FLATTENING,
                FramesFactory.getITRF(IERSConventions.IERS_2010, true)
        );

        TopocentricFrame station = new TopocentricFrame(earth, observer, "MyStation");

    
     
        AbsoluteDate now = new AbsoluteDate(2027,12,8,9,20,10,TimeScalesFactory.getUTC());
        SpacecraftState state = propagator.propagate(now);

        PVCoordinates pv = state.getPVCoordinates(
                FramesFactory.getITRF(IERSConventions.IERS_2010, true)
        );

        double azimuth = station.getAzimuth(pv.getPosition(), earth.getBodyFrame(), now);
        double elevation = station.getElevation(pv.getPosition(), earth.getBodyFrame(), now);
        double Range = station.getRange(pv.getPosition(), earth.getBodyFrame(), now);

        System.out.println("Azimuth   = " + Math.toDegrees(azimuth));
        System.out.println("Elevation = " + Math.toDegrees(elevation));
        System.out.println("Range = " + Range/1000);
       


      
        XYSeries points = new XYSeries("Satellite");

        if (elevation > 0) {
            points.add(Math.toDegrees(azimuth), Math.toDegrees(elevation));
        }

        XYSeriesCollection dataset = new XYSeriesCollection(points);

        JFreeChart radar = ChartFactory.createPolarChart(
                "Satellite Radar View",
                dataset,
                true,
                false,
                false
        );

        ChartFrame frame = new ChartFrame("Radar View", radar);
        frame.setSize(600, 600);
        frame.setVisible(true);
    }
}





