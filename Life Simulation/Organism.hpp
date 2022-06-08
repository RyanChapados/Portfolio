#ifndef ORGANISM 
#define ORGANISM

class Organism {
protected:
	bool moved;				//Set to false by default, switches to true if this organism has moved in this time step
	int breed_time;			//The number of time steps since the organism last bred

public:
	Organism();
	Organism(bool, int);

	void setMoved(bool);
	bool getMoved();
	int getBreedTime();
	Organism& operator--();
	Organism& operator++();

	virtual char getType();
	virtual void move(int, int, Organism*[100][100]);
	virtual void breed(int, int, Organism*[100][100]);
	
	friend void randMove(int, int, Organism*[100][100]);
};

#endif