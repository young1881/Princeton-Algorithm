# Princeton-Algorithm

Cousera online course, [Introduction to Algorithms (part1)](https://www.coursera.org/learn/algorithms-part1) and [Introduction to Algorithms (part2)](https://www.coursera.org/learn/algorithms-part2), created by Princeton University, taught by: Kevin Wayne, Senior Lecturer and Robert Sedgewick, Professor.

The course feathers in its autograder, whose Assessment Details section is subdivided into Compilation, API, SpotBugs, PMD, Checkstyle, Correctness, Memory, and Timing.

This repository contains my assignments source code.

## Assignments Specifications and Tips

### Week 1: [Percolation](https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php)

**The Backwash Problem**: Backwash is a like a false positive for `isFull` method and arises after a system percolates. Here a site is not connected to top, but `isFull` method returns true because its connected to any site bottom site and bottom in turn is connected to top via (using virtual bottom) some other path. Fact is water flows down only from top to bottom, but here a short circuit of connectivity happens.

It comes as a result of using both virtual top and virtual bottom. Another way is to use 2 copies of UF data structure to keep calculations separate. 

More advanced way will be to maintain additional state with each site (using single UF) on whether its connected to bottom. This is quite tricky to figure out.

### Week 2: [Deques and Randomized Queues](https://coursera.cs.princeton.edu/algs4/assignments/queues/specification.php)

**Randomized Queues**: I tried first using a linked list because I thought the access time will be faster than the array approach but the problem was the memory usage and the running time; I switch to the array implementation and my biggest concern here was "how I order the array after remove a random element from it to avoid having null elements in between? and how I do it fast?", well the answer it's pretty simple, because the order doesn't matter, there's no need to order the array but to swap the random selected  element with the latest element in the array, then just return that latest element and mark it null; About the iterator, as Tomasz Pankowski said, the important part is the "amortized time", the assignment give us a little hint : "*you may (and will need to) use a linear amount of extra memory per iterator.*" 

### Week 3: [Collinear Points](https://coursera.cs.princeton.edu/algs4/assignments/collinear/specification.php)

**Points**: Use `Double.POSITIVE_INFINITY` & `Double.NEGATIVE_INFINITY` to describe $+\infty$ & $-\infty$

**FastCollinearPoints**: How to avoid duplicating segment? One simple way is to order the Points array and check the order of adjacent points to ensure we find collinear points in order: consider we go through the ordered coloned Points array $\{p_n\}$, since the iterator variant $p$ is ordered by the comparator of `Points`, it will cause a negative comparison result if we find the collinear points $p_i$ of $p_j$  where $i<j$.