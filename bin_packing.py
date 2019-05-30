from gurobipy import *

filename = "bpp_instances/instance0.bpp"

f = open(filename, "r")

a = f.read().split()

for i in range(len(a)):
	a[i] = int(a[i])

n = a[0]
c = a[1]

w = []

for i in a[2:]:
	w.append(i)


model = Model("bin_packing")
model.setParam("TimeLimit", 60 * 10)

y = []
for i in range(n):
	y.append(model.addVar(vtype=GRB.BINARY, name="y[" + str(i) + "]"))

x = []
for i in range(n):
	x.append([])
	for j in range(n):
		x[i].append(model.addVar(vtype=GRB.BINARY, name="x[" + str(i) + "][" + str(j) + "]"))

obj = 0
for i in range(n):
	obj += y[i]

model.setObjective(obj, GRB.MINIMIZE)

for i in range(n):
	constr = 0
	for j in range(n):
		constr += w[j] * x[i][j]
	model.addConstr(constr <= c * y[i], "c" + str(i))

for j in range(n):
	constr = 0
	for i in range(n):
		constr += x[i][j]
	model.addConstr(constr == 1, "d" + str(j))

model.optimize()

for yi in y:
	print(yi.varname + ": " + str(yi.x))

for xi in x:
	for xij in xi:
		print(xij.varname + ": " + str(xij.x))

print("Obj: " + str(model.objVal))
