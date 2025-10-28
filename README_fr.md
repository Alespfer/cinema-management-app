<!-- Language Navigation -->
<div align="right">
  <a href="./README.md">English</a> | <b><a href="./README_fr.md">Français</a></b> | <a href="./README_es.md">Español</a>
</div>

# Application Java de Gestion de Cinéma

[![License: MIT](https://img.shields.io/badge/Licence-MIT-blue.svg)](https://opensource.org/licenses/MIT)
![Language](https://img.shields.io/badge/Langage-Java_21-orange)
![UI](https://img.shields.io/badge/UI-Java_Swing-blue)
![Build](https://img.shields.io/badge/Build-Maven-red)

Une application de bureau complète pour la gestion de cinéma, développée dans le cadre d'un projet universitaire. Cette application, entièrement conçue en **Java** avec le framework **Swing** pour l'interface graphique, fournit une solution complète à la fois pour les clients et les administrateurs du cinéma. Elle est structurée selon une robuste **architecture 3-tiers** et utilise **Maven** pour la gestion du projet.

![Vue Client - Détails du Film](img/client-view-film.png)

## Table des matières
- [À propos du projet](#à-propos-du-projet)
- [Fonctionnalités principales](#fonctionnalités-principales)
- [Architecture et conception](#architecture-et-conception)
- [Stack technique](#stack-technique)
- [Démarrage rapide](#démarrage-rapide)
- [Remerciements](#remerciements)
- [Contact](#contact)
- [Licence](#licence)

## À propos du projet
Ce projet simule un système complet de gestion de cinéma. Il propose deux interfaces distinctes :
1.  Une **application client** où les utilisateurs peuvent consulter les films, voir les horaires des séances, réserver des places et gérer leur compte.
2.  Un **panneau d'administration** offrant un contrôle total sur le catalogue du cinéma (films, séances, salles), les ventes et la gestion du personnel.

L'objectif principal était d'appliquer les concepts fondamentaux du génie logiciel, notamment l'architecture en couches, les patrons de conception (DAO), et la gestion manuelle de l'intégrité des données sans système de base de données relationnelle. La persistance des données est réalisée par la sérialisation d'objets Java dans des fichiers `.dat`.

## Fonctionnalités principales

### 🎬 Côté Client
- **Gestion de compte :** Inscription, connexion, mise à jour des informations et suppression de compte.
- **Consultation de la programmation :** Affichage de la liste des films et de leurs séances, avec des options de filtrage (par date, titre, genre).
- **Sélection de sièges interactive :** Une interface graphique pour choisir ses places dans la salle, avec mise à jour du prix en temps réel.
- **Processus de réservation :** Un parcours de réservation complet, incluant l'ajout de snacks et un formulaire de paiement simulé.
- **Historique des réservations :** Les utilisateurs peuvent consulter leurs réservations passées et à venir, et les annuler si nécessaire.
- **Avis sur les films :** Possibilité de laisser et de modifier des notes et commentaires sur les films.

### ⚙️ Côté Administrateur
- **Opérations CRUD complètes :** Les administrateurs peuvent ajouter, modifier et supprimer des films, genres, séances, salles et tarifs.
- **Gestion des snacks et de l'inventaire :** Gérer le catalogue des produits de snacking et leur stock.
- **Point de vente (PDV) :** Une interface dédiée aux employés pour gérer les ventes de snacks au comptoir.
- **Gestion du personnel et des plannings :** Gérer les comptes des employés, leurs rôles et leurs horaires de travail.
- **Rapports de ventes :** Consulter des rapports détaillés sur les réservations et les ventes pour suivre l'activité commerciale.

## Architecture et conception
L'application est fondée sur une **architecture 3-Tiers** classique pour assurer une séparation claire des responsabilités, rendant le code modulaire, facile à maintenir et évolutif.

1.  **La couche de présentation (View) :**
    *   Réalisée avec **Java Swing** à l'aide de l'éditeur graphique de NetBeans.
    *   Gère toutes les interactions avec l'utilisateur via divers `JPanel` organisés par un `CardLayout` pour la navigation client et un `JTabbedPane` pour l'administration.
    *   Elle délègue toutes les actions à la couche de service et ne contient aucune logique métier.

2.  **La couche de service (Service) :**
    *   Le cœur fonctionnel de l'application. Elle orchestre les opérations et applique toutes les règles métier (ex: vérifier la disponibilité d'un siège, valider un mot de passe, empêcher les conflits d'horaires).
    *   Sert de point d'entrée unique pour la couche de présentation et coordonne les appels à la couche d'accès aux données.

3.  **La couche d'accès aux données (DAO) :**
    *   Responsable de la persistance des données. Au lieu d'une base de données SQL, les données sont persistées par **sérialisation et désérialisation d'objets Java** dans des fichiers `.dat`.
    *   Cette couche implémente le patron de conception **Data Access Object (DAO)**, abstrayant la source de données du reste de l'application. Cette modularité permet de remplacer la méthode de persistance (par exemple par une base de données SQL) avec un minimum de modifications.

## Stack technique
- **Langage :** Java 21 (LTS)
- **Framework UI :** Java Swing
- **Gestion de projet :** Apache Maven
- **IDE :** Développé avec NetBeans

## Démarrage rapide
Pour exécuter ce projet, vous aurez besoin d'un JDK (Java Development Kit) et de Maven.

1.  **Prérequis :**
    *   JDK 21 ou supérieur.
    *   Apache Maven.
    *   Un IDE Java qui supporte les projets Maven (ex: NetBeans, IntelliJ IDEA, Eclipse).

2.  **Clonez le dépôt :**
    ```bash
    git clone https://github.com/Alespfer/cinema-management-app.git
    ```

3.  **Ouvrez le projet dans votre IDE :**
    *   Ouvrez votre IDE et choisissez "Ouvrir un Projet".
    *   Naviguez jusqu'au dossier cloné et sélectionnez-le. L'IDE devrait le reconnaître comme un projet Maven et résoudre les dépendances.

4.  **Lancez l'application :**
    *   Le projet contient une classe `DataInitializer.java` qui peuple automatiquement le système avec des données d'exemple (films, utilisateurs, etc.) si le dossier `/data` est manquant au premier lancement.
    *   Trouvez la classe principale de l'application client (ex: `FenetrePrincipaleClient.java`) et exécutez-la depuis votre IDE.

## Remerciements
- Ce projet a été développé par Alberto ESPERON et Axelle MORICE.
- Dans le cadre du Master "Projets Informatiques et Stratégie d'Entreprise (PISE)" de l'Université Paris Cité.

## Contact

Alberto Esperon - [LinkedIn](https://www.linkedin.com/in/alberto-espfer) - [Profil GitHub](https://github.com/Alespfer)

## Licence

Distribué sous la Licence MIT. Voir le fichier `LICENSE` pour plus d'informations.
