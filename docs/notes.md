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

### Closures and Dynamic Scope Leak
How to fix closures so they aren't shadowed by block they are executed in instead of using the value of the variable they were lexicall
defined in? E.g. how to get
```
var a = "outer";
{ fun showA() { print a; } showA(); var a = "block"; showA(); }
```

to print "global" twice?
Currently, the a -> "global" in the global scope.

Try deep copying the environment into closure instead; this seems to work, but figure out why.
Also an issue with the copying: it is adding an additional empty environment on top of the stack; fix this.
This is not a good solution in general; will lead to lots of duplicate environments, one for every function definition.
Many functions could share the same copy if they are defined in the same scope.
How could that be done?