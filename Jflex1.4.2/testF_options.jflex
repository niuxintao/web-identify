table
16bit
yyeof
notunix
%column
%line
%char
%caseless
%cup
%apiprivate
%public


%%


%class Scanner
%standalone

%function nextToken
%type Foo




/* main character classes */
LineTerminator = \r|\n|\r\n

WhiteSpace = [ \t\f]

/* identifiers */
Identifier = [:jletter:][:jletterdigit:]*

%%

<YYINITIAL> {

/* keywords */
"double" { System.out.println(yytext()); }
"int" { System.out.println(yytext()); }

/* end of line */
{LineTerminator} { System.out.println("LINE TERMINATOR"); }

/* whitespace */
{WhiteSpace} { /* ignore */ }

/* identifiers */
{Identifier} { System.out.println("identifier: "+ yytext()); }
}
