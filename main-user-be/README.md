Download code of external services from GitHub repository(opens in a new tab).

2 Launch external services using the instruction in README(opens in a new tab).

3 Create a Spring Boot application.

4 Implement UserInfoRepository for getting user info from MongoDB.

5 Create integrations with Order Search service and Product Info service.

6 Prepare logic of getting and aggregation info about all user’s orders.

7 Implement REST API to return all orders by userId. API should return a response in multi-value stream format.

8 Create unit tests for your code using the reactor-test library. Use WireMock(opens in a new tab) for stubbing external services’ responses in tests.

9 Launch and test the application:

Make a call to your endpoint.
Check that the service aggregates all required data from the external services correctly.
Stop the Product Info service.
Validate that your service works as expected (response doesn’t contain products’ names).
Make sure that logs contain all required information.