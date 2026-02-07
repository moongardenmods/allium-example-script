local MinecraftServerMixin = mixin.to("net.minecraft.server.MinecraftServer")
MinecraftServerMixin:createInjectMethod("loadLevel", {
    mixin.annotation.inject({ at = { { "HEAD" } }, method = { "loadLevel()V" } })
})
MinecraftServerMixin:build()