package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.*;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.sl.nodes.access.SLWritePropertyCacheNode;
import com.oracle.truffle.sl.nodes.access.SLWritePropertyNode;
import com.oracle.truffle.sl.runtime.SLContext;
import com.oracle.truffle.sl.runtime.SLUndefinedNameException;

import java.util.concurrent.locks.Lock;

public class MySLWritePropertyNodeT implements SLWritePropertyNodeT {

    private final SLExpressionNodeT receiverNode_;
    private final SLExpressionNodeT nameNode_;
    private final SLExpressionNodeT valueNode_;
    private final SLWritePropertyNode it;
    @CompilerDirectives.CompilationFinal
    private int state_;
    @Node.Child
    private SLWritePropertyCacheNode write_writeNode_;
    @Node.Child
    private Node writeForeign_foreignWriteNode_;

    public MySLWritePropertyNodeT(ExecSLRevisitor execSLRevisitor, SLWritePropertyNode it) {
        receiverNode_ = execSLRevisitor.$(it.getReceiverNode());
        nameNode_ = execSLRevisitor.$(it.getNameNode());
        valueNode_ = execSLRevisitor.$(it.getValueNode());
        this.it = it;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        Object receiverNodeValue_ = this.receiverNode_.executeGeneric(frameValue);
        Object nameNodeValue_ = this.nameNode_.executeGeneric(frameValue);
        Object valueNodeValue_ = this.valueNode_.executeGeneric(frameValue);
        if ((state & 0b1) != 0 /* is-active write(DynamicObject, Object, Object, SLWritePropertyCacheNode) */ && receiverNodeValue_ instanceof DynamicObject) {
            DynamicObject receiverNodeValue__ = (DynamicObject) receiverNodeValue_;
            if ((SLContext.isSLObject(receiverNodeValue__))) {
                return write(receiverNodeValue__, nameNodeValue_, valueNodeValue_, this.write_writeNode_);
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(receiverNodeValue_, nameNodeValue_, valueNodeValue_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        int state = state_;
        if ((state & 0b1) != 0 /* is-active write(DynamicObject, Object, Object, SLWritePropertyCacheNode) */) {
            executeGeneric(frameValue);
            return;
        }
        Object receiverNodeValue_ = this.receiverNode_.executeGeneric(frameValue);
        Object nameNodeValue_ = this.nameNode_.executeGeneric(frameValue);
        Object valueNodeValue_ = this.valueNode_.executeGeneric(frameValue);
        if ((state & 0b110) != 0 /* is-active writeForeign(TruffleObject, Object, Object, Node) || updateShape(Object, Object, Object) */) {
            if ((state & 0b10) != 0 /* is-active writeForeign(TruffleObject, Object, Object, Node) */ && receiverNodeValue_ instanceof TruffleObject) {
                TruffleObject receiverNodeValue__ = (TruffleObject) receiverNodeValue_;
                if ((!(SLContext.isSLObject(receiverNodeValue__)))) {
                    writeForeign(receiverNodeValue__, nameNodeValue_, valueNodeValue_, this.writeForeign_foreignWriteNode_);
                    return;
                }
            }
            if ((state & 0b100) != 0 /* is-active updateShape(Object, Object, Object) */) {
                if (fallbackGuard_(receiverNodeValue_, nameNodeValue_, valueNodeValue_)) {
                    updateShape(receiverNodeValue_, nameNodeValue_, valueNodeValue_);
                    return;
                }
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        executeAndSpecialize(receiverNodeValue_, nameNodeValue_, valueNodeValue_);
        return;
    }

    private Object executeAndSpecialize(Object receiverNodeValue, Object nameNodeValue, Object valueNodeValue) {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        int oldState = state;
        try {
            if (receiverNodeValue instanceof DynamicObject) {
                DynamicObject receiverNodeValue_ = (DynamicObject) receiverNodeValue;
                if ((SLContext.isSLObject(receiverNodeValue_))) {
                    this.write_writeNode_ = it.insert2((SLWritePropertyCacheNode.create()));
                    this.state_ = state = state | 0b1 /* add-active write(DynamicObject, Object, Object, SLWritePropertyCacheNode) */;
                    lock.unlock();
                    hasLock = false;
                    return write(receiverNodeValue_, nameNodeValue, valueNodeValue, this.write_writeNode_);
                }
            }
            if (receiverNodeValue instanceof TruffleObject) {
                TruffleObject receiverNodeValue_ = (TruffleObject) receiverNodeValue;
                if ((!(SLContext.isSLObject(receiverNodeValue_)))) {
                    this.writeForeign_foreignWriteNode_ = it.insert2((Message.WRITE.createNode()));
                    this.state_ = state = state | 0b10 /* add-active writeForeign(TruffleObject, Object, Object, Node) */;
                    lock.unlock();
                    hasLock = false;
                    writeForeign(receiverNodeValue_, nameNodeValue, valueNodeValue, this.writeForeign_foreignWriteNode_);
                    return null;
                }
            }
            this.state_ = state = state | 0b100 /* add-active updateShape(Object, Object, Object) */;
            lock.unlock();
            hasLock = false;
            updateShape(receiverNodeValue, nameNodeValue, valueNodeValue);
            return null;
        } finally {
            if (oldState != 0) {
                checkForPolymorphicSpecialize(oldState);
            }
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    private void checkForPolymorphicSpecialize(int oldState) {
        int newState = this.state_;
        if ((oldState ^ newState) != 0) {
            it.reportPolymorphicSpecialize2();
        }
    }

    @SuppressWarnings("unused")
    private static boolean fallbackGuard_(Object receiverNodeValue, Object nameNodeValue, Object valueNodeValue) {
        if (receiverNodeValue instanceof DynamicObject) {
            DynamicObject receiverNodeValue_ = (DynamicObject) receiverNodeValue;
            if ((SLContext.isSLObject(receiverNodeValue_))) {
                return false;
            }
        }
        if (receiverNodeValue instanceof TruffleObject) {
            TruffleObject receiverNodeValue_ = (TruffleObject) receiverNodeValue;
            if ((!(SLContext.isSLObject(receiverNodeValue_)))) {
                return false;
            }
        }
        return true;
    }

    @Specialization(guards = "isSLObject(receiver)")
    protected Object write(DynamicObject receiver, Object name, Object value,
                           @Cached("create()") SLWritePropertyCacheNode writeNode) {
        /**
         * The polymorphic cache node that performs the actual write. This is a separate node so
         * that it can be re-used in cases where the receiver, name, and value are not nodes but
         * already evaluated values.
         */
        writeNode.executeWrite(receiver, name, value);
        return value;
    }

    /**
     * Language interoperability: If the receiver object is a foreign value we use Truffle's interop
     * API to access the foreign data.
     */
    @Specialization(guards = "!isSLObject(receiver)")
    protected void writeForeign(TruffleObject receiver, Object name, Object value,
                                // The child node to access the foreign object
                                @Cached("WRITE.createNode()") Node foreignWriteNode) {

        try {
            /* Perform the foreign object access. */
            ForeignAccess.sendWrite(foreignWriteNode, receiver, name, value);

        } catch (UnknownIdentifierException | UnsupportedTypeException | UnsupportedMessageException e) {
            /* Foreign access was not successful. */
            throw SLUndefinedNameException.undefinedProperty(it, name);
        }
    }

    /**
     * When no specialization fits, the receiver is not an object (which is a type error).
     */
    @Fallback
    @SuppressWarnings("unused")
    protected void updateShape(Object r, Object name, Object value) {
        throw SLUndefinedNameException.undefinedProperty(it, name);
    }
}
