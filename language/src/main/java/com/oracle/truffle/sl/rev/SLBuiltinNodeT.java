package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;

public interface SLBuiltinNodeT extends SLExpressionNodeT {
    Object execute(VirtualFrame frame);

}
