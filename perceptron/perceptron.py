
from random import random
from random import shuffle
import numpy as np
import csv


QTD_CLASSES = 3
FEATURES = 4
QTD_DADOS = 150
MAX_IT = 100
ALFA = 0.1





def leDados():
    dados = []
    classes = []
    with open('iris2.data', 'r') as csvfile:
        spamreader = csv.reader(csvfile, delimiter=',')
        for row in spamreader:
            dados.append([float(i) for i in row[0:-QTD_CLASSES]])
            classes.append([float(i) for i in row[-QTD_CLASSES:]])

    return dados, classes





def f(val):
    return 1 if val >= 0 else 0

def fa(vec):
    return [f(v) for v in vec]

def perceptron(max_it, alfa, X, D):
    dim = len(X[0]) # quantity of features
    n_perc = len(D[0]) # quantity of classes -> quantity of perceptrons.
    N = len(X) # length of the train dataset
    W = np.random.rand(n_perc, dim)
    b = np.random.rand(n_perc)

    t=1; E=1
    while t < max_it and E > 0:
        E = 0
        for i in range(N):
            yi= fa(W.dot(X[i]) + b)
            e = D[i] - yi
            W = W + alfa*np.dot(np.atleast_2d(e).T, np.atleast_2d(X[i]))
            b = b + alfa*e
            E = E + sum([ej*ej for ej in e])
        t = t + 1
        print("t=" + str(t) + "\tE=" + str(E))
    return W, b, E





def testa_perceptron(W, b, X, D):
    N = len(X)
    E = 0
    for i in range(N):
        yi= fa(W.dot(X[i]) + b)
        e = D[i] - yi
        if(any(e)):
            print("Erro: encontrado " + str(yi) + " quando esperado" + str(D[i]))
            E += 1
    perc_acertos = str(100-((100*E)/N))
    print("\n\n---------- Estatísticas ----------")
    print(str(N) + " entradas de teste")
    print(str(N-E) + " classificadas Corretamente")
    print(str(E) + " classificadas incorretamente")
    print("Taxa de acerto: " + str(100-((100*E)/N)) + "%")




## EXECUÇÃO COMEÇA AQUI

QTD_CLASSES = 3
FEATURES = 4
QTD_DADOS = 150
MAX_IT = 100
ALFA = 0.1


dados, classes = leDados()
QTD_DADOS = len(dados)

dadosTreino = np.array(dados[:100])
classesTreino = np.array(classes[:100])
dadosTeste = np.array(dados[100:])
classesTeste = np.array(classes[100:])

# Só printa os dados
#for i in range(QTD_DADOS):
#    row = dados[i] + classes[i]
#    print(row)

W, d, E = perceptron(MAX_IT, ALFA, dadosTreino, classesTreino)
print("")
testa_perceptron(W, d, dadosTeste, classesTeste)
