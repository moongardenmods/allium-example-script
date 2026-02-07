package dev.moongarden.datagen;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.moongarden.datagen.structure.ClassLuaElement;
import me.basiqueevangelist.enhancedreflection.api.EClass;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.server.MinecraftServer;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class LuaTemplateGenerator implements DataGeneratorEntrypoint {
    public static final Logger LOGGER = LoggerFactory.getLogger("docgen");

    @Override
    public void onInitializeDataGenerator(@NonNull FabricDataGenerator fabricDataGenerator) {
        List<EClass<?>> visible = new ArrayList<>();
        visible.add(EClass.fromJava(MinecraftServer.class));
        visible.add(EClass.fromJava(MinecraftClient.class));
        Path base = Path.of("./docs");
        try {
            if (Files.exists(base)) {
                LOGGER.info("Clearing docs directory, this may take a while...");
                Files.walkFileTree(base, new FileVisitor<>() {
                    @Override
                    public @NonNull FileVisitResult preVisitDirectory(Path dir, @NonNull BasicFileAttributes attrs) {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public @NonNull FileVisitResult visitFile(Path file, @NonNull BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public @NonNull FileVisitResult visitFileFailed(Path file, @NonNull IOException exc) {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public @NonNull FileVisitResult postVisitDirectory(Path dir, @Nullable IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
            Files.createDirectories(base);
            LOGGER.info(String.valueOf(base.toRealPath()));
            for (int i = 0; i < visible.size(); i++) {
                EClass<?> clazz = visible.get(i);
                    LOGGER.info(clazz.name());
                    ClassLuaElement.from(clazz, visible).write(base);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
