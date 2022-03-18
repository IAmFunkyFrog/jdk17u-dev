package jdk.vm.compiler;

import jdk.vm.ci.amd64.AMD64;
import jdk.vm.ci.code.*;
import jdk.vm.ci.code.site.DataSectionReference;
import jdk.vm.ci.meta.ConstantPool;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.JavaField;
import jdk.vm.ci.meta.ResolvedJavaField;
import jdk.vm.compiler.test.TestAssembler;
import jdk.vm.compiler.test.TestHotSpotVMConfig;
import jdk.vm.compiler.test.amd64.AMD64TestAssembler;
import jdk.vm.ci.hotspot.*;
import jdk.vm.ci.runtime.JVMCIBackend;
import jdk.vm.ci.runtime.JVMCICompiler;

import java.util.Arrays;

public class MyEmptyCompiler implements JVMCICompiler {
    private final JVMCIBackend backend = HotSpotJVMCIRuntime.runtime().getHostJVMCIBackend();
    private final HotSpotCodeCacheProvider codeCache = (HotSpotCodeCacheProvider) backend.getCodeCache();

    private InstalledCode compileStaticGet(TestAssembler asm, HotSpotResolvedJavaMethod resolvedJavaMethod, HotSpotCompilationRequest compilationRequest) {
        asm.emitPrologue();

        ConstantPool constantPool = resolvedJavaMethod.getConstantPool();
        HotSpotResolvedJavaField javaField = (HotSpotResolvedJavaField) constantPool.lookupField(1, resolvedJavaMethod, 178);
        JavaConstant javaConstant = backend.getConstantReflection().readFieldValue(javaField, null);
        Register ret = asm.emitLoadInt(javaConstant.asInt());
        asm.emitIntRet(ret);

        asm.emitEpilogue();
        HotSpotCompiledCode code = asm.finish(resolvedJavaMethod, compilationRequest);
        return codeCache.setDefaultCode(resolvedJavaMethod, code);
    }

    @Override
    public CompilationRequestResult compileMethod(CompilationRequest request) {
        HotSpotCompilationRequest hotSpotCompilationRequest = (HotSpotCompilationRequest) request;
        HotSpotResolvedJavaMethod resolvedJavaMethod = hotSpotCompilationRequest.getMethod();

        if (resolvedJavaMethod.getName().equals("getField")) {
            compileStaticGet((TestAssembler) new AMD64TestAssembler(codeCache, new TestHotSpotVMConfig(HotSpotJVMCIRuntime.runtime().getConfigStore())), resolvedJavaMethod, (HotSpotCompilationRequest) request);
            return HotSpotCompilationRequestResult.success(0);
        } else return HotSpotCompilationRequestResult.failure("Empty compiler test failure", false);
    }

    @Override
    public boolean isGCSupported(int gcIdentifier) {
        return false;
    }
}
