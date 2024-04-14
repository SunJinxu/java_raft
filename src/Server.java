import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * id 服务器标号
 * role 角色
 * term 任期
 * messages 启动以来收到的所有消息
 * clientIn server接收客户端消息的端口
 * clusterIn server接收集群消息的端口
 * peers 集群其他服务器
 * leader 集群leader
 */
public class Server {
    private final int id;
    int role;
    private Peer Leader;
    private int term;

    private int clusterIn;
    private int clientIn;
    private List<byte[]> messages;
    private List<Peer> peers;


    public Server(int id, Map<Integer, Peer> peersMap) throws IOException {
        this.id = id;
        this.peers = new ArrayList<>();
        this.messages = new ArrayList<>();

        for (int peerId : peersMap.keySet()) {
            if (peerId == this.id) {
                this.clusterIn = peersMap.get(peerId).clusterPort;
                this.clientIn = peersMap.get(peerId).clientPort;
            } else {
                Peer peer = peersMap.get(peerId);
                peers.add(peer);
            }
        }
    }

    public void start() throws IOException {
        new Thread(new PortHandler(0, clientIn)).start();
        new Thread(new PortHandler(0, clusterIn)).start();
    }

    /**
     * leader方法内部消息广播到集群
     * @param bytes 消息内容
     */
    private void broadcastMsgToPeers(byte[] bytes) {
        for (Peer peer : this.peers) {
            try (Socket socket = new Socket(peer.ip, peer.clusterPort)) {
                OutputStream outputStream = socket.getOutputStream();
                System.out.println("sending to peer[" + peer.ip + " " + peer.clusterPort + "]");
                outputStream.write(bytes);
                outputStream.flush();
            } catch (Exception e) {
                System.out.println(new String(bytes));
            }
        }
    }

    /**
     * 转发外部消息到master节点
     * @param bytes 消息内容
     */
    private void forwardMsgToLeader(byte[] bytes) {

    }

    private class PortHandler implements Runnable{

        // 内部listener或外部listener
        private final int type;

        private final ServerSocket serverSocket;

        public PortHandler(int type, int port) throws IOException {
            this.type = type;
            serverSocket = new ServerSocket(port);
        }

        @Override
        public void run() {
            while (true) {
                try (Socket accept = serverSocket.accept()){
                    byte[] bytes = accept.getInputStream().readAllBytes();

                    // 外部listener
                    if (this.type == 0) {
                        if (role == 0) {
                            // raft一致性处理，本地日志+广播
                        } else {
                            forwardMsgToLeader(bytes);
                        }
                    } else if (this.type == 1) {    // 内部listener
                        // 根据消息类型处理
                        // 1. heartBeat
                        // 2. appendLogs
                        // 3. election
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
