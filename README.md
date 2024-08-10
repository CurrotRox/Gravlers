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
