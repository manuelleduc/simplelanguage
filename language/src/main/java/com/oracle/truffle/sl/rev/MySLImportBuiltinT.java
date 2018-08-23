package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.sl.builtins.SLImportBuiltin;
import com.oracle.truffle.sl.runtime.SLNull;

import java.util.concurrent.locks.Lock;

public class MySLImportBuiltinT extends MySLBuiltinNodeT implements SLImportBuiltinT {
    private final SLImportBuiltin it;
    private final SLExpressionNodeT arguments0_;
    @CompilerDirectives.CompilationFinal
    private int state_;


    public MySLImportBuiltinT(ExecSLRevisitor execSLRevisitor, SLImportBuiltin it) {
        super(it);
        this.it = it;
        arguments0_ = execSLRevisitor.$(it.getArgument0());
    }

    @Override
    public Object execute(VirtualFrame frameValue) {
        int state = state_;
        Object arguments0Value_ = this.arguments0_.executeGeneric(frameValue);
        if (state != 0 /* is-active importSymbol(String) */ && arguments0Value_ instanceof String) {
            String arguments0Value__ = (String) arguments0Value_;
            return importSymbol(arguments0Value__);
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
            if (arguments0Value instanceof String) {
                String arguments0Value_ = (String) arguments0Value;
                this.state_ = state = state | 0b1 /* add-active importSymbol(String) */;
                lock.unlock();
                hasLock = false;
                return importSymbol(arguments0Value_);
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new UnsupportedSpecializationException(it, new Node[]{it.getArgument0()}, arguments0Value);
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    @Specialization
    public Object importSymbol(String name) {
        try {
            return ForeignAccess.sendRead(it.getReadNode(), it.getContext().getPolyglotBindings(), name);
        } catch (UnknownIdentifierException e) {
            return SLNull.SINGLETON;
        } catch (UnsupportedMessageException e) {
            // polyglot bindings should always support reading
            CompilerDirectives.transferToInterpreter();
            throw new AssertionError(e);
        }
    }
}
