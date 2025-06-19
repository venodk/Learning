# Getting Started

### Assumptions
1. For numbers between 20 and 99, there is a hyphen between the digits(`52 -> fifty-two`) for numbers less than 100. It is unclear whether to follow the same format for numbers greater than 20000.(eg, `25000` can be converted to `twenty-five thousand`, `twenty five thousand` or `twentyfive thousand`). Here I assumed to add a hyphen between the digits to make the conversion consistent.
2. Since the command must be: `./bin/numbers-to-words [number]`, I assumed the test will be run on a macOS and prepared the script accordingly. 


### How to run
1. unzip the .zip file
2. move to the `loveholidays_app` folder
   * ```cd loveholidays_app```
3. build the project
   * ```./mvnw clean install```
4. Now the project is ready for execution. Start testing the app using the command
   * ```./bin/numbers-to-words [number]```


