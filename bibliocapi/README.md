# BibliOC-API

## Contexte
Ce projet a été développé en 2020 dans le cadre du cursus "Développeur d'application Java" d'OpenClassrooms et correspond à la partie API du projet 7.
Cette API permet d'exposer les informatinos concernant le catalogues et la gestion des prêts d'une bibliothèque.

version 1.1.0 - projet OC 10
Ajout de la fonctionalité de réservations (https://github.com/NlCO/BibliOC/issues/1)

## Pré-requis
Version de java : 1.8 (jdk utilisé : jdk1.8.0_202)
Maven 3.6
Un serveur SMTP

## Installation et déploiement
1.Configuration

L'application est livrée avec 2 modes de fonctionnement gérés par le paramètre "spring.profiles.active" dans le fichier src\resources\application.properties :

  * **dev** (défaut) : utilisant une base de données "in-Memory" (H2) recréée et peuplée avec un jeu de test à chaque lancement.
  
  * **prod** : utilisant une base PostgresSQL (pré-installation nécessaire) à paramétrer et peupler (optionnel).
  Les paramètres de connection à la base sont à modifier dans le fichier src\resources\application-prod.properties (spring.datasource.url, spring.datasource.username et spring.datasource.password)
  La base contient aucune données. Le jeu de test src\resources\sql\demodata.sql est disponible via exécution des requêtes SQL.
  
  Release 1.1.0 :
  l'application est codée en code first (la base de données est mise à jour automatiquement). Il est tout de même recommande de faire un dump avant la montée de version.
  Un script SQL de mise à jour est tout de même disponible dans src\resources\sql\V1.1.0_update.sql 
  La configuration du serveur SMTP doit être renseignée dans le fichier src\resources\application.properties.
  Le repo est configuré pour l'utilisation d'un serveur SMTP local fakeSMTP (http://nilhcem.com/FakeSMTP/)
    
2.Déploiement

Il faut d'un premier temps lancer le serveur SMTP :
    Par exemple pour un serveur local : la commande suivante dans le répertoire du jar de fakeSMTP :
    
        java -jar fakeSMTP-2.0.jar -s
                
Puis au choix :

  * application standalone intégrant un conteneur web (grace à SpringBoot)
  
        mvn clean spring-boot:run
    
  * utilisation d'une webapp (**war**) dans un conteneur web (comme Tomcat) ou lancer via une commande java
    - création du package à la racine du projet 
     
            mvn clean package
          
    - lancement du package généré dans le sous-répertoire target
     
            java -jar target\bibliocapi-1.1.0.war

3.Accès

L'API est exposée via l'URL : http://localhost:8088/

Une documentation swagger de l'API est disponible à l'adresse : http://localhost:8088/swagger-ui.html

