# Notes

## Parser
The parser used by jLox uses recursive descent.
The way it is structured relies on the grammar being hierarchical based on the precedence rules.
So, the least precedent operators are checked first, which then call the parsing functions for the higher precedent operators.
If there is no lower precedent operator, the parser falls through to the next lowest precedence.
Each precedent level does not call itself correctly, so where is the recursion?

### Statements
Try to add statements to the parser on your own without reading the text.
How would we proceed?
We have new rules for the grammar:
program -> statement* EOF;
statement -> printStatement | exprStatement;
exprStatement -> expr ;
printStatement -> print expr ;

previously, we only had
program -> expr

plus the rules for expression parsing.
Now, we want to add some rules to the top of the parse tree, but continue to fall through to the lower levels.
We also need to get the types to match up properly.