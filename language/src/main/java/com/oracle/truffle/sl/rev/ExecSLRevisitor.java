package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.sl.builtins.SLPrintlnBuiltin;
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
    /*@CompilerDirectives.TruffleBoundary */ default SLAddNodeT _sLAddNode(SLAddNode it) {
        return new MySLAddNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLEvalRootNodeT _sLEvalRootNode(SLEvalRootNode it) {
        return new MySLEvalRootNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLReturnNodeT _sLReturnNode(SLReturnNode it) {
        return new MySLReturnNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLRootNodeT _sLRootNode(SLRootNode it) {
        return new MySLRootNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLLexicalScopeT _sLLexicalScope(SLLexicalScope it) {
        return new MySLLexicalScopeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLFunctionLiteralNodeT _sLFunctionLiteralNode(SLFunctionLiteralNode it) {
        return new MySLFunctionLiteralNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLFunctionBodyNodeT _sLFunctionBodyNode(SLFunctionBodyNode it) {
        return new MySLFunctionBodyNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLStringLiteralNodeT _sLStringLiteralNode(SLStringLiteralNode it) {
        return new MySLStringLiteralNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLContinueExceptionT _sLContinueException(SLContinueException it) {
        return new MySLContinueExceptionT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLBreakNodeT _sLBreakNode(SLBreakNode it) {
        return new MySLBreakNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLReturnExceptionT _sLReturnException(SLReturnException it) {
        return new MySLReturnExceptionT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLDebuggerNodeT _sLDebuggerNode(SLDebuggerNode it) {
        return new MySLDebuggerNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLReadArgumentNodeT _sLReadArgumentNode(SLReadArgumentNode it) {
        return new MySLReadArgumentNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLParenExpressionNodeT _sLParenExpressionNode(SLParenExpressionNode it) {
        return new MySLParenExpressionNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLUndefinedFunctionRootNodeT _sLUndefinedFunctionRootNode(SLUndefinedFunctionRootNode it) {
        return new MySLUndefinedFunctionRootNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLWhileNodeT _sLWhileNode(SLWhileNode it) {
        return new MySLWhileNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLIfNodeT _sLIfNode(SLIfNode it) {
        return new MySLIfNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLContinueNodeT _sLContinueNode(SLContinueNode it) {
        return new MySLContinueNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLBlockNodeT _sLBlockNode(SLBlockNode it) {
        return new MySLBlockNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLLogicalAndNodeT _sLLogicalAndNode(SLLogicalAndNode it) {
        return new MySLLogicalAndNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLLogicalOrNodeT _sLLogicalOrNode(SLLogicalOrNode it) {
        return new MySLLogicalOrNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLInvokeNodeT _sLInvokeNode(SLInvokeNode it) {
        return new MySLInvokeNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLLongLiteralNodeT _sLLongLiteralNode(SLLongLiteralNode it) {
        return new MySLLongLiteralNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLBreakExceptionT _sLBreakException(SLBreakException it) {
        return new MySLBreakExceptionT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLBigIntegerLiteralNodeT _sLBigIntegerLiteralNode(SLBigIntegerLiteralNode it) {
        return new MySLBigIntegerLiteralNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLLogicalNotNodeT _sLLogicalNotNode(SLLogicalNotNode it) {
        return new MySLLogicalNotNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLLessThanNodeT _sLLessThanNode(SLLessThanNode it) {
        return new MySLLessThanNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLMulNodeT _sLMulNode(SLMulNode it) {
        return new MySLMulNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLSubNodeT _sLSubNode(SLSubNode it) {
        return new MySLSubNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLEqualNodeT _sLEqualNode(SLEqualNode it) {
        return new MySLEqualNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLDivNodeT _sLDivNode(SLDivNode it) {
        return new MySLDivNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLReadPropertyNodeT _sLReadPropertyNode(SLReadPropertyNode it) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLWritePropertyNodeT _sLWritePropertyNode(SLWritePropertyNode it) {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLUnboxNodeT _sLUnboxNode(SLUnboxNode it) {
        return new MySLUnboxNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLReadLocalVariableNodeT _sLReadLocalVariableNode(SLReadLocalVariableNode it) {
        return new MySLReadLocalVariableNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLWriteLocalVariableNodeT _sLWriteLocalVariableNode(SLWriteLocalVariableNode it) {
        return new MySLWriteLocalVariableNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLLessOrEqualNodeT _sLLessOrEqualNode(SLLessOrEqualNode it) {
        return new MySLLessOrEqualNodeT(this, it); // MySLLessOrEqualNodeT(this, it);
    }

    @Override
    /*@CompilerDirectives.TruffleBoundary */ default SLPrintlnBuiltinT _sLPrintlnBuiltin(SLPrintlnBuiltin it) {
        return new MySLPrintlnBuiltinT(this, it);
    }
}
