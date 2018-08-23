package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.sl.SLLanguage;
import com.oracle.truffle.sl.builtins.SLDefineFunctionBuiltin;

import java.util.concurrent.locks.Lock;

public class MySLDefineFunctionBuiltinT extends MySLBuiltinNodeT implements SLDefineFunctionBuiltinT {
    private final SLDefineFunctionBuiltin it;
    private final SLExpressionNodeT arguments0_;
    @CompilerDirectives.CompilationFinal
    private int state_;


    public MySLDefineFunctionBuiltinT(ExecSLRevisitor execSLRevisitor, SLDefineFunctionBuiltin it) {
        super(it);
        this.it = it;
        arguments0_ = execSLRevisitor.$(it.getArgument0());
    }

    @Override
    public Object execute(VirtualFrame frameValue) {
        int state = state_;
        Object arguments0Value_ = this.arguments0_.executeGeneric(frameValue);
        if (state != 0 /* is-active defineFunction(String) */ && arguments0Value_ instanceof String) {
            String arguments0Value__ = (String) arguments0Value_;
            return defineFunction(arguments0Value__);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(arguments0Value_);
    }

    private String executeAndSpecialize(Object arguments0Value) {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        try {
            if (arguments0Value instanceof String) {
                String arguments0Value_ = (String) arguments0Value;
                this.state_ = state = state | 0b1 /* add-active defineFunction(String) */;
                lock.unlock();
                hasLock = false;
                return defineFunction(arguments0Value_);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new UnsupportedSpecializationException(it, new Node[]{it.getArgument0()}, arguments0Value);
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
    }


    @CompilerDirectives.TruffleBoundary
    @Specialization
    public String defineFunction(String code) {
        // @formatter:off
        Source source = Source.newBuilder(code).
                name("[defineFunction]").
                language(SLLanguage.ID).
                build();
        // @formatter:on
        /* The same parsing code as for parsing the initial source. */
        it.getContext().getFunctionRegistry().register(source);

        return code;
    }
}
