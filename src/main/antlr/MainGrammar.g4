grammar MainGrammar;

// This will create package statements for the generated Java files.
@header {package com.levelrin.antlr.generated;}

file
    : sentences EOF
    ;

sentences
    : sentence*
    ;

sentence
    : print
    | elementById
    | whenElementClicked
    ;

print
    : 'Print ' STRING ' on the console.'
    ;

elementById
    : 'The ' NAME ' is the element with the ID ' STRING '.'
    ;

whenElementClicked
    : 'When the ' NAME ' is clicked, do the following: {' sentences '}'
    ;

NAME: [a-z]([a-z0-9]|'_'[a-z0-9])*;
STRING: '`' ~[`]* '`';
WS: [ \t\r\n]+ -> skip;
