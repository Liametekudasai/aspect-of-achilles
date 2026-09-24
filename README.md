# Aspect of Achilles

**Version actuelle : 1.1.2**

Aspect of Achilles est un mod Minecraft centré sur le trident. Il transforme l'arme en projectile relié au joueur par une corde visuelle, avec rappel manuel, limite de portée, traction du joueur et compatibilités d'enchantements supplémentaires.

## État du dépôt

Ce dépôt est une **base multi-loader Balm** pour **Minecraft 1.21.1**. Le travail le plus avancé est côté **NeoForge** ; la couche Fabric reste surtout structurelle.

### Modules

- `common` : logique métier partagée, ressources, enchantements JSON, renderer commun de la corde
- `neoforge` : bootstrap NeoForge, événements, réseau, enregistrement d'entité
- `fabric` : bootstrap minimal pour la future parité loader
- `build-logic` : conventions Gradle multi-loader

## Fonctionnalités actuellement couvertes dans le code

### Déjà branché

- enchantements data-driven `Obedience` et `Aspect of Achilles`
- fusion enclume `Obedience + Riptide -> Aspect of Achilles`
- application d'enchantements de compatibilité `Sharpness` / `Power` sur trident via enclume
- détection du trident lancé
- état runtime du tether par joueur
- entité légère `TetherEntity` côté serveur pour la synchro des extrémités
- rendu client d'une corde segmentée inspirée du leash vanilla
- rappel du trident par clic droit
- pull d'Achille par clic gauche, maintenu sur plusieurs ticks pour une sensation de vitesse constante
- retour forcé en cas de changement de dimension / déconnexion / disparition du trident
- garde-fous d'inventaire pour le slot réservé
- bonus de dégâts projectile pour `Power`

### Encore à valider en build réel

- signatures exactes de certaines APIs NeoForge 1.21.1
- derniers détails du verrouillage de slot dans tous les écrans
- bonus de mêlée `Sharpness` après test réel pour éviter un double comptage
- couche Fabric équivalente à NeoForge
- polissage du renderer et des cas réseau rares

## Structure du code importante

```text
common/src/main/java/com/liametekudasai/aspectofachilles/
├─ client/render/TetherEntityRenderer.java
├─ enchantment/
│  ├─ AnvilFusionService.java
│  ├─ TridentDamageService.java
│  └─ TridentEnchantmentHelper.java
├─ entity/TetherEntity.java
├─ network/TetherActionPayload.java
└─ tether/
   ├─ AchillesPullController.java
   ├─ TetherConstants.java
   ├─ TetherMode.java
   ├─ TetherPlayerState.java
   ├─ TetherRuntime.java
   └─ TetherService.java
```

## Build local recommandé

Premier passage :

```bash
./gradlew :neoforge:compileJava :neoforge:processResources
```

Puis build complet :

```bash
./gradlew :neoforge:build
```

## Fichiers de suivi

- `IMPLEMENTATION_STATUS.md` : vue d'ensemble rapide
- `BUILD_NOTES_V6.md` : remarques sur les risques de build déjà repérés
- `docs/ARCHITECTURE_V7.md` : architecture détaillée du prototype
- `docs/TEST_MATRIX_V7.md` : checklist de tests en jeu
- `docs/NEOFORGE_FINISHING_GUIDE_V7.md` : ordre recommandé pour finir la version NeoForge

## Position honnête sur cette archive

Ce ZIP est maintenant **plus qu'un simple squelette**, mais ce n'est toujours pas un mod fini prêt à publier. C'est une base **fortement densifiée** conçue pour vous faire gagner du temps au moment du vrai build local, puis des premières corrections de compilation et des tests en jeu.
