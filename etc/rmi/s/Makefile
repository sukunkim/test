all: client/ComputePi.class engine/ComputeEngine.class


client/ComputePi.class: client/ComputePi.java client/Pi.class compute/Compute.class
	javac client/ComputePi.java

client/Pi.class: client/Pi.java
	javac client/Pi.java

engine/ComputeEngine.class: engine/ComputeEngine.java compute/Compute.class
	javac engine/ComputeEngine.java

compute/Compute.class: compute/Compute.java compute/Task.class
	javac compute/Compute.java

compute/Task.class: compute/Task.java
	javac compute.Task.java


clean:
	rm -f client/*.class engine/*.class compute/*.class
