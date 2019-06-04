import sys
from gurobipy import *

filename = sys.argv[1]
timeLimit = int(sys.argv[2])

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
model.setParam("TimeLimit", timeLimit)

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
	if yi.x > 0.5: print(yi.varname)

for xi in x:
	for xij in xi:
		if xij.x > 0.5: print(xij.varname)

print("Obj: " + str(model.objVal))
