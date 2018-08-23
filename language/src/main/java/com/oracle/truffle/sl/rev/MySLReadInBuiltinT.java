package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.builtins.SLReadlnBuiltin;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.locks.Lock;

public class MySLReadInBuiltinT extends MySLBuiltinNodeT implements SLReadlnBuiltinT {
    private final SLReadlnBuiltin it;

    public MySLReadInBuiltinT(ExecSLRevisitor execSLRevisitor, SLReadlnBuiltin it) {
        super(it);
        this.it = it;
    }


    @CompilerDirectives.CompilationFinal
    private int state_;

    @Override
    public Object execute(VirtualFrame frameValue) {
        int state = state_;
        if (state != 0 /* is-active readln() */) {
            return readln();
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
            this.state_ = state = state | 0b1 /* add-active readln() */;
            lock.unlock();
            hasLock = false;
            return readln();
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    @Specialization
    public String readln() {
        String result = doRead(it.getContext().getInput());
        if (result == null) {
            /*
             * We do not have a sophisticated end of file handling, so returning an empty string is
             * a reasonable alternative. Note that the Java null value should never be used, since
             * it can interfere with the specialization logic in generated source code.
             */
            result = "";
        }
        return result;
    }


    @CompilerDirectives.TruffleBoundary
    private String doRead(BufferedReader in) {
        try {
            return in.readLine();
        } catch (IOException ex) {
            throw new SLException(ex.getMessage(), it);
        }
    }
}
