package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;

public interface SLStringLiteralNodeT extends SLExpressionNodeT {

    @Override
    String executeGeneric(VirtualFrame frame);
}
