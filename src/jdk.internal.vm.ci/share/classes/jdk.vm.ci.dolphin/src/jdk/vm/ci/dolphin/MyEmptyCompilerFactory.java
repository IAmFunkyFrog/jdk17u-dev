package jdk.vm.ci.dolphin;

import jdk.vm.ci.code.CompilationRequest;
import jdk.vm.ci.runtime.JVMCICompiler;
import jdk.vm.ci.runtime.JVMCICompilerFactory;
import jdk.vm.ci.runtime.JVMCIRuntime;

import jdk.vm.ci.code.CompilationRequestResult;

public class MyEmptyCompilerFactory implements JVMCICompilerFactory {

    public MyEmptyCompilerFactory() {
    }

    @Override
    public String getCompilerName() {
        return "MyEmptyCompilerFactory";
    }

    @Override
    public JVMCICompiler createCompiler(JVMCIRuntime rt) {
        return new MyEmptyCompiler();
    }

}

