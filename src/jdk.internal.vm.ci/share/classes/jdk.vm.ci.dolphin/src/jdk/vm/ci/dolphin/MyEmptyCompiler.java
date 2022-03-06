package jdk.vm.ci.dolphin;

import jdk.vm.ci.code.CompilationRequest;
import jdk.vm.ci.code.CompilationRequestResult;
import jdk.vm.ci.hotspot.HotSpotCompilationRequestResult;
import jdk.vm.ci.runtime.JVMCICompiler;

public class MyEmptyCompiler implements JVMCICompiler {
    @Override
    public CompilationRequestResult compileMethod(CompilationRequest request) {
        System.out.println(request.getMethod().getName());
        return HotSpotCompilationRequestResult.failure("Empty compiler test failure", false);
    }

    @Override
    public boolean isGCSupported(int gcIdentifier) {
        return false;
    }
}
