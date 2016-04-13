OS := $(shell uname)

SRC = src
BIN = bin
LIB = lib
PACKAGE = com/pepebecker

build:
	javac -d bin -sourcepath $(SRC) -cp .:lib/* $(SRC)/$(PACKAGE)/*.java

run:
ifeq ($(OS), Darwin)
	java -cp $(BIN):$(LIB)/* -Djava.library.path=native/macosx $(PACKAGE)/Pong
else ifeq $(OS) Linux
	java -cp $(BIN):$(LIB)/* -Djava.library.path=native/linux $(PACKAGE)/Pong
endif