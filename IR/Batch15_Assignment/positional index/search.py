import json
from nltk.stem import PorterStemmer as ps
import nltk

with open("index_without_stem.json", "r") as read_file:
    index = json.load(read_file)

def intersection(a,b):
    result = []
    for item in a:
        if item in b and not item in result:
            result.append(item)
    return result

def single_word(list):
    counter = 0
    newlist  = []
    while counter<len(list):
        if not list[counter][0] in newlist:
            newlist.append(list[counter][0])
        counter = counter + 1
    return newlist

def invert(list):
    newlist = range(1, 51)
    newlist2 = []
    for element in newlist:
        if not element in list:
            newlist2.append(element)
    return newlist2

string = input('Enter your query please: ')

query1  = string
query = nltk.word_tokenize(query1.lower())


if '/' in query1:
    if len(query) == 3:
        count = int(query[2][1:])
        ##print (count)
        list = index[query[0]]
        list2 = index[query[1]]
        #print (list)   uncomment to see the lists
        #print(list2)
        result = []
        for element in list:
            for element1 in list2:
                ans = int(int(element[1])-int(element1[1]))
                if ans<0:
                    ans = ans * -1
                if (int(element[0]) == int(element1[0])) and (ans <= count) and (not element[0] in result):
                    result.append(element[0])
        print(result)
else:
    string = string.replace(' ','')
    strint = string.split('or')
    print(strint)
    value = list(range(0,len(strint)))
    counter = 0
    for elem in strint:
        value[counter] = elem.split('and')
        counter = counter + 1
    #print(value)
    #print(len(value))
    counter = 0
    temp = []
    result = []
    for elem in value:
        if len(elem) == 1:
            for x in elem:
                if 'not' in x:
                    var = invert(single_word(index[x[3:]]))
                    result.append(var)
                else:
                    var = single_word(index[x])
                    result.append(var)
        else:
            #temp = list(range(0,len(value)))
            for x in elem:
                if 'not' in x:
                    var = invert(single_word(index[x[3:]]))
                    temp.append(var)
                else:
                    var = single_word(index[x])
                    temp.append(var)

            if len(temp) > 1:
                counter = 1
                result2 = temp[0]
                while int(counter) <int(len(temp)):
                    result2 = intersection(result2,temp[counter])
                    counter = counter+1
                result.append(result2)

    result3 = []
    for elem in result:
        for elem1 in elem:
            if not elem1 in result3:
                result3.append(elem1)
    var = len(result3)
    print('There are ' + str(var) + ' results for your query')
    print(result3)
