/* Macro java grammar */
%code requires{
	#ifndef CUSTOM_DATA
	#define CUSTOM_DATA
	enum _token_type {
		_ID_, _COMMA_, _LPAR_, _RPAR_, _DEF_
	};
	struct node{
		char* data;
		enum _token_type type;
		struct node* next;
	};

	struct non_term{
		struct node *head;
		struct node *tail;
		struct non_term *next;
	};

	struct macro_id{
		struct node* idPtr;
		unsigned int type : 1;
		struct non_term* exp;
		struct macro_id* next;
	};

	struct macro_id* macro_list_head;

	struct id_map {
		struct node *head;
		struct node *tail;
	};
	struct node *mem_alloc(char *, enum _token_type);
	#endif
}

%{
	#include <stdio.h>
	#include <stdlib.h>
	#include <string.h>
	#include "P1.tab.h"

	extern int yylex();
	extern void yyerror(char *s);
	static void printAll(struct non_term* );
	static void mem_free();
	static void mem_nt_free();
	static void nt_nt_memalloc(struct non_term**, struct non_term*);
	static void nt_t_memalloc(struct non_term**, struct node*);
	static struct non_term* replace_macro(struct macro_id*,struct node*);
	static struct macro_id* find_macro(char*);
	static void process_exp(struct id_map* ,int ,struct node*);
	static struct non_term* copy_list(struct non_term*);
	static void error_if_exist(const char *);
%}


%union {
	struct node* id_val;
	struct non_term * nt_val;
};

%token <id_val> ID
%token <id_val>  INT SEMICOLON PUBLIC STATIC VOID SYSTEM DOT CLASS
%token <id_val> BOOLEAN TRUE FALSE THIS NEW IF ELSE WHILE EXTENDS RETURN STRING INTEGER
%token <id_val> HASHDEF LPAR RPAR LSQB RSQB LBRACE RBRACE COMMA
%token <id_val> ADD SUB MULT DIV LTEQ NEQ AND OR EQ EXCLAM
%type <nt_val> Goal Goal_wMacro Macro TypeDec MainClass TypeDeclaration Stmt Mthdec MethodDeclaration Nmeth Rnext Stmrec Idn
%type <nt_val> Type Recstmt Statement Exp Expression PrimaryExpression MacroDefinition MacroDefStatement Idmac
%type <nt_val> MacroDefExpression Identifier Integer

%nonassoc with_
%nonassoc AND OR NEQ LTEQ ADD SUB MULT DIV LSQB DOT 
%nonassoc then
%nonassoc ELSE


%start Goal

%%

Goal 	:	Macro Goal_wMacro {
				nt_nt_memalloc(&($$), $2);
				mem_nt_free(&($1));
				printAll($$);
			}
		| Goal_wMacro	{
				nt_nt_memalloc(&($$), $1);
				printAll($$);
			}
		;

Goal_wMacro :  MainClass {
			nt_nt_memalloc(&($$), $1);
		}
	| MainClass TypeDec {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = ($2)->head;
			tail = ($2)->tail;
			($$)->tail = tail;
			mem_nt_free(&($2));
		}
	;

Macro : MacroDefinition ;
	| Macro MacroDefinition 
	;

TypeDec : TypeDeclaration {
			nt_nt_memalloc(&($$), $1);
		}
	| TypeDec TypeDeclaration {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = ($2)->head;
			tail = ($2)->tail;
			($$)->tail = tail;
			mem_nt_free(&($2));
		}
	;

MainClass 	: 	CLASS Identifier LBRACE PUBLIC STATIC VOID Identifier LPAR STRING LSQB RSQB Identifier
 RPAR LBRACE SYSTEM DOT Identifier DOT Identifier LPAR Expression RPAR SEMICOLON RBRACE RBRACE {
				if (strcmp("main",$7->head->data) != 0) {
					yyerror(" ");
				}
				if (strcmp("out", $17->head->data) != 0) {
					yyerror(" ");
				}
				if (strcmp("println", $19->head->data) != 0) {
					yyerror(" ");
				}
				struct node* head = mem_alloc("class", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("{\npublic static void main( String[]", _DEF_);
				tail = tail->next;
				tail->next = ($12)->head;
				tail = ($12)->tail;
				tail->next = mem_alloc(") {\n System.out.println(", _DEF_);
				tail = tail->next;
				tail->next = ($21)->head;
				tail = ($21)->tail;
				tail->next = mem_alloc(");\n }}", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
				mem_nt_free(&($12));
				mem_nt_free(&($21));
				mem_nt_free(&($7));
				mem_nt_free(&($17));
				mem_nt_free(&($19));
			}
	;

TypeDeclaration :	CLASS Identifier LBRACE RBRACE {
				struct node* head = mem_alloc("\nclass", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("{}\n", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
			}
	| CLASS Identifier LBRACE Stmt RBRACE {
				struct node* head = mem_alloc("\nclass", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("{\n", _DEF_);
				tail = tail->next;
				tail->next = ($4)->head;
				tail = ($4)->tail;
				tail->next = mem_alloc("}\n", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
				mem_nt_free(&($4));
			}
	| CLASS Identifier LBRACE Stmt Mthdec RBRACE {
				struct node* head = mem_alloc("\nclass", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("{\n", _DEF_);
				tail = tail->next;
				tail->next = ($4)->head;
				tail = ($4)->tail;
				tail->next = ($5)->head;
				tail = ($5)->tail;
				tail->next = mem_alloc("}\n", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
				mem_nt_free(&($4));
				mem_nt_free(&($5));
			}
	| CLASS Identifier LBRACE Mthdec RBRACE {
				struct node* head = mem_alloc("\nclass", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("{\n", _DEF_);
				tail = tail->next;
				tail->next = ($4)->head;
				tail = ($4)->tail;
				tail->next = mem_alloc("}\n", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
				mem_nt_free(&($4));
			}
	| CLASS Identifier EXTENDS Identifier LBRACE RBRACE {
				struct node* head = mem_alloc("\nclass", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("extends", _DEF_);
				tail = tail->next;
				tail->next = ($4)->head;
				tail = ($4)->tail;
				tail->next = mem_alloc("{}\n", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
				mem_nt_free(&($4));
			}
	| CLASS Identifier EXTENDS Identifier LBRACE Stmt RBRACE {
				struct node* head = mem_alloc("\nclass", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("extends", _DEF_);
				tail = tail->next;
				tail->next = ($4)->head;
				tail = ($4)->tail;
				tail->next = mem_alloc("{\n", _DEF_);
				tail = tail->next;
				tail->next = ($6)->head;
				tail = ($6)->tail;
				tail->next = mem_alloc("}\n", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
				mem_nt_free(&($4));
				mem_nt_free(&($6));
			}
	| CLASS Identifier EXTENDS Identifier LBRACE Mthdec RBRACE {
				struct node* head = mem_alloc("\nclass", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("extends", _DEF_);
				tail = tail->next;
				tail->next = ($4)->head;
				tail = ($4)->tail;
				tail->next = mem_alloc("{\n", _DEF_);
				tail = tail->next;
				tail->next = ($6)->head;
				tail = ($6)->tail;
				tail->next = mem_alloc("}\n", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
				mem_nt_free(&($4));
				mem_nt_free(&($6));
			}
	| CLASS Identifier EXTENDS Identifier LBRACE Stmt Mthdec RBRACE {
				struct node* head = mem_alloc("\nclass", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("extends", _DEF_);
				tail = tail->next;
				tail->next = ($4)->head;
				tail = ($4)->tail;
				tail->next = mem_alloc("{\n", _DEF_);
				tail = tail->next;
				tail->next = ($6)->head;
				tail = ($6)->tail;
				tail->next = ($7)->head;
				tail = ($7)->tail;
				tail->next = mem_alloc("}\n", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
				mem_nt_free(&($4));
				mem_nt_free(&($6));
				mem_nt_free(&($7));
			}
	;

Stmt: Type Identifier SEMICOLON  {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = ($2)->head;
			tail = ($2)->tail;
			tail->next = mem_alloc(";\n", _DEF_);
			tail = tail->next;
			($$)->tail = tail;
			mem_nt_free(&($2));
		}
	| Stmt Type Identifier SEMICOLON {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = ($2)->head;
			tail = ($2)->tail;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			tail->next = mem_alloc(";\n", _DEF_);
			tail = tail->next;
			($$)->tail = tail;
			mem_nt_free(&($2));
			mem_nt_free(&($3));
		}
	;

Mthdec: MethodDeclaration {
			nt_nt_memalloc(&($$), $1);
		}
	| Mthdec MethodDeclaration {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = ($2)->head;
			tail = ($2)->tail;
			($$)->tail = tail;
			mem_nt_free(&($2));
		}
	;

MethodDeclaration : PUBLIC Type Identifier LPAR Nmeth {
				struct node* head = mem_alloc("public", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = ($3)->head;
				tail = ($3)->tail;
				tail->next = mem_alloc("(", _DEF_);
				tail = tail->next;
				tail->next = ($5)->head;
				tail = ($5)->tail;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
				mem_nt_free(&($3));
				mem_nt_free(&($5));
			}
	;

Nmeth : RPAR LBRACE Rnext {
				struct node* head = mem_alloc(") {\n", _DEF_);
				struct node* tail = head;
				tail->next = ($3)->head;
				tail = ($3)->tail;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($3));
			}
	| Idn RPAR LBRACE Rnext {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc(") {\n", _DEF_);
			tail = tail->next;
			tail->next = ($4)->head;
			tail = ($4)->tail;
			($$)->tail = tail;
			mem_nt_free(&($4));
		}
	;

Rnext: RETURN Expression SEMICOLON RBRACE {
				struct node* head = mem_alloc("return", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("; }\n", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
			}
	| Stmrec RETURN Expression SEMICOLON RBRACE {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("return", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			tail->next = mem_alloc("; }\n", _DEF_);
			tail = tail->next;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	| Stmt RETURN Expression SEMICOLON RBRACE {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("return", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			tail->next = mem_alloc("; }\n", _DEF_);
			tail = tail->next;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	| Stmt Stmrec RETURN Expression SEMICOLON RBRACE {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = ($2)->head;
			tail = ($2)->tail;
			tail->next = mem_alloc("return", _DEF_);
			tail = tail->next;
			tail->next = ($4)->head;
			tail = ($4)->tail;
			tail->next = mem_alloc("; }\n", _DEF_);
			tail = tail->next;
			($$)->tail = tail;
			mem_nt_free(&($2));
			mem_nt_free(&($4));
		}
	;

Stmrec: Statement {
			nt_nt_memalloc(&($$), $1);
		}
	| Stmrec Statement {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = ($2)->head;
			tail = ($2)->tail;
			($$)->tail = tail;
			mem_nt_free(&($2));
		}
	;

Idn: Type Identifier {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = ($2)->head;
			tail = ($2)->tail;
			($$)->tail = tail;
			mem_nt_free(&($2));
		}
	| Idn COMMA Type Identifier {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc(",", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			tail->next = ($4)->head;
			tail = ($4)->tail;
			($$)->tail = tail;
			mem_nt_free(&($3));
			mem_nt_free(&($4));
		}
	;

Type 	: 	INT LSQB RSQB {
				struct node* head = mem_alloc("int []", _DEF_);
				struct node* tail = head;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
			}
	| 	BOOLEAN {
				struct node* head = mem_alloc("boolean", _DEF_);
				struct node* tail = head;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
			}
	| 	INT {
				struct node* head = mem_alloc("int", _DEF_);
				struct node* tail = head;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
			}
	| 	Identifier {
			nt_nt_memalloc(&($$), $1);
		}
	;

Recstmt: Statement {
			nt_nt_memalloc(&($$), $1);
		}
	| Recstmt Statement {
			nt_nt_memalloc(&($$), $1);
			($$)->tail->next = ($2)->head;
			($$)->tail = ($2)->tail;
			mem_nt_free(&($2));
		}
	;

Statement 	: 	LBRACE  RBRACE {
				struct node* head = mem_alloc("{}\n", _DEF_);
				struct node* tail = head;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
			}
	|	LBRACE Recstmt RBRACE {
				struct node* head = mem_alloc("{\n", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("}\n", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
			}
	| 	SYSTEM DOT Identifier DOT Identifier LPAR Expression RPAR SEMICOLON {
				if (strcmp("out", $3->head->data) != 0) {
					yyerror(" ");
				}
				if (strcmp("println", $5->head->data) != 0) {
					yyerror(" ");
				}
				struct node* head = mem_alloc("System.out.println(", _DEF_);
				struct node* tail = head;
				tail->next = ($7)->head;
				tail = ($7)->tail;
				tail->next = mem_alloc(");\n", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($3));
				mem_nt_free(&($5));
				mem_nt_free(&($7));
			}
	| 	Identifier EQ Expression SEMICOLON {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("=", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			tail->next = mem_alloc(";\n", _DEF_);
			tail = tail->next;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	| 	Identifier LSQB Expression RSQB EQ Expression SEMICOLON {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("[", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			tail->next = mem_alloc("] =", _DEF_);
			tail = tail->next;
			tail->next = ($6)->head;
			tail = ($6)->tail;
			tail->next = mem_alloc(";\n", _DEF_);
			tail = tail->next;
			($$)->tail = tail;
			mem_nt_free(&($3));
			mem_nt_free(&($6));
		}
	| 	IF LPAR Expression RPAR Statement ELSE Statement  {
				struct node* head = mem_alloc("if (", _DEF_);
				struct node* tail = head;
				tail->next = ($3)->head;
				tail = ($3)->tail;
				tail->next = mem_alloc(")", _DEF_);
				tail = tail->next;
				tail->next = ($5)->head;
				tail = ($5)->tail;
				tail->next = mem_alloc("else", _DEF_);
				tail = tail->next;
				tail->next = ($7)->head;
				tail = ($7)->tail;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($3));
				mem_nt_free(&($5));
				mem_nt_free(&($7));
			}
	| 	IF LPAR Expression RPAR Statement 		%prec then  {
				struct node* head = mem_alloc("if (", _DEF_);
				struct node* tail = head;
				tail->next = ($3)->head;
				tail = ($3)->tail;
				tail->next = mem_alloc(")", _DEF_);
				tail = tail->next;
				tail->next = ($5)->head;
				tail = ($5)->tail;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($3));
				mem_nt_free(&($5));
			}
	| 	WHILE LPAR Expression RPAR Statement  {
				struct node* head = mem_alloc("while (", _DEF_);
				struct node* tail = head;
				tail->next = ($3)->head;
				tail = ($3)->tail;
				tail->next = mem_alloc(")", _DEF_);
				tail = tail->next;
				tail->next = ($5)->head;
				tail = ($5)->tail;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($3));
				mem_nt_free(&($5));
			}
	| 	Identifier LPAR RPAR SEMICOLON {
			struct macro_id* temp = find_macro($1->head->data);
			if (temp == NULL || temp->type != 1 || temp->idPtr->next != NULL) {
				yyerror(" ");
			} else {
				$$ = copy_list(temp->exp);
				mem_nt_free(&($1));
			}
		}
	| 	Identifier LPAR Exp RPAR SEMICOLON {
			struct macro_id* temp = find_macro($1->head->data);	//find the identifier
			if (temp == NULL || temp->type != 1) {
				yyerror(" ");
			} else {
				struct non_term* rep = replace_macro(temp, $3->head);
				nt_nt_memalloc(&($$), rep);
				mem_nt_free(&($1));
				mem_nt_free(&($3));
			}
			
		}
	;

Expression 	: 	PrimaryExpression AND PrimaryExpression {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("&&", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}	
	| 	PrimaryExpression OR PrimaryExpression {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("||", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	| 	PrimaryExpression NEQ PrimaryExpression {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("!=", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	| 	PrimaryExpression LTEQ PrimaryExpression {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("<=", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	| 	PrimaryExpression ADD PrimaryExpression {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("+", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	| 	PrimaryExpression SUB PrimaryExpression {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("-", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	| 	PrimaryExpression MULT PrimaryExpression {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("*", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	| 	PrimaryExpression DIV PrimaryExpression {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("/", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	| 	PrimaryExpression LSQB PrimaryExpression RSQB {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc("[", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			tail->next = mem_alloc("]", _DEF_);
			tail = tail->next;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	| 	PrimaryExpression DOT Identifier {
			if (strcmp("length",$3->head->data) != 0) {
				yyerror(" ");
			}
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc(".length", _DEF_);
			($$)->tail = tail->next;
			mem_nt_free(&($3));
		}
	| 	PrimaryExpression	%prec with_  {
			nt_nt_memalloc(&($$), $1);
		}
	|	PrimaryExpression DOT Identifier LPAR RPAR {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = mem_alloc(".", _DEF_);
			tail = tail->next;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			tail->next = mem_alloc("()", _DEF_);
			tail = tail->next;
			($$)->tail = tail;
			mem_nt_free(&($3));
		}
	|	PrimaryExpression DOT Identifier LPAR Exp RPAR {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = mem_alloc(".", _DEF_);
			($$)->tail->next = tail;
			tail->next = ($3)->head;
			tail = ($3)->tail;
			tail->next = mem_alloc("(", _LPAR_);
			tail = tail->next;
			tail->next = ($5)->head;
			tail = ($5)->tail;
			tail->next = mem_alloc(")", _RPAR_);
			tail = tail->next;
			($$)->tail = tail;
			mem_nt_free(&($3));
			mem_nt_free(&($5));
		}
	|	Identifier LPAR RPAR {
			struct macro_id* temp = find_macro($1->head->data);
			if (temp == NULL || temp->type != 0 || temp->idPtr->next != NULL ) {
				yyerror(" ");
			} else {
				$$ = copy_list(temp->exp);
				mem_nt_free(&($1));
			}
		}
	|	Identifier LPAR Exp RPAR {
			struct macro_id* temp = find_macro($1->head->data);	//find the identifier
			if (temp == NULL || temp->type != 0 ) {
				yyerror(" ");
			} else {
				struct non_term* rep = replace_macro(temp, $3->head);
				nt_nt_memalloc(&($$), rep);
				mem_nt_free(&($1));
				mem_nt_free(&($3));
			}
		}
	;

Exp: Expression {
				nt_nt_memalloc(&($$), $1);
			}
	| Exp COMMA Expression {
				nt_nt_memalloc(&($$), $1);
				struct node *tail = mem_alloc(",", _COMMA_);
				($$)->tail->next = tail;
				tail->next = ($3)->head;
				tail = ($3)->tail;
				($$)->tail = tail;
				mem_nt_free(&($3));
			}
	;

PrimaryExpression 	: 	Integer {
				nt_nt_memalloc(&($$), $1);
			}
	| 	TRUE {
				struct node* head = mem_alloc("true", _DEF_);
				struct node* tail = head;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
			}
	| 	FALSE {
				struct node* head = mem_alloc("false", _DEF_);
				struct node* tail = head;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
			}
	| 	Identifier {
				nt_nt_memalloc(&($$), $1);
			}
	| 	THIS {
				struct node* head = mem_alloc("this", _DEF_);
				struct node* tail = head;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
			}
	| 	NEW INT LSQB Expression RSQB {
				struct node* head = mem_alloc("new int [", _DEF_);
				struct node* tail = head;
				tail->next = ($4)->head;
				tail = ($4)->tail;
				tail->next = mem_alloc("]", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($4));
			}
	| 	NEW Identifier LPAR RPAR {
				struct node* head = mem_alloc("new", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc("()", _DEF_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
			}
	| 	EXCLAM Expression {
				struct node* head = mem_alloc("!", _DEF_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
			}
	| 	LPAR Expression RPAR {
				struct node* head = mem_alloc("(", _LPAR_);
				struct node* tail = head;
				tail->next = ($2)->head;
				tail = ($2)->tail;
				tail->next = mem_alloc(")", _RPAR_);
				tail = tail->next;
				$$ = (struct non_term*) malloc(sizeof(struct non_term));
				$$->head = head;
				$$->tail = tail;
				mem_nt_free(&($2));
			}
	;

MacroDefinition 	: 	MacroDefExpression ;
	| 	MacroDefStatement 
	;

MacroDefStatement 	: 	HASHDEF Identifier LPAR  RPAR Statement {
			if ($5->head->next != NULL) {
				struct node* n_t = $5->head;
				$5->head = n_t->next;
				free(n_t);
				struct node *t_curr = $5->head;
				while(t_curr->next != $5->tail) {
					t_curr = t_curr->next;
				}
				$5->tail = t_curr;
				free(t_curr->next);
			}
			error_if_exist($2->head->data);
			struct macro_id* temp = (struct macro_id*) malloc(sizeof(struct macro_id));
			temp->type = 1;
			temp->idPtr = $2->head;
			$2->tail->next = NULL;
			temp->exp = $5;
			$5->tail->next = NULL;
			temp->next = macro_list_head->next;
			macro_list_head->next = temp;
			mem_nt_free(&($2));
		}
	| HASHDEF Identifier LPAR Idmac RPAR  Statement {
			if ($6->head->next != NULL) {
				struct node* n_t = $6->head;
				$6->head = n_t->next;
				free(n_t);
				struct node *t_curr = $6->head;
				while(t_curr->next != $6->tail) {
					t_curr = t_curr->next;
				}
				$6->tail = t_curr;
				free(t_curr->next);
			}
			error_if_exist($2->head->data);
			struct macro_id* temp = (struct macro_id*) malloc(sizeof(struct macro_id));
			temp->type = 1;
			temp->idPtr = $2->head;
			$2->tail->next = $4->head;
			$4->tail->next = NULL;
			temp->exp = $6;
			$6->tail->next = NULL;
			temp->next = macro_list_head->next;
			macro_list_head->next = temp;
			mem_nt_free(&($2));
			mem_nt_free(&($4));
		}
	;

Idmac: Identifier {
			nt_nt_memalloc(&($$), $1);
		}
	| Idmac COMMA Identifier {
			nt_nt_memalloc(&($$), $1);
			struct node* tail = ($$)->tail;
			tail->next = ($3)->head;
			($$)->tail = ($3)->tail;
			mem_nt_free(&($3));
		}
	;

MacroDefExpression 	: 	HASHDEF Identifier LPAR  RPAR LPAR Expression RPAR {
			error_if_exist($2->head->data);
			struct macro_id* temp = (struct macro_id*) malloc(sizeof(struct macro_id));
			temp->type = 0;
			temp->idPtr = $2->head;
			$2->tail->next = NULL;
			temp->exp = (struct non_term*) malloc(sizeof(struct non_term));
			temp->exp->head = mem_alloc("(", _LPAR_);
			temp->exp->head->next = $6->head;
			$6->tail->next = mem_alloc(")", _RPAR_);
			temp->exp->tail = $6->tail->next;
			temp->next = macro_list_head->next;
			macro_list_head->next = temp;
			mem_nt_free(&($2));
			mem_nt_free(&($6));
		}
	| HASHDEF Identifier LPAR Idmac RPAR LPAR Expression RPAR {
			error_if_exist($2->head->data);
			struct macro_id* temp = (struct macro_id*) malloc(sizeof(struct macro_id));
			temp->type = 0;	//expression type
			temp->idPtr = $2->head;
			$2->tail->next = $4->head;
			$4->tail->next = NULL;
			temp->exp = (struct non_term*) malloc(sizeof(struct non_term));
			temp->exp->head = mem_alloc("(", _LPAR_);
			temp->exp->head->next = $7->head;
			$7->tail->next = mem_alloc(")", _RPAR_);
			temp->exp->tail = $7->tail->next;
			temp->next = macro_list_head->next;
			macro_list_head->next = temp;
			mem_nt_free(&($2));
			mem_nt_free(&($4));
			mem_nt_free(&($7));
		}
	;

Identifier 	: 	ID  {
						nt_t_memalloc(&($$),$1);
				}
	;
Integer : INTEGER 	{   nt_t_memalloc(&($$),$1);
					}
	;

%%


static struct non_term* copy_list(struct non_term* curr) {
	struct non_term* dest = (struct non_term*) malloc(sizeof(struct non_term));
	struct node *head = curr->head;
	struct node* temp;
	dest->head = mem_alloc(head->data, _DEF_);
	dest->tail = dest->head;
	head = head->next;
	while (head != NULL) {
		dest->tail->next = mem_alloc(head->data, _DEF_);
		dest->tail = dest->tail->next;
		head = head->next;
	}
	return dest;
}

static void error_if_exist(const char *ss) {
	struct macro_id* curr = macro_list_head->next;
	while (curr != NULL) {
		if (strcmp(ss, curr->idPtr->data) == 0) {
			yyerror(" ");
		}
		curr = curr->next;
	}
}

static void nt_nt_memalloc(struct non_term **nt, struct non_term *src) {
	*nt = src;
}

static void nt_t_memalloc(struct non_term **nt, struct node *src) {
	*nt = (struct non_term*) malloc(sizeof(struct non_term));
	if (*nt == NULL) {
		puts("error memory alloc");
		exit(1);
	}
	(*nt)->head = src;
	(*nt)->tail = src;
}

struct node* mem_alloc(char *str, enum _token_type tp) {
	struct node *temp = (struct node*) malloc(sizeof(struct node));
	if (temp == NULL) {
		puts("error memory alloc");
		exit(1);
	}
	temp->data = (char*) malloc(strlen(str)+1);
	strcpy(temp->data, str);
	temp->type = tp;
	temp->next = NULL;
	return temp;
}

static void mem_free(struct node *ptr) {
	free(ptr->data);
	free(ptr);
}

static void mem_nt_free(struct non_term** nt) {
	free(*nt);
}

static void printAll(struct non_term *root) {
	struct node* curr, *temp;
	curr = root->head;
	while (curr != NULL) {
		temp = curr;
		printf("%s ", curr->data);
		curr = curr->next;
		mem_free(temp);
	}
	puts("");
}

static struct macro_id* find_macro(char* key) {
	struct macro_id *temp = macro_list_head->next;
	while(temp != NULL) {
		if (strcmp(temp->idPtr->data, key) == 0) {
			return temp;
		}
		temp = temp->next;
	}
	return NULL;
}

static int len(struct node* head) {
	int sz = 0;
	while (head != NULL) {
		sz++;
		head = head->next;
	}
	return sz;
}


static int getIn(char *ss, struct node *args) {
	int count = 0;
	while (args != NULL) {
		if (strcmp(ss,args->data) == 0) {
			return count;
		}
		count++;
		args = args->next;
	}
	return -1;
}

static void process_exp(struct id_map* map,int size, struct node* curr) {
	if (size == 0) {
		yyerror(" ");
	}
	struct node *head;
	int index = 0;
	int par = 0;
	struct node *prev = curr;
	head = curr;
	while (curr != NULL) {
		if (curr->type == _COMMA_ && par == 0) {
			(map[index]).head = head;
			(map[index]).tail = prev;
			head = curr->next;
			prev = head;
			par = 0;
			index++;
		} else if (curr->type == _LPAR_) {
			par++;
			prev = curr;
		} else if (curr->type == _RPAR_) {
			par--;
			prev = curr;
		} else {
			prev = curr;
		}
		curr = curr->next;
	}
	if (index+1 == size) {
		(map[index]).head = head;
		(map[index]).tail = prev;
	} else {
		yyerror(" ");
	}
}

static void copy_sublist(struct node **tail, struct id_map* imap) {
	struct node *curr = imap->head;
	struct node *t_tail = *tail;
	while(curr != NULL && curr != imap->tail) {
		t_tail->next = mem_alloc(curr->data, _DEF_);
		t_tail = t_tail->next;
		curr = curr->next;
	}
	if (curr != NULL && curr == imap->tail && curr) {
		t_tail->next = mem_alloc(curr->data, _DEF_);
		t_tail = t_tail->next;
	}
	(*tail) = t_tail;
}


static struct non_term* replace_macro(struct macro_id* src, struct node* exprs) {
	struct non_term *dest = (struct non_term*) malloc(sizeof(struct non_term));
	dest->head = mem_alloc(src->exp->head->data,_DEF_);
	dest->tail = dest->head;
	struct node* tail = dest->tail;
	struct node *curr = src->exp->head->next;
	int size = len(src->idPtr->next);
	int index_id;
	struct id_map map[size];
	process_exp(map,size,exprs);	//mapping of identifier with expression passed

	while (curr != NULL) {
		if (curr->type == _ID_)	{			//type identifier
			index_id = getIn(curr->data,src->idPtr->next);
			if (index_id == -1) {							//if identifier used from other context
				tail->next = mem_alloc(curr->data,_DEF_);
				tail = tail->next;
			} else {
				copy_sublist(&tail,map+index_id);
			}
		} else {
			tail->next = mem_alloc(curr->data,_DEF_);
			tail = tail->next;
		}
		curr = curr->next;
	}
	dest->tail = tail;
	return dest;
}

int main(void) {
	macro_list_head = (struct macro_id*) malloc(sizeof(struct macro_id));
	macro_list_head->next = NULL;
	yyparse();
}