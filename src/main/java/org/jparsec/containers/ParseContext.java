package org.jparsec.containers;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class ParseContext {
    public int line;
    public int column;
    public int index;
    public String content;
    public String indentPattern;
    public int indentLevel;
    public Deque<String> traceErrors = new LinkedList<>();
    public Deque<String> traceErrorsSnapshot = new LinkedList<>();
    public List<String> allErrors = new ArrayList<>();

    public ParseContext copy() {
        var nw = new ParseContext(content);
        nw.line = line;
        nw.column = column;
        nw.index = index;
        nw.indentPattern = indentPattern;
        nw.indentLevel = indentLevel;
        nw.traceErrors = traceErrors;
        nw.allErrors = allErrors;
        nw.traceErrorsSnapshot = traceErrorsSnapshot;
        return nw;
    }

    private ParseContext(String content) {
        this.content = content;
    }

    public static ParseContext of(String content) {
        return new ParseContext(content);
    }

    public void set(ParseContext ctx) {
        this.line = ctx.line;
        this.column = ctx.column;
        this.index = ctx.index;
        this.indentPattern = "  ";
        this.indentLevel = 0;
        this.traceErrors = traceErrors;
        this.traceErrorsSnapshot = traceErrorsSnapshot;
        this.allErrors = allErrors;
    }

    public void appendTrace(String msg) {
        this.traceErrors.offerFirst(wrapInLineDetails(msg));
    }

    public void clearTrace() {
        this.traceErrors.clear();
    }

    public void traceSnapshot() {
        this.traceErrorsSnapshot.clear();
        this.traceErrorsSnapshot.addAll(this.traceErrors);
    }

    public void recoverTraceSnapshot() {
        this.traceErrors.clear();
        this.traceErrors.addAll(this.traceErrorsSnapshot);
    }

    public void addVerboseError(String msg) {
        this.allErrors.add(wrapInLineDetails(msg));
    }

    private String wrapInLineDetails(String msg) {
        return String.format("Line: %d, Column: %d => %s",
                this.line, this.column, msg);
    }
}
