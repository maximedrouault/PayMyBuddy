# PayMyBuddy

## Introduction

PayMyBuddy est une application Spring Boot conçue pour faciliter les transactions financières entre amis. Elle permet aux utilisateurs de gérer leurs profils, ajouter des connexions, transférer des fonds et suivre les transactions, incluant les commissions appliquées.

## Configuration requise

- Java 17
- Maven pour la gestion des dépendances et l'exécution du projet
- Spring Boot version 3.2.4
- MySQL version 8.0.36

## Technologies Utilisées

- Spring Boot pour faciliter le développement.
- Spring Security pour la gestion de l'authentification et de la sécurisation des accès.
- Hibernate / JPA pour la persistance des données dans la base de données relationnelle.
- Thymeleaf pour la création des vues HTML dynamiques côté serveur.
- MySQL pour stocker les données des utilisateurs, des connexions, des transactions et des commissions.
- Bootstrap pour la conception de l'interface utilisateur, web et mobile.

## Installation

Pour installer et exécuter ce projet, vous aurez besoin de Java, Maven et MySQL. Voici les étapes à suivre :

1. Clonez le dépôt : `git clone https://github.com/maximedrouault/paymybuddy.git`
2. Accédez au répertoire du projet : `cd paymybuddy`

## Configuration

Avant de pouvoir exécuter l'application, vous devez configurer l'accès à la base de données. Modifiez le fichier `src/main/resources/application.properties` pour définir les paramètres de votre base de données MySQL :

```ini
spring.datasource.url=jdbc:mysql://localhost:3306/paymybuddy?serverTimezone=UTC
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
```

Vous devrez également créer une base de données vierge dans MySQL nommée `paymybuddy` :

```sql
CREATE DATABASE paymybuddy;
```

Importez ensuite le schéma et les données de tests dans la base de données en exécutant le script SQL : `src/main/resources/script.sql`.
```shell
mysql -u <your-username> -p paymybuddy < src/main/resources/script.sql
```

## Exécution

Pour exécuter l'application, utilisez la commande Maven suivante :

```shell
mvn spring-boot:run
```

**L'application sera accessible à l'adresse suivante : [http://localhost:8080](http://localhost:8080)**


## Utilisation

### Connexion

Pour vous connecter à l'application, des utilisateurs fictifs sont présents avec les identifiants suivants :

- **Utilisateur 1 :** `user1@example.com`
- **Mot de passe :** `1234`
####
- **Utilisateur 2 :** `user2@example.com`
- **Mot de passe :** `1234`- 
####
- **Utilisateur 3 :** `user3@example.com`
- **Mot de passe :** `1234`
####
- **Utilisateur 4 :** `user4@example.com`
- **Mot de passe :** `1234`
####
- **Utilisateur 5 :** `user5@example.com`
- **Mot de passe :** `1234`
####
- **Admin :** `admin@example.com`
- **Mot de passe :** `1234`


## Fonctionnalités

### Gestion des utilisateurs

- Les utilisateurs peuvent se connecter via le formulaire de connexion.
- Des roles sont définis pour les utilisateurs : **ADMIN** ou **USER** afin de sécuriser les différents accès.

### Connexions

- Les utilisateurs peuvent gérer leurs connexions via la modale `Add connection` de la page `Transfer`, ce qui leur permet d'ajouter des amis à leur réseau et de réaliser des transactions avec eux.

### Transactions

- Permet aux utilisateurs d'envoyer de l'argent à leurs amis enregistrés et `Connectés` dans l'application via le formulaire `Send money` de la page `Transfer`. Chaque transaction inclut une commission fixe.
- Les utilisateurs peuvent consulter l'historique de leurs transactions.

### Profil

- Les utilisateurs peuvent ajouter ou retirer de l'argent de leur portefeuille virtuel, via le formulaire `Wallet management` de la page `Profile`.
- Les utilisateurs peuvent consulter leur solde actuel.

### Commissions

- Les transactions effectuées par les utilisateurs sont soumises à une commission fixe de 5% actuellement.
- Une liste de toutes les commissions est accessible aux `ADMIN` à l'adresse suivante : [http://localhost:8080/commissions](http://localhost:8080/commissions)

### Pagination

- La pagination est utilisée pour afficher les transactions et les connexions.

### Déconnexion

- Les utilisateurs peuvent se déconnecter de l'application via le bouton `Logout` dans la barre de navigation.

### Gestion des Erreurs

- Les erreurs sont gérées via des pages d'erreur personnalisées.
- Les erreurs de validation des formulaires sont affichées dans les vues HTML.


## Tests
Le projet PayMyBuddy inclut des tests unitaires et d'intégrations pour assurer la qualité et la fiabilité du code.

### Exécution des Tests Unitaires
Pour exécuter les tests unitaires, utilisez la commande suivante :
`mvn clean test`

### Exécution des Tests Unitaires et Intégrations
Pour exécuter les tests unitaires et intégrations, utilisez la commande suivante :
`mvn clean verify`

### Rapports de Tests
#### Rapport de couverture de code par les tests
À la suite de l'une ou l'autre des précédentes commande, vous pouvez consulter le rapport de couverture JaCoCo dans : `target/site/jacoco/index.html`

#### Rapport de résultat d'exécution des tests
- À la suite de l'une ou l'autre des précédentes commande, vous pouvez aussi lancer la commande `mvn site`.
- Cela vous donnera accès au rapport Maven Surefire dans `target/site/index.html`
- Une JAVADOC est aussi générée dans `target/site/apidocs/index.html` et visible dans le rapport Maven Site.