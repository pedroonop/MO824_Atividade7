import os

for i in range(10):
	os.system("java -jar TS.jar ./bpp_instances/instance" + str(i) + ".bpp " + str(10 * 60 * 1000) + ">./results/" + str(i) + "/ts")
