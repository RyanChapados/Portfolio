#include <utility>
#include "Util.hpp"
#include "Doodlebug.hpp"

Doodlebug::Doodlebug(bool m, int b, int s) {
	this->breed_time = b;
	this->moved = m;
	this->starve = s;
}

char Doodlebug::getType() { return 'X'; }

int Doodlebug::getStarve() { return this->starve; }

void Doodlebug::move(int x, int y, Organism* grid[][100]) {
	pair<int,int> c = get4AdjType(x, y, grid, 'o');
	int newx = c.first;
	int newy = c.second;

	//Moves the bug to an adjacent ant and eats the ant
	if (newx != x || newy != y) {
		this->starve = 0;
		this->moved = true;
		this->breed_time++;
		grid[newy][newx] = this;
		grid[y][x] = NULL;
	}
	//If an ant is not adjacent, moves the bug randomly
	else {
		this->starve++;
		if (starve > 4) {
			//Lets the program know this can be deleted
			this->operator--();
		}
		else {
			randMove(x, y, grid);
		}
	}

	//Removes this from memory if it has starved
	if (breed_time < 0) {
		grid[y][x] = NULL;
		delete this;
	}
}

//Breeds the doodlebug in accordance with the rules
void Doodlebug::breed(int x, int y, Organism* grid[100][100]) {
	if (breed_time > 10) {
		pair<int,int> c = get4AdjType(x, y, grid, '_');

		if (c.first != x || c.second != y) {
			grid[c.second][c.first] = new Doodlebug(false, 0, 0);

			this->operator++();
		}
	}
}