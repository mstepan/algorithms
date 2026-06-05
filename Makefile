JAVA_HOME ?= $(shell java -XshowSettings:properties -version 2>&1 | awk -F= '/^[[:space:]]*java.home =/ {gsub(/^[[:space:]]+|[[:space:]]+$$/, "", $$2); print $$2; exit}')
INCLUDE=-I $(JAVA_HOME)/include -I $(JAVA_HOME)/include/darwin
CC=g++
CPPFLAGS=-dynamiclib
AGENT_OUT=enhancedNPE.dylib
AGENT_CPP=enhancedNPE.cpp

all: clean
	$(CC) $(CPPFLAGS) $(INCLUDE) -o $(AGENT_OUT) $(AGENT_CPP)

clean:
	rm -rf $(AGENT_OUT)
