#include "Util.hpp"
#include <iostream>
#include <utility>

//Returns a random pair of x y values that are 4 adjacent to the given x y values
pair<int, int> rand4Adj(int x, int y) {
	int move = rand() % 4;
	int newx = x, newy = y;

	if (move == 0) {
		newx += 1;
	}
	else if (move == 1) {
		newx += -1;
	}
	else if (move == 2) {
		newy += 1;
	}
	else {
		newy += -1;
	}

	return loopCoords(newx, newy);
}

//Loops the coords if they need to be looped
pair<int, int> loopCoords(int x, int y) {
	//Loops the x values
	if (x > 99) {
		x = 0;
	}
	else if (x < 0) {
		x = 99;
	}

	//loops the y values
	if (y > 99) {
		y = 0;
	}
	else if (y < 0) {
		y = 99;
	}

	pair<int, int> out(x, y);
	return out;
}

//Gets a pair of coords that are 4 adjacent to the given x and y and have the given type
pair<int, int> get4AdjType(int x, int y, Organism* grid[100][100], char type) {
	int newx = x;
	int newy = y;
	pair<int, int> coords;

	//Searches for the char to the left and right
	for (int i : {-1, 1}) {
		char c;

		//Looks at coords with a different x
		coords = loopCoords(x + i, y);
		c = getChar(coords.first, coords.second, grid);

		//If the char has been found, sets the newx and newy equal to the coords of the char
		if (c == type) {
			newx = coords.first;
			newy = coords.second;
		}

		//Looks at coords with a different y
		coords = loopCoords(x, y + i);
		c = getChar(coords.first, coords.second, grid);

		//If the char has been found, sets the newx and newy equal to the coords of the char
		if (c == type) {
			newx = coords.first;
			newy = coords.second;
		}
	}
	
	pair<int, int> out(newx, newy);
	return out;
}


//Gets a pair of coords that are 8 adjacent to the given x and y and have the given type
pair<int, int> get8AdjType(int x, int y, Organism* grid[100][100], char type) {
	int newx = x;
	int newy = y;
	pair<int, int> coords;

	//Searches for an ant to the left and right
	for (int i : {-1,0,1}) {
		for (int j : {-1, 0, 1}) {
			char c;

			//Searches for the char
			coords = loopCoords(x + i, y + j);
			c = getChar(coords.first, coords.second, grid);

			//If the char has been found, sets the newx and newy equal to the coords of the char
			if (c == type) {
				newx = coords.first;
				newy = coords.second;
			}
		}
	}

	pair<int, int> out(newx, newy);
	return out;
}

//Gets the char that represents 
char getChar(int x, int y, Organism* grid[100][100]) {
	if (grid[y][x] != NULL) {
		return grid[y][x]->getType();
	}
	else {
		return '_';
	}
}


//Gets a random pair of coords
pair<int, int> randCoords() {
	int x = rand() % 100;
	int y = rand() % 100;
	pair<int, int> c(x, y);
	return c;
}

//Prints the given grid
void printGrid(Organism* grid[100][100]) {
	for (int y = 0; y < 100; y++) {
		for (int x = 0; x < 100; x++) {
			cout << getChar(x, y, grid);
		}
		cout << "\n";
	}
}