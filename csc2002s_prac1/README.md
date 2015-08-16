There are four .java files here. Main, ProcessorThread, and FilterObject make up one program, and FileChecker is a program by itself.

Main takes 3 command-line arguments in the form <input file> <filter size> <output file>.
Example: java Main inp1.txt 3 out1.txt

The program will then prompt you to choose between median filtering and mean filtering. My entire report is based on the
median filtering (as you would expect) and the mean filtering was only implemented as an extra-credit exercise.

After that, you can choose between using the sequential implementation, or one of TWO parallel implementations. Option 2
is a parallelisation that uses Java's Fork/Join Framework, and the third option uses standard threads. This was also done
for extra credit, but all three implementations have been analysed in my report.

No matter which algorithm you choose, it will execute 20 times, record the runtimes, remove the largest outlier, and output
an average. Once you've run each of the implementations a couple of times, you can compare the output files to each other
using my FileChecker program. It takes NO command-line arguments. Instead, it will prompt you to input two files to compare
Example: out1.txt out2.txt

If there are any differences, the program will print the line at which the first difference occurs (or it will print that
the files are different sizes and not even bother checking any more). Otherwise, it will state that they are identical.

Sequential threshold is fixed. You can read my report to see my reasoning for this. Feel free to change it in the code 
yourself if you so wish. The field is static and located in FilterObject.

The makefile supports "make clean" and "make". There is no "make run" because the invocation requires command-line arguments.

I spent a lot of time on my program. I hope that my thorough commenting (for your convenience) and efficient
code doesn't go unnoticed.

My git archive is a remote repository on GitHub. 

Have a great day.