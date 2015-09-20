#	$s0  	char counter
#	$s1  	temp char holder
#	$s2  	end line constant
#	$s3  	char loop counter
#	$s4	exponent countdown
#	$s6	exponent counter
#	$s7	exponent of 10	

.data
	buffer: .space 8
	str1: .asciiz "Enter a string:\n"
	str2: .asciiz "The value +5 is:\n"

.text
.globl main

main:
	li	$s0, 0
	li 	$t0, 10			#number of times to loop
	li 	$v0, 4			#print_string service
	la 	$a0, str1		#pass str1 as argument 0
	syscall

input:
	li 	$v0, 8			#read_string service
	la, 	$a0, buffer		#pass buffer as argument 0
	li, 	$a1, 8			#pass 8 (1 byte) as argument 1
	syscall
				
countloop:
	lb 	$s1, buffer+0($s0)	#load byte at buffer+$s0 into $s1
 	beqz 	$s1, countend		#if $s1 == 0: invoke countend
	beq	$s1, $t0, countend	#if $s1 == $t0: invoke countend
	addiu	$s0, 1			#else: increment $s0
	j 	countloop		#loop

countend:
	addi 	$s0, -1			#decrement $s0
	move 	$s4, $s0		#move contents of $s0 to $s4 (this value is highest power of 10)
	addiu 	$s3, 1 			#increment $s3
	move	$s2, $0			#ensure $s2 = 0
	
multloop:
	bgt 	$s3, $s0, addfive	#if $s3>$0: invoke addfive
	lb 	$s1, buffer+0($s3)	#load byte at buffer+$s3 to $s1
	addi	$s1, -48		#parse char as int
	move 	$s6, $0			#ensure $s6 = 0
 	move 	$s7, $0			#ensure $s7 = 0
	addiu 	$s7, 1			#increment $s7
	jal	exploop			#invoke exploop
	mult	$s7, $s1		#$s1*$s7
	mflo	$s1			#move answer into $s1
	addu	$s2, $s1, $s2		#$s1 + $s2 -> $s2
	addiu	$s3, 1			#increment $s3
	addi	$s4, -1			#decrement $s4
	j	multloop		#loop

exploop:
	addiu 	$s6, 1			#increment s6
	beq 	$s6, $s4, expend	#if s6 == $s4: invoke expend
	mult 	$s7, $t0		#s7*$t0
	mflo	$s7			#$s7 = $s7*$s0
	j 	exploop			#loop			
	
expend:
	jr	$ra			#return to multloop

addfive:
	addi 	$t5, $s2, 5		#$t2 = $s2 + 5

output:
	li 	$v0, 4			#print_string service
	la 	$a0, str2		#pass str2 as argument 0
	syscall
	li 	$v0, 1			#print_int service
	move	$a0, $t5		#$t5 -> $a0
	syscall

end:
	li 	$v0, 10			#exit service
	syscall
	
	
	
