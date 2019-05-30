#include <bits/stdc++.h>

using namespace std;

int main(){
	
	string filename = "/home/msc2019-ceb/ra230256/git/MO824_Atividade7/bpp_instances/instance0.bpp";
	
	ifstream file;
	file.open(filename);
	
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
		if (w[i] <= c - bin.back()) bin.back() += w[i];
		else bin.push_back(w[i]);
	}
	
	cout << bin.size() << endl;
	
	return 0;
}
