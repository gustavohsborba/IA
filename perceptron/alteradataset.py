
from random import random
from random import shuffle
import csv

def unique(vec):
    unique = []
    for c in vec:
        if c not in unique:
            unique.append(c)
    return unique


# LÊ OS DADOS
dados = []
classes = []
with open('iris.data', 'r') as csvfile:
    spamreader = csv.reader(csvfile, delimiter=',')
    for row in spamreader:
        dados.append([float(i) for i in row[0:-1]])
        classes.append(row[-1])


# PEGA A LISTA DE NOMES DE CLASSES:
nomeClasses = unique(classes)
QTD_DADOS = len(dados)
QTD_CLASSES = len(nomeClasses)


# DÁ UM SHUFFLE NOS DADOS:
x = [i for i in range(QTD_DADOS)]
shuffle(x)
dadosShuffled = [dados[i] for i in x]
classesShuffled = [classes[i] for i in x]

# TRANSFORMA OS DADOS EM BINÁRIO:
classesBinarias = []
for i in range(QTD_DADOS):
    binario_i = []
    for j in range(QTD_CLASSES):
        binario_i.append(1 if classesShuffled[i]==nomeClasses[j] else 0)
    classesBinarias.append(binario_i)

with open('iris2.data', 'w') as csvfile:
    spamwriter = csv.writer(csvfile, delimiter=',')
    for i in range(QTD_DADOS):
        row = dadosShuffled[i] + classesBinarias[i]
        print(row)
        spamwriter.writerow(row)
