# Changelog

## 1.1.2

- correction de la prise en compte de **Tranchant** sur le trident tenu en main
- ajout d'un modificateur d'attribut pour que le bonus de dégâts de mêlée soit réellement appliqué et visible quand le trident est équipé en main principale
- précision de comportement : **Puissance** continue d'augmenter uniquement les dégâts du trident lancé et ne modifie pas l'attaque de mêlée affichée en main

## 1.1.1
- Correction d'un bug de duplication du trident après la mort du joueur.
- Lors d'un rappel forcé après mort, le service tente désormais de récupérer le `ThrownTrident` réel encore présent dans le monde avant de rendre l'objet.
- Suppression de l'entité projectile lors du retour forcé pour éviter qu'un second trident reste récupérable au sol après le respawn.

## 1.1.0
- Finalisation du build NeoForge 1.21.1 jusqu'à obtention d'un build réussi.
- Intégration complète du cycle de vie du tether : rappel manuel, rappel inter-dimension et rappel après mort.
- Correction du rappel du trident lorsqu'il est planté dans un bloc ou au sol.
- Amélioration de la gestion du slot réservé pendant le tether.
- Mise en place du rendu de corde côté client via entité dédiée et renderer custom.
- Implémentation de la fusion `Obedience + Riptide -> Aspect of Achilles`.
- Activation de la mécanique de traction du joueur vers le trident pour `Aspect of Achilles`.
- Ajout de la prise en charge de `Power` sur les dégâts du trident lancé.
- Stabilisation des ressources et des métadonnées de build.

## 1.0.0
- Restructured the project from the NeoForge 1.21.1 MDK into a Balm-based multi-loader workspace.
- Added `common`, `fabric`, and `neoforge` modules.
- Moved the old single-loader bootstrap into a shared common initialization flow.
- Added loader-specific entrypoints for Fabric and NeoForge.
- Added scaffolding for the future trident tether and enchantment systems.
