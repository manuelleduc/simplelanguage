package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameInstance;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.builtins.SLHelloEqualsWorldBuiltin;

import java.util.concurrent.locks.Lock;

public class MySLHelloEqualsWorldBuiltinT extends MySLBuiltinNodeT implements SLHelloEqualsWorldBuiltinT {
    private final SLHelloEqualsWorldBuiltin it;
    @CompilerDirectives.CompilationFinal
    private int state_;


    public MySLHelloEqualsWorldBuiltinT(ExecSLRevisitor execSLRevisitor, SLHelloEqualsWorldBuiltin it) {
        super(it);
        this.it = it;
    }

    @Override
    public Object execute(VirtualFrame frameValue) {
        int state = state_;
        if (state != 0 /* is-active change() */) {
            return change();
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize();
    }

    private String executeAndSpecialize() {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        try {
            this.state_ = state = state | 0b1 /* add-active change() */;
            lock.unlock();
            hasLock = false;
            return change();
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    @Specialization
    @CompilerDirectives.TruffleBoundary
    public String change() {
        FrameInstance frameInstance = Truffle.getRuntime().getCallerFrame();
        Frame frame = frameInstance.getFrame(FrameInstance.FrameAccess.READ_WRITE);
        FrameSlot slot = frame.getFrameDescriptor().findOrAddFrameSlot("hello");
        frame.setObject(slot, "world");
        return "world";
    }

}
