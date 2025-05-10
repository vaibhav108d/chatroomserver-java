# chatroomserver
A simple chatroom server built with Java, allowing multiple clients to connect and communicate in real time. This project demonstrates the use of multithreading and socket programming.

Features
Supports multiple clients simultaneously.
Real-time messaging.
Simple and lightweight.
Requirements
Java Development Kit (JDK) 8 or above.
Any IDE or command line to run the Java files.
Setup Instructions
1. Clone the Repository
bash
Copy code
git clone https://github.com/saurabhkushwaha438/chatroomserver.git
cd chatroom-server
2. Compile the Code
Navigate to the project's root directory and compile the Java files:
javac -d bin src/*.java
3. Run the Server
Start the chat server:
java -cp bin ChatServer
4. Run the Client(s)
In separate terminal windows or on different machines, start the client applications:
java -cp bin ChatClient
