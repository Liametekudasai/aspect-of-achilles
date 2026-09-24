# Progress notes V6

## What changed

- NeoForge game event handlers are no longer manually registered from the mod constructor when `@EventBusSubscriber` already covers them.
- Tether initialization now ignores a trident already tracked for the same player/runtime state.
- The rope renderer now computes interpolated positions manually from `xo/yo/zo` and current coordinates.
- The compatibility enchant helpers now use `mutable.set(...)` with `max(existing, incoming)` instead of relying on `mutable.upgrade(...)`.

## Why

The goal of V6 is to reduce likely compile/runtime friction for a local NeoForge 1.21.1 build, not to add flashy unfinished systems.
