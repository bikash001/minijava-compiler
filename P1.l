	//FLEX FILE adder.l 

%{
#include <stdio.h>
#include <stdlib.h>
#include "P1.tab.h" 
void yyerror(char *s); 
int yyparse(void);
%}

%%
				
[0-9]+			{ 
					yylval.id_val = mem_alloc(yytext, _DEF_);
					return INTEGER; 
				}

"//"[^\n]*		;

"/*"("*"[^/]|[^*])*"*/" ;

"int"			return INT;
"public"		{return PUBLIC;}
"if"			return IF;
"else"			return ELSE;
"while"			return WHILE;
"extends"		return EXTENDS;
"static"		{ return STATIC;}
"void"			{ return VOID;}
"System"		{ return SYSTEM;}
"."				{ return DOT;}
"class"			{ 
					
					return CLASS;
				}
"boolean"		return BOOLEAN;
"true"			return TRUE;
"false"			return FALSE;
"this"			return THIS;
"new"			return NEW;
"String"		{ return STRING;}
"return"		return RETURN;
"#define"		return HASHDEF;

[a-zA-Z_][a-zA-Z_0-9]*  { 
							yylval.id_val = mem_alloc(yytext, _ID_);
							return ID;
						}

"+"				return ADD;
"-"				return SUB;
"/"				return DIV;
"*"				return MULT;
";"				return SEMICOLON;
","				return COMMA;
"("				{ return LPAR;}
")"				{ return RPAR;}
"["				{ return LSQB;}
"]"				{ return RSQB;}
"{"				{ return LBRACE;}
"}"				{  
					return RBRACE;
				}
"&&"			return AND;
"||"			return OR;
"!="			return NEQ;
"<="			return LTEQ;
"!"				return EXCLAM;
"="				return EQ;

[ \t\n]+		;		//do nothing on whitespace
<<EOF>>			{	
					 return 0; 
					}
.               { 
					yyerror(" ");
				}

%%

void yyerror(char *er) { 
	printf("// Failed to parse macrojava code.");
	exit(1); 
}

int yywrap(void) { return 1; }