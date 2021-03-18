**Compile files**

Inside src folder: javac *.java

**Start RMI**

Inside src folder: start rmiregistry

**Run Peer**

* java Peer <protocol_version> <peer_id> <service_access_point> <MC_IP_address> <MC_port> <MDB_IP_address> <MDB_port> <MDR_IP_address> <MDR_port>

* (ex.) java Peer 1.0 1 Peer1 224.0.0.15 8001 224.0.0.16 8002 224.0.0.17 8003

**Run Client**

* **Backup:**
  - java Client <peer_ap> BACKUP <file_path_name> <replication_degree>
  - (ex.) java Client Peer1 BACKUP "D:\U. Porto\3 ano\2 semestre\sdis_09\proj1\pinguim.png" 2
