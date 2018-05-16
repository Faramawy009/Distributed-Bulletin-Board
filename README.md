# Distributed Bulletin Board with Multiple Consistency Protocols
The purpose of this project is to design a Bulletin Board distributed system that allows clients to connect, post, reply and read on any server in the system. The system was implemented 3 times using 3 different consistency protocols; Sequential Consistency using Primary-Backup protocol, Quorum Consistency, and Read-your-Write Consistency. For all designs, the system consists if 5 servers. In each branch of this repo is a detailed description of each implementation with the run time of each case.
Note: All these measurements changed by non trivial amount, but the average was taken over a 100 test, switching replica also affected these numbers.


## Running The Program

This project was developed using Intellij IDEA. To run any of the 3 versions, follow the steps below:

1- Open the project using Intellij IDE

2- Run the following file 5 times with 5 different server ID’s ranging from 1 to 5: src/edu/umn/FaraHany/ServerSide/ServerMain

3- Once the server is up, it will print the port on which it’s listening

4- Run the following file to start a client : src/edu/umn/FaraHany/ClientSide/ClientMain

5- Use “localhost” when asked for the server ip

6- Use the port number from the server prompt to connect
