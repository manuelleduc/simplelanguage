package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.SLEvalRootNode;
import com.oracle.truffle.sl.nodes.SLRootNode;
import com.oracle.truffle.sl.nodes.SLUndefinedFunctionRootNode;
import com.oracle.truffle.sl.nodes.access.SLReadPropertyNode;
import com.oracle.truffle.sl.nodes.access.SLWritePropertyNode;
import com.oracle.truffle.sl.nodes.call.SLInvokeNode;
import com.oracle.truffle.sl.nodes.controlflow.*;
import com.oracle.truffle.sl.nodes.expression.*;
import com.oracle.truffle.sl.nodes.local.SLLexicalScope;
import com.oracle.truffle.sl.nodes.local.SLReadArgumentNode;
import com.oracle.truffle.sl.nodes.local.SLReadLocalVariableNode;
import com.oracle.truffle.sl.nodes.local.SLWriteLocalVariableNode;
import com.oracle.truffle.sl.revisitor.SLRevisitor;

public interface ExecSLRevisitor extends SLRevisitor<SLAddNodeT, SLBigIntegerLiteralNodeT, SLBinaryNodeT, SLBlockNodeT,
        SLBreakExceptionT, SLBreakNodeT, SLBuiltinNodeT, SLContinueExceptionT, SLContinueNodeT, SLDebuggerNodeT,
        SLDefineFunctionBuiltinT, SLDispatchNodeT, SLDivNodeT, SLEqualNodeT, SLEvalBuiltinT, SLEvalRootNodeT,
        SLExpressionNodeT, SLForeignToSLTypeNodeT, SLFunctionBodyNodeT, SLFunctionLiteralNodeT, SLGetSizeBuiltinT,
        SLHasSizeBuiltinT, SLHelloEqualsWorldBuiltinT, SLIfNodeT, SLImportBuiltinT, SLInvokeNodeT, SLIsExecutableBuiltinT,
        SLIsNullBuiltinT, SLLessOrEqualNodeT, SLLessThanNodeT, SLLexicalScopeT, SLLogicalAndNodeT, SLLogicalNotNodeT,
        SLLogicalOrNodeT, SLLongLiteralNodeT, SLMulNodeT, SLNanoTimeBuiltinT, SLNewObjectBuiltinT,
        SLParenExpressionNodeT, SLPrintlnBuiltinT, SLPropertyCacheNodeT, SLReadArgumentNodeT, SLReadLocalVariableNodeT,
        SLReadPropertyCacheNodeT, SLReadPropertyNodeT, SLReadlnBuiltinT, SLReturnExceptionT, SLReturnNodeT, SLRootNodeT,
        SLShortCircuitNodeT, SLStackTraceBuiltinT, SLStatementNodeT, SLStringLiteralNodeT, SLSubNodeT, SLTypesT,
        SLUnboxNodeT, SLUndefinedFunctionRootNodeT, SLWhileNodeT, SLWriteLocalVariableNodeT, SLWritePropertyCacheNodeT,
        SLWritePropertyNodeT> {
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

    @Override
    default SLReadPropertyNodeT _sLReadPropertyNode(SLReadPropertyNode it) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    default SLWritePropertyNodeT _sLWritePropertyNode(SLWritePropertyNode it) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    default SLUnboxNodeT _sLUnboxNode(SLUnboxNode it) {
        return MySLUnboxNodeT.INSTANCE(this, it);
    }

    @Override
    default SLReadLocalVariableNodeT _sLReadLocalVariableNode(SLReadLocalVariableNode it) {
        return MySLReadLocalVariableNodeT.INSTANCE(this, it);
    }

    @Override
    default SLWriteLocalVariableNodeT _sLWriteLocalVariableNode(SLWriteLocalVariableNode it) {
//        throw new RuntimeException("Not Implemented");
        return MySLWriteLocalVariableNodeT.INSTANCE(this, it);
    }

    @Override
    default SLLessOrEqualNodeT _sLLessOrEqualNode(SLLessOrEqualNode it) {
        return MySLLessOrEqualNodeT.INSTANCE(this, it);
    }
}
