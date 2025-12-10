package satcom.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.FramesFactory;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.propagation.analytical.tle.*;
import org.orekit.utils.IERSConventions;



public class GlobeView extends StackPane {

    private Image map;
    private Canvas canvas;
    private GraphicsContext gc;
    private OneAxisEllipsoid earth;

    public GlobeView(double width, double height) {
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        map = new Image(getClass().getResource("/textures/earth.jpg").toExternalForm());

        earth = new OneAxisEllipsoid(6378137.0, 1.0 / 298.257223563, FramesFactory.getITRF(IERSConventions.IERS_2010, true));

        gc.drawImage(map, 0, 0, width, height);
        getChildren().add(canvas);
    }

    public void plotSatellite(TLE tle) {
        try {
            var propagator = TLEPropagator.selectExtrapolator(tle);
            AbsoluteDate now = new AbsoluteDate(new java.util.Date(), TimeScalesFactory.getUTC());

            PVCoordinates pv = propagator.getPVCoordinates(now, FramesFactory.getITRF(IERSConventions.IERS_2010, true));
            GeodeticPoint gp = earth.transform(pv.getPosition(), FramesFactory.getITRF(IERSConventions.IERS_2010, true), now);

            double lat = Math.toDegrees(gp.getLatitude());
            double lon = Math.toDegrees(gp.getLongitude());

            drawDot(lat, lon);

        } catch (Exception e) {
            System.out.println("Error plotting: " + e.getMessage());
        }
    }

    private void drawDot(double lat, double lon) {
        double x = (lon + 180) * (canvas.getWidth() / 360);
        double y = (90 - lat) * (canvas.getHeight() / 180);

        gc.setFill(Color.RED);
        gc.fillOval(x, y, 5, 5);
    }
}
