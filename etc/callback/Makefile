all: TickerServerImpl.class ClientProgram.class


TickerServerImpl.class: TickerServerImpl.java TickerServer.class Client.class
	javac TickerServerImpl.java

ClientProgram.class: ClientProgram.java TickerServer.class Client.class
	javac ClientProgram.java

TickerServer.class: TickerServer.java
	javac TickerServer.java

Client.class: Client.java
	javac Client.java


clean:
	rm -f *.class
