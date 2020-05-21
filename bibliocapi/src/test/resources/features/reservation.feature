Feature: reservation

  Scenario: premier sur liste d'attente
    Given un livre indisponible emprunte par 2020020805 et n'ayant pas encore de reservation
    When  le membre 2020020806 le reserve
    Then  le membre 2020020806 est en position 1 sur la liste d'attente

  Scenario: second sur liste d'attente
    Given un livre indisponible emprunte par 2020020805 et reserver par 2020020806 uniquement
    When  le membre 2020020807 le reserve
    Then  le membre 2020020807 est en position 2 sur la liste d'attente

  Scenario: exemplaire encore disponible
    Given un livre encore disponible et sans file d'attente
    When le membre 2020020806 le reserve
    Then la liste d'attente du livre est vide

  Scenario: Liste de réservation pleine
    Given un livre indisponible emprunte par 2020020805 avec les membres 2020020806 et 2020020807 sur liste d'attente
    When le membre 2020020808 le reserve
    Then le livre est toujours emprunte par 2020020805 reserve par 2020020806 et 2020020807 sans 2020020808 dans la liste

  Scenario: reservation non possible car pret en cours
    Given un livre indisponible emprunte par 2020020805 et reserver par 2020020806 uniquement
    When le membre 2020020805 le reserve
    Then le livre est toujours emprunte par 2020020805 reserve seulement par 2020020806 sans l'emprunteur
