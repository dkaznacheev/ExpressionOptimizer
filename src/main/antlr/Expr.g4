grammar Expr;

expression:
    | <assoc = left> left = expression op = (ADD | SUB) right = expression
    | identifier
    | literal
    | '(' expression ')';

identifier: id = ID_REGEX;

literal: lit = LIT_REGEX;

LIT_REGEX: '0' | [1-9][0-9]*;

ID_REGEX: [a-zA-Z][a-zA-Z0-9_]*;

ADD: '+';
SUB: '-';

TO_SKIP: (' ' | '\t' | '\r' | '\n') -> skip;