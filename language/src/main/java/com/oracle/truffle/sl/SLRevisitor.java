package com.oracle.truffle.sl;

import fr.mleduc.revisitor.annotation.processor.Revisitor;

@Revisitor(packages = {
        "com.oracle.truffle.sl.nodes",
        "com.oracle.truffle.sl.nodes.access",
        "com.oracle.truffle.sl.nodes.call",
        "com.oracle.truffle.sl.nodes.controlflow",
        "com.oracle.truffle.sl.nodes.expression",
        "com.oracle.truffle.sl.nodes.interop",
        "com.oracle.truffle.sl.nodes.local",
        "com.oracle.truffle.sl.builtins"
}, name = "SimpleLanguage")
public class SLRevisitor {

}
