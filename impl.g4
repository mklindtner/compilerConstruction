grammar impl;

/* A small imperative language */

//implement fn as startf 	
// start   :  
// 	cs+=command* 
// 	EOF ;
start   :  
	fn* expr	
	EOF ;

//int fn(...) {};
//no expr for now
fn : type '(' fnparams ')' '{'expr'};';

type	: 'int' FUNCNAMES #IntegerType
		| 'bool' FUNCNAMES #BoolType
	;


fnparams : type (',' | type)* ;

program : 
	c=command                      	 # SingleCommand
	| '{' cs+=command* '}'           # MultipleCommands
	;
	
command : 
	x=ID '=' e=expr ';'	         # Assignment
	| 'output' e=expr ';'            # Output
        | 'while' '('c=condition')' p=program  # WhileLoop
	;
	
expr	
	: MIN e1=expr 					# UnaryMinus	
	| expr op=MULTDIVOP expr 		# MultDiv		
	| e1=expr op=ADDSUBOP e2=expr 	# AddSub
	| c=FLOAT     	      			# Constant
	| x=ID		      				# Variable
	| '(' e=expr ')'      			# Parenthesis
	;

condition : e1=expr '!=' e2=expr # Unequal
	  // ... extend me 
	  ;  

ID    : ALPHA (ALPHA|NUM)* ;
FLOAT : NUM+ ('.' NUM+)? ;

ALPHA      	   : [a-zA-Z_ÆØÅæøå] ;
NUM        	   : [0-9] ;
MIN	       	   : '-' ;
MULTDIVOP  	   : ('*' | '/') ;
ADDSUBOP	   : ('+' | '-') ;
FUNCNAMES	   : ('A'..'Z'|'a'..'z'|'^') ; //why '^' ? BECAUSE I CAN, ALSO FUCK NUMBERS IN FUNCTION NAMES


WHITESPACE : [ \n\t\r]+ -> skip;
COMMENT    : '//'~[\n]*  -> skip;
COMMENT2   : '/*' (~[*] | '*'~[/]  )*   '*/'  -> skip;
