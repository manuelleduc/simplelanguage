package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.local.SLLexicalScope;

import java.util.HashMap;
import java.util.Map;

public class MySLLexicalScopeT implements SLLexicalScopeT {
    private final static Map<SLLexicalScope, SLLexicalScopeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLLexicalScope it;

    public MySLLexicalScopeT(ExecSLRevisitor alg, SLLexicalScope it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLLexicalScopeT INSTANCE(ExecSLRevisitor alg, SLLexicalScope it) {
        if (!cache.containsKey(it)) {
            cache.put(it, new MySLLexicalScopeT(alg, it));
        }
        return cache.get(it);
    }
}
