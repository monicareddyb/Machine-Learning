import re
import csv
import sys
import math
import random

stopwordPath = sys.argv[3]
f = open(stopwordPath,'r')
temp = f.read()
stopwordList= re.split('\W+',temp)
dictionary = {}
for word in stopwordList:
    dictionary[word]=""


train_path = sys.argv[1]
test_path = sys.argv[2]
file_path = train_path + "\\ham"
from os import listdir
from os.path import isfile, join
allfiles = [ f for f in listdir(file_path) if isfile(join(file_path,f)) ]
text_docs = ""
no_hams = len(allfiles)
hamvector = []
for file_no in range(0,len(allfiles),1):
    f = open(file_path+"\\"+allfiles[file_no],'r')
    temp = f.read()
    text_docs=text_docs+ temp
    hamvector.insert(len(hamvector),temp)
content_ham = text_docs

for word, initial in dictionary.items():
    content_ham=content_ham.lower()
    content_ham = content_ham.replace(" "+word.lower()+" ", " "+initial+" ")

file_path = train_path + "\\spam"
allfiles = [ f for f in listdir(file_path) if isfile(join(file_path,f)) ]
text_docs = ""
no_spams = len(allfiles)
spamvector = []
for file_no in range(0,len(allfiles),1):
    f = open(file_path+"\\"+allfiles[file_no],'r')
    temp = f.read()
    text_docs=text_docs+ temp
    spamvector.insert(len(spamvector),temp)
content_spam = text_docs
for word, initial in dictionary.items():
    content_spam=content_spam.lower()
    content_spam = content_spam.replace(" "+word.lower()+" ", " "+initial+" ")
import re
words_spam = re.split('\W+',content_spam)
words_ham  = re.split('\W+',content_ham)

count_spam = {}
for token in range(0,len(words_spam),1):
    if words_spam[token] in count_spam:
        count_spam [words_spam[token]] = count_spam [words_spam[token]] + 1
    else:
        count_spam [words_spam[token]] = 1
count_ham = {}
for token in range(0,len(words_ham),1):
    if words_ham[token] in count_ham:
        count_ham [words_ham[token]] = count_ham [words_ham[token]] + 1
    else:
        count_ham [words_ham[token]] = 1
    if not words_ham[token] in count_spam:
        count_spam[words_ham[token]] = 0
for token in range(0,len(words_spam),1):
    if not words_spam[token] in count_ham:
        count_ham[words_spam[token]] = 0
words = count_spam.keys()
a = []
b = []

for hamelement in hamvector:
    hamterms = re.split('\W+',hamelement)
    hamtermsfreq = {}
   
    for token in hamterms:
        if token in hamtermsfreq:
            hamtermsfreq[token] = hamtermsfreq[token] + 1
        else:
            hamtermsfreq[token] = 1
    atemp = {}
    for item in count_spam:
        if item in hamtermsfreq:
            atemp[item] = hamtermsfreq[item] + 1
        else:
            atemp[item] =  1
    a.insert(len(a),atemp)
    b.insert(len(b),0)
for Spamelement in spamvector:
    spamterms = re.split('\W+',Spamelement)
    spamtermsfreq = {}
    
    for token in spamterms:
        if token in spamtermsfreq:
            spamtermsfreq[token] = spamtermsfreq[token] + 1
        else:
            spamtermsfreq[token] = 1
    atemp = {}
    for item in count_spam:
        if item in spamtermsfreq:
            atemp[item] = spamtermsfreq[item] + 1
        else:
            atemp[item] =  1
    a.insert(len(a),atemp)
    b.insert(len(b),1)
lamda = 0.001

learning_rate   = 0.1

convrate = 12

import random

wt=[random.uniform(-1,1) for _ in xrange(len(count_spam)+1)]

converged = 0
while converged !=1:
    prob = []
    wt_sum = wt[0]
    for i in range(0,len(a),1):
        line = a[i]
        
        for item in range(0,len(wt)-1,1):
           
            wt_sum = wt[item+1]*line[words[item]]

        if wt_sum> math.log(1e308):
            wt_sum = 1e308
        elif wt_sum< -math.log(1e308):
             wt_sum = -math.log(1e308)
        
        prob.insert(len(prob),float(1)/(1+math.exp(wt_sum)))
    dw = []
    maxdw = 0
    par=0
    for i in range(0,30,1):
	
        	dwi = 0
        	for i in range(0,len(a),1):
            		line = a[i]
            		dwi = dwi + line[words[i]]*(b[i]-prob[i]) - wt[i]*lamda
        	dw.insert(len(dw),dwi)
        	if abs(maxdw) < abs(dwi):
            		maxdw = abs(dwi)
        	
        	wt[i] = wt[i] + learning_rate * dwi
        	    
    if abs(learning_rate*maxdw) < abs(convrate):
    		converged = 1
correct = 0
total_size = 0
file_path = test_path + "\\ham"
from os import listdir
from os.path import isfile, join
allfiles = [ f for f in listdir(file_path) if isfile(join(file_path,f)) ]
text_docs = ""
no_hams = len(allfiles)
for file_no in range(0,len(allfiles),1):
    f = open(file_path+"\\"+allfiles[file_no],'r')
    temp = f.read()
    total_size = total_size +1
    words_doc = re.split('\W+',temp)
    data_vectorx = {}
    for token in words_doc:
        if token in words:
            if token in data_vectorx:
                data_vectorx[token] = data_vectorx[token] + 1
            else:
                data_vectorx[token] = 1
    for token in words:
        if token in data_vectorx:
            data_vectorx[token] = data_vectorx[token] + 1
        else:
            data_vectorx[token] = 1
    mean = 0
    index = -1
    for token in data_vectorx:
        index = index + 1
        mean = mean + data_vectorx[token] * wt[index]
    if mean < 0:
        correct = correct +1
file_path = test_path + "\\spam"
allfiles = [ f for f in listdir(file_path) if isfile(join(file_path,f)) ]
#allfiles[1]
text_docs = ""
no_spams = len(allfiles)
for file_no in range(0,len(allfiles),1):
    f = open(file_path+"\\"+allfiles[file_no],'r')
    temp = f.read()
    total_size = total_size +1
    words_doc = re.split('\W+',temp)
    data_vectorx = {}
    for token in words_doc:
        if token in words:
            if token in data_vectorx:
                data_vectorx[token] = data_vectorx[token] + 1
            else:
                data_vectorx[token] = 1
    for token in words:
        if token in data_vectorx:
            data_vectorx[token] = data_vectorx[token] + 1
        else:
            data_vectorx[token] = 1
    mean = 0
    index = -1
    for token in data_vectorx:
        index = index + 1
        mean = mean + data_vectorx[token] * wt[index]
    if mean > 0:
        correct = correct +1

print (' the accuracy of logistic regression after filtering the stop words is ')
precision = float(correct)/total_size
accuracy=precision*100
print(accuracy)

  






