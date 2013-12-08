/**
 * Recovery
 */
package com.mod_snmp.SmiParser.ErrorHandler;

public class Recovery {
    private static final int default_stack_size = 70;
    private RecoveryItem stack[];
    private int top;

    public Recovery(int heapsize) {
        stack = new RecoveryItem[ heapsize ];
        top = 0;
    }
    public Recovery() {
        this(default_stack_size);
    }
    public Recovery(int items[]) {
        this(default_stack_size);
        for (int i = 0; (i < default_stack_size) && (i < items.length); i++) {
            push(items[i]);
        }
    }
    public boolean empty() {
        if (top == 0) {
            return true;
        }
        return false;
    }
    public RecoveryItem pop() {
        if (top > 0) {
            return stack[--top];
        }
        return null;
    }
    public RecoveryItem pop(int nr) {
        while (--nr > 0) {
            pop();
        }
        return pop();
    }
    public void push(RecoveryItem item) {
        if (top < stack.length) {
            stack[ top++ ] = item;
        } else {
        }
    }
    public void push(int first) {
        if (top < stack.length) {
            stack[ top++ ] = new RecoveryItem(first);
        }
    }
    public boolean contains(int first, int second) {
        for (int i = 0; i < top; i++) {
            if (stack[i].has(first, second)) {
                return true;
            }
        }
        return false;
    }
    public boolean contains(int first) {
        for (int i = 0; i < top; i++) {
            if (stack[i].has(first)) {
                return true;
            }
        }
        return false;
    }
}
