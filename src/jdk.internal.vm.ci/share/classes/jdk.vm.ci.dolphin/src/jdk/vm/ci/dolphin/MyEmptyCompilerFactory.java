package jdk.vm.ci.dolphin;

import jdk.vm.ci.hotspot.HotSpotJVMCICompilerFactory;
import jdk.vm.ci.runtime.JVMCICompiler;
import jdk.vm.ci.runtime.JVMCICompilerFactory;
import jdk.vm.ci.runtime.JVMCIRuntime;

public class MyEmptyCompilerFactory extends HotSpotJVMCICompilerFactory {

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

