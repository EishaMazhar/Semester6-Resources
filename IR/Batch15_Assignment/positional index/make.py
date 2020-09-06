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
                for idx, word in enumerate(f1):
                    if not word in stop_words:
                        if not word in index:
                            index[word] = [(doc_num,idx)]
                        else: index[word].append((doc_num,idx))

        with open("index_without_stem.json", "w") as write_file:
            json.dump(index, write_file)

        for elem in index:
            print(f'{elem} is present at    {index[elem]}')

if __name__=="__main__":
    main()
