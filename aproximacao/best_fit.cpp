#include <bits/stdc++.h>

using namespace std;

int main(){
	
	string filename = "/home/pedro/git/MO824_Atividade7/bpp_instances/instance9.bpp";
	
	ifstream file;
	file.open(filename.c_str());
	
	int n;
	int c;
	vector<int> w;
	
	file >> n >> c;
	
	w.resize(n);
	
	for (int i = 0; i < n; i++){
		file >> w[i];
	}
	
	vector<int> bin;
	bin.push_back(0);
	
	for (int i = 0; i < n; i++){
		int k = -1;
		int minimum = c;
		for (long unsigned j = 0; j < bin.size(); j++){
			if (w[i] <= c - bin[j]){
				if (minimum > c - bin[j] - w[i]){
					minimum = c - bin[j] - w[i];
					k = (int)j;
				}
			}
		}
		if (k == -1) bin.push_back(w[i]);
		else bin[k] += w[i];
	}
	
	cout << bin.size() << endl;
	
	return 0;
}
