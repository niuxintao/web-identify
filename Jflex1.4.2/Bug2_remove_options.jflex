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
%class Test
%{
%}

foo="<foo>"|":"
bar=([:letter:])+

%%

{bar} { return 1; }
(.) { return 0; }