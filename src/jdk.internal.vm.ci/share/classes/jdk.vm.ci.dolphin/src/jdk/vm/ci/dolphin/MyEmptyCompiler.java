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

    private InstalledCode compileAddMethod(HotSpotResolvedJavaMethod resolvedMethod) {
        AMD64TestAssembler asm = new AMD64TestAssembler(codeCache, new TestHotSpotVMConfig(HotSpotJVMCIRuntime.runtime().getConfigStore()));
        asm.emitPrologue();
        Register arg0 = asm.emitIntArg0();
        Register arg1 = asm.emitIntArg1();
        Register ret = asm.emitIntAdd(arg0, arg1);
        asm.emitIntRet(ret);
        asm.emitEpilogue();

        HotSpotCompiledCode code = asm.finish(resolvedMethod);
        return codeCache.addCode(resolvedMethod, code, null, null);
    }

    @Override
    public CompilationRequestResult compileMethod(CompilationRequest request) {
        HotSpotResolvedJavaMethod resolvedJavaMethod = ((HotSpotCompilationRequest) request).getMethod();

        if(resolvedJavaMethod.getName().equals("add")) {
            InstalledCode result = compileAddMethod(resolvedJavaMethod);
            try {
                System.out.println("Installed code: " + result.toString() + " " + result.getName() + " " + result.isAlive() + " " + result.executeVarargs(1, 2));
                String str = codeCache.disassemble(result);
                System.out.println(str);
            } catch (Exception e) {

            }
            return HotSpotCompilationRequestResult.success(4);
        }
        return HotSpotCompilationRequestResult.failure("Empty compiler test failure", false);
    }

    @Override
    public boolean isGCSupported(int gcIdentifier) {
        return false;
    }
}
