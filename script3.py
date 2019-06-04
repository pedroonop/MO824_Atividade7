import os

for i in range(10):
	os.system("./aproximacao/next_fit ./bpp_instances/instance" + str(i) + ".bpp >./results/" + str(i) + "/nf")
	os.system("./aproximacao/first_fit ./bpp_instances/instance" + str(i) + ".bpp >./results/" + str(i) + "/ff")
	os.system("./aproximacao/best_fit ./bpp_instances/instance" + str(i) + ".bpp >./results/" + str(i) + "/bf")
