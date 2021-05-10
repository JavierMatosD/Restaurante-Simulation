# Front of House Restaurant-Simulation

## Description

The program simulates a restaurant. The simulation serves as a platform for conducting experiments related to queueing theory with certain probabilistic components. For simplicity, the simulation abstracts several elements. Specifically, the kitchen has no issues cooking for any number of customers at any given point in time.<br>

The simulation is comprised of multiple servers that can each serve/process multiple tables/jobs. However, each server has a certain soft capacity such that if they will become overburdened if they are assigned a number of jobs passed said capacity. Each job has a random component to its processing time. A restaurant does not know exactly how long a party will stay when they arrive, but generally speaking, larger parties have a higher probability of staying longer.<br>

The expected number of guest hours generated per hour = (1/p)_((2-q/q^2). In other words, E[X] _ E[Y^2] where X ~ Geometric(p) and Y ~ Geometric(q).
Crucially, however, if servers become overburdened during the course of the simulation they might slow down and no longer be able
to keep up with the expected incoming load.

## Usage

To run the simulation open terminal within the project directory and run the following commands:

- javac \*.java
- java Main

The output will look similar to the following: <br>
Time: 10000 <br>
Unseated tables: 0 <br>

===== <br>
Server 0: <br>
Tables: 1 | Guests: 4 / 15 <br>
===== <br>
===== <br>
Server 1: <br>
Tables: 1 | Guests: 1 / 15 <br>
===== <br>===== <br>
Server 2: <br>
Tables: 1 | Guests: 0 / 15 <br>
===== <br>===== <br>
Server 3: <br>
Tables: 1 | Guests: 0 / 15 <br>
===== <br>===== <br>
.<br>
.<br>
.<br>

Seating Delay: 0.0 <br>
Server Delay: 0.0 <br>
Guest hours average: 11.9636 <br>
Theoretical expected hours rate: 12.0 <br>

## Authors and Acknowledgments

- Alexander Deatrick, Sam Sutherland, Javier Matos
- Professor Kristy Gardner

## Project Status

Implementing different strategies for:
<br>

- Seating arrangement
- Reducing seating and serving delay

<br>
We are currently conducting experiments and amalgamating data
