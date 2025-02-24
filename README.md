# Flappy-Bird
To run the program, you will need to input a command line argument of either, 
	
	a) your name
	b) the number "1"

By entering your name, you will allow the program to run, and your name will be used to keep track of the leaderboard.

By entering the number "1", the program will display the leaderboard. Although the leaderboard is shown after playing the game and clicking once on the game over screen, by entering 1, you can view the leaderboard without having to play the game a first time.

After starting the game, the program checks if there is a csv file to store the leaderboard, and if there is not one, it will create one for you. If there is, the game continues as usual. 

During gameplay, scores are updated and saved ensuring progress is preserved across sessions. The program features error handling to address the issues that might arise with the leaderboard. 

Once you start playing, your goal is to go through the green tubes by clicking either the space bar or mouse, and every time you enter a tube, you gain a point and a sound plays. A sound also plays when you fly with the bird and when you die. 

Upon death, a game over screen pops up with the message of clicking space to continue to the leaderboard, and another message pops up saying to click any button to restart the game. 
