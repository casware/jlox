# Error Productions
*Desiderata:* *Add error productions to handle each binary operator appearing without a left-hand operand. In other words, detect a binary operator appearing at the beginning of an expression. Report that as an error, but also parse and discard a right-hand operand with the appropriate precedence.*

## Design
How do we do this?
Do we add a new rule to the grammar, or do we modify the existing rule for operators?
Binary operators are +,*,/,"," if we include the comma operator.

### New Rules Approach
We could add new error rules such as:
term -> + factor
factor -> * unary
factor -> / unary
expression -> ,expression

How would we incorporate this into the hierarchy we have set up of
expression -> comma -> equality -> comparison -> term -> factor -> unary -> primary

Maybe we need to add them as fallthroughs for each expression parsing function;
do we need to do this before or after getting the left operand?
If we do it after w/o changing the current implementation in any way, we will fall all the way through to primary and then throw an error because we will not match any of the primary rules.
So we either need to add it before, or add a condition to primary to consume the erroring tokens and then continue parsing.
Can we report errors without aborting parsing?
Yes, we can just use the error function but not throw the exception it returns.
Add to the unary rule and use primary() to consume the next token.
Then call comma() to return to the top of the parse tree.
However, this won't actually result in an AST being created, since factor() is expecting a unary to be returned.
This is ok, since we are in an error state.