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
%{
public static final int WORD1 = 1;
public static final int WORD2 = 2;
public static final int WS = 3;
public static final int EOF = 4;
%}
%unicode
%xstate STATE2
WORD=[a-zA-Z]+
WS=[ \t\n\r]+
%%
<YYINITIAL,STATE2>
{
{WS}
{
return WS;
}
<<EOF>>
{
return EOF;
}
}
<YYINITIAL>
{
{WORD}
{
yybegin(STATE2);
return WORD1;
}
}
<STATE2>
{
{WORD}
{
yybegin(YYINITIAL);
return WORD2;
}
}
