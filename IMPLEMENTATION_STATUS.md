# Aspect of Achilles - Implementation Status

## Gameplay status in this archive

### Implemented
- Obedience enchantment data and Loyalty exclusivity
- Aspect of Achilles enchantment data
- Anvil fusion: Obedience + Riptide -> Aspect of Achilles
- Server-spawned lightweight tether entity
- Client rope renderer
- Manual recall on right click, including empty-click payload path
- Achilles pull on left click, including empty-click payload path
- Different rope lengths for Obedience (20) and Achilles (40)
- Hard tether boundary clamp so the trident cannot travel beyond the rope length
- Auto-recall on dimension change / logout cleanup
- Reserved inventory slot enforcement with forced empty-slot maintenance and return-to-original-slot priority
- Power compatibility on thrown-trident damage
- Sharpness compatibility via vanilla enchantable tag expansion for tridents
- Power compatibility via vanilla enchantable tag expansion for tridents

### Still not finished in this archive
- Full Fabric gameplay port; the project is currently configured NeoForge-first for buildability
- Real in-game validation and API cleanup after a local Gradle build
