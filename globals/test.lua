---@class Test: InstanceUserdata
local Test = {}

local ChunkPos = require("net.minecraft.world.level.ChunkPos")
local pos = ChunkPos(0, 0)


---@class TestClass: ClassUserdata
---@overload fun(): Test
local TestClass = {}

function Test:a() end

return TestClass