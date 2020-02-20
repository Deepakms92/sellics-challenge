# Sellics Challenge

#### 1. What assumptions did you make?
As per the description the autocomplete url provided by amazon actually gives 10 suggestions for,
every character you type, which i assumed it to be one of the top searched items for the word formed.
Assumed that the time that my service can hit the API would be strictly 10 seconds . I believe these ar two assumptions that i made. 

#### 2. How does your algorithm work?
Once the user provides the keyword he wants to search.

* First it splits the keyword into sequentially increasing prefix until he gets the 
 complete word.
* Lets say in case of iphone it splits the word into i,ip,iph,ipho, iphon, iphone.
* Once split the service makes call the amazon API with the prefix 
* I attach a callable task to this which will be called by the executor service .
* Score is calculated based on the total number of matches found and the response list which is multiplied by 10 
since each response list will be size of 10 and then its converted into a percentage.

#### 3. Do you think the (*hint) that we gave you earlier is correct and if so - why?
Yeah i personally believe that the order is insignificant , not sure why Amazon API return the result in that order though .
To put it with an example when i typed iphone , the first result was iphone 11 case which i felt may be wouldn't have been
the really hot searched item , so yeah i  personally believe the hint is correct . 

#### 4. How precise do you think your outcome is and why?
Well i'm really not sure how accurate the result is .But i did test that the service
always returned the same result every time i searched for a keyword , for example the score for batman toys 
was always 34. I believe that depends on the location as well not sure though but just a guess:) 

## Built with 
* JDK 1.8.
* Spring boot .
* Maven .

Have not included any tests cases for the project .

## Running the application
* git clone https://github.com/Deepakms92/sellics-challenge.git
* cd sellics-challenge
* mvn clean install
* The above gives you the target folder.
* Go to target folder and run the jar created by java -jar sellics-challenge-0.0.1-SNAPSHOT.jar
* The application runs now.
* Now hit the required en point .




