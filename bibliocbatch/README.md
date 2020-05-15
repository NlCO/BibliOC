# BibliOC-Batch

## Contexte
Ce projet a été développé en 2020 dans le cadre du cursus "Développeur d'application Java" d'OpenClassrooms et correspond à la partie Batch du projet 7.
Ce batch permet d'envoyer un mail de rappel aux membres ayant dépassé la date de retour d'au moins 1 de leur prêt.
Il se base sur l'API BibliOC-API (https://github.com/NlCO/BibliOC-API)

## Pré-requis
Version de java : 1.8 (jdk utilisé : jdk1.8.0_202)
Maven 3.6
L'API BibliOC-API (https://github.com/NlCO/BibliOC-API)
Un serveur SMTP

## Installation et déploiement
1.Configuration

La configuration du serveur SMTP doit être renseignée dans le fichier src\resources\application.properties.
Le repo est configuré pour l'utilisation d'un serveur SMTP local fakeSMTP (http://nilhcem.com/FakeSMTP/)

    
2.Déploiement

- Création du package via la commande suivante à la racine du repo :
     
        mvn clean package

- un fichier "bibliocbatch-0.0.1-SNAPSHOT.jar" dans le sous-repertoire target doit être généré

3.Utilisation

- Après avoir démarré le serveur SMTP et l'API BibliOC

- Ordonnancer avec le scheduler de votre choix la commande :

        java -jar <chemin de votre fichier>\bibliocbatch-0.0.1-SNAPSHOT.jar

