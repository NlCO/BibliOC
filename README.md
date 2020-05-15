# BibliOC

## Contexte
Ce projet a été développé en 2020 dans le cadre du cursus "Développeur d'application Java" d'OpenClassrooms.

Il regroupe les 3 modules du projet 7 : Développez le nouveau système d’information de la bibliothèque d’une grande ville (Front / API / Batch).
Les modules sont indépendants, packagés par Maven et disposent d'une documentation via leur fichier README.md respectif.
 
release 1.0.0 :
Mise en place d'un site permettant aux usagers de BibliOC de consulter le catalogue, consulter leurs prêts et renouveler cette période une fois. 
Mise en place d'un batch de mail de relance pour les retards de restitution de livres.


## Installation et déploiement
1.Configuration

Chaque module est indépendant et dispose de sa propre configuration (se référer à leur readme pour plus d'information).

Cependant les modules front (bibliocweb) et batch (bibliocbatch) necessitent que le module API (bibliocapi) soit actif.
De plus, le module batch necessite aussi qu'un serveur SMTP soit parametré. La configuration du serveur SMTP doit être renseignée dans le fichier src\resources\application.properties du module bibliocbatch.
Le repo est configuré pour l'utilisation d'un serveur SMTP local fakeSMTP (http://nilhcem.com/FakeSMTP/)

L'application est configurée par défaut pour packager : 
le module API sur l'adresse http://localhost:8088/ avec une base de données "in-Memory" (H2)
le module front sur l'adresse http://localhost:8080/
le module batch utilise le serveur SMTP local.
  
2.Déploiement

  * création des packages via la commande à la racine du repository :
  
        mvn clean package
    
  * lancement de l'API
    
        java -jar bibliocapi\target\bibliocapi-1.0.0.war

  * lancement du site WEB
    
        java -jar bibliocweb\target\bibliocweb-1.0.0.war
          
3.Accès - Utilisation

  * Accès au site WEB
  
Le site est accessible par http://localhost:8080/

L'API contient 4 membres (2020020801, 2020020802, 2020020803 et 2020020804) ayants pour password : azertyui.


  * Accès à l'API
    
L'API est exposée via l'URL : http://localhost:8088/

Une documentation swagger de l'API est disponible à l'adresse : http://localhost:8088/swagger-ui.html


  * Utilisation du batch de relance

Après avoir démarrer le serveur SMTP, lancer la ligne de commande :
   
    java -jar bibliocbatch\target\bibliocbatch-1.0.0.jar
