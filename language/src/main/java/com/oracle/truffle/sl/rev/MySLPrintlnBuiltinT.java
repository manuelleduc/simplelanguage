package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.builtins.SLPrintlnBuiltin;

import java.io.PrintWriter;
import java.util.concurrent.locks.Lock;

public class MySLPrintlnBuiltinT implements SLPrintlnBuiltinT {

    private final SLPrintlnBuiltin it;
    private final SLExpressionNodeT arguments0_;
    @CompilerDirectives.CompilationFinal
    private int state_;

    public MySLPrintlnBuiltinT(ExecSLRevisitor execSLRevisitor, SLPrintlnBuiltin it) {
        this.it = it;
        this.arguments0_ = execSLRevisitor.$(it.getArgument0());


    }

    @Override
    public final Object executeGeneric(VirtualFrame frame) {
        try {
            return execute(frame);
        } catch (UnsupportedSpecializationException e) {
            throw SLException.typeError(e.getNode(), e.getSuppliedValues());
        }
    }

    @Override
    public Object execute(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b1110) == 0 /* only-active println(long) */ && state != 0  /* is-not println(long) && println(boolean) && println(String) && println(Object) */) {
            return execute_long0(frameValue, state);
        } else if ((state & 0b1101) == 0 /* only-active println(boolean) */ && state != 0  /* is-not println(long) && println(boolean) && println(String) && println(Object) */) {
            return execute_boolean1(frameValue, state);
        } else {
            return execute_generic2(frameValue, state);
        }
    }

    private Object execute_long0(VirtualFrame frameValue, int state) {
        long arguments0Value_;
        try {
            arguments0Value_ = this.arguments0_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(ex.getResult());
        }
        assert (state & 0b1) != 0 /* is-active println(long) */;
        return println(arguments0Value_);
    }

    private Object execute_boolean1(VirtualFrame frameValue, int state) {
        boolean arguments0Value_;
        try {
            arguments0Value_ = this.arguments0_.executeBoolean(frameValue);
        } catch (UnexpectedResultException ex) {
            return executeAndSpecialize(ex.getResult());
        }
        assert (state & 0b10) != 0 /* is-active println(boolean) */;
        return println(arguments0Value_);
    }

    private Object execute_generic2(VirtualFrame frameValue, int state) {
        Object arguments0Value_ = this.arguments0_.executeGeneric(frameValue);
        if ((state & 0b1) != 0 /* is-active println(long) */ && arguments0Value_ instanceof Long) {
            long arguments0Value__ = (long) arguments0Value_;
            return println(arguments0Value__);
        }
        if ((state & 0b10) != 0 /* is-active println(boolean) */ && arguments0Value_ instanceof Boolean) {
            boolean arguments0Value__ = (boolean) arguments0Value_;
            return println(arguments0Value__);
        }
        if ((state & 0b100) != 0 /* is-active println(String) */ && arguments0Value_ instanceof String) {
            String arguments0Value__ = (String) arguments0Value_;
            return println(arguments0Value__);
        }
        if ((state & 0b1000) != 0 /* is-active println(Object) */) {
            return println(arguments0Value_);
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(arguments0Value_);
    }

    private Object executeAndSpecialize(Object arguments0Value) {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        int oldState = state;
        try {
            if (arguments0Value instanceof Long) {
                long arguments0Value_ = (long) arguments0Value;
                this.state_ = state = state | 0b1 /* add-active println(long) */;
                lock.unlock();
                hasLock = false;
                return println(arguments0Value_);
            }
            if (arguments0Value instanceof Boolean) {
                boolean arguments0Value_ = (boolean) arguments0Value;
                this.state_ = state = state | 0b10 /* add-active println(boolean) */;
                lock.unlock();
                hasLock = false;
                return println(arguments0Value_);
            }
            if (arguments0Value instanceof String) {
                String arguments0Value_ = (String) arguments0Value;
                this.state_ = state = state | 0b100 /* add-active println(String) */;
                lock.unlock();
                hasLock = false;
                return println(arguments0Value_);
            }
            this.state_ = state = state | 0b1000 /* add-active println(Object) */;
            lock.unlock();
            hasLock = false;
            return println(arguments0Value);
        } finally {
            if (oldState != 0) {
                checkForPolymorphicSpecialize(oldState);
            }
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    private void checkForPolymorphicSpecialize(int oldState) {
        int newState = this.state_;
        if ((oldState ^ newState) != 0) {
            it.reportPolymorphicSpecialize2();
        }
    }


    @Specialization
    public long println(long value) {
        doPrint(it.getContext().getOutput(), value);
        return value;
    }

    @CompilerDirectives.TruffleBoundary
    private static void doPrint(PrintWriter out, long value) {
        out.println(value);
    }

    @Specialization
    public boolean println(boolean value) {
        doPrint(it.getContext().getOutput(), value);
        return value;
    }

    @CompilerDirectives.TruffleBoundary
    private static void doPrint(PrintWriter out, boolean value) {
        out.println(value);
    }

    @Specialization
    public String println(String value) {
        doPrint(it.getContext().getOutput(), value);
        return value;
    }

    @CompilerDirectives.TruffleBoundary
    private static void doPrint(PrintWriter out, String value) {
        out.println(value);
    }

    @Specialization
    public Object println(Object value) {
        doPrint(it.getContext().getOutput(), value);
        return value;
    }

    @CompilerDirectives.TruffleBoundary
    private static void doPrint(PrintWriter out, Object value) {
        out.println(value);
    }
}
