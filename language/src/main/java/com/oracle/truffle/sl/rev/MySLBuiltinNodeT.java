package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.SLLanguage;
import com.oracle.truffle.sl.runtime.SLContext;

public abstract class MySLBuiltinNodeT implements SLBuiltinNodeT {

    private final Node it;

    public MySLBuiltinNodeT(Node it) {
        this.it = it;
    }

    @Override
    public final Object executeGeneric(VirtualFrame frame) {
        try {
            return execute(frame);
        } catch (UnsupportedSpecializationException e) {
            throw SLException.typeError(e.getNode(), e.getSuppliedValues());
        }
    }

    public final SLContext getContext() {
        return it.getRootNode().getLanguage(SLLanguage.class).getContextReference().get();
    }
}
