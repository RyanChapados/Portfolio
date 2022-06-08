#include <utility>
#include "Util.hpp"
#include "Organism.hpp"
#include "Ant.hpp"

using namespace std;

Ant::Ant(bool m, int b) {
	this->breed_time = b;
	this->moved = m;
}

//Moves the ant in a random direction
void Ant::move(int x, int y, Organism* grid[100][100]) {
	randMove(x, y, grid);
}

//Returns a char that represents the ant
char Ant::getType() {
	return 'o';
}

//Breeds the ant in accordance with the given rules
void Ant::breed(int x, int y, Organism* grid[100][100]) {
	pair<int, int> c;

	//Breeds twins if possible
	if (breed_time > 9) {
		//Gets the coords of an empty slot
		c = get8AdjType(x, y, grid, '_');

		//Places an ant in the empty slot
		if (c.first != x || c.second != y) {
			grid[c.second][c.first] = new Ant(false, 0);
			this->operator++();
		}

		//Gets the coords of an empty slot
		c = get8AdjType(x, y, grid, '_');

		//Places an ant in the empty slot
		if (c.first != x || c.second != y) {
			grid[c.second][c.first] = new Ant(false, 0);
			this->operator++();
		}
	}
	//Breeds one ant if possible
	else if (breed_time > 4) {
		//Gets the coords of an empty slot
		c = get4AdjType(x, y, grid, '_');

		//Places an ant in the empty slot
		if (c.first != x || c.second != y) {
			grid[c.second][c.first] = new Ant(false, 0);
			this->operator++();
		}
	}
}