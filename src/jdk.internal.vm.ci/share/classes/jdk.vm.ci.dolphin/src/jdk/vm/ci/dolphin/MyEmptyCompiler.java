package jdk.vm.ci.dolphin;

import jdk.vm.ci.amd64.AMD64;
import jdk.vm.ci.code.*;
import jdk.vm.ci.dolphin.test.TestHotSpotVMConfig;
import jdk.vm.ci.dolphin.test.amd64.AMD64TestAssembler;
import jdk.vm.ci.hotspot.*;
import jdk.vm.ci.meta.ResolvedJavaMethod;
import jdk.vm.ci.runtime.JVMCI;
import jdk.vm.ci.runtime.JVMCIBackend;
import jdk.vm.ci.runtime.JVMCICompiler;

public class MyEmptyCompiler implements JVMCICompiler {
    private final JVMCIBackend backend = HotSpotJVMCIRuntime.runtime().getHostJVMCIBackend();
    private final HotSpotCodeCacheProvider codeCache = (HotSpotCodeCacheProvider) backend.getCodeCache();

    private InstalledCode compileAddMethod(HotSpotResolvedJavaMethod resolvedMethod, HotSpotCompilationRequest hotSpotCompilationRequest) {
        AMD64TestAssembler asm = new AMD64TestAssembler(codeCache, new TestHotSpotVMConfig(HotSpotJVMCIRuntime.runtime().getConfigStore()));
        asm.emitPrologue();
        Register arg0 = asm.emitIntArg0();
        Register arg1 = asm.emitIntArg1();
        Register ret = asm.emitIntAdd(arg0, arg1);
        asm.emitIntRet(ret);
        asm.emitEpilogue();

        HotSpotCompiledCode code = asm.finish(resolvedMethod, hotSpotCompilationRequest);
        return codeCache.setDefaultCode(resolvedMethod, code);
    }

    @Override
    public CompilationRequestResult compileMethod(CompilationRequest request) {
        HotSpotCompilationRequest hotSpotCompilationRequest = (HotSpotCompilationRequest) request;
        HotSpotResolvedJavaMethod resolvedJavaMethod = hotSpotCompilationRequest.getMethod();

        if(resolvedJavaMethod.getName().equals("add") && !resolvedJavaMethod.hasCompiledCodeAtLevel(4)) {
            InstalledCode result = compileAddMethod(resolvedJavaMethod, hotSpotCompilationRequest);
            return HotSpotCompilationRequestResult.success(resolvedJavaMethod.getCodeSize());
        }
        else return HotSpotCompilationRequestResult.failure("Empty compiler test failure", false);
    }

    @Override
    public boolean isGCSupported(int gcIdentifier) {
        return false;
    }
}
