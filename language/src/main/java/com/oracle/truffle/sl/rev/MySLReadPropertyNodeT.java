package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.interop.*;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.sl.nodes.access.SLReadPropertyCacheNode;
import com.oracle.truffle.sl.nodes.access.SLReadPropertyNode;
import com.oracle.truffle.sl.nodes.interop.SLForeignToSLTypeNode;
import com.oracle.truffle.sl.runtime.SLContext;
import com.oracle.truffle.sl.runtime.SLUndefinedNameException;

import java.util.concurrent.locks.Lock;

public class MySLReadPropertyNodeT implements SLReadPropertyNodeT {
    private final SLReadPropertyNode it;
    private final SLExpressionNodeT receiverNode_;
    private final SLExpressionNodeT nameNode_;
    @Node.Child
    private SLReadPropertyCacheNode read_readNode_;
    @Node.Child
    private Node readForeign_foreignReadNode_;
    @Node.Child
    private SLForeignToSLTypeNode readForeign_toSLTypeNode_;

    @CompilerDirectives.CompilationFinal
    private int state_;


    public MySLReadPropertyNodeT(ExecSLRevisitor execSLRevisitor, SLReadPropertyNode it) {
        this.it = it;
        receiverNode_ = execSLRevisitor.$(it.getReceiverNode());
        this.nameNode_ = execSLRevisitor.$(it.getNameNode());
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        int state = state_;
        Object receiverNodeValue_ = this.receiverNode_.executeGeneric(frameValue);
        Object nameNodeValue_ = this.nameNode_.executeGeneric(frameValue);
        if (state != 0 /* is-active read(DynamicObject, Object, SLReadPropertyCacheNode) || readForeign(TruffleObject, Object, Node, SLForeignToSLTypeNode) || typeError(Object, Object) */) {
            if ((state & 0b1) != 0 /* is-active read(DynamicObject, Object, SLReadPropertyCacheNode) */ && receiverNodeValue_ instanceof DynamicObject) {
                DynamicObject receiverNodeValue__ = (DynamicObject) receiverNodeValue_;
                if ((SLContext.isSLObject(receiverNodeValue__))) {
                    return read(receiverNodeValue__, nameNodeValue_, this.read_readNode_);
                }
            }
            if ((state & 0b10) != 0 /* is-active readForeign(TruffleObject, Object, Node, SLForeignToSLTypeNode) */ && receiverNodeValue_ instanceof TruffleObject) {
                TruffleObject receiverNodeValue__ = (TruffleObject) receiverNodeValue_;
                if ((!(SLContext.isSLObject(receiverNodeValue__)))) {
                    return readForeign(receiverNodeValue__, nameNodeValue_, this.readForeign_foreignReadNode_, this.readForeign_toSLTypeNode_);
                }
            }
            if ((state & 0b100) != 0 /* is-active typeError(Object, Object) */) {
                if (fallbackGuard_(receiverNodeValue_, nameNodeValue_)) {
                    return typeError(receiverNodeValue_, nameNodeValue_);
                }
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(receiverNodeValue_, nameNodeValue_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeGeneric(frameValue);
        return;
    }

    private Object executeAndSpecialize(Object receiverNodeValue, Object nameNodeValue) {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        int oldState = state;
        try {
            if (receiverNodeValue instanceof DynamicObject) {
                DynamicObject receiverNodeValue_ = (DynamicObject) receiverNodeValue;
                if ((SLContext.isSLObject(receiverNodeValue_))) {
                    this.read_readNode_ = it.insert2((SLReadPropertyCacheNode.create()));
                    this.state_ = state = state | 0b1 /* add-active read(DynamicObject, Object, SLReadPropertyCacheNode) */;
                    lock.unlock();
                    hasLock = false;
                    return read(receiverNodeValue_, nameNodeValue, this.read_readNode_);
                }
            }
            if (receiverNodeValue instanceof TruffleObject) {
                TruffleObject receiverNodeValue_ = (TruffleObject) receiverNodeValue;
                if ((!(SLContext.isSLObject(receiverNodeValue_)))) {
                    this.readForeign_foreignReadNode_ = it.insert2((Message.READ.createNode()));
                    this.readForeign_toSLTypeNode_ = it.insert2((SLForeignToSLTypeNode.create()));
                    this.state_ = state = state | 0b10 /* add-active readForeign(TruffleObject, Object, Node, SLForeignToSLTypeNode) */;
                    lock.unlock();
                    hasLock = false;
                    return readForeign(receiverNodeValue_, nameNodeValue, this.readForeign_foreignReadNode_, this.readForeign_toSLTypeNode_);
                }
            }
            this.state_ = state = state | 0b100 /* add-active typeError(Object, Object) */;
            lock.unlock();
            hasLock = false;
            return typeError(receiverNodeValue, nameNodeValue);
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
    private static boolean fallbackGuard_(Object receiverNodeValue, Object nameNodeValue) {
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
    protected Object read(DynamicObject receiver, Object name,
                          @Cached("create()") SLReadPropertyCacheNode readNode) {
        /**
         * The polymorphic cache node that performs the actual read. This is a separate node so that
         * it can be re-used in cases where the receiver and name are not nodes but already
         * evaluated values.
         */
        return readNode.executeRead(receiver, name);
    }

    /**
     * Language interoperability: if the receiver object is a foreign value we use Truffle's interop
     * API to access the foreign data.
     */
    @Specialization(guards = "!isSLObject(receiver)")
    protected Object readForeign(TruffleObject receiver, Object name,
                                 // The child node to access the foreign object
                                 @Cached("READ.createNode()") Node foreignReadNode,
                                 // The child node to convert the result of the foreign read to a SL value
                                 @Cached("create()") SLForeignToSLTypeNode toSLTypeNode) {

        try {
            /* Perform the foreign object access. */
            Object result = ForeignAccess.sendRead(foreignReadNode, receiver, name);
            /* Convert the result to a SL value. */
            return toSLTypeNode.executeConvert(result);

        } catch (UnknownIdentifierException | UnsupportedMessageException e) {
            /* Foreign access was not successful. */
            throw SLUndefinedNameException.undefinedProperty(it, name);
        }
    }

    /**
     * When no specialization fits, the receiver is either not an object (which is a type error), or
     * the object has a shape that has been invalidated.
     */
    @Fallback
    protected Object typeError(@SuppressWarnings("unused") Object r, Object name) {
        /* Non-object types do not have properties. */
        throw SLUndefinedNameException.undefinedProperty(it, name);
    }
}
