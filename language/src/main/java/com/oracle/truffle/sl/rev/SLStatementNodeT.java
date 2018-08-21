package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.frame.VirtualFrame;

public interface SLStatementNodeT {

    /**
     * Execute this node as as statement, where no return value is necessary.
     */
    void executeVoid(VirtualFrame frame);
}
