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
public static final int WORD = 1;
public static final int WS = 2;
public static final int EOF1 = 3;
public static final int EOF2 = 4;
%}
%unicode
%xstate STATE2
WORD=[a-zA-Z]+
WS=[ \t\n\r]+
%%
<YYINITIAL,STATE2>
{
{WORD}
{
yybegin(STATE2);
return WORD;
}
{WS}
{
yybegin(YYINITIAL);
return WS;
}
}
<YYINITIAL>
{
<<EOF>>
{
return EOF1;
}
}
<STATE2>
{
<<EOF>>
{
return EOF2;
}
}
