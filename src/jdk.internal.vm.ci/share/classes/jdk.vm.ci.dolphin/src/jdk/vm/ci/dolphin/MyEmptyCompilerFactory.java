package jdk.vm.ci.dolphin;

import jdk.vm.ci.code.CompilationRequest;
import jdk.vm.ci.runtime.JVMCICompiler;
import jdk.vm.ci.runtime.JVMCICompilerFactory;
import jdk.vm.ci.runtime.JVMCIRuntime;

import jdk.vm.ci.code.CompilationRequestResult;

//my own
public class MyEmptyCompilerFactory implements JVMCICompilerFactory, JVMCICompiler {

    public MyEmptyCompilerFactory() {
    }

    @Override
    public CompilationRequestResult compileMethod(CompilationRequest request) {
        System.out.println(request.getMethod().getName());
        return null;
    }

    @Override
    public String getCompilerName() {
        return "MyEmptyCompilerFactory";
    }

    @Override
    public JVMCICompiler createCompiler(JVMCIRuntime rt) {
        return this;
    }

    @Override
    public boolean isGCSupported(int gcIdentifier) {
        return false;
    }
}

