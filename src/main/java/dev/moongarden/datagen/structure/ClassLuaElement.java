package dev.moongarden.datagen.structure;

import me.basiqueevangelist.enhancedreflection.api.EClass;
import me.basiqueevangelist.enhancedreflection.api.ModifierHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClassLuaElement {
    private final List<ConstructorLuaElement> constructors;
    private final List<MethodLuaElement> instanceMethods;
    private final List<FieldLuaElement> instanceFields;
    private final List<MethodLuaElement> classMethods;
    private final List<FieldLuaElement> classFields;

    private final String fullName;
    private final String name;
    private final EClass<?> superClass;
    private final String packagePath;


    public ClassLuaElement(EClass<?> clazz, List<EClass<?>> visible) {
        fullName = clazz.name();
        name = clazz.simpleName();
        superClass = clazz.superclass();
        packagePath = clazz.packageName().replace(".", "/");
        if (clazz.superclass() != null && !visible.contains(clazz.superclass())) visible.add(clazz.superclass());

        classMethods = new ArrayList<>();
        instanceMethods = new ArrayList<>();
        classFields = new ArrayList<>();
        instanceFields = new ArrayList<>();
        constructors = clazz.constructors().stream().filter(ModifierHolder::isPublic).map((c) -> new ConstructorLuaElement(c, visible)).toList();
        clazz.declaredMethods().stream().filter(ModifierHolder::isPublic).forEach((m) -> (m.isStatic() ? classMethods : instanceMethods).add(new MethodLuaElement(m, visible)));
        clazz.declaredFields().stream().filter(ModifierHolder::isPublic).forEach((f) -> (f.isStatic() ? classFields : instanceFields).add(new FieldLuaElement(f, visible)));
    }

    public void write(Path p) throws IOException {
        // Instance documentation
        StringBuilder builder = new StringBuilder();
        builder.append("--- @meta ").append(fullName).append("\n--- @class ").append(name).append(": ");
        if (superClass == null) {
            builder.append("InstanceUserdata");
        } else {
            builder.append(superClass.simpleName());
        }
        builder.append("\n");
        instanceFields.forEach((f) -> f.build(builder));
        builder.append("local ").append(name).append(" = {}\n\n");
        instanceMethods.forEach((m) -> m.build(builder));

        // Static documentation
        builder.append("--- @class ").append(name).append("Class: ");
        if (superClass == null) {
            builder.append("ClassUserdata");
        } else {
            builder.append(superClass.name()).append("Class");
        }
        builder.append("\n");
        classFields.forEach((f) -> f.build(builder));
        constructors.forEach((c) -> c.build(builder));
        builder.append("local ").append(name).append("Class = {}\n\nreturn ").append(name).append("Class\n");
        classMethods.forEach((m) -> m.build(builder));

        // Save to file
        Path dir = p.resolve(packagePath);
        Files.createDirectories(dir);
        Path save = dir.resolve(name + ".lua");
        Files.writeString(save, builder);
    }

    public static ClassLuaElement from(EClass<?> clazz, List<EClass<?>> visited) {
        return new ClassLuaElement(clazz, visited);
    }
}
