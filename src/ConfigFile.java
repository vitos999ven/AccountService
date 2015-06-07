
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigFile {

    private int port, rCount, wCount, first, last;
    private String host;

    public ConfigFile() throws NumberFormatException, IOException {
        Properties props = new Properties();
        try (InputStream in = Files.newInputStream(Paths.get("Config.txt"))) {
            props.load(in);
        }

        host = props.getProperty("host");
        port = Integer.parseInt(props.getProperty("port"));
        rCount = Integer.parseInt(props.getProperty("rCount"));
        wCount = Integer.parseInt(props.getProperty("wCount"));
        first = Integer.parseInt(props.getProperty("idListFirst"));
        last = Integer.parseInt(props.getProperty("idListLast"));
        if (first > last){
            first =+ last;
            last = first - last;
            first =- last;
        }

    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public int getRCount() {
        return rCount;
    }

    public int getWCount() {
        return wCount;
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }

}
