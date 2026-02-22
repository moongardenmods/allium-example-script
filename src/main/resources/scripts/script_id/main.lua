print("hello world!")

script:registerReloadable("reloadable")

local definition = {}
function definition:loadLevel()
    -- This code is injected into the start of MinecraftServer.loadLevel()V
end

mixin.get("minecraft_server_mixin"):define(definition)