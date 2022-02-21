Feature: Rebrandly Links
  As a user of Rebrandly APIs
  I would like to work with links (Create, Get, Delete, Update them)
  so I can fill my page with links

  Scenario: Get all links
    Given the base URL "https://api.rebrandly.com/"
    When all links are requested
    Then a status code 200 is returned

  @getAllLinksWithLimitQueryParam
  Scenario: Get all links with limit query param
    Given the base URL "https://api.rebrandly.com/"
    When all links are requested with the following query params
      | limit |
      | 1     |
    Then a status code 200 is returned
    And only 1 link is returned













### here we store all positive scenarios

