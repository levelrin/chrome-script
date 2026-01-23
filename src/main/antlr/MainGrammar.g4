grammar MainGrammar;

// This will create package statements for the generated Java files.
@header {package com.levelrin.antlr.generated;}

file
    : popupLogic EOF
    ;

popupLogic
    : 'On the pop-up, do the following: {' sentencesWithDom '}'
    ;

sentencesWithDom
    // openNewTab must be the last sentcne because the rest of sentences will lose its effect when the new tab is opened.
    : sentenceWithDom* openNewTab?
    ;

sentenceWithDom
    : elementById
    | elementByTagName
    | overwriteTextAndEnter
    | whenElementClicked
    | sentenceWithoutDom
    ;

sentencesWithoutDom
    : sentenceWithoutDom*
    ;

sentenceWithoutDom
    : print
    ;

print
    : 'Print ' STRING ' on the console.'
    ;

elementById
    : 'The ' NAME ' is the element with the ID ' STRING '.'
    ;

whenElementClicked
    : 'When the ' NAME ' is clicked, do the following: {' sentencesWithDom '}'
    ;

openNewTab
    : 'Open a new tab with the URL ' STRING ' and do the following: {' sentencesWithDom '}'
    ;

elementByTagName
    : 'The ' NAME ' is the first element with the tag name ' STRING '.'
    ;

overwriteTextAndEnter
    : 'Overwrite the text of ' NAME ' to ' STRING ' and press enter.'
    ;

NAME: [a-z]([a-z0-9]|'_'[a-z0-9])*;
STRING: '`' ~[`]* '`';
WS: [ \t\r\n]+ -> skip;
