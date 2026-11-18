package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

class Environment {
    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<String,Object>();

    Environment() {
        this.enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    Environment(Map<String,Object> values, Environment enclosing) {
        this.values.putAll(values);
        this.enclosing = enclosing;
    }

    // deep copy values
    private Environment copy(Environment another) {
        Map<String, Object> values = new HashMap<String, Object>();
        values.putAll(another.values);
        Environment encl = another.enclosing != null ? copy(another.enclosing) : null;
        return new Environment(values, encl);
    }

    public Environment copy() {
        return copy(this);
    }

    void assign(Token name, Object val) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, val);
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, val);
            return;
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    void define(String name, Object val) {
        values.put(name, val);
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            Object value = values.get(name.lexeme);
            if (value == null) throw new RuntimeError(name, "Uninitialized variable '" + name.lexeme + "'.");
            return value;
        }

        // Check enclosing scope if variable can't be found
        if (enclosing != null) return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    public String toString(int depth) {
        String offset = "";
        for (int j = 0; j < depth; j++) {
            offset += "\t";
        }
        String ret = offset + "<env " + depth + ">\n";
        for(String key: values.keySet()) {
            ret += offset + " " + key + " -> "+ values.get(key).toString() + "\n";
        }
        ret += offset + "<env " + depth + ">";
        if (enclosing != null) ret += "\n" + enclosing.toString(depth+1);

        return ret;
    }
}