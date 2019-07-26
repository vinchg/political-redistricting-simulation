# Applying Simulated Annealing to Political Redistricting

Given a graph of cities and their populations, simulated annealing is applied to reform 66 districts into 28 with the goal of minimizing population difference between districts.

### Scoring
![](../media/media/score.PNG?raw=true)

### Data
![](../media/media/data.PNG?raw=true)

## Prerequisites
* Java 9.0

## Usage
The console offers 3 options:

![](../media/media/console.PNG?raw=true)

### Option 1
* **Given a list of districts, print connected components of the subgraph formed by these districts.**

![](../media/media/1-2.PNG?raw=true)

### Option 2
* **Given a state of grouped districts in a text file, return an S-value. (Debug purposes)**
* *Text files must be formatted so each line represents a set of districts whose IDs can be comma or space delimited.*

### Option 3
* **Run simulated annealing and return the best state found during the search.**
* *Temperature, cooling rate, and score cutoff can be modified in Anneal.java.*

![](../media/media/3.PNG?raw=true)
