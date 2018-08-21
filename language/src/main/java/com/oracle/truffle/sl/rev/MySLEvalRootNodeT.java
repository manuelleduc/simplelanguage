package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.SLEvalRootNode;

import java.util.HashMap;
import java.util.Map;

public class MySLEvalRootNodeT implements SLEvalRootNodeT {
	private final static Map<SLEvalRootNode, SLEvalRootNodeT> cache = new HashMap<>();
	private final ExecSLRevisitor alg;
	private final SLEvalRootNode it;

	private MySLEvalRootNodeT(ExecSLRevisitor alg, SLEvalRootNode it) {
		this.alg = alg;
		this.it = it;
	}

	public static SLEvalRootNodeT INSTANCE(ExecSLRevisitor alg, SLEvalRootNode it) {
		if (!cache.containsKey(it)) cache.put(it, new MySLEvalRootNodeT(alg, it));
		return cache.get(it);
	}
}
