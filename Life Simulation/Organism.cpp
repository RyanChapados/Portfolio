#ifndef ORGANISM 

#include <utility>
#include "Organism.hpp"
#include "Util.hpp"

using namespace std;

//Constructor with Default values for the organism
Organism :: Organism() {
	this->breed_time = -1;
	this->moved = false;
}

//Constructor with user given values for organism
Organism :: Organism(bool m, int b) : moved(m), breed_time(b) {}

bool Organism::getMoved() { return this->moved; }
int Organism::getBreedTime() { return this->breed_time; }

//Gets a char that represents the type of organism
char Organism :: getType() {
		return '_';
}


//Sets the moved variable to false
void Organism::setMoved(bool b) { 
	this->moved = b; 
}


//Sets the breed_time to a dummy value so the program knows it can be removed
Organism& Organism :: operator--() {
	this->breed_time = -1;
	return *this;
}


//Resets the breed time of the organism so the program knows it has bred
Organism& Organism :: operator++() {
	this->breed_time = 0;
	return *this;
}


//Breeds the organism at the given position
void Organism::breed(int x, int y, Organism* grid[100][100]) {
	//
}

//Moves an organism according to the rules of that organism
void Organism::move(int x, int y, Organism* grid[100][100]) {
	randMove(x, y, grid);
}


//This movement can be used for both the ant and the doodlebug so I made it a friend function
//Randomly moves the organism at the given coords according to the rules
void randMove(int x, int y, Organism* grid[100][100]) {
	grid[y][x]->breed_time++;

	pair<int, int> newxy = rand4Adj(x, y);
	int newx = newxy.first, newy = newxy.second;

	//Ends the function if there is an organism in the space that is being moved to
	//Otherwise moves the bug to the new location, and updates its moved and breed fields
	if (grid[newy][newx] == NULL) {
		grid[newy][newx] = *(&grid[y][x]);

		grid[y][x] = NULL;
	}
}

#endif