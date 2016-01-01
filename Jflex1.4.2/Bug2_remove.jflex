%%
%class Test
%{
%}

foo="<foo>"|":"
bar=([:letter:])+

%%

{bar} { return 1; }
(.) { return 0; }