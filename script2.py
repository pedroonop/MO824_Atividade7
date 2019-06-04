import os

for i in range(10):
	os.system("python bin_packing.py ./bpp_instances/instance" + str(i) + ".bpp " + str(10 * 60) + ">./results/" + str(i) + "/gurobi")