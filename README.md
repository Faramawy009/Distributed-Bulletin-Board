# Read-your-Write consistency (Local-Write Protocol)

This architecture is similar to the primary-backup protocol,however, the main difference is that the coordinator changes on write requests. Similar to primary-backup protocol, read requests are processed on the local replica. However, When a server receives a write request, it requests the primary from the leader. Then, The leader sends the most-recent database (primary) to that server and declares that it’s not the leader anymore. On the other side, the server processes the write request and sends the confirmation message to the client. After that, the same server declares itself as the new leader by broadcasting its address along with the most up-to-date primary (database).
This architecture resulted in faster turnaround (time between a client request and server response) than the first architecture because the response is sent to the client before propagating the updates to all the replicas. However, reads are not guaranteed to see the most-recent updates, but the reads will have the same order on all replicas (sequential consistency is preserved).

## Run Time

Read: 1 ms(Grows as database grows!)

Choose: <1 ms

Post/Reply: 3 ms
