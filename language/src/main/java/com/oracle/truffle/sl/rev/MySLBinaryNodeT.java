package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.SLBinaryNode;

import java.util.HashMap;
import java.util.Map;

public abstract class MySLBinaryNodeT implements SLBinaryNodeT {
	private final static Map<SLBinaryNode, SLBinaryNodeT> cache = new HashMap<>();
	private final ExecSLRevisitor alg;
	private final SLBinaryNode it;

	private MySLBinaryNodeT(ExecSLRevisitor alg, SLBinaryNode it) {
		this.alg = alg;
		this.it = it;
	}

	public static SLBinaryNodeT INSTANCE(ExecSLRevisitor alg, SLBinaryNode it) {
		if (!cache.containsKey(it)) cache.put(it, /*new MySLBinaryNodeT(alg, it)*/null);
		return cache.get(it);
	}
}
