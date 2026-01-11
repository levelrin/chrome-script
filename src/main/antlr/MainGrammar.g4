grammar MainGrammar;

// This will create package statements for the generated Java files.
@header {package com.levelrin.antlr.generated;}

greeting
    : HELLO WORLD
    ;

HELLO: 'Hello';
WORLD: 'World';
WS: [ \t\r\n]+ -> skip;
