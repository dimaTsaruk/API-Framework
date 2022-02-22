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

  @getAllLinksWithDomainIdQueryParam
  Scenario: Get all links with domain id query param
    Given the base URL "https://api.rebrandly.com/"
    When all links are requested with the following query params
      | domainId                         | limit |
      | 8f104cc5b6ee4a4ba7897b06ac2ddcfb | 3     |
    Then a status code 200 is returned
    And verify that 3 links has been returned with the following domainId "8f104cc5b6ee4a4ba7897b06ac2ddcfb"

  @createLink
  Scenario: Create a link
    Given the base URL "https://api.rebrandly.com/"
    When the following link is created
      | destination         |
      | https://youtube.com |
    Then a status code 200 is returned
    And the following link has been created
      | destination         |
      | https://youtube.com |

  @createLinkAndGetLinkDetails
  Scenario: Create a link and get link details
    Given the base URL "https://api.rebrandly.com/"
    When the following link is created
      | destination         |
      | https://youtube.com |
    Then a status code 200 is returned
    When the link details has been requested
    Then a status code 200 is returned
    And the following link has been created




































