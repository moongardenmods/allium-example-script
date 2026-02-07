package dev.moongarden.datagen.structure;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.EConstructor;

import java.util.List;

public class ConstructorLuaElement extends AbstractExecutableLuaElement {
    private final EClass<?> parent;
    public ConstructorLuaElement(EConstructor<?> constructor, List<EClass<?>> visible) {
        super(constructor, visible);
        this.parent = constructor.declaringClass();
    }

    @Override
    public boolean addFirst() {
        return true;
    }

    @Override
    public void build(StringBuilder builder) {
        builder.append("--- @overload fun(");
        if (!parameters.isEmpty()) {
            ParameterLuaElement last = parameters.getLast();
            for (ParameterLuaElement parameter : parameters) {
                parameter.inConstructor(builder);
                if (!last.equals(parameter)) builder.append(", ");
            }
        }
        builder.append("): ").append(parent.simpleName()).append("\n");
    }
}
