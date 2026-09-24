# Notes de progression V3

## Ce qui a changé dans cette itération
- migration du payload `TetherActionPayload` vers `RegistryFriendlyByteBuf`
- migration de `TetherEntity` vers `ValueInput` / `ValueOutput`
- ajout de `TridentDamageService`
- implémentation réelle du bonus `Power` sur les tridents lancés via `LivingDamageEvent`
- synchronisation d'inventaire renforcée après déplacement / retour du trident
- mise à jour du fichier `IMPLEMENTATION_STATUS.md`

## Pourquoi c'est utile
Ces changements réduisent les risques de casse API sur les versions modernes de NeoForge 1.21.x et rendent le draft plus proche d'un mod réellement compilable. Ils ajoutent aussi une partie gameplay concrète manquante : `Power` a maintenant un effet sur les dégâts à distance du trident, ce que vanilla ne fait pas pour un trident sans intervention du mod.

## Point volontairement laissé prudent
`Sharpness` n'a pas encore reçu un hook explicite de dégâts mêlée. En 1.21+, les enchantements reposent sur des effect components data-driven, donc il faut d'abord vérifier dans un vrai workspace si le simple fait d'autoriser Sharpness sur le trident suffit déjà à activer le bonus vanilla, afin d'éviter un double comptage.
