#include <iostream>
#include <fstream>
using namespace std;

int main () {
  ofstream myfile;
  for (int tags = 100; tags <= 1000; tags += 100) {
    for (int s = 0; s < 20; ++s) {
      const std::string filename(to_string(tags) + "tags/tags" + to_string(s+1) + ".in");
      myfile.open(filename);

      for (int lines = 0; lines < tags; ++lines) {
        for (int i = 0; i < 96; ++i) {
          myfile << rand() % 2;
        }
        myfile << "\n";
      }
      myfile.close();
    }
  }

  return 0;
}