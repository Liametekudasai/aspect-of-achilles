# Project Structure

## Modules

### common
Shared Java code and resources.

Planned packages:
- `enchantment` — enchantment registration and rules
- `tether` — tether state, recall, pull mechanics
- `entity` — lightweight rope/tether entity
- `network` — common payload definitions
- `event` — event-based gameplay hooks
- `client.render` — shared renderer registration targets and render helpers

### fabric
Contains:
- Fabric `ModInitializer`
- Fabric `ClientModInitializer`
- `fabric.mod.json`

### neoforge
Contains:
- NeoForge `@Mod` entrypoint
- NeoForge client entrypoint
- `META-INF/neoforge.mods.toml`

## Conventions used in this ZIP

- Shared code lives only in `common`
- Loader modules should stay thin
- New gameplay systems should be added in `common` first
- Platform-specific differences should be isolated to loader modules only when absolutely necessary
