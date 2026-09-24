# Build notes for V6

Cette archive est pensée pour être la meilleure base NeoForge 1.21.1 possible sans compilation réseau dans ce conteneur.

## Priorités de cette V6

- suppression d'un double-enregistrement potentiel des handlers d'événements NeoForge
- renderer de corde rendu moins dépendant de helpers d'interpolation susceptibles de varier selon mappings/API
- helpers d'enchantements rendus plus prudents pour `Sharpness` / `Power`
- garde anti-duplication sur l'initialisation d'un tether déjà suivi

## Commande locale recommandée

```bash
./gradlew :neoforge:build
```

Ou pour aller plus vite au premier passage :

```bash
./gradlew :neoforge:compileJava :neoforge:processResources
```

## Si ça casse

Les zones les plus susceptibles de demander un petit ajustement local sont :

1. signatures exactes de certains events NeoForge 1.21.1
2. API exacte du renderer / `VertexConsumer` selon vos mappings téléchargés
3. détails Balm côté client sur NeoForge

Le plus utile sera de regarder la **première erreur Java réelle** du build, pas les cascades qui suivent.
