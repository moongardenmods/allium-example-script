print("hello world!")

mixin.get("loadLevel"):hook(function()
    -- This code is injected into the start of MinecraftServer.loadLevel()V
end)