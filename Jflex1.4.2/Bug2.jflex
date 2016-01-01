%%
%class Test
%{
%}

foo="<foo>"|":"
bar=([:letter:])+

%%

{bar}/{foo} { return 1; }
(.) { return 0; }