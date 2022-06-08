#include <utility>
#include "Organism.hpp"

using namespace std;

pair<int, int> rand4Adj(int, int);
pair<int, int> loopCoords(int, int);
pair<int, int> get4AdjType(int, int, Organism*[100][100], char);
pair<int, int> get8AdjType(int, int, Organism* [100][100], char);
pair<int, int> randCoords();
void printGrid(Organism* [100][100]);
char getChar(int, int, Organism* [100][100]);

