#include "Organism.hpp"

class Doodlebug : public Organism {
protected:
	int starve;
public:
	Doodlebug(bool, int, int);
	int getStarve();
	void move(int, int, Organism*[][100]);
	char getType();
	void breed(int, int, Organism*[100][100]);

};