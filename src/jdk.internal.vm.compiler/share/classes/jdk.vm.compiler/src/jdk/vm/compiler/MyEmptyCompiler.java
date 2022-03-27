package jdk.vm.compiler;

import jdk.vm.ci.amd64.AMD64;
import jdk.vm.ci.amd64.AMD64Kind;
import jdk.vm.ci.code.*;
import jdk.vm.ci.code.site.DataSectionReference;
import jdk.vm.ci.meta.*;
import jdk.vm.compiler.test.TestAssembler;
import jdk.vm.compiler.test.TestHotSpotVMConfig;
import jdk.vm.compiler.test.amd64.AMD64TestAssembler;
import jdk.vm.ci.hotspot.*;
import jdk.vm.ci.runtime.JVMCIBackend;
import jdk.vm.ci.runtime.JVMCICompiler;

public class MyEmptyCompiler implements JVMCICompiler {
    private final JVMCIBackend backend = HotSpotJVMCIRuntime.runtime().getHostJVMCIBackend();
    private final HotSpotCodeCacheProvider codeCache = (HotSpotCodeCacheProvider) backend.getCodeCache();

    private InstalledCode compileTest(TestAssembler asm, HotSpotResolvedJavaMethod resolvedJavaMethod, HotSpotCompilationRequest compilationRequest) {
        asm.emitPrologue();

        Register arg0 = asm.emitIntArg0();
        RegisterValue argRegisterValue = arg0.asValue(asm.valueKindFactory.getValueKind(JavaKind.Int));
        BytecodeFrame bytecodeFrame = new BytecodeFrame(null, resolvedJavaMethod, 0, false, false, new JavaValue[]{ argRegisterValue }, new JavaKind[]{ JavaKind.Int }, 1, 0, 0);
        DebugInfo info = new DebugInfo(bytecodeFrame);
        info.setReferenceMap(new HotSpotReferenceMap(new Location[]{ Location.register(arg0) }, new Location[]{ null }, new int[]{ argRegisterValue.getValueKind().getPlatformKind().getSizeInBytes() }, 8));

        asm.code.emitByte((byte)0x89);
        asm.code.emitByte((byte)0x75);
        asm.code.emitByte((byte)0xec);

        asm.code.emitByte((byte)0x83);

        asm.code.emitByte((byte)0x7d);
        asm.code.emitByte((byte)0xec);

        asm.code.emitByte((byte)0x0);

        asm.code.emitByte((byte)0x75);
        asm.code.emitByte((byte)0x7);

        asm.emitIntRet(arg0);
        asm.emitTrap(info);

        asm.emitEpilogue();
        HotSpotCompiledCode code = asm.finish(resolvedJavaMethod, compilationRequest);
        return codeCache.setDefaultCode(resolvedJavaMethod, code);
    }

    @Override
    public CompilationRequestResult compileMethod(CompilationRequest request) {
        HotSpotCompilationRequest hotSpotCompilationRequest = (HotSpotCompilationRequest) request;
        HotSpotResolvedJavaMethod resolvedJavaMethod = hotSpotCompilationRequest.getMethod();

        if (resolvedJavaMethod.getName().equals("test")) {
            compileTest(new AMD64TestAssembler(codeCache, new TestHotSpotVMConfig(HotSpotJVMCIRuntime.runtime().getConfigStore())), resolvedJavaMethod, (HotSpotCompilationRequest) request);
            return HotSpotCompilationRequestResult.success(0);
        } else return HotSpotCompilationRequestResult.failure("Empty compiler test failure", false);
    }

    @Override
    public boolean isGCSupported(int gcIdentifier) {
        return false;
    }
}
