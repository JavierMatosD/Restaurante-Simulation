# Front of House Restaurant-Simulation

## Description

The program simulates a restaurant. The simulation serves as a platform for conducting experiments related to queueing theory with certain probabilistic components. For simplicity, the simulation abstracts several elements. Specifically, the kitchen has no issues cooking for any number of customers at any given point in time.<br>

The simulation is comprised of multiple servers that can each serve/process multiple tables/jobs. However, each server has a certain soft capacity such that if they will become overburdened if they are assigned a number of jobs passed said capacity. Each job has a random component to its processing time. A restaurant does not know exactly how long a party will stay when they arrive, but generally speaking, larger parties have a higher probability of staying longer.<br>

The expected number of guest hours generated per hour = (1/p)_((2-q/q^2). In other words, E[X] _ E[Y^2] where X ~ Geometric(p) and Y ~ Geometric(q).
Crucially, however, if servers become overburdened during the course of the simulation they might slow down and no longer be able
to keep up with the expected incoming load.

## The Simulation

The program consists of 4 classes:

- **Manager** : The manager is responsible for assigning tables to servers and keeps track of how many guests abandon. It maintains a list of servers, a queue for unseated tables, and the current number of guests who abandon.
- **Table** : A table or party consists of a number of guests. Every table keeps track of the number of guests in their party, their arrival time, and their length of stay. A table will wait at most 2 hours to be seated before abandoning.
- **Server** : Every server has a capacity of 10 guests and maintains a list of tables and the current number of guests. The server is responsible for _processing_ tables assigned by the manager and keeps track of number of guests served during the simulation.
- **Main** : Main class / entry point for the program. Is responsible for running the simulations, generating random variables, and amalgamating data.

The simulation tracks 6 metrics:

- **P** : Value used to generate a geometrically distributed random variable that determines the rate of incoming parties such that lower the value of P the higher the rate of incoming tables.
- **Mean Seating Delay** : The average time it takes for an incoming table to be seated. This metric is measured in hours per guest.
- **Mean Server Delay** : This average time it takes for job completion. Tables experience higher server delays when the server becomes overburdened. This metric is measured in hours per guest.
- **Abandonment Rate** : The proportion of guests abandoned during the simulation. In the current version of the simulation, guests abandon if their seating delay exceeds two hours. The only exception is assignment strategy 5 which includes the 2 hour max seating delay but guests can also abandon randomly. **(See Assignment Strategy 5 for details on abandonment)**
- **Guest Departures** : The total number of guest served.
- **Strategy Used** : The assignment strategy used in the simulation. The assignment strategies relate to how the Manager assigns incoming tables to servers.

### Overview

In short, the simulation begins by instantiating a Manager object with the number of servers. The manager then initializes a list of servers and a queue for incoming tables. As tables arrive, the manager assigns those tables to the appropriate server determined by the assignment strategy. The server then seats the table and departs any tables that are done.

Every simulation is ran with an assignment strategy, fixed time cap measured in _ticks_, a fixed number of servers, and values _p_ and _q_. _p_ and _q_ are values between 0 and 1 that are used to generate geometrically distributed random variables. _q_ determines the the size of incoming tables while _p_ determines the rate of incoming parties. For consistency, the number of servers is always 10, _q_ is always 0.5, and the time cap is 10,000 ticks.

Currently, for every assignment strategy, the simulation runs 10 times for every value of _p_, where (0.15 <= _p_ <= 0.05). The value of _p_ decrements by 0.01 after every run starting at _p_ = 0.15 and ends with _p_ = 0.05. This results in 111 simulations for every assignment strategy.

NOTE: With the number of simulation set to 10, the time to completion can take upwards of 1min. If you wish to adjust the number of simulations run for each assignment strategy, you can adjust the variable **numSimulations** located near the top of Main.main(). However, be advised that increasing the number of simulations results in increasingly larger completion times. For reference, running the simulation 100 times for each assignment strategy resulted in a completion time upwards of 10 mins.

## Assignment Strategies

As previously stated, the assignment strategies relate to how the manager assigns incoming tables to servers.

### Assignment Strategy 1 - Least Busy

Strategy assigns incoming parties to the least busy server. The servers will eventually become overburdened as the rate of incoming parties reaches the restaurant capacity. If all servers are busy, seating is delayed.

### Assignment Strategy 2 - Random

Strategy assigns incoming parties to random servers.

### Assignment Strategy 3 - In Order

Strategy assigns incoming parties to servers in order. I.e. The first party goes to server 1, 2 goes to 2, etc. When the last server is reach, it cycles back to the first server.

### Assignment Strategy 4 - Instant Least Busy

Strategy is similar to Strategy 1 - Least Busy except it assigns tables to the least busy server regardless of server capacity. This results in no seating delay or abandonment, but has high server delays as servers become overburdened.

### Assignment Strategy 5 - Covid Strategy

Strategy uses the least busy strategy, but with the following conditions:

- If the incoming table is larger than the 2 least busy servers, they cannot be seated and abandon.
- If the incoming table is larger than the least busy server, but can be split between two servers then they are offered the opportunity to split their party between the servers. There is a 50% chance that they stay.

This strategy results in 0 server delay since no server can become overburdened and performs similarly to the least busy strategy with respect to seating delay. However, this strategy results in slightly higher abandonment rates than the least busy server strategy.

## Usage

To run the simulation open terminal within the project directory and run the following commands:

- javac \*.java
- java Main

Note that completion time may be upwards of one minute. Program stores the mean results of the experiments in their respective .csv files located in the current directory.

The files are named:

- assignmentStrategy_1.csv
- assignmentStrategy_2.csv
- assignmentStrategy_3.csv
- assignmentStrategy_4.csv
- assignmentStrategy_5.csv

The data is organized in the csv files such that column:

- **1** : P (0.15 - 0.05)
- **2** : Mean Seating Delay (hr / guest)
- **3** : Mean Server Delay (hr / guest)
- **4** : Abandonment Rate
- **5** : Number of departed guests.
- **6** : Assignment Strategy

## Authors and Acknowledgments

- Alexander Deatrick, Sam Sutherland, Javier Matos
- Professor Kristy Gardner

## Project Status

Analyzing Results...
