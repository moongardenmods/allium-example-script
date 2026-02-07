package dev.moongarden.datagen.structure;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.EExecutable;
import me.basiqueevangelist.enhancedreflection.api.EParameter;

import java.util.List;
import java.util.Set;

public abstract class AbstractExecutableLuaElement implements LuaElement {
    protected final List<ParameterLuaElement> parameters;
    protected final EClass<?> parent;

    public AbstractExecutableLuaElement(EExecutable executable, List<EClass<?>> visible) {
        parameters = executable.parameters().stream().map((p) -> new ParameterLuaElement(p, visible)).toList();
        parent = executable.declaringClass();
    }
}
