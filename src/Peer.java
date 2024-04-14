/**
 * 专门用于标记集群通讯信息的类
 */
public class Peer {
    int id;
    public String ip;
    public int clusterPort;
    public int clientPort;

    public Peer(int id, String ip, int clusterPort, int clientPort) {
        this.id = id;
        this.ip = ip;
        this.clusterPort = clusterPort;
        this.clientPort = clientPort;
    }
}
