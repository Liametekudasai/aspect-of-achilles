# Progress Notes V5

This pass focused on NeoForge hardening rather than adding more unfinished systems.

## What changed

- Added a stronger common bootstrap path so `ModRegistries.initialize()` is now called from the shared entrypoint.
- Added an extra NeoForge inventory safety layer using `ItemStackedOnOtherEvent` to block carried-stack GUI swaps into the locked trident slot.
- Hardened inventory sync by broadcasting both `inventoryMenu` and `containerMenu` changes after forced slot moves / trident return.
- Marked the entity renderer registration subscriber as client-only to avoid accidental dedicated-server classloading of client render classes.
- Refined the rope renderer anchor so the tether starts closer to the player's main hand instead of the torso center.

## Why this matters

The biggest practical NeoForge risks left in V4 were not feature gaps but small compatibility hazards:
- client-only renderer registration being visible to non-client classloading
- GUI slot stacking bypassing the reserved trident slot rule
- inventory refresh being too dependent on the currently open menu

Those three points are now tighter in V5.

## Still not validated here

This environment still cannot download Gradle/NeoForge dependencies, so the project has not been compiled in-container.
