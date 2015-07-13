#include <iostream>
#include <fstream>
using namespace std;

int main () {
  ofstream myfile;

  const string fixed = "111110000010000101011000010000000011011011111010010100000100";
  
  for (int tags = 100; tags <= 1000; tags += 100) {
    for (int s = 0; s < 20; ++s) {
      const std::string filename(to_string(tags) + "tags/tags" + to_string(s+1) + ".in");
    myfile.open(filename);

    for (int lines = 0; lines < tags; ++lines) {
      myfile << fixed;
      for (int i = 0; i < 36; ++i) {
        myfile << rand() % 2;
      }
      myfile << "\n";
    }
    myfile.close();
  }}

  return 0;
}