# Gravlers
Graveler hell roller simulator 2024

For Shoddycast's graveler thing (https://www.youtube.com/watch?v=M8C8dHQE2Ro&t=410s)

I decided to go with Java, as it's the language I know best, implementing multithreading, and not storing any results that aren't relevant (non-zeros).
I've also implemented a "timer" that just tells you how long the program has run, in miliseconds, though in hindsight I probably should've converted it to seconds, those are easier to deal with.

I've dabbled in multi-threading before, but nothing to the scale of a billion operations, so this was an interesting challenge to take up.
The biggest issue I ran into was race-conditions. I wanted to be sure I had found the largest, and didn't accidentally overwrite it, ended up using AtomicIntegers, because a simple "Synchronized" class left me with a number of instances less than there should have been.

My initial run took a staggering amount of time, and that was with 28 threads running, and it used almost over 30 GB of RAM.

Here's the output of my first run:

![image](https://github.com/user-attachments/assets/9b858e7e-9fcf-4b97-9408-357b5276f418)


Max ones: 100
Completed Instances: 1000000000
Total duration (ms): 7625833

Process finished with exit code 0

7625833 ms
7625.833 seconds
127.0972 minutes
2.118286 hours

I'm not sure how replicable these results would be on other machines, but it's what I've got.


Edit: 
I figure I should explain the program so anyone who comes to look can look at my code and hopefully understand what it's doing. I've included a lot of comments with my code, but I'll explain it here too.
First, import required libraries.</br>
Class main</br>
   Start the clock</br>
   Generate an instance of the Random library, to be used with generating numbers</br>
   Set initial variables - Amount of rolls, and the amount of threads to use on the computer.</br>
   Setup Executor service, which will manage the threads running on the CPU, and setup the "instance" we'll be cloning will all those threads.</br>
   For every roll, start a new thread with the instance.</br>
   The executor.shutdown() doesn't turn everything off immediately, it starts a soft-shutdown, so as threads finish and record their results, they're ignored.</br>
   While the executor is still running, wait and check again every 25ms. (This can probably be larger, given that this whole thing takes hours, a few miliseconds isn't going to matter).</br>
   Record the end time, and calculate it in seconds.</br>
   Print results</br>

class RollInstance</br>
  Setup static variables (A static variable is a shared variable between ALL instances of the class)</br>
  Constructor for the class, taking in the Random instance. When passing a complex object like this, it's just referenced, so we're not generating this 1 billion times.</br>
  The @override is because we're overwriting the "Runnable's" run method with a new one, which is what each thread calls when it starts.</br>
  The run just rolls 231 random numbers, breaks it into 4 possibilies, and if one of those possibilities is 0 (the computer's 1), add one to count. After 231 iterations, it updatesMaxOnes</br>
  updateMaxOnes, takes in a contender number, and checks it against the static variable. I had to use AtomicNumbers for this, which is a library specifically used to avoid read-write overlap on variables like this.</br>
    It checks whichever one is bigger, and assigns it to be the variable.</br>
    Then it increments the run counter, which is how I make sure that we're actually running the simulation a verifiable 1 billion times.</br>
  The last two are static methods that are called to return the results to the main function, to be printed.</br>
