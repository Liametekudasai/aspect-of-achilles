# Progress Notes V4

## Main goal of this pass
This pass focused on two things:
1. Replace the placeholder tether rendering with a real rope render.
2. Move the client/entity code closer to NeoForge 1.21.1 compatibility instead of the newer render-state APIs.

## What changed
- Replaced the placeholder `TetherEntityRenderer` with an old-style `EntityRenderer<TetherEntity>` implementation.
- Added a rope render that draws a segmented curved strip using `RenderType.leash()`.
- Removed the now-invalid `TetherRenderState` class from the common sources.
- Reverted `TetherEntity` save/load hooks back to `CompoundTag`, which is much more plausible for a 1.21.1 target.
- Kept the server-side tether entity design intact: the server still spawns a helper entity, and the client now renders an actual rope from it.

## Why this matters
The previous renderer draft targeted the newer render-state pipeline (`SubmitNodeCollector`, `CameraRenderState`, etc.).
For a NeoForge 1.21.1 target, that was risky. This pass intentionally biases the renderer toward the older and more likely 1.21.1-compatible entity rendering path.

## Remaining likely compatibility checks
These still need a real IDE + Gradle sync to confirm exactly against 1.21.1:
- final event names/signatures on the client click hooks
- exact `VertexConsumer` chaining methods available in the mapped workspace
- any small data-component/enchantment API edge cases in the anvil/enchantment helpers
- full runtime behavior of the leash-style strip under Fabulous/translucent render modes
