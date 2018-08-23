package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.sl.builtins.SLEvalBuiltin;

import java.util.concurrent.locks.Lock;

public class MySLEvalBuiltinT extends MySLBuiltinNodeT implements SLEvalBuiltinT {
    private final SLEvalBuiltin it;
    private final SLExpressionNodeT arguments0_;
    private final SLExpressionNodeT arguments1_;
    @CompilerDirectives.CompilationFinal
    private int state_;
    @CompilerDirectives.CompilationFinal
    private int exclude_;
    @Node.Child
    private EvalCachedData evalCached_cache;

    @GeneratedBy(SLEvalBuiltin.class)
    private static final class EvalCachedData extends Node {

        @Child
        EvalCachedData next_;
        @CompilerDirectives.CompilationFinal
        String cachedId_;
        @CompilerDirectives.CompilationFinal
        String cachedCode_;
        @Child
        DirectCallNode callNode_;

        EvalCachedData(EvalCachedData next_) {
            this.next_ = next_;
        }

        @Override
        public NodeCost getCost() {
            return NodeCost.NONE;
        }

    }

    public MySLEvalBuiltinT(ExecSLRevisitor execSLRevisitor, SLEvalBuiltin it) {
        super(it);
        this.it = it;
        arguments0_ = execSLRevisitor.$(it.getArguments0());
        arguments1_ = execSLRevisitor.$(it.getArguments1());
    }

    @ExplodeLoop(kind = ExplodeLoop.LoopExplosionKind.FULL_EXPLODE_UNTIL_RETURN)
    @Override
    public Object execute(VirtualFrame frameValue) {
        int state = state_;
        Object arguments0Value_ = this.arguments0_.executeGeneric(frameValue);
        Object arguments1Value_ = this.arguments1_.executeGeneric(frameValue);
        if (state != 0 /* is-active evalCached(String, String, String, String, DirectCallNode) || evalUncached(String, String) */ && arguments0Value_ instanceof String) {
            String arguments0Value__ = (String) arguments0Value_;
            if (arguments1Value_ instanceof String) {
                String arguments1Value__ = (String) arguments1Value_;
                if ((state & 0b1) != 0 /* is-active evalCached(String, String, String, String, DirectCallNode) */) {
                    EvalCachedData s1_ = this.evalCached_cache;
                    while (s1_ != null) {
                        if ((stringsEqual(s1_.cachedId_, arguments0Value__)) && (stringsEqual(s1_.cachedCode_, arguments1Value__))) {
                            return evalCached(arguments0Value__, arguments1Value__, s1_.cachedId_, s1_.cachedCode_, s1_.callNode_);
                        }
                        s1_ = s1_.next_;
                    }
                }
                if ((state & 0b10) != 0 /* is-active evalUncached(String, String) */) {
                    return evalUncached(arguments0Value__, arguments1Value__);
                }
            }
        }
        CompilerDirectives.transferToInterpreterAndInvalidate();
        return executeAndSpecialize(arguments0Value_, arguments1Value_);
    }

    private Object executeAndSpecialize(Object arguments0Value, Object arguments1Value) {
        Lock lock = it.getLock2();
        boolean hasLock = true;
        lock.lock();
        int state = state_;
        int exclude = exclude_;
        int oldState = state;
        int oldExclude = exclude;
        int oldCacheCount = state == 0 ? 0 : countCaches();
        try {
            if (arguments0Value instanceof String) {
                String arguments0Value_ = (String) arguments0Value;
                if (arguments1Value instanceof String) {
                    String arguments1Value_ = (String) arguments1Value;
                    if ((exclude) == 0 /* is-not-excluded evalCached(String, String, String, String, DirectCallNode) */) {
                        int count1_ = 0;
                        EvalCachedData s1_ = this.evalCached_cache;
                        if ((state & 0b1) != 0 /* is-active evalCached(String, String, String, String, DirectCallNode) */) {
                            while (s1_ != null) {
                                if ((stringsEqual(s1_.cachedId_, arguments0Value_)) && (stringsEqual(s1_.cachedCode_, arguments1Value_))) {
                                    break;
                                }
                                s1_ = s1_.next_;
                                count1_++;
                            }
                        }
                        if (s1_ == null) {
                            {
                                String cachedId__ = (arguments0Value_);
                                if ((stringsEqual(cachedId__, arguments0Value_))) {
                                    String cachedCode__ = (arguments1Value_);
                                    if ((stringsEqual(cachedCode__, arguments1Value_)) && count1_ < (3)) {
                                        s1_ = new EvalCachedData(evalCached_cache);
                                        s1_.cachedId_ = cachedId__;
                                        s1_.cachedCode_ = cachedCode__;
                                        s1_.callNode_ = (DirectCallNode.create(parse(arguments0Value_, arguments1Value_)));
                                        this.evalCached_cache = it.insert2(s1_);
                                        this.state_ = state = state | 0b1 /* add-active evalCached(String, String, String, String, DirectCallNode) */;
                                    }
                                }
                            }
                        }
                        if (s1_ != null) {
                            lock.unlock();
                            hasLock = false;
                            return evalCached(arguments0Value_, arguments1Value_, s1_.cachedId_, s1_.cachedCode_, s1_.callNode_);
                        }
                    }
                    this.exclude_ = exclude = exclude | 0b1 /* add-excluded evalCached(String, String, String, String, DirectCallNode) */;
                    this.evalCached_cache = null;
                    state = state & 0xfffffffe /* remove-active evalCached(String, String, String, String, DirectCallNode) */;
                    this.state_ = state = state | 0b10 /* add-active evalUncached(String, String) */;
                    lock.unlock();
                    hasLock = false;
                    return evalUncached(arguments0Value_, arguments1Value_);
                }
            }
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw new UnsupportedSpecializationException(it, new Node[]{it.getArguments0(), it.getArguments1()}, arguments0Value, arguments1Value);
        } finally {
            if (oldState != 0 || oldExclude != 0) {
                checkForPolymorphicSpecialize(oldState, oldExclude, oldCacheCount);
            }
            if (hasLock) {
                lock.unlock();
            }
        }
    }

    private void checkForPolymorphicSpecialize(int oldState, int oldExclude, int oldCacheCount) {
        int newState = this.state_;
        int newExclude = this.exclude_;
        if ((oldState ^ newState) != 0 || (oldExclude ^ newExclude) != 0 || oldCacheCount < countCaches()) {
            it.reportPolymorphicSpecialize2();
        }
    }

    private int countCaches() {
        int cacheCount = 0;
        EvalCachedData s1_ = this.evalCached_cache;
        while (s1_ != null) {
            cacheCount++;
            s1_ = s1_.next_;
        }
        return cacheCount;
    }


    @Specialization(guards = {"stringsEqual(cachedId, id)", "stringsEqual(cachedCode, code)"})
    public Object evalCached(String id, String code,
                             @Cached("id") String cachedId,
                             @Cached("code") String cachedCode,
                             @Cached("create(parse(id, code))") DirectCallNode callNode) {
        return callNode.call(new Object[]{});
    }

    @CompilerDirectives.TruffleBoundary
    @Specialization(replaces = "evalCached")
    public Object evalUncached(String id, String code) {
        return parse(id, code).call();
    }

    protected CallTarget parse(String id, String code) {
        // mimetype needs to be set for compatibility reasons with the old TCK.
        final Source source = Source.newBuilder(code).name("(eval)").language(id).mimeType(id).build();
        return getContext().parse(source);
    }

    /* Work around findbugs warning in generate code. */
    protected static boolean stringsEqual(String a, String b) {
        return a.equals(b);
    }
}
