# BibliOC-API

## Contexte
Ce projet a été développé en 2020 dans le cadre du cursus "Développeur d'application Java" d'OpenClassrooms et correspond à la partie API du projet 7.
Cette API permet d'exposer les informatinos concernant le catalogues et la gestion des prêts d'une bibliothèque.

## Pré-requis
Version de java : 1.8 (jdk utilisé : jdk1.8.0_202)
Maven 3.6

## Installation et déploiement
1.Configuration

L'application est livrée avec 2 modes de fonctionnement gérés par le paramètre "spring.profiles.active" dans le fichier src\resources\application.properties :

  * **dev** (défaut) : utilisant une base de données "in-Memory" (H2) recréée et peuplée avec un jeu de test à chaque lancement.
  
  * **prod** : utilisant une base PostgresSQL (pré-installation nécessaire) à paramétrer et peupler (optionnel).
  Les paramètres de connection à la base sont à modifier dans le fichier src\resources\application-prod.properties (spring.datasource.url, spring.datasource.username et spring.datasource.password)
  La base contient aucune données. Le jeu de test src\resources\sql\demodata.sql est disponible via exécution des requêtes SQL.
    
2.Déploiement

Au choix :

  * application standalone intégrant un conteneur web (grace à SpringBoot)
  
        mvn clean spring-boot:run
    
  * utilisation d'une webapp (**war**) dans un conteneur web (comme Tomcat) ou lancer via une commande java
    - création du package à la racine du projet 
     
            mvn clean package
          
    - lancement du package généré dans le sous-répertoire target
     
            java -jar target\bibliocapi-0.0.1-SNAPSHOT.war

3.Accès

L'API est exposée via l'URL : http://localhost:8088/

Une documentation swagger de l'API est disponible à l'adresse : http://localhost:8088/swagger-ui.html

