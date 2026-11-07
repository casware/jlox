statement       ->  exprStmt
                    | ifStmt
                    | printStmt
                    | whileStmt
                    | forStmt
                    | block;

forStmt         -> "for" "(" (varDecl | exprStmt | ";")
                    expression? ";"
                    expression? ")" statement ;

whleStmt        -> "while" "(" expression ")" statement ;

block           ->  "{" declaration* "}"

ifStmt          ->  "if" "(" expression ")" statement
                    ( "else" statement )? ;

expression      -> assignment ;
assignment      -> IDENTIFIER "=" assignment | logic_or ;
logic_or        -> logic_and ( "or" logic_and )* ;
logic_and       -> comma ( "and" comma )* ;
comma           -> expression "," expression