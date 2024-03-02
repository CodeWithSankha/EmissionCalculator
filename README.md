# EmissionCalculator
CO2 emission calculator


## Pre-Requisites
It is assumed that Java17, Maven is installed on your computer

### Build the program

./mvnw clean install


### Setup ORS Token
Windows

set ORS_TOKEN=<Your Token>

Linux

export ORS_TOKEN=<Your Token>


###Run Unit test
./mvnw test

###Run the tomcat server
./mvnw spring-boot:run

### Run Scripts
./co2-calculator --start=Hamburg --end=Berlin --transportation-method=medium-diesel-car

./co2-calculator --start "Los Angeles" --end "New York" --transportation-method large-electric-car

./co2-calculator --end "New York" --start "Los Angeles" --transportation-method large-electric-car

./co2-calculator --end "New York" --start "Los Angeles" --transportation-method=large-electric-car

./co2-calculator --transportation-method large-electric-car --end="New York" --start "Los Angeles"