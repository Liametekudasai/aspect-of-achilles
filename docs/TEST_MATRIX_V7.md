# Test Matrix V7

## Basic tether lifecycle

- Throw a trident with `Obedience`
- Confirm rope appears
- Confirm original hotbar slot stays reserved
- Recall mid-flight with right click
- Confirm trident returns to the original slot

## Achilles pull

- Throw a trident with `Aspect of Achilles`
- Press left click while the trident is in flight
- Confirm player is pulled in a straight line
- Confirm pull stops near the trident
- Confirm repeated left clicks do not duplicate the tether

## Range limits

- Throw beyond 20 blocks with `Obedience`
- Confirm tether switches to recall behavior
- Throw beyond 40 blocks with `Aspect of Achilles`
- Confirm tether switches to recall behavior

## Inventory edge cases

- Open inventory while tether is active
- Try to place an item into the locked slot
- Try mouse-stack interactions on that slot
- Confirm intruding items are rejected or moved elsewhere

## World / lifecycle edge cases

- Change dimension while tether is active
- Disconnect while tether is active
- Die while tether is active
- Force chunk unload or teleport away from the trident
- Confirm the trident is not lost or duplicated

## Enchantment compatibility

- Apply `Power` book to trident in an anvil
- Confirm thrown damage increases
- Apply `Sharpness` book to trident in an anvil
- Verify whether vanilla already grants melee damage in 1.21.1
- If not, add explicit melee damage hook afterwards
