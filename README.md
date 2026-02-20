# Juice Bottler Lab

---

## Description

This project simulates a multithreaded juice bottling system using a producer–consumer model. 
Plant threads produce oranges while Worker threads process them through several stages until 
bottling is complete with a thread-safe mailbox coordinating communication. All in all, this project 
demonstrates multithreaded process management concepts such as data and task parallelization, synchronization, 
and safe handling of shared resources.

---

## Requirements

- Java JDK 21
- Apache Ant 1.10.15

Links to these required resources:

[Java JDK 21](https://www.oracle.com/java/technologies/downloads/#java21) (select your operating system and download the compressed archive)

[Apache Ant](https://ant.apache.org/bindownload.cgi) (download the zip file)

---

## Outline

Within this project in GitHub, there are two main directories:\
`src` and `documentation`

Within the `src` directory, you will find all the Java code involved in the completion of this lab.

Within the `documentation` directory you will find a UML diagram of all classes and methods (with UML formatted code in a separate file called JuiceBottler.puml) along
with a PDF explaining how the project requirements were met and what challenges were faced throughout the process.

---

## How to Run

To run this program, clone this GitHub repository into your desired directory.
After you have done this, there are two separate ways to run the program:
1. Run using ant
2. Run using Java compilation

*Option 1: Run using ant*

Ensure you have ant installed. This is listed in the *Requirements* section above, but run the following command in a terminal for confirmation:
```bash
ant -version
```
You should see output similar to the following:
```bash
Apache Ant(TM) version 1.10.15 compiled on August 25 2024
```
Once you have confirmed that ant is installed, navigate to the project directory and run the following command:

```bash
ant clean run
```
This command enables ant and runs a clean version every time. It will create
a `dist` directory, but that can be removed by running `ant clean`. After running,
you should see output similar to the following:
```bash
     [java] Plant[1]-Worker[1] started.
     [java] Plant[1]-Worker[2] started.
     [java] Plant[2]-Worker[1] started.
     [java] Plant[2] Processing oranges
     [java] Plant[1] Processing oranges
     [java] Plant[2]-Worker[2] started.
     [java] ....................................
     [java] Plant[1] Done
     [java] .
     [java] Plant[2] Done
     [java] Plant[1]-Worker[1] stopped.
     [java] Plant[2]-Worker[1] stopped.
     [java] Plant[1]-Worker[2] stopped.
     [java] Plant[2]-Worker[2] stopped.
     [java] Total provided/processed = 236/236
     [java] Created 78, wasted 2 oranges
```

*Option 2: Run using Java compilation*

Navigate to the `src` directory:
```bash
cd src/
```
Once you are within the `src` directory, run the following commands to compile and run:
```bash
javac *.java
```
```bash
java Plant
```
You will see output similar to the following, nearly identical to the ant output:
```bash
Plant[1]-Worker[2] started.
Plant[2]-Worker[1] started.
Plant[1]-Worker[1] started.
Plant[1] Processing oranges
Plant[2]-Worker[2] started.
Plant[2] Processing oranges
.....................................
Plant[1] Done
.
Plant[2] Done
Plant[1]-Worker[2] stopped.
Plant[2]-Worker[1] stopped.
Plant[1]-Worker[1] stopped.
Plant[2]-Worker[2] stopped.
Total provided/processed = 240/240
Created 80, wasted 0 oranges
```
Either way you run the program, it won't affect the output. But, it is fun and interesting
to run the program in different ways!

---

## Closing Remarks

In conclusion, this project offered great experience with multithreaded process management. Managing synchronization, shared 
resources, and thread lifecycles reinforced key concepts in the world of multithreaded processes. 
Overall, it strengthened my understanding of building correct and efficient multithreaded systems.
