package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.sl.builtins.SLIsExecutableBuiltin;

import java.util.concurrent.locks.Lock;

public class MySLIsExecutableBuiltinT extends MySLBuiltinNodeT implements SLIsExecutableBuiltinT {
    private final SLIsExecutableBuiltin it;
    private final SLExpressionNodeT arguments0_;
    @CompilerDirectives.CompilationFinal
    private int state_;


    public MySLIsExecutableBuiltinT(ExecSLRevisitor execSLRevisitor, SLIsExecutableBuiltin it) {
        super(it);
        this.it = it;
        arguments0_ = execSLRevisitor.$(it.getArguments0());
    }

    @Override
    public Object execute(VirtualFrame frameValue) {
        int state = state_;
        Object arguments0Value_ = this.arguments0_.executeGeneric(frameValue);
        if (state != 0 /* is-active isExecutable(TruffleObject) */ && arguments0Value_ instanceof TruffleObject) {
            TruffleObject arguments0Value__ = (TruffleObject) arguments0Value_;
            return isExecutable(arguments0Value__);
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
                this.state_ = state = state | 0b1 /* add-active isExecutable(TruffleObject) */;
                lock.unlock();
                hasLock = false;
                return isExecutable(arguments0Value_);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new UnsupportedSpecializationException(it, new Node[]{it.getArguments0()}, arguments0Value);
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    @Specialization
    public Object isExecutable(TruffleObject obj) {
        return ForeignAccess.sendIsExecutable(it.getIsExecutable(), obj);
    }
}
