# Aspect of Achilles

**Version actuelle : 1.1.2 (avec les changements en cours)**

Aspect of Achilles est un mod Minecraft centré sur le trident. Il transforme l'arme en projectile relié au joueur par une corde visuelle, avec rappel manuel, limite de portée et mobilité configurables.

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
- `Loyauté` est incompatible avec `Obedience`, `Riptide` et `Aspect of Achilles`
- le trident ne reçoit que ses enchantements vanilla habituels, `Obedience`, `Aspect of Achilles` et `Fire Aspect` ; il ne peut donc plus recevoir des enchantements sans rapport, tels que `Sweeping Edge`
- `Fire Aspect` est applicable au trident et enflamme les cibles au lancer comme en mêlée
- `Empalement` augmente les dégâts de mêlée et de projectile contre tous les mobs, sans condition de type ou de météo
- JEI affiche, depuis le livre `Aspect of Achilles`, l'explication de la fusion `Obedience + Riptide -> Aspect of Achilles`
- détection du trident lancé
- état runtime du tether par joueur
- entité légère `TetherEntity` côté serveur pour la synchro des extrémités
- rendu client d'une corde segmentée inspirée du leash vanilla
- rappel du trident par clic droit
- activation d'Achille par clic droit : une seule impulsion par clic, ou une téléportation au trident selon la configuration serveur
- configuration serveur Balm synchronisée : longueurs maximales de corde, intensité de l'impulsion et choix exclusif téléportation/impulsion
- à la limite de corde, un trident volant est stoppé ; un trident planté reste fixe et le joueur est empêché d'aller plus loin
- retour forcé en cas de changement de dimension / déconnexion / disparition du trident
- garde-fous d'inventaire pour le slot réservé

### Encore à valider en build réel

- signatures exactes de certaines APIs NeoForge 1.21.1
- derniers détails du verrouillage de slot dans tous les écrans
- validation en jeu du bonus universel d'Empalement et de l'application de Fire Aspect
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

Le serveur crée `config/aspectofachilles-server.toml`. Les réglages disponibles sont
`obedienceMaxDistance`, `achillesMaxDistance`, `achillesPullImpulse` et
`teleportToTrident`. Lorsque `teleportToTrident` vaut `true`, aucune impulsion ne sera
appliquée après la téléportation.

## Fichiers de suivi

- `IMPLEMENTATION_STATUS.md` : vue d'ensemble rapide
- `BUILD_NOTES_V6.md` : remarques sur les risques de build déjà repérés
- `docs/ARCHITECTURE_V7.md` : architecture détaillée du prototype
- `docs/TEST_MATRIX_V7.md` : checklist de tests en jeu
- `docs/NEOFORGE_FINISHING_GUIDE_V7.md` : ordre recommandé pour finir la version NeoForge

## Position honnête sur cette archive

Ce ZIP est maintenant **plus qu'un simple squelette**, mais ce n'est toujours pas un mod fini prêt à publier. C'est une base **fortement densifiée** conçue pour vous faire gagner du temps au moment du vrai build local, puis des premières corrections de compilation et des tests en jeu.
