JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_211.jdk/Contents/Home
INCLUDE=-I $(JAVA_HOME)/include -I $(JAVA_HOME)/include/darwin
CC=g++
CPPFLAGS=-dynamiclib
AGENT_OUT=enhancedNPE.dylib
AGENT_CPP=enhancedNPE.cpp

all: clean
	$(CC) $(CPPFLAGS) $(INCLUDE) -o $(AGENT_OUT) $(AGENT_CPP)

clean:
	rm -rf $(AGENT_OUT)
