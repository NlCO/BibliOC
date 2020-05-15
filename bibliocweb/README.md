# BibliOC-WEB

## Contexte
Ce projet a été développé en 2020 dans le cadre du cursus "Développeur d'application Java" d'OpenClassrooms et correspond à la partie WEB du projet 7.
Ce site permet aux usagers de consulter le catalogue de consulter leurs prêts de BibliOC.

## Pré-requis
Version de java : 1.8 (jdk utilisé : jdk1.8.0_202)
 
Maven 3.6

BibliOC-API (https://github.com/NlCO/BibliOC-API) 

## Installation et déploiement
1.Configuration

L'application est livrée pour se connecter à l'api en localhost sur le port 8088 (interface BibliocapiProxy.java).
A modifier si l'API est déployée sur un autre endpoint.

  
2.Déploiement

Au choix :

  * application standalone intégrant un conteneur web (grace à SpringBoot)
  
        mvn clean spring-boot:run
    
  * utilisation d'un webapp (**war**) dans un conteneur web (comme Tomcat) ou lancer via une commande java
    - création du package à la racine du projet 
     
            mvn clean package
          
    - lancement du package généré dans le sous-répertoire target
     
            java -jar target\bibliocweb-0.0.1-SNAPSHOT.war

3.Accès

L'application est accessible par http://localhost:8080/

L'API en mode dev contient 4 membres (2020020801, 2020020802, 2020020803 et 2020020804) ayants pour password : azertyui. 