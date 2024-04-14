import java.io.FileInputStream;
import java.util.*;

/**
 * 配置文件读取类
 */
public class PropReader {
    public static Map<Integer, Peer> readPeers(String path) {
        Map<Integer, Peer> peers = new HashMap<>();

        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
            int serverNum = Integer.parseInt(properties.getProperty("server.num"));
            if (serverNum <= 0) {
                throw new IllegalArgumentException("not enough servers");
            }
            for (int i = 0; i < serverNum; i++) {
                String ip = properties.getProperty("server" + i + ".ip");
                int clusterPort = Integer.parseInt(properties.getProperty("server" + i + ".clusterPort"));
                int clientPort = Integer.parseInt(properties.getProperty("server" + i + ".clientPort"));
                peers.put(i, new Peer(i, ip, clusterPort, clientPort));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return peers;
    }
}
