.data
	str1:		.asciiz	"Enter a series of 5 formulae:\n"
	str2:		.asciiz "The values are:\n"
		.align	4
	buffer:		.space	8	# buffer for user input
	array:		.space  40	# user input storage

.text
.globl main

main:
	li	$t8, 61			#load 0x61="=" into $t8
	li	$t9, 10			#ASCII - signifies end
	li	$s3, 40		 	#load 40 into $s3
	li	$v0, 4			#print_string service
	la	$a0, str1		#pass str1 as argument 0
	syscall

input:
	li	$s2, 0 			#count number of loops

inputloop:	
	bge	$s2, $s3, output 	#if $s2 >= $s3: invoke output
	li	$v0, 8			#read_string service
	la	$a0, buffer		#pass buffer as argument 0
	li	$a1, 8			#pass 8 bytes as arguent 1
	syscall	
	lb	$t1, buffer		#load byte from buffer into $t1
	beq	$t1, $t8, saveref	#if $t1 = "=": invoke saveref
	j	savenum			#jump to savenum

savenum:
	ld	$s0, buffer 		#load doubleword buffer in #s0
	sd	$s0, array+0($s2)	#store doubleword from $s0 into array[$s2]
	addiu	$s2, 8			#add 8 (bytes) to $s2
	j	inputloop

saveref:
	j	getint			#jump to getint

output:
	li	$s2, 0 			#initialise $s2 with 0
	li	$v0, 4			#print_string service
	la	$a0, str2		#pass str2 as argument 0
	syscall

outputloop:	
	bge	$s2, $s3, end		#if $s2 >= $s3: invoke end
	li	$v0, 4			#print_string service
	la	$a0, array+0($s2)	#pass array[$s2] as argument 0
	syscall
	addiu 	$s2, 8			#add 8 to $s2
	j 	outputloop		#jump to outputloop

getint:
	move	$t0, $0			#0 -> $t0	
	move	$t3, $0			#0 -> $t3

countloop:
	lb	$t1, buffer+0($t0)	#load byte from buffer[$t0] into $t1
	beqz	$t1, countend		#if $t1 == 0: invoke countend
	beq	$t1, $t9, countend	#if $t1 == $t9: invoke countend
	addiu	$t0, 1			#increment $t0
	j	countloop		#loop

countend:
	addi	$t0, -1 		#decrement $t0 to ignore first character
	move	$t4, $t0 		#$t4 -> $t0
	addiu	$t3, 1 			#$t3 is the char counter, so set it to 1 to ignor first char
	move	$t2, $0			#$0 -> $t2

multloop:
	bgt	$t3, $t0, multend 		#$t3 > $t0: invoke multend
	lb	$t1, buffer+0($t3) 	#load byet from buffer[$t3] to $t1
	addi	$t1, -48 		#convert from ascii
	move	$t6, $0			#ensure $t6 = 0
	move	$t7, $0			#ensure $t7 = 0
	addiu	$t7, 1			#incrememnt $t7
	jal	exploop 		#get the power of ten for this digit
	mult	$t7, $t1		#$t7*$t1
	mflo	$t1			#$t1 = $t7*$t1
	addu	$t2, $t1, $t2		#$t2 = $t1 + $t2
	addiu	$t3, 1			#incrememnt $t3
	addi	$t4, -1			#decrement $t4
	j	multloop		#loop

multend: 
	li	$t9, 8			#load 8 into $t9
	mult	$t2, $t9		#$t2*$t9
	mflo	$t2			#$t2 = $t2*$t9
	li	$t9, 10			#load 10 into $t9
	ld	$s0, array+0($t2)	#load doubleword from array[$t2]
	sd	$s0, array+0($s2)	#store doubleword into array[$s2]
	addiu	$s2, 8			#add 8 to $s2
	j	inputloop		#jump to inputloop

exploop: 
	addiu	$t6, 1			#increment $t6
	beq	$t6, $t4, expend	#if $t6 == $t3: invoke expend
	mult	$t7, $t9		#$t7*$t9
	mflo	$t7			#$t7 = $t7*$t9
	j 	exploop			#jump to expend

expend:
	jr	$ra			#jump to return address

end:	
	li	$v0, 10			#exit_service
	syscall
