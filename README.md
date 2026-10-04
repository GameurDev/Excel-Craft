# Vildoria

Vildoria est un mod client pour Minecraft qui ajoute un espace de notes sous
forme de tableur directement dans le jeu. Créez des feuilles, saisissez des
valeurs ou des formules, puis enregistrez-les dans votre dossier de configuration.

## Fonctionnalités

| Fonctionnalité | Description |
| --- | --- |
| Feuilles de calcul | Grille de 50 lignes sur 50 colonnes, avec navigation au clavier et à la souris. |
| Valeurs et formules | Saisie dans la cellule sélectionnée ; les formules commencent par `=`. |
| Références de cellules | Réutilisez une cellule avec une référence comme `A1` ou une plage comme `A1:B3`. |
| Enregistrement | Créez, ouvrez et enregistrez des feuilles au format `.vnotes`. |
| Personnalisation | Réglez la taille, le thème, la police, la densité, la couleur du texte et l’affichage de la grille. |
| Langues | Interface disponible en français et en anglais. |

## Prérequis

| Élément | Version / information |
| --- | --- |
| Minecraft | 26.2 |
| Java | 25 ou supérieur |
| Fabric Loader | 0.19.5 ou supérieur |
| Fabric API | Requis |
| Mod Menu | Facultatif ; ajoute un accès à la configuration du mod. |

## Utilisation

| Action | Comment faire |
| --- | --- |
| Ouvrir les notes | Appuyez sur `N` en jeu. La touche peut être modifiée dans **Options > Contrôles > Vildoria**. |
| Modifier une cellule | Sélectionnez une cellule, puis saisissez son contenu dans le champ de valeur ou de formule. |
| Naviguer dans la grille | Utilisez les flèches du clavier ou cliquez sur une cellule. Faites défiler la grille avec la molette. |
| Gérer une feuille | Utilisez les boutons **Nouveau**, **Ouvrir** et **Enregistrer**. |
| Configurer l’apparence | Ouvrez la configuration de Vildoria depuis Mod Menu, si celui-ci est installé. |

Les feuilles sont enregistrées dans `<dossier Minecraft>/config/vildoria/notes/`.
Leurs paramètres d’affichage sont conservés dans
`<dossier Minecraft>/config/vildoria/settings.properties`.

## Formules

Les formules acceptent les opérateurs arithmétiques, les références de cellules,
les plages et les fonctions ci-dessous.

| Syntaxe | Description | Exemple |
| --- | --- | --- |
| `+`, `-`, `*`, `/` | Addition, soustraction, multiplication et division | `=A1+B1` |
| `^`, `%` | Puissance et pourcentage | `=2^3`, `=25%` |
| `A1` | Référence à une cellule | `=A1*2` |
| `SUM` / `SOMME` | Somme des valeurs | `=SOMME(A1:A5)` |
| `AVERAGE` / `MOYENNE` | Moyenne des valeurs | `=MOYENNE(A1:A5)` |
| `MIN` | Plus petite valeur | `=MIN(A1:A5)` |
| `MAX` | Plus grande valeur | `=MAX(A1:A5)` |
| `COUNT` / `NB` | Nombre de valeurs | `=NB(A1:A5)` |

Les arguments d’une fonction peuvent être séparés par une virgule ou un point-virgule.

## Compilation

| Étape | Commande Windows |
| --- | --- |
| Compiler et exécuter les tests | `.\gradlew.bat build` |
| Exécuter les tests uniquement | `.\gradlew.bat test` |

Le fichier du mod est généré dans `build/libs/`. Pour l’utiliser, placez le JAR
dans le dossier `mods` de votre installation Fabric avec Fabric API installé.

## Licence

Ce projet est distribué sous licence **CUSTOM**. Consultez le fichier
[LICENSE](LICENSE) pour les informations complètes.
