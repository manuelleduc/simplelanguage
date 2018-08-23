package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.builtins.SLNewObjectBuiltin;

import java.util.concurrent.locks.Lock;

public class MySLNewObjectBuiltinT extends MySLBuiltinNodeT implements SLNewObjectBuiltinT {
    private final SLNewObjectBuiltin it;
    @CompilerDirectives.CompilationFinal
    private int state_;


    public MySLNewObjectBuiltinT(ExecSLRevisitor execSLRevisitor, SLNewObjectBuiltin it) {
        super(it);
        this.it = it;
    }

    @Override
    public Object execute(VirtualFrame frameValue) {
        int state = state_;
        if (state != 0 /* is-active newObject() */) {
            return newObject();
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize();
    }

    private Object executeAndSpecialize() {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        try {
            this.state_ = state = state | 0b1 /* add-active newObject() */;
            lock.unlock();
            hasLock = false;
            return newObject();
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    @Specialization
    public Object newObject() {
        if (it.getSLContext() != it.getContext()) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            it.setSLContext(it.getContext());
        }
        return it.getSLContext().createObject();
    }
}
