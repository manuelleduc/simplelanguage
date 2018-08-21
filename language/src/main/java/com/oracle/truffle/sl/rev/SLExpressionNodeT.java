package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.nodes.SLStatementNode;
import com.oracle.truffle.sl.nodes.SLTypes;
import com.oracle.truffle.sl.nodes.SLTypesGen;

@TypeSystemReference(SLTypes.class)
public interface SLExpressionNodeT extends SLStatementNodeT {

    /**
     * The execute method when no specialization is possible. This is the most general case,
     * therefore it must be provided by all subclasses.
     */
    Object executeGeneric(VirtualFrame frame);

    /**
     * When we use an expression at places where a {@link SLStatementNode statement} is already
     * sufficient, the return value is just discarded.
     */
    @Override
    default void executeVoid(VirtualFrame frame) {
        this.executeGeneric(frame);
    }

    /*
     * Execute methods for specialized types. They all follow the same pattern: they call the
     * generic execution method and then expect a result of their return type. Type-specialized
     * subclasses overwrite the appropriate methods.
     */

    default long executeLong(VirtualFrame frame) throws UnexpectedResultException {
        return SLTypesGen.expectLong(executeGeneric(frame));
    }

    default boolean executeBoolean(VirtualFrame frame) throws UnexpectedResultException {
        return SLTypesGen.expectBoolean(executeGeneric(frame));
    }


}
