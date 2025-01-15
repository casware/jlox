# Notes

## Parser
The parser used by jLox uses recursive descent.
The way it is structured relies on the grammar being hierarchical based on the precedence rules.
So, the least precedent operators are checked first, which then call the parsing functions for the higher precedent operators.
If there is no lower precedent operator, the parser falls through to the next lowest precedence.
Each precedent level does not call itself correctly, so where is the recursion?
