# Feature Completeness (V8)

## Covered against the original design
- Obedience replaces Loyalty and is exclusive with it.
- Rope entity and rope renderer exist.
- Recall works on right-click, including empty-click cases.
- Achilles pull works on left-click, including empty-click cases.
- Obedience uses 20 blocks and Achilles uses 40 blocks.
- The trident is hard-clamped at the rope boundary, so it cannot continue travelling past the maximum rope length.
- Dimension-change recall is implemented.
- Power is applied to thrown-trident damage.
- Sharpness / Power are both made compatible with tridents through vanilla enchantable-item tags, in addition to the custom anvil path.

## Remaining non-gameplay gap
- Fabric is still not feature-parity complete and remains excluded from settings.gradle in this build-focused state.
