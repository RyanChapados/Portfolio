#include "Organism.hpp"

class Ant : public Organism {
public :
	Ant(bool, int);
	char getType();
	void move(int, int, Organism*[100][100]);
	void breed(int, int, Organism*[100][100]);
};