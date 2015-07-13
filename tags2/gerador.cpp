#include <iostream>
#include <fstream>
using namespace std;

int main () {
  ofstream myfile;

  const string fixed = "000010000101010101011011011110111011011011111010101100000100";

  for (int tags = 100; tags <= 1000; tags += 100) {
    const std::string filename(to_string(tags) + "tags/tagss.in");
    myfile.open(filename);

    for (int lines = 0; lines < tags; ++lines) {
      for (int i = 0; i < 36; ++i) {
        myfile << rand() % 2;
      }

      myfile << fixed << "\n";
    }
    myfile.close();
  }

  return 0;
}