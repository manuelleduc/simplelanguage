package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.sl.nodes.SLStatementNode;
import com.oracle.truffle.sl.nodes.controlflow.SLBlockNode;

import java.util.HashMap;
import java.util.Map;

public class MySLBlockNodeT implements SLBlockNodeT {
    private final static Map<SLBlockNode, SLBlockNodeT> cache = new HashMap<>();
    private final ExecSLRevisitor alg;
    private final SLBlockNode it;

    private MySLBlockNodeT(ExecSLRevisitor alg, SLBlockNode it) {
        this.alg = alg;
        this.it = it;
    }

    public static SLBlockNodeT INSTANCE(ExecSLRevisitor alg, SLBlockNode it) {
        if (!cache.containsKey(it)) cache.put(it, new MySLBlockNodeT(alg, it));
        return cache.get(it);
    }

    /**
     * Execute all child statements. The annotation {@link ExplodeLoop} triggers full unrolling of
     * the loop during compilation. This allows the {@link SLStatementNode#executeVoid} method of
     * all children to be inlined.
     */
    @Override
    @ExplodeLoop
    public void executeVoid(VirtualFrame frame) {
        /*
         * This assertion illustrates that the array length is really a constant during compilation.
         */
        CompilerAsserts.compilationConstant(it.getBodyNodes().length);

        for (SLStatementNode statement : it.getBodyNodes()) {
            alg.$(statement).executeVoid(frame);
        }
    }
}
