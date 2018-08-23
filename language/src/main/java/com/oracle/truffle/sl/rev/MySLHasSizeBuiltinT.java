package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.sl.builtins.SLHasSizeBuiltin;

import java.util.concurrent.locks.Lock;

public class MySLHasSizeBuiltinT extends MySLBuiltinNodeT implements SLHasSizeBuiltinT {
    private final SLHasSizeBuiltin it;
    private final SLExpressionNodeT arguments0_;
    @CompilerDirectives.CompilationFinal
    private int state_;


    public MySLHasSizeBuiltinT(ExecSLRevisitor execSLRevisitor, SLHasSizeBuiltin it) {
        super(it);
        this.it = it;
        arguments0_ = execSLRevisitor.$(it.getArguments0());
    }

    @Specialization
    public Object hasSize(TruffleObject obj) {
        return ForeignAccess.sendHasSize(it.getHasSize(), obj);
    }

    @Override
    public Object execute(VirtualFrame frameValue) {
        int state = state_;
        Object arguments0Value_ = this.arguments0_.executeGeneric(frameValue);
        if (state != 0 /* is-active hasSize(TruffleObject) */ && arguments0Value_ instanceof TruffleObject) {
            TruffleObject arguments0Value__ = (TruffleObject) arguments0Value_;
            return hasSize(arguments0Value__);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(arguments0Value_);
    }

    private Object executeAndSpecialize(Object arguments0Value) {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        try {
            if (arguments0Value instanceof TruffleObject) {
                TruffleObject arguments0Value_ = (TruffleObject) arguments0Value;
                this.state_ = state = state | 0b1 /* add-active hasSize(TruffleObject) */;
                lock.unlock();
                hasLock = false;
                return hasSize(arguments0Value_);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new UnsupportedSpecializationException(it, new Node[]{it.getArguments0()}, arguments0Value);
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
    }
}
