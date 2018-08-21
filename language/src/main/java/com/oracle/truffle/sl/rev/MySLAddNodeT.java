package com.oracle.truffle.sl.rev;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.ImplicitCast;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import com.oracle.truffle.sl.SLException;
import com.oracle.truffle.sl.nodes.SLExpressionNode;
import com.oracle.truffle.sl.nodes.SLTypes;
import com.oracle.truffle.sl.nodes.SLTypesGen;
import com.oracle.truffle.sl.nodes.expression.SLAddNode;
import com.oracle.truffle.sl.runtime.SLBigNumber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class MySLAddNodeT implements SLAddNodeT {
	private final static Map<SLAddNode, SLAddNodeT> cache = new HashMap<>();
	private final ExecSLRevisitor alg;
	private final SLAddNode it;
	@CompilerDirectives.CompilationFinal
	private int state_;
	@CompilerDirectives.CompilationFinal
	private int exclude_;

	private MySLAddNodeT(ExecSLRevisitor alg, SLAddNode it) {
		this.alg = alg;
		this.it = it;
	}

	public static SLAddNodeT INSTANCE(ExecSLRevisitor alg, SLAddNode it) {
		if (!cache.containsKey(it)) cache.put(it, null/*new MySLAddNodeT(alg, it)*/);
		return cache.get(it);
	}

	private boolean fallbackGuard_(int state, Object leftNodeValue, Object rightNodeValue) {
		if (SLTypesGen.isImplicitSLBigNumber(0b11, leftNodeValue) && SLTypesGen.isImplicitSLBigNumber(0b11, rightNodeValue)) {
			return false;
		}
		if (((state & 0b100)) == 0 /* is-not-active add(Object, Object) */ && (isString(leftNodeValue, rightNodeValue))) {
			return false;
		}
		return true;
	}

	@Override
	public Object executeGeneric(VirtualFrame frameValue) {
		int state = state_;
		if ((state & 0b1110) == 0 /* only-active add(long, long) */ && (state & 0b1111) != 0  /* is-not add(long, long) && add(SLBigNumber, SLBigNumber) && add(Object, Object) && typeError(Object, Object) */) {
			return executeGeneric_long_long0(frameValue, state);
		} else {
			return executeGeneric_generic1(frameValue, state);
		}
	}



	private Object executeGeneric_long_long0(VirtualFrame frameValue, int state) {
		long leftNodeValue_;
		try {
			leftNodeValue_ = alg.$(it.getLeftNode()).executeLong(frameValue);
		} catch (UnexpectedResultException ex) {
			Object rightNodeValue = alg.$(it.getRightNode()).executeGeneric(frameValue);
			return executeAndSpecialize(ex.getResult(), rightNodeValue);
		}
		long rightNodeValue_;
		try {
			rightNodeValue_ = alg.$(it.getRightNode()).executeLong(frameValue);
		} catch (UnexpectedResultException ex) {
			return executeAndSpecialize(leftNodeValue_, ex.getResult());
		}
		assert (state & 0b1) != 0 /* is-active add(long, long) */;
		try {
			return add(leftNodeValue_, rightNodeValue_);
		} catch (ArithmeticException ex) {
			// implicit transferToInterpreterAndInvalidate()
			Lock lock = it.getLock2();
			lock.lock();
			try {
				this.exclude_ = this.exclude_ | 0b1 /* add-excluded add(long, long) */;
				this.state_ = this.state_ & 0xfffffffe /* remove-active add(long, long) */;
			} finally {
				lock.unlock();
			}
			return executeAndSpecialize(leftNodeValue_, rightNodeValue_);
		}
	}

	private Object executeGeneric_generic1(VirtualFrame frameValue, int state) {
		Object leftNodeValue_ = alg.$(it.getLeftNode()).executeGeneric(frameValue);
		Object rightNodeValue_ = alg.$(it.getRightNode()).executeGeneric(frameValue);
		if ((state & 0b1) != 0 /* is-active add(long, long) */ && leftNodeValue_ instanceof Long) {
			long leftNodeValue__ = (long) leftNodeValue_;
			if (rightNodeValue_ instanceof Long) {
				long rightNodeValue__ = (long) rightNodeValue_;
				try {
					return add(leftNodeValue__, rightNodeValue__);
				} catch (ArithmeticException ex) {
					// implicit transferToInterpreterAndInvalidate()
					Lock lock = it.getLock2();
					lock.lock();
					try {
						this.exclude_ = this.exclude_ | 0b1 /* add-excluded add(long, long) */;
						this.state_ = this.state_ & 0xfffffffe /* remove-active add(long, long) */;
					} finally {
						lock.unlock();
					}
					return executeAndSpecialize(leftNodeValue__, rightNodeValue__);
				}
			}
		}
		if ((state & 0b10) != 0 /* is-active add(SLBigNumber, SLBigNumber) */ && SLTypesGen.isImplicitSLBigNumber((state & 0b110000) >>> 4 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_)) {
			SLBigNumber leftNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b110000) >>> 4 /* extract-implicit-active 0:SLBigNumber */, leftNodeValue_);
			if (SLTypesGen.isImplicitSLBigNumber((state & 0b11000000) >>> 6 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_)) {
				SLBigNumber rightNodeValue__ = SLTypesGen.asImplicitSLBigNumber((state & 0b11000000) >>> 6 /* extract-implicit-active 1:SLBigNumber */, rightNodeValue_);
				return add(leftNodeValue__, rightNodeValue__);
			}
		}
		if ((state & 0b1100) != 0 /* is-active add(Object, Object) || typeError(Object, Object) */) {
			if ((state & 0b100) != 0 /* is-active add(Object, Object) */) {
				if ((isString(leftNodeValue_, rightNodeValue_))) {
					return add(leftNodeValue_, rightNodeValue_);
				}
			}
			if ((state & 0b1000) != 0 /* is-active typeError(Object, Object) */) {
				if (fallbackGuard_(state, leftNodeValue_, rightNodeValue_)) {
					return typeError(leftNodeValue_, rightNodeValue_);
				}
			}
		}
		CompilerDirectives.transferToInterpreterAndInvalidate();
		return executeAndSpecialize(leftNodeValue_, rightNodeValue_);
	}

	@Override
	public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
		int state = state_;
		if ((state & 0b1000) != 0 /* is-active typeError(Object, Object) */) {
			return SLTypesGen.expectLong(executeGeneric(frameValue));
		}
		long leftNodeValue_;
		try {
			leftNodeValue_ = alg.$(it.getLeftNode()).executeLong(frameValue);
		} catch (UnexpectedResultException ex) {
			Object rightNodeValue = alg.$(it.getRightNode()).executeGeneric(frameValue);
			return SLTypesGen.expectLong(executeAndSpecialize(ex.getResult(), rightNodeValue));
		}
		long rightNodeValue_;
		try {
			rightNodeValue_ = alg.$(it.getRightNode()).executeLong(frameValue);
		} catch (UnexpectedResultException ex) {
			return SLTypesGen.expectLong(executeAndSpecialize(leftNodeValue_, ex.getResult()));
		}
		if ((state & 0b1) != 0 /* is-active add(long, long) */) {
			try {
				return add(leftNodeValue_, rightNodeValue_);
			} catch (ArithmeticException ex) {
				// implicit transferToInterpreterAndInvalidate()
				Lock lock = it.getLock2();
				lock.lock();
				try {
					this.exclude_ = this.exclude_ | 0b1 /* add-excluded add(long, long) */;
					this.state_ = this.state_ & 0xfffffffe /* remove-active add(long, long) */;
				} finally {
					lock.unlock();
				}
				return SLTypesGen.expectLong(executeAndSpecialize(leftNodeValue_, rightNodeValue_));
			}
		}
		CompilerDirectives.transferToInterpreterAndInvalidate();
		return SLTypesGen.expectLong(executeAndSpecialize(leftNodeValue_, rightNodeValue_));
	}

	@Override
	public void executeVoid(VirtualFrame frameValue) {
		int state = state_;
		try {
			if ((state & 0b1110) == 0 /* only-active add(long, long) */ && (state & 0b1111) != 0  /* is-not add(long, long) && add(SLBigNumber, SLBigNumber) && add(Object, Object) && typeError(Object, Object) */) {
				executeLong(frameValue);
				return;
			}
			executeGeneric(frameValue);
			return;
		} catch (UnexpectedResultException ex) {
			return;
		}
	}

	private Object executeAndSpecialize(Object leftNodeValue, Object rightNodeValue) {
		Lock lock = it.getLock2();
		boolean hasLock = true;
		lock.lock();
		int state = state_;
		int exclude = exclude_;
		int oldState = (state & 0b1111);
		int oldExclude = exclude;
		try {
			if ((exclude) == 0 /* is-not-excluded add(long, long) */ && leftNodeValue instanceof Long) {
				long leftNodeValue_ = (long) leftNodeValue;
				if (rightNodeValue instanceof Long) {
					long rightNodeValue_ = (long) rightNodeValue;
					this.state_ = state = state | 0b1 /* add-active add(long, long) */;
					try {
						lock.unlock();
						hasLock = false;
						return add(leftNodeValue_, rightNodeValue_);
					} catch (ArithmeticException ex) {
						// implicit transferToInterpreterAndInvalidate()
						lock.lock();
						try {
							this.exclude_ = this.exclude_ | 0b1 /* add-excluded add(long, long) */;
							this.state_ = this.state_ & 0xfffffffe /* remove-active add(long, long) */;
						} finally {
							lock.unlock();
						}
						return executeAndSpecialize(leftNodeValue_, rightNodeValue_);
					}
				}
			}
			{
				int sLBigNumberCast0;
				if ((sLBigNumberCast0 = SLTypesGen.specializeImplicitSLBigNumber(leftNodeValue)) != 0) {
					SLBigNumber leftNodeValue_ = SLTypesGen.asImplicitSLBigNumber(sLBigNumberCast0, leftNodeValue);
					int sLBigNumberCast1;
					if ((sLBigNumberCast1 = SLTypesGen.specializeImplicitSLBigNumber(rightNodeValue)) != 0) {
						SLBigNumber rightNodeValue_ = SLTypesGen.asImplicitSLBigNumber(sLBigNumberCast1, rightNodeValue);
						state = (state | (sLBigNumberCast0 << 4) /* set-implicit-active 0:SLBigNumber */);
						state = (state | (sLBigNumberCast1 << 6) /* set-implicit-active 1:SLBigNumber */);
						this.state_ = state = state | 0b10 /* add-active add(SLBigNumber, SLBigNumber) */;
						lock.unlock();
						hasLock = false;
						return add(leftNodeValue_, rightNodeValue_);
					}
				}
			}
			if ((isString(leftNodeValue, rightNodeValue))) {
				this.state_ = state = state | 0b100 /* add-active add(Object, Object) */;
				lock.unlock();
				hasLock = false;
				return add(leftNodeValue, rightNodeValue);
			}
			this.state_ = state = state | 0b1000 /* add-active typeError(Object, Object) */;
			lock.unlock();
			hasLock = false;
			return typeError(leftNodeValue, rightNodeValue);
		} finally {
			if (oldState != 0 || oldExclude != 0) {
				checkForPolymorphicSpecialize(oldState, oldExclude);
			}
			if (hasLock) {
				lock.unlock();
			}
		}
	}

	protected Object typeError(Object left, Object right) {
		throw SLException.typeError(it, left, right);
	}

	private void checkForPolymorphicSpecialize(int oldState, int oldExclude) {
		int newState = (this.state_ & 0b1111);
		int newExclude = this.exclude_;
		if ((oldState ^ newState) != 0 || (oldExclude ^ newExclude) != 0) {
			it.reportPolymorphicSpecialize2();
		}
	}

//	@Override
//	public NodeCost getCost() {
//		int state = state_;
//		if ((state & 0b1111) == 0b0) {
//			return NodeCost.UNINITIALIZED;
//		} else if (((state & 0b1111) & ((state & 0b1111) - 1)) == 0 /* is-single-active  */) {
//			return NodeCost.MONOMORPHIC;
//		}
//		return NodeCost.POLYMORPHIC;
//	}



	protected boolean isString(Object a, Object b) {
		return a instanceof String || b instanceof String;
	}

	protected long add(long left, long right) {
		return Math.addExact(left, right);
	}

	protected SLBigNumber add(SLBigNumber left, SLBigNumber right) {
		return new SLBigNumber(left.getValue().add(right.getValue()));
	}

	/**
	 * Specialization for String concatenation. The SL specification says that String concatenation
	 * works if either the left or the right operand is a String. The non-string operand is
	 * converted then automatically converted to a String.
	 * <p>
	 * To implement these semantics, we tell the Truffle DSL to use a custom guard. The guard
	 * function is defined in {@link #isString this class}, but could also be in any superclass.
	 */
	@Specialization(guards = "isString(left, right)")
	@CompilerDirectives.TruffleBoundary
	protected String add(Object left, Object right) {
		return left.toString() + right.toString();
	}
}
