package dev.moongarden.datagen.structure;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.EParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParameterLuaElement {
    private final EParameter param;
    private final ValueDetails type;
    public ParameterLuaElement(EParameter param, List<EClass<?>> visible) {
        this.param = param;
        this.type = valueName(param.rawParameterType());
        if (!param.rawParameterType().raw().isPrimitive() && !param.rawParameterType().raw().isArray() && !visible.contains(param.rawParameterType())) visible.add(param.rawParameterType());
    }

    public void inConstructor(StringBuilder builder) {
        builder.append(param.name()).append(": ").append(param.rawParameterType().simpleName());
    }

    public void inMethodDoc(StringBuilder builder) {
        builder.append("--- @param ").append(param.name()).append(" ").append(type.type());
        if (type.details() != null) {
            builder.append(" ").append(type.details());
        }
        builder.append("\n");
    }

    public void inMethodDeclaration(StringBuilder builder) {
        builder.append(param.name());
    }

    private static final Map<Class<?>, String> types = new HashMap<>();

    static {
        types.put(byte.class, "\\-127 to 128");
        types.put(short.class, "\\-32,768 to 32767");
        types.put(int.class, "\\-2^31 to 2^31-1 OR 0 to 2^32-1");
        types.put(long.class, "\\-2^63 to 2^63-1 OR 0 to 2^64-1");
        types.put(float.class, "Single-precision 32-bit IEEE 754 floating point");
        types.put(double.class, "Double-precision 64-bit IEEE 754 floating point.");
        types.put(char.class, "A string with a single character.");
    }

    public record ValueDetails(String type, String details) {
        public ValueDetails asArray() {
            return new ValueDetails(type+"[]", details == null ? null : "Array contents: " + details);
        }
    }

    public static ValueDetails valueName(EClass<?> clazz) {
        ValueDetails type;
        type = new ValueDetails(clazz.simpleName(), types.getOrDefault(clazz.raw(), null));
        return clazz.raw().isArray() ? type.asArray() : type;
    }
}
