package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.SLEvalRootNode;
import com.oracle.truffle.sl.nodes.SLRootNode;
import com.oracle.truffle.sl.nodes.SLUndefinedFunctionRootNode;
import com.oracle.truffle.sl.nodes.call.SLInvokeNode;
import com.oracle.truffle.sl.nodes.controlflow.*;
import com.oracle.truffle.sl.nodes.expression.*;
import com.oracle.truffle.sl.nodes.local.SLLexicalScope;
import com.oracle.truffle.sl.nodes.local.SLReadArgumentNode;
import com.oracle.truffle.sl.revisitor.SLRevisitor;

public interface ExecSLRevisitor extends SLRevisitor<SLAddNodeT, SLBigIntegerLiteralNodeT, SLBinaryNodeT,
        SLBlockNodeT, SLBreakExceptionT, SLBreakNodeT, SLContinueExceptionT, SLContinueNodeT, SLDebuggerNodeT,
        SLDispatchNodeT, SLDivNodeT, SLEqualNodeT, SLEvalRootNodeT, SLExpressionNodeT, SLForeignToSLTypeNodeT,
        SLFunctionBodyNodeT, SLFunctionLiteralNodeT, SLIfNodeT, SLInvokeNodeT, SLLessOrEqualNodeT, SLLessThanNodeT,
        SLLexicalScopeT, SLLogicalAndNodeT, SLLogicalNotNodeT, SLLogicalOrNodeT, SLLongLiteralNodeT, SLMulNodeT,
        SLParenExpressionNodeT, SLReadArgumentNodeT, SLReadLocalVariableNodeT,
        SLReturnExceptionT, SLReturnNodeT, SLRootNodeT,
        SLShortCircuitNodeT, SLStatementNodeT, SLStringLiteralNodeT, SLSubNodeT, SLTypesT, SLUnboxNodeT,
        SLUndefinedFunctionRootNodeT, SLWhileNodeT, SLWriteLocalVariableNodeT> {

    @Override
    default SLAddNodeT _sLAddNode(SLAddNode it) {
        return MySLAddNodeT.INSTANCE(this, it);
    }

    @Override
    default SLEvalRootNodeT _sLEvalRootNode(SLEvalRootNode it) {
        return MySLEvalRootNodeT.INSTANCE(this, it);
    }

    @Override
    default SLReturnNodeT _sLReturnNode(SLReturnNode it) {
        return MySLReturnNodeT.INSTANCE(this, it);
    }

    @Override
    default SLRootNodeT _sLRootNode(SLRootNode it) {
        return MySLRootNodeT.INSTANCE(this, it);
    }

    @Override
    default SLLexicalScopeT _sLLexicalScope(SLLexicalScope it) {
        return MySLLexicalScopeT.INSTANCE(this, it);
    }

    @Override
    default SLFunctionLiteralNodeT _sLFunctionLiteralNode(SLFunctionLiteralNode it) {
        return MySLFunctionLiteralNodeT.INSTANCE(this, it);
    }

    @Override
    default SLFunctionBodyNodeT _sLFunctionBodyNode(SLFunctionBodyNode it) {
        return MySLFunctionBodyNodeT.INSTANCE(this, it);
    }

    @Override
    default SLStringLiteralNodeT _sLStringLiteralNode(SLStringLiteralNode it) {
        return MySLStringLiteralNodeT.INSTANCE(this, it);
    }

    @Override
    default SLContinueExceptionT _sLContinueException(SLContinueException it) {
        return MySLContinueExceptionT.INSTANCE(this, it);
    }

    @Override
    default SLBreakNodeT _sLBreakNode(SLBreakNode it) {
        return MySLBreakNodeT.INSTANCE(this, it);
    }

    @Override
    default SLReturnExceptionT _sLReturnException(SLReturnException it) {
        return MySLReturnExceptionT.INSTANCE(this, it);
    }

    @Override
    default SLDebuggerNodeT _sLDebuggerNode(SLDebuggerNode it) {
        return MySLDebuggerNodeT.INSTANCE(this, it);
    }

    @Override
    default SLReadArgumentNodeT _sLReadArgumentNode(SLReadArgumentNode it) {
        return MySLReadArgumentNodeT.INSTANCE(this, it);
    }

    @Override
    default SLParenExpressionNodeT _sLParenExpressionNode(SLParenExpressionNode it) {
        return MySLParenExpressionNodeT.INSTANCE(this, it);
    }

    @Override
    default SLUndefinedFunctionRootNodeT _sLUndefinedFunctionRootNode(SLUndefinedFunctionRootNode it) {
        return MySLUndefinedFunctionRootNodeT.INSTANCE(this, it);
    }

    @Override
    default SLWhileNodeT _sLWhileNode(SLWhileNode it) {
        return MySLWhileNodeT.INSTANCE(this, it);
    }

    @Override
    default SLIfNodeT _sLIfNode(SLIfNode it) {
        return MySLIfNodeT.INSTANCE(this, it);
    }

    @Override
    default SLContinueNodeT _sLContinueNode(SLContinueNode it) {
        return MySLContinueNodeT.INSTANCE(this, it);
    }

    @Override
    default SLBlockNodeT _sLBlockNode(SLBlockNode it) {
        return MySLBlockNodeT.INSTANCE(this, it);
    }

    @Override
    default SLLogicalAndNodeT _sLLogicalAndNode(SLLogicalAndNode it) {
        return MySLLogicalAndNodeT.INSTANCE(this, it);
    }

    @Override
    default SLLogicalOrNodeT _sLLogicalOrNode(SLLogicalOrNode it) {
        return MySLLogicalOrNodeT.INSTANCE(this, it);
    }

    @Override
    default SLInvokeNodeT _sLInvokeNode(SLInvokeNode it) {
        return MySLInvokeNodeT.INSTANCE(this, it);
    }

    @Override
    default SLLongLiteralNodeT _sLLongLiteralNode(SLLongLiteralNode it) {
        return MySLLongLiteralNodeT.INSTANCE(this, it);
    }

    @Override
    default SLBreakExceptionT _sLBreakException(SLBreakException it) {
        return MySLBreakExceptionT.INSTANCE(this, it);
    }

    @Override
    default SLBigIntegerLiteralNodeT _sLBigIntegerLiteralNode(SLBigIntegerLiteralNode it) {
        return MySLBigIntegerLiteralNodeT.INSTANCE(this, it);
    }


    @Override
    default SLLogicalNotNodeT _sLLogicalNotNode(SLLogicalNotNode it) {
        return MySLLogicalNotNodeT.INSTANCE(this, it);
    }

    @Override
    default SLLessThanNodeT _sLLessThanNode(SLLessThanNode it) {
        return MySLLessThanNodeT.INSTANCE(this, it);
    }

    @Override
    default SLMulNodeT _sLMulNode(SLMulNode it) {
        return MySLMulNodeT.INSTANCE(this, it);
    }

    @Override
    default SLSubNodeT _sLSubNode(SLSubNode it) {
        return MySLSubNodeT.INSTANCE(this, it);
    }

    @Override
    default SLEqualNodeT _sLEqualNode(SLEqualNode it) {
        return MySLEqualNodeT.INSTANCE(this, it);
    }

    @Override
    default SLDivNodeT _sLDivNode(SLDivNode it) {
        return MySLDivNodeT.INSTANCE(this, it);
    }


}
