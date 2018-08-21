package com.oracle.truffle.sl.rev;

import com.oracle.truffle.sl.nodes.SLRootNode;

import java.util.HashMap;
import java.util.Map;

public class MySLRootNodeT implements SLRootNodeT {
	private final static Map<SLRootNode, SLRootNodeT> cache = new HashMap<>();
	private final ExecSLRevisitor alg;
	private final SLRootNode it;

	private MySLRootNodeT(ExecSLRevisitor alg, SLRootNode it) {
		this.alg = alg;
		this.it = it;
	}

	public static SLRootNodeT INSTANCE(ExecSLRevisitor alg, SLRootNode it) {
		if (!cache.containsKey(it)) cache.put(it, new MySLRootNodeT(alg, it));
		return cache.get(it);
	}
}
