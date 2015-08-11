#Simple makefile for compiling classes for csc2002s_prac1
#DNBAID001 - Aidan de Nobrega

JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	FileChecker.java \
	FilterObject.java \
	Main.java \
        ProcessorThread.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
	$(RM) *.out
	$(RM) *.txt

#Not applicable. Takes command line arguments
#run: default
#	java Main
#