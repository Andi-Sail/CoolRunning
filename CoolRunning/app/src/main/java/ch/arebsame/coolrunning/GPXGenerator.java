package ch.arebsame.coolrunning;

import android.location.Location;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// https://stackoverflow.com/questions/46490192/how-to-export-a-gpx-file-from-a-latlng-arraylist

public class GPXGenerator {

    String fileName;
    String header;
    String nameTag;
    String footer;
    DateFormat df;
    String segments;
    String path;

    private boolean dir_exists(String dir_path)
    {
        boolean ret = false;
        File dir = new File(dir_path);
        if(dir.exists() && dir.isDirectory())
            ret = true;
        return ret;
    }

    public GPXGenerator(String name) {
        this.fileName = name + ".gpx";
        this.path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CoolRunning";

        if (!dir_exists(this.path)){
            File directory = new File(this.path);
            directory.mkdirs();
        }

        this.header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"MapSource 6.15.5\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk>\n";
        this.nameTag = "<name>" + name + "</name><trkseg>\n";

        this.segments = "";
        this.df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        this.footer = "</trkseg></trk></gpx>";

    }

    public void addPoint(Location location) {
        segments += "<trkpt lat=\"" + location.getLatitude() + "\" lon=\"" + location.getLongitude() + "\"><time>" + df.format(new Date(location.getTime())) + "</time></trkpt>\n";
    }

    public void writeToFile() {
        try {
            File file = new File(path + "/" + fileName);

            FileWriter writer = new FileWriter(file, false);
            writer.append(header);
            writer.append(nameTag);
            writer.append(segments);
            writer.append(footer);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            Log.e("generateGfx", "Error Writting Path",e);
        }
    }

}
