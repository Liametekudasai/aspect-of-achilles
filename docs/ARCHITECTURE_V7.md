# Architecture V7

## Gameplay loop

1. A vanilla `ThrownTrident` enters the world.
2. NeoForge event wiring inspects the trident stack.
3. If the stack has `Obedience` or `Aspect of Achilles`, the mod:
   - creates a `TetherEntity`
   - records a `TetherPlayerState`
   - reserves the inventory slot used to throw the trident
4. On each server tick, `TetherService`:
   - keeps the slot reserved
   - verifies owner / trident validity
   - enforces max rope distance
   - processes queued recall / Achilles pull actions
   - returns the trident if the tether becomes invalid
5. On the client, `TetherEntityRenderer` renders a rope strip between the player and the trident.

## Why the state is runtime-only for now

For the current prototype, keeping state in `TetherRuntime` is simpler than introducing a persistent attachment layer while the NeoForge build is still not validated locally here. Once the local build is green, moving to a data attachment or capability-like abstraction will make sense.

## Why Achilles pull lasts multiple ticks

A single velocity injection often feels weak or inconsistent under server movement correction. The V7 draft therefore refreshes the pull over a short burst of ticks so the player motion better matches the intended “constant speed straight to the trident” behavior.

## Inventory strategy

The slot is treated as reserved while the trident is out:
- if the original slot becomes occupied, the occupant is pushed elsewhere
- if no free slot exists, the item is dropped
- when the tether ends, the trident tries to return to its original slot first

## Rendering strategy

The mod does not replace the trident model. Instead, it introduces a tiny helper entity whose only real purpose is:
- sync endpoints to the client
- provide something stable to render
- keep rope visuals independent from the trident entity class itself

## Suggested next refactors after first successful build

- migrate constants to Balm config
- migrate runtime state to a proper attachment abstraction
- add client HUD/overlay for tether distance and mode
- finish Fabric parity
- add automated game-test coverage if the chosen workspace supports it cleanly
