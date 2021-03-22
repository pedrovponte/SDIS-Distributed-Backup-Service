import java.util.Arrays;

public class PutChunkMessageThread implements Runnable {
    private byte[] message;
    private Peer peer;
    private byte[] header;
    private byte[] body;
    private int senderId;
    private String fileId;
    private int chunkNo;
    private int replication_degree;

    // <Version> PUTCHUNK <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>
    public PutChunkMessageThread(byte[] message, Peer peer) {
        this.message = message;
        this.peer = peer;
        splitHeaderAndBody();
        String[] headerStr = new String(this.header).split(" ");
        this.senderId = Integer.parseInt(headerStr[2]);
        this.fileId = headerStr[3];
        this.chunkNo = Integer.parseInt(headerStr[4]);
        this.replication_degree = Integer.parseInt(headerStr[5]);
        // System.out.println("SenderId: " + this.senderId);
        // System.out.println("FileId: " + this.fileId);
        // System.out.println("ChunkNo: " + this.chunkNo);
        // System.out.println("Replication: " + this.replication_degree);

    }

    @Override
    public void run() {
        // in case senderId and peerId are equal, the thread returns because a peer must never store the chunks of its own files.
        if(checkIfSelf() == 1) {
            // System.out.println("Equals");
            return;
        }
        // System.out.println("Not equals");

        //check if the peer already has stored this chunk
        if(this.peer.getStorage().hasChunk(this.fileId, this.chunkNo) == true) {
            return;
        }

        Chunk chunk = new Chunk(this.fileId, this.chunkNo, this.body, this.replication_degree);

        this.peer.getStorage().addChunk(chunk);

        
    }

    public void splitHeaderAndBody() {
        int i;
        for(i = 0; i < this.message.length; i++) {
            if(this.message[i] == 0xD && this.message[i + 1] == 0xA && this.message[i + 2] == 0xD && this.message[i + 3] == 0xA) {
                break;
            }
        }

        this.header = Arrays.copyOfRange(this.message, 0, i);
        this.body = Arrays.copyOfRange(this.message, i + 4, message.length); // i+4 because between i and i+4 are \r\n\r\n
    }

    // checks if the senderId is equal to the receiver peerId. In case it is equal, returns 1, else returns 0.
    int checkIfSelf() {
        if(this.peer.getPeerId() == this.senderId) {
            return 1;
        }
        return 0;
    }
    
}
