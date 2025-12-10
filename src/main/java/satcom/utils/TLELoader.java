package satcom.utils;

import org.orekit.propagation.analytical.tle.TLE;
import java.io.*;
import java.util.*;

public class TLELoader {

    public static List<TLE> loadFromFile(String filePath) {
        ArrayList<TLE> tleList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line1, line2;
            while ((line1 = br.readLine()) != null && (line2 = br.readLine()) != null) {
                if (line1.startsWith("1 ") && line2.startsWith("2 ")) {
                    tleList.add(new TLE(line1.trim(), line2.trim()));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading TLE file: " + e.getMessage());
        }

        return tleList;
    }
}
