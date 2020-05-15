Feature: BiblioOCapi

  Scenario: Get list of books and their availability
    Given a library with n copies of y books
    When I ask the list of books
    Then a list of y books with their availability is returned

  Scenario: Get member's loaned books
    Given the member 2020020801 with loaned books
    When he consult his loans
    Then a list of his loaned book is returned

  Scenario: Extend once the loaned period
    Given a loaned book which due date is not extended
    When the loan period is extended
    Then the book is flagged with the extend loaning period

  Scenario: list member with their late loans
    Given a list of 4 members in the database
    When the batch look for late loans of member
    Then a list of 2 member is return