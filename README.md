# Sequential Consistency (Primary-Backup Protocol)

In this design, the servers have a coordinator. They are configured to know the leader. Yet, it’s transparent to the client. When a server receives a read request, it reads it from the local database and send the result back to the client.

On reception of a write request, the server sends the request to the leader to process the write and issue an article ID. After that the new article ID is broadcasted to all the servers. Once the updates are propagated, the server adds the new server to its replica and then sends a confirmation receipt to the client.

This design allows for fast reads by ensuring that reads are processed on local database will have the same order on different replicas. On the other hand, writes are relatively slow because they require sending the request to the leader and waiting for the leader to broadcast the update to all the other replicas.

## Run Time
Read: 1 ms(Grows as database grows!)

Choose: <1 ms

Post/Reply: 5 ms
