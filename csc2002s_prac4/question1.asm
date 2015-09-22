.data
	prompt: 	.asciiz "Enter a series of 5 formulae:\n"
	reply: 		.asciiz "The values are:\n"
		.align 4
	buffer: 	.space 8
	arr: 		.space 40

.text
.globl main

main:					##print out prompt for input strings

	li 	$v0, 4			#print_string service
	la 	$a0, prompt		#load prompt as argument 0
	syscall				#system call to print prompt

input:					##set up loop counter

	li 	$t0, 40			#exit condition: load 40 into $t0
	li 	$t1, 0 			#counter for loop: load 0 into $t1

input_loop:				##input 5 strings
				
	beq 	$t1, $t0, output	#branch to output when $t0 = $t1
	
	##loop body
	li 	$v0, 8			#read_int service
	la 	$a0, buffer 		#load byte space into address (8 bytes)
        li 	$a1, 8 			#allot the byte space for string
        syscall
        
        ld 	$s0, buffer		#load buffer into $s0 (doubleword)
 	sd 	$s0, arr+0($t1)		#store doubleword in "array"
        addi 	$t1, 8			#increment counter	
        
        j 	input_loop		#loop
	
output:					##set up loop counter and print input strings

	li 	$t1, 0			#counter for loop: load 0 into $t1	
	li 	$v0, 4			#print_string service
	la 	$a0, reply		
	syscall
	
output_loop:
	beq 	$t1, $t0, done		#branch to done when $t0 = $t1
	
	li 	$v0, 4			#print_string service
	la 	$a0, arr+0($t1)		#print string in array
	syscall

        addi 	$t1, 8			#increment counter
        j 	output_loop		#loop
        
done:
	li 	$v0, 10			#exit serivce
	syscall	
