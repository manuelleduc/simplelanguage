package com.oracle.truffle.sl.rev;

public interface SLShortCircuitNodeT extends SLExpressionNodeT {

    /**
     * This method is called after the left child was evaluated, but before the right child is
     * evaluated. The right child is only evaluated when the return value is {code true}.
     */
    boolean isEvaluateRight(boolean leftValue);

    /**
     * Calculates the result of the short circuit operation. If the right node is not evaluated then
     * <code>false</code> is provided.
     */
    boolean execute(boolean leftValue, boolean rightValue);
}
