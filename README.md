# Hangman

1. To run, first run "make clean", followed by "make". This will compile all the necessary java files to run the program.
2. First start a server by typing "java server <port> [data.txt]" in the command line
3. To connect a client, type "java client <address> <port>"

Type "y" to start the Hangman game. Only 3 clients can be connected at a time. Enter a letter to guess it. If wrong, then the letter will be added to "Incorrect Guesses". If the letter is in the word, then the letter will show up in the client. If all letters are guessed, then you win. If the number of incorrect guesses gets to 6, then you lose. The client will disconnect after.
