Feature: BiblioOCapi

  Scenario: List de livre de la bibliothèque
    Given Une bibliothèque disposant de plusieurs exemplaires de X ouvrages
    When Le membre 2020020802 demande la liste des ouvrages
    Then La liste des X ouvrages avec leur disponibilté est retournée

  Scenario: Liste des prêts
    Given un membre 2020020801 avec au moins un prêt
    When le membre consulte la liste de ses prêts
    Then la liste de ses prêts est obtenue

  Scenario: Prolonger une fois un prêt non expiré
    Given un prêt non expiré et non prolongé
    When une demande de prolonagation est demandée
    Then le prêt est marqué comme prolongé

  Scenario: Impossibilté de prolonger un prêt en reatrd
    Given un prêt en retard et non prolongé
    When une demande de prolonagation est demandée
    Then le prêt reste marqué comme non prolongé

  Scenario: Liste des prêts en retards
    Given une base avec au moins un utilisateur ayant un prêt en retard
    When on interroge la liste des prêts en retard
    Then une liste contenant les membres est retournée
