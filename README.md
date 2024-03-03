# EmissionCalculator
CO2 emission calculator


## Pre-Requisites

It is assumed that Java17, Maven, curl is installed on your computer and running on Linux System (preferred)

### Extract the tar

$> ./setup.sh (It should also build the package and run the Unit tests)

### Build the program (if unsuccessful with setup.sh)

$> ./mvnw clean install

## Navigate to package directory

$> cd emissioncalculator

### Setup ORS Token

$> export ORS_TOKEN=<Your Token>


###Run Unit test
$> ./mvnw test

###Run the tomcat server

$> ./mvnw spring-boot:run

### Calculate co2-emission using script

Open another terminal and naviate to source package and execute the script to hit the server
The script make a curl http request and prints the output in console.

$> cd emissioncalculator 

$> ./co2-calculator --start=Hamburg --end=Berlin --transportation-method=medium-diesel-car

$> ./co2-calculator --start "Los Angeles" --end "New York" --transportation-method large-electric-car

$> ./co2-calculator --end "New York" --start "Los Angeles" --transportation-method large-electric-car

$> ./co2-calculator --end "New York" --start "Los Angeles" --transportation-method=large-electric-car

$> ./co2-calculator --transportation-method large-electric-car --end="New York" --start "Los Angeles"
