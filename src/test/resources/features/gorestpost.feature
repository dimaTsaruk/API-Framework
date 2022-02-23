Feature: Gorest Products

  Scenario: Create a post and get post details
    When the following post has been created
      | id  | user_id | title | body                  |
      | 019 | 977     | SDET  | Software DevEngInTest |
    Then a status code 200 is returned
    And the following post has been returned
      | id  | user_id | title | body                  |
      | 019 | 977     | SDET  | Software DevEngInTest |
    When the post details has been requested
    Then a status code 200 is returned
    And the following post has been returned
      | id  | user_id | title | body                  |
      | 019 | 977     | SDET  | Software DevEngInTest |