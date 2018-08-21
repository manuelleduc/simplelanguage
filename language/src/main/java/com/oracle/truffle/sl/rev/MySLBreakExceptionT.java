package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.controlflow.SLBreakException;

import java.util.HashMap;
import java.util.Map;

public class MySLBreakExceptionT implements SLBreakExceptionT {
	private final static Map<SLBreakException, SLBreakExceptionT> cache = new HashMap<>();
	private final ExecSLRevisitor alg;
	private final SLBreakException it;

	private MySLBreakExceptionT(ExecSLRevisitor alg, SLBreakException it) {
		this.alg = alg;
		this.it = it;
	}

	public static SLBreakExceptionT INSTANCE(ExecSLRevisitor alg, SLBreakException it) {
		if (!cache.containsKey(it)) cache.put(it, new MySLBreakExceptionT(alg, it));
		return cache.get(it);
	}
}
