#Simple makefile for compiling classes for csc2002s_prac1
#DNBAID001 - Aidan de Nobrega

JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	DrivingRangeApp.java \
	BallStash.java \
	Bollie.java \
        GolfBall.java \
	Golfer.java \
	Range.java \
	GolferGenerator.java
	
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
