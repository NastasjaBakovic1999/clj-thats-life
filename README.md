# What is a clj-thats-life? 

A simply implemented web application representing the popular board game ***That's Life***, which shows the consequences of the decisions we make in life and the fact that good decisions do not always lead to a series of good events, and vice versa.

## How to run?

To enjoy the charms of That's life, run the following command in the terminal

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. 

## Ok, now, what are the rules?

The aim of the game is for players, using their skill and luck, to collect as many positive cards as possible, as well as lucky cards, while trying to avoid negative cards. The player with the most points at the end of the game is the winner.

The best way to learn the rules is to play. So, let's go.   

At the beginning of the game, we need to enter the names of the players:

![image](https://user-images.githubusercontent.com/56804110/219508160-7aa3926c-5bf7-4ab8-ba37-f9514c69eb4c.png) 

Enter the names of the players and press the Entry button. After entering two or more players, we get a Start button. The maximum number of players is 6.

![image](https://user-images.githubusercontent.com/56804110/219508553-67d1b14f-ada2-4c30-abf7-bd407e747d4b.png)

Now we see the players we entered and their pawns. Each player has his own pawn color. If there are 2-4 players in the game, the number of pawns each player has is 3, while if there are 5-6 players, each player has 2 pawns . The third player is called Robot-Zika and he is actually a bot that plays automatically. For now, we won't worry too much about it, but more about the bot will be in one of the next sections.

Ok, we hit the Start button.

![image](https://user-images.githubusercontent.com/56804110/219509005-d8e55dc1-0a71-431c-b770-2ea5b0380e1d.png)

The game has started and we see the path of the cards consisting of 32 cards, not counting the start and finish cards. Now it's time to explain what each card represents.

<img src="https://user-images.githubusercontent.com/56804110/219514860-45691317-0d5f-4f68-868b-21f77eda16f1.png" width="60" height="60">  This is a negative card, there are 18 of them in total and they are represented by the poison icon. Each card has one of the values between -1 and -10 and costs as many points as are indicated on it.

<img src="https://user-images.githubusercontent.com/56804110/219517480-b8543b7f-e561-4162-86b3-49736c8fcc1c.png" width="60" height="60">  This is a positive card, there are 8 of them in total and they are presented with a potion icon. Each card has one of the values between 1 and 8 and scores as many points as are indicated on it.
 
<img src="https://user-images.githubusercontent.com/56804110/219518354-4d4a6e7a-102e-4091-9aaf-0cf2b050448d.png" width="60" height="60">  This is a fortune card and there are 6 of them in total. At the end of the game, fortune cards convert the player's highest negative card into a positive card. If a player have serveral fortune cards, then the same number of negative cards are converted into positive cards. Should a player have more fortune cards than negative cards, the other fortune cards do not score.

<img src="https://user-images.githubusercontent.com/56804110/219518957-7bfe2a2c-70d5-4ab7-9d53-1331efcc0564.png" width="60" height="60">  When the negative card, thanks to the effect of the fortune card, turns into a positive card, and its icon changes, so it is no longer a poison but an antitoxin.



## Sources

[1] Higginbotham, D., 2015, Clojure for the Brave and True: Learn the Ultimate Language and Become a Better Programmer      
[2] Sotnikov D., Brown S., 2021, Web Development with Clojure: Build Large, Maintainable Web Applications Interactively   
[3] Reagent: Minimalistic React for ClojureScript (https://reagent-project.github.io/)   
[4] Introduction to ClojureScript and Reagent, Decypher Media (https://www.youtube.com/watch?v=wq6ctyZBb0A)  
[5] React without JavaScript: ClojureScript, Reagent, Figwheel (React Helsinki meetup, 23.10.2018), Codexpanse (https://www.youtube.com/watch?v=R07s6JpJICo)  
[6] Thats Life! Board Game Review, Jeremy Reviews It (https://www.youtube.com/watch?v=nSEdCEaAznw)  

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
