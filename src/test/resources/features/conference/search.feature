Feature: Search Conferences

Scenario Outline: search by name
Given i navigate to search conference page
When i search a conference with name "<name>"
Then resulting list should contain conferences with "<name>" and total records of <records> 
Examples:
| name | records |
| TDC 	  | 2 |
| TDC Poa | 1 |
| OSCon	  | 0 |