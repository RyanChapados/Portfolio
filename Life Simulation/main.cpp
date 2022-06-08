#include <iostream>
#include <utility>
#include <string>
#include <cstdio>
#include <chrono>
#include <thread>
#include "Util.hpp"
#include "Organism.hpp"
#include "Ant.hpp"
#include "Doodlebug.hpp"




using namespace std::chrono;
using namespace std::this_thread;

int main() {
	Organism* grid[100][100];

	//Builds the default grid
	for (int y = 0; y < 100; y++) {
		for (int x = 0; x < 100; x++) {
			//Organism o = Organism();
			grid[y][x] = NULL;
		}
	}

	//Adds ants to the grid
	int i = 0;
	while (i < 100) {
		pair<int, int> c = randCoords();

		//Ensures the spot that the new ant will be placed is null
		if (grid[c.second][c.first] == NULL) {
			Ant* a = new Ant(false, 0);
			grid[c.second][c.first] = a;
			i++;
		}
	}

	//Puts the bugs into the grid
	i = 0;
	while (i < 11) {
		pair<int, int> c = randCoords();

		//Ensures the spot that the new bug will be placed is null
		if (grid[c.second][c.first] == NULL) {
			Doodlebug* d = new Doodlebug(false, 0, 0);
			grid[c.second][c.first] = d;
			i++;
		}
	}

	//Gets a mode from the user
	string mode;
	while (true) {
		cout << "A: Automatic\nM: Manual\n";
		cin >> mode;

		if (mode == "A" || mode == "M" || mode == "m" || mode == "a") {
			break;
		}
		else {
			cout << "That is not a valid input" << endl;
		}
	}
	
	//Loops until all of 1 species are gone or 100 time steps have been reached
	bool bug = true;
	bool ant = true;
	int time_step = 0;
	while (bug && ant && time_step < 100) {
		//Moves all the doodlebugs
		for (int y = 0; y < 100; y++) {
			for (int x = 0; x < 100; x++) {
				if (grid[y][x] != NULL) {
					if (!grid[y][x]->getMoved() && grid[y][x]->getType() == 'X') {
						grid[y][x]->setMoved(true);
						grid[y][x]->move(x, y, grid);
					}
				}
			}
		}

		//Moves all the ants
		for (int y = 0; y < 100; y++) {
			for (int x = 0; x < 100; x++) {
				if (grid[y][x] != NULL) {
					if (!grid[y][x]->getMoved()) {
						grid[y][x]->setMoved(true);
						grid[y][x]->move(x, y, grid);
					}
				}
			}
		}

		//Sets all the Organisms to a false move state, attempts to breed all organisms
		//And checks to see if all the doodlebugs have died
		bug = false;
		ant = false;
		for (int y = 0; y < 100; y++) {
			for (int x = 0; x < 100; x++) {
				if (grid[y][x] != NULL) {
					grid[y][x]->breed(x, y, grid);
					grid[y][x]->setMoved(false);
					char c = grid[y][x]->getType();

					if (c == 'X') {
						bug = true;
					}
					else if (c == 'o') {
						ant = true;
					}
				}
			}
		}
		cout << "\n\n";

		time_step++;
		cout << "Time step: " << time_step << endl;
		printGrid(grid);

		//If the mode is manual waits for user input, otherwise waits 1 second
		if (mode == "M" || mode == "m") {
			string s;
			getline(cin, s);
		}
		else {
			sleep_until(system_clock::now() + seconds(1));
		}
		
	}

}