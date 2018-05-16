# Quorum Consistency

In this design, we assume that Nw (number of write replicas) is 4, including the server itself. And Nr (number of read replicas) is 2, including the server itself. The highest ID in a database is used as the version number. Therefore, the replica with the largest ID in its database is the most recently updated one.When a server receives a write request, it requests the databases of the most recently updated write replica, performs the write operation and sends a confirmation receipt to the client. After that, the server’s updates/changes are broadcasted to the write replicas to be processes. Which allows faster write throughput to the client as opposed to broadcasting changes before sending the confirmation receipt.
On the other hand, when a server receives a read request, the server compares its read to the other read replica, and performs the read operation on the one with the higher version number.

The ratio 4:2 balances the trade-off between fast reads and fast write throughput. It allows for a reasonable write throughput and very fast read throughput. Below is the average runtime of the 4 operations the client can run.

## Run Time
Read: 5 ms(Grows as database grows!)
Choose: 3 ms
Post/Reply: 8 ms
