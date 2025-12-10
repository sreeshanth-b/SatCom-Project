package satcom.utils;

import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;

import java.io.File;

public class OrekitInitializer {

    public static void init() {
        File orekitData = new File("/home/shreeshanth/Documents/satcom-app/satcomapp/orekit-data");
        DataProvidersManager manager = DataContext.getDefault().getDataProvidersManager();
        manager.addProvider(new DirectoryCrawler(orekitData));
    }
}
