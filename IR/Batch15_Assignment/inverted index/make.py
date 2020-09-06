import nltk
import sys
import operator
import json

import time


def preprocess(data):
    for p in "!.,:@#$%^&?<>*()[}{]-=;/\"\\\t\n":
        if p in '\n;?:!.,.-':
            data = data.replace(p,' ')
        else: data = data.replace(p,'')
    return data.lower()


def main():
        index = {}
        stop_words = ['a','is','the','of','all','and','to','can','be','as','once','for','at','am','are','has','have','had','up','his','her','in','on','no','we','do']
        for doc_num in range(1, 51):
            f= open(f"{doc_num}.txt","r")
            if f.mode == 'r':
                f1 = f.read()
                f1 = preprocess(f1)
                f1 = nltk.word_tokenize(f1)
                for word in f1:
                    if not word in stop_words:
                        if not word in index:
                            index[word] = [doc_num]
                        else:
                            if doc_num not in index[word]:
                                index[word].append(doc_num)

        with open("indexing", "w") as write_file:
            json.dump(index, write_file)
        #d= open("index.txt","w+")
        #d.write(index)
        #d.close()
        f.close()
        for elem in index:
            print(f'{elem} is present at    {index[elem]}')
        print(index['breakfast'])
if __name__=="__main__":
    main()
