# MBTA Explorer

## Goals
1) Display a list of all of MBTA's light and heavy rails.
2) Route Analytics including the following

    - The name of the route with the most stops as well as the number stops in that route
    
    - The name of the route with the fewest amount of stops as well as the number stops in that route
    
    - A list of stops that connect two or more routes
3) Provide a route between two subway stops.

## How to Run
After cloning the code locally, run the following command from the root of the project to run the project: mvn spring-boot:run

This will start a Spring Boot application on port 8080 of localhost.

To get answers for the three questions, you can enter the following URLs in your browser:

Question One:
http://localhost:8080/1

Question Two:
http://localhost:8080/2

Question Three:
http://localhost:8080/3?start=Davis&end=Kendall

Note: The start and end parameters can be replaced with any stop name.

## Testing
Dependencies
After cloning the code locally, run the following command from the root of the project to test: mvn test
## Dependencies
Must have Java 11 and Maven installed.

## Potential Improvements
- Clean up data structures to more efficiently search for membership and intersections.
- Error Handling
- Additional Testing
- Efficiency of path finding.
