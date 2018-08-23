package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.builtins.SLNanoTimeBuiltin;

import java.util.concurrent.locks.Lock;

public class MySLNanoTimeBuiltinT extends MySLBuiltinNodeT implements SLNanoTimeBuiltinT {
    private final SLNanoTimeBuiltin it;

    public MySLNanoTimeBuiltinT(ExecSLRevisitor execSLRevisitor, SLNanoTimeBuiltin it) {
        super(it);
        this.it = it;
    }

    @CompilerDirectives.CompilationFinal
    private int state_;


    @Override
    public Object execute(VirtualFrame frameValue) {
        int state = state_;
        if (state != 0 /* is-active nanoTime() */) {
            return nanoTime();
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize();
    }

    private long executeAndSpecialize() {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        try {
            this.state_ = state = state | 0b1 /* add-active nanoTime() */;
            lock.unlock();
            hasLock = false;
            return nanoTime();
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    @Specialization
    public long nanoTime() {
        return System.nanoTime();
    }
}
