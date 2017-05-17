import csv
import sys
import math
import random

train_path = sys.argv[1]
test_path = sys.argv[2]
stopwordPath = sys.argv[3]

import re
f = open(stopwordPath,'r')
temp = f.read()
stopwordList= re.split('\W+',temp)
dictionary = {}
for word in stopwordList:
    dictionary[word]=""
file_path = train_path + "\\ham"
from os import listdir
from os.path import isfile, join
allfiles = [ f for f in listdir(file_path) if isfile(join(file_path,f)) ]
text_docs = ""
no_hams = len(allfiles)
for file_no in range(0,len(allfiles),1):
    f = open(file_path+"\\"+allfiles[file_no],'r')
    temp = f.read()
    text_docs=text_docs+ temp
content_ham = text_docs


for word, initial in dictionary.items():
    content_ham=content_ham.lower()
    content_ham = content_ham.replace(" "+word.lower()+" ", " "+initial+" ")

file_path = train_path + "\\spam"
allfiles = [ f for f in listdir(file_path) if isfile(join(file_path,f)) ]
text_docs = ""
no_spams = len(allfiles)
for file_no in range(0,len(allfiles),1):
    f = open(file_path+"\\"+allfiles[file_no],'r')
    temp = f.read()
    text_docs=text_docs+ temp
content_spam = text_docs

for word, initial in dictionary.items():
    content_spam=content_spam.lower()
    content_spam = content_spam.replace(" "+word.lower()+" ", " "+initial+" ")
prior_ham = float(no_hams)/(no_hams + no_spams)
prior_spam = 1- prior_ham
import re
words_spam = re.split('\W+',content_spam)
words_ham  = re.split('\W+',content_ham)

count_spam = {}
for token in range(0,len(words_spam),1):
    if words_spam[token] in count_spam:
        count_spam [words_spam[token]] = count_spam [words_spam[token]] + 1
    else:
        count_spam [words_spam[token]] = 1

count_ham  = {}
for token in range(0,len(words_ham),1):
    if words_ham[token] in count_ham :
        count_ham  [words_ham[token]] = count_ham  [words_ham[token]] + 1
    else:
        count_ham  [words_ham[token]] = 1
    if not words_ham[token] in count_spam:
        count_spam[words_ham[token]] = 0
for token in range(0,len(words_spam),1):
    if not words_spam[token] in count_ham :
        count_ham [words_spam[token]] = 0
prob_spam = {}
key_spam = 0
for keys in count_spam:
   key_spam = key_spam + count_spam[keys]+1
for keys in count_spam:
    prob_spam[keys] = float(count_spam[keys]+1)/key_spam

prob_ham = {}
key_ham = 0
for keys in count_spam:
   key_ham = key_ham + count_ham [keys]+1
for keys in count_spam:
    prob_ham[keys] = float(count_ham [keys]+1)/key_ham
correct = 0
total_files = 0

file_path = test_path + "\\ham"
from os import listdir
from os.path import isfile, join
allfiles = [ f for f in listdir(file_path) if isfile(join(file_path,f)) ]
text_docs = ""
no_hams = len(allfiles)
for file_no in range(0,len(allfiles),1):
    f = open(file_path+"\\"+allfiles[file_no],'r')
    temp = f.read()
    total_files = total_files +1
    a = math.log(prior_ham)
    b = math.log(prior_spam)
    words_doc = re.split('\W+',temp)
  
    for item in words_doc:
        if item in prob_spam:
            b = b + math.log(prob_spam[item])
    
    for item in words_doc:
        if item in prob_ham:
            a = a + math.log(prob_ham[item])
    if a > b:
        correct = correct +1
file_path = test_path + "\\spam"
allfiles = [ f for f in listdir(file_path) if isfile(join(file_path,f)) ]

text_docs = ""
no_spams = len(allfiles)
for file_no in range(0,len(allfiles),1):
    f = open(file_path+"\\"+allfiles[file_no],'r')
    temp = f.read()
    total_files = total_files +1
    a = math.log(prior_ham)
    b = math.log(prior_spam)
    words_doc = re.split('\W+',temp)
   
    for item in words_doc:
        if item in prob_spam:
            b = b + math.log(prob_spam[item])
    
    for item in words_doc:
        if item in prob_ham:
            a = a + math.log(prob_ham[item])
    if a < b:
        correct = correct +1

print (' the accuracy of Naive Bayes algorithm after filtering the stop words is ')
precision = float(correct)/total_files
accuracy = precision*100
print(accuracy)









