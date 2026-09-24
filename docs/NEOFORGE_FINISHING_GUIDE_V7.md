# NeoForge Finishing Guide V7

## 1. First build pass

Run:

```bash
./gradlew :neoforge:compileJava :neoforge:processResources
```

Fix only the **first real Java error** each time. Do not shotgun-edit multiple APIs at once.

## 2. Priorities when build errors appear

1. event class / method signature mismatches
2. renderer method signature mismatches
3. packet registration generics / codecs
4. NBT save / load signatures
5. inventory menu field or slot API differences

## 3. First in-game validation pass

Use creative mode and a single-player dev instance first.

Recommended manual script:
- give trident + custom enchanted book
- create Aspect via anvil
- throw / recall / pull
- dimension change
- logout while tether is active

## 4. After first successful run

- decide whether `Sharpness` already works through vanilla effect components
- if not, add explicit melee damage hook
- move constants to a synced Balm config
- harden Fabric parity or drop Fabric temporarily from the build if NeoForge is the only immediate target
