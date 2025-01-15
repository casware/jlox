# Comma operator
Desiderata:
- comma operator should allow chaining expressions where a single expression is expected, except inside function argument lists.
- should evaluate the left operand, discard result, then evaluate right operand and return it.
- should have same precedence and associativity as in C (what associativity makes sense)? 3+1,4+1,5+1 -> 6 implies ((3+1),(4+1)),(5+1) -> left associative; expect precedence to be the lowest of all operators.

## Grammar rules
expr -> equality, expr

Use equality to avoid left recursion.
The way I have implemented it seems to be left-associative by accident.
How would it need to be modified to be right-associative?

## Scratch
3,4,5 -> 3, (4, 5)

-> (, 3 comma())
    -> (, 3 (, 4 5))

3,4,5,6
 -> (, 3 comma()) 
    -> (, 3 (, 4 comma()))
        -> (, 3 (, 4 (, 5 comma())))
            -> (, 3 (, 4 (, 5 6)))

if !match(COMMA) {
    return equality()
}
Expr left = equality();
Token operator = previous();
return new Expr.Binary(left, operator, comma())

3,4,5,6
3,4
    3, (4,5)
        3, (4, (5, 6))
Replace the right expression with the new right value;
for that to work, need a way to get the left and right values of Expr.Binary.

left = equality();
right = equality();
Expr.Binary expr = new Expr.Binary(left, COMMA, right);
while (match(COMMA)) {
    Expr new = equality();
    expr.right = new Expr.Binary(expr.right, COMMA, right);
}

Currently:
3,4,5,6,7 -> (, 3.0 (, (, (, 4.0 5.0) 6.0) 7.0))
3,4
    -> 3, (4, 5)
        -> 3, ((4,5), 6)
            -> 3, (((4,5), 6), 7)

In theory, there should be a straightforward algorithm for rewriting a recursive function as a while loop:

base case -> while condition
recursion step -> accumulator

Problem is: we need to keep getting the right values until we get a null when we
replace the value in the old expression. There is not a clean way of doing that
without another loop.

Maybe use a "last" value to keep track of it?
Still hard to see how we get the right nesting without using recursion.
fn recursive() {
    val = pre();
    if (end()) {
        return val;
    }

    return (val, recursive());
}

Hard to see even how this could be made tail recursive.
Try to make it tail recursive:
private Expr rightComma() {
    Expr left = equality();
    if (!match(COMMA)) {
        return left;
    }

    Token operator = previous();
    Expr expr = new Expr.Binary(left, operator, rightComma());
    return expr;
}
Need to build up the return value on the way down.

Or, instead of tail recursion, use a loop and then reverse the association at the end:
Expr rightComma() {
    Expr expr = equality();
    while (match(COMMA)) {
        Token operator = previous();
        Expr right = equality();
        expr = new Expr.Binary(expr, operator, right);
    }

    // Reverse the associativity
}