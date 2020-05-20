Feature: reservation

  Scenario: premier sur liste d'attente
    Given un livre indisponible emprunter par 2020020805 et n'ayant pas encore de reservation
    When  le membre 2020020806 le reserver
    Then  le membre 2020020806 est en position 1 sur la liste d'attente

  Scenario: second sur liste d'attente
    Given un livre indisponible emprunter par 2020020805 et reserver par 2020020806 uniquement
    When  le membre 2020020807 le reserver
    Then  le membre 2020020807 est en position 2 sur la liste d'attente
