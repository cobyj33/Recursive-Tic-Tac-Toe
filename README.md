# ![#f03c15](https://placehold.co/15x15/f03c15/f03c15.png) Recursive Tic-Tac-Toe ![#1589F0](https://placehold.co/15x15/1589F0/1589F0.png)

A pit of Tic-Tac-Toe games of N depth

Works through a 9-node tree where each node has a won or lost state, which changes depending on its children, or if it is a leaf node, depending on the result of its game.
As you can see from these visualizations of a game between depths of 2 and 3, the tree gets pretty large, pretty fast

![1 Depth Tree Display](<readme/1 depth tree.png>)
![2 Depth Tree Display](<readme/2 depth tree.png>)
![3 Depth Tree Display](<readme/3 depth tree.png>)

If I could do it all again, I would **NOT** write this in Java with Java Swing. But still, I wrote it in Java and Java Swing. One day I'll move it onto the web though for accessibility's sake, I promise. The logic in itself is still pretty concise and understandable, which is why I keep it here.

I've had ideas on how to make this more fun. It can't be that fun right now, since the amount of games grows at a rate of 9^n, so at a depth of 3, there are already a maximum 729 games to play between 2 people. Maybe something with teams, more aimed toward [Ultimate Tic-Tac-Toe](https://en.wikipedia.org/wiki/Ultimate_tic-tac-toe) than normal Tic-Tac-Toe so that there's more thought in each move, and timed or with some sort of higher stakes. But that's just a dream for now.

With Apache Ant installed to the system, the project can be built with the Apache Ant build system by running the terminal command "ant" from the repository's root directory. That should result in an executable .jar file in the created dist/ directory.

## Screenshots

![Display of the Game Screen at Low Depth](<readme/rttt_1.png>)
![Display of the Game Screen at High Depth](<readme/rttt_2.png>)
