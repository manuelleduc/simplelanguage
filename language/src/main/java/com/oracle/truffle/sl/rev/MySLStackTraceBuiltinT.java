package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.*;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.sl.builtins.SLStackTraceBuiltin;

import java.util.concurrent.locks.Lock;

public class MySLStackTraceBuiltinT extends MySLBuiltinNodeT implements SLStackTraceBuiltinT {
    private final SLStackTraceBuiltin it;

    public MySLStackTraceBuiltinT(ExecSLRevisitor execSLRevisitor, SLStackTraceBuiltin it) {
        super(it);
        this.it = it;
    }

    @CompilerDirectives.CompilationFinal
    private int state_;

    @Override
    public Object execute(VirtualFrame frameValue) {
        int state = state_;
        if (state != 0 /* is-active trace() */) {
            return trace();
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
            this.state_ = state = state | 0b1 /* add-active trace() */;
            lock.unlock();
            hasLock = false;
            return trace();
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    @Specialization
    public String trace() {
        return createStackTrace();
    }

    @CompilerDirectives.TruffleBoundary
    private static String createStackTrace() {
        final StringBuilder str = new StringBuilder();

        Truffle.getRuntime().iterateFrames(new FrameInstanceVisitor<Integer>() {
            private int skip = 1; // skip stack trace builtin

            @Override
            public Integer visitFrame(FrameInstance frameInstance) {
                if (skip > 0) {
                    skip--;
                    return null;
                }
                CallTarget callTarget = frameInstance.getCallTarget();
                Frame frame = frameInstance.getFrame(FrameInstance.FrameAccess.READ_ONLY);
                RootNode rn = ((RootCallTarget) callTarget).getRootNode();
                // ignore internal or interop stack frames
                if (rn.getLanguageInfo() == null) {
                    return 1;
                }
                if (str.length() > 0) {
                    str.append(System.getProperty("line.separator"));
                }
                str.append("Frame: ").append(rn.toString());
                FrameDescriptor frameDescriptor = frame.getFrameDescriptor();
                for (FrameSlot s : frameDescriptor.getSlots()) {
                    str.append(", ").append(s.getIdentifier()).append("=").append(frame.getValue(s));
                }
                return null;
            }
        });
        return str.toString();
    }
}
