import java.io.IOException;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {
        Map<Integer, Peer> peerMap = PropReader.readPeers("config/cluster.properties");
        assert peerMap != null;
        Server server0 = new Server(0, peerMap);
        server0.role = 0;
        Server server1 = new Server(1, peerMap);
        server1.role = 1;
        Server server2 = new Server(2, peerMap);
        server2.role = 1;
    }
}
