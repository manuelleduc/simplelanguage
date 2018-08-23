package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.builtins.*;
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
        return new MySLAddNodeT(this, it);
    }

    @Override
    default SLEvalRootNodeT _sLEvalRootNode(SLEvalRootNode it) {
        return new MySLEvalRootNodeT(this, it);
    }

    @Override
    default SLReturnNodeT _sLReturnNode(SLReturnNode it) {
        return new MySLReturnNodeT(this, it);
    }

    @Override
    default SLRootNodeT _sLRootNode(SLRootNode it) {
        return new MySLRootNodeT(this, it);
    }

    @Override
    default SLLexicalScopeT _sLLexicalScope(SLLexicalScope it) {
        return new MySLLexicalScopeT(this, it);
    }

    @Override
    default SLFunctionLiteralNodeT _sLFunctionLiteralNode(SLFunctionLiteralNode it) {
        return new MySLFunctionLiteralNodeT(this, it);
    }

    @Override
    default SLFunctionBodyNodeT _sLFunctionBodyNode(SLFunctionBodyNode it) {
        return new MySLFunctionBodyNodeT(this, it);
    }

    @Override
    default SLStringLiteralNodeT _sLStringLiteralNode(SLStringLiteralNode it) {
        return new MySLStringLiteralNodeT(this, it);
    }

    @Override
    default SLContinueExceptionT _sLContinueException(SLContinueException it) {
        return new MySLContinueExceptionT(this, it);
    }

    @Override
    default SLBreakNodeT _sLBreakNode(SLBreakNode it) {
        return new MySLBreakNodeT(this, it);
    }

    @Override
    default SLReturnExceptionT _sLReturnException(SLReturnException it) {
        return new MySLReturnExceptionT(this, it);
    }

    @Override
    default SLDebuggerNodeT _sLDebuggerNode(SLDebuggerNode it) {
        return new MySLDebuggerNodeT(this, it);
    }

    @Override
    default SLReadArgumentNodeT _sLReadArgumentNode(SLReadArgumentNode it) {
        return new MySLReadArgumentNodeT(this, it);
    }

    @Override
    default SLParenExpressionNodeT _sLParenExpressionNode(SLParenExpressionNode it) {
        return new MySLParenExpressionNodeT(this, it);
    }

    @Override
    default SLUndefinedFunctionRootNodeT _sLUndefinedFunctionRootNode(SLUndefinedFunctionRootNode it) {
        return new MySLUndefinedFunctionRootNodeT(this, it);
    }

    @Override
    default SLWhileNodeT _sLWhileNode(SLWhileNode it) {
        return new MySLWhileNodeT(this, it);
    }

    @Override
    default SLIfNodeT _sLIfNode(SLIfNode it) {
        return new MySLIfNodeT(this, it);
    }

    @Override
    default SLContinueNodeT _sLContinueNode(SLContinueNode it) {
        return new MySLContinueNodeT(this, it);
    }

    @Override
    default SLBlockNodeT _sLBlockNode(SLBlockNode it) {
        return new MySLBlockNodeT(this, it);
    }

    @Override
    default SLLogicalAndNodeT _sLLogicalAndNode(SLLogicalAndNode it) {
        return new MySLLogicalAndNodeT(this, it);
    }

    @Override
    default SLLogicalOrNodeT _sLLogicalOrNode(SLLogicalOrNode it) {
        return new MySLLogicalOrNodeT(this, it);
    }

    @Override
    default SLInvokeNodeT _sLInvokeNode(SLInvokeNode it) {
        return new MySLInvokeNodeT(this, it);
    }

    @Override
    default SLLongLiteralNodeT _sLLongLiteralNode(SLLongLiteralNode it) {
        return new MySLLongLiteralNodeT(this, it);
    }

    @Override
    default SLBreakExceptionT _sLBreakException(SLBreakException it) {
        return new MySLBreakExceptionT(this, it);
    }

    @Override
    default SLBigIntegerLiteralNodeT _sLBigIntegerLiteralNode(SLBigIntegerLiteralNode it) {
        return new MySLBigIntegerLiteralNodeT(this, it);
    }

    @Override
    default SLLogicalNotNodeT _sLLogicalNotNode(SLLogicalNotNode it) {
        return new MySLLogicalNotNodeT(this, it);
    }

    @Override
    default SLLessThanNodeT _sLLessThanNode(SLLessThanNode it) {
        return new MySLLessThanNodeT(this, it);
    }

    @Override
    default SLMulNodeT _sLMulNode(SLMulNode it) {
        return new MySLMulNodeT(this, it);
    }

    @Override
    default SLSubNodeT _sLSubNode(SLSubNode it) {
        return new MySLSubNodeT(this, it);
    }

    @Override
    default SLEqualNodeT _sLEqualNode(SLEqualNode it) {
        return new MySLEqualNodeT(this, it);
    }

    @Override
    default SLDivNodeT _sLDivNode(SLDivNode it) {
        return new MySLDivNodeT(this, it);
    }

    @Override
    default SLReadPropertyNodeT _sLReadPropertyNode(SLReadPropertyNode it) {
        return new MySLReadPropertyNodeT(this, it);
    }

    @Override
    default SLWritePropertyNodeT _sLWritePropertyNode(SLWritePropertyNode it) {
        return new MySLWritePropertyNodeT(this, it);
    }

    @Override
    default SLUnboxNodeT _sLUnboxNode(SLUnboxNode it) {
        return new MySLUnboxNodeT(this, it);
    }

    @Override
    default SLReadLocalVariableNodeT _sLReadLocalVariableNode(SLReadLocalVariableNode it) {
        return new MySLReadLocalVariableNodeT(this, it);
    }

    @Override
    default SLWriteLocalVariableNodeT _sLWriteLocalVariableNode(SLWriteLocalVariableNode it) {
        return new MySLWriteLocalVariableNodeT(this, it);
    }

    @Override
    default SLLessOrEqualNodeT _sLLessOrEqualNode(SLLessOrEqualNode it) {
        return new MySLLessOrEqualNodeT(this, it); // MySLLessOrEqualNodeT(this, it);
    }

    @Override
    default SLPrintlnBuiltinT _sLPrintlnBuiltin(SLPrintlnBuiltin it) {
        return new MySLPrintlnBuiltinT(this, it);
    }

    @Override
    default SLReadlnBuiltinT _sLReadlnBuiltin(SLReadlnBuiltin it) {
        return new MySLReadInBuiltinT(this, it);
    }

    @Override
    default SLNewObjectBuiltinT _sLNewObjectBuiltin(SLNewObjectBuiltin it) {
        return new MySLNewObjectBuiltinT(this, it);
    }

    @Override
    default SLImportBuiltinT _sLImportBuiltin(SLImportBuiltin it) {
        return new MySLImportBuiltinT(this, it);
    }

    @Override
    default SLNanoTimeBuiltinT _sLNanoTimeBuiltin(SLNanoTimeBuiltin it) {
        return new MySLNanoTimeBuiltinT(this, it);
    }

    @Override
    default SLDefineFunctionBuiltinT _sLDefineFunctionBuiltin(SLDefineFunctionBuiltin it) {
        return new MySLDefineFunctionBuiltinT(this, it);
    }

    @Override
    default SLEvalBuiltinT _sLEvalBuiltin(SLEvalBuiltin it) {
        return new MySLEvalBuiltinT(this, it);
    }

    @Override
    default SLStackTraceBuiltinT _sLStackTraceBuiltin(SLStackTraceBuiltin it) {
        return new MySLStackTraceBuiltinT(this, it);
    }
}
