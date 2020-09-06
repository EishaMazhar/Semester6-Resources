cd from nltk.tokenize import word_tokenize
import sys
import operator
import textmining
import os
import numpy as np
import pandas as pd
import scipy
def preprocess(data):
    for p in "!.,:@#$%^&?<>*()[}{]-=;/\"\\\t\n":
        if p in '\n;?:!.,.-':
            data = data.replace(p,' ')
        else: data = data.replace(p,'')
    return data.lower()


def main():
        tdm = textmining.TermDocumentMatrix()
        index = {}
        stop_words = ['a','is','the','of','all','and','to','can','be','as','once','for','at','am','are','has','have','had','up','his','her','in','on','no','we','do']
        for doc_num in range(1, 51):
            f= open(f"{doc_num}.txt","r")
            if f.mode == 'r':
                f1 = f.read()                                                   #reading story
                f1 = preprocess(f1)                                             #removing punctuation marks
                word_tokens = word_tokenize(f1)                                 #breaking story into words
                filtered_sentence = ' '
                for w in word_tokens:
                    if w not in stop_words:
                        filtered_sentence = filtered_sentence + ' ' + w         #removing stopwords
                tdm.add_doc(filtered_sentence)                                  #document sent for document term matrix



        count= 0
        mat = [0] * 50
        filename = os.path.join(os.path.dirname(__file__), 'mat.csv')
        tdm.write_csv(filename, cutoff=1)                                       #writing matrix to excel file

    #    print(mat)
        for row in tdm.rows(cutoff=1):                                          #taking documentword as 2D matrix
            if count==0:
                print('')
            else:
                mat[count-1]=row
            count= count+1

        a = np.array(mat)                                                       #taking list as numpy array
        a = a.transpose()                                                       #converting documentword matrix to word_document matrix
        nonzeros = []                                                           #number of documents the word has appeared in (DF)
        for elem in a:
            nonzeros.append(np.count_nonzero(elem))

        nonzeros = np.divide(50, nonzeros)                                      #dividing df by 50 (IDF)
        nonzeros = np.log2(nonzeros)                                            #taking log of IDF

        idf = []
        count = 0
        for elem in a:
            idf.append(np.multiply(nonzeros[count],elem))                       #taking TF-IDF
            count = count+1


        idf = np.array(idf)                                                     #taking it as np array
        tfidf = idf.transpose()                                                 #taking transpose to make it documentword matrix

        while True:

            query = input('Enter your query: \n')
            alpha = float(input('Enter value of alpha:\n'))
            query = preprocess(query)                                           #removing punctuation marks
            word_tokens = word_tokenize(query)                                  #breaking query into words
            filtered_sentence = ' '
            for w in word_tokens:
                if w not in stop_words:
                    filtered_sentence = filtered_sentence + ' ' + w             #removing stopwords
            tdm.add_doc(filtered_sentence)
            tdm.write_csv(filename, cutoff=1)
            data = pd.read_csv("mat.csv")
            query = data.iloc[-1].tolist()                                      #finding vector for query
            query = np.multiply(query,nonzeros)                                 #getting TF-IDF for query

            cosine_similarity = {}
            doc_num = 1
            for elem in tfidf:
                score = 1 - scipy.spatial.distance.cosine(elem, query)          #function for cosine similarity
                cosine_similarity[doc_num]=score                                #key = doc_num, value = score
                doc_num = doc_num + 1                                           #

            result=sorted(cosine_similarity.items(),key=lambda kv:kv[1],reverse=True)       #for sorting
            for eleme in result:
                if float(eleme[1])>=float(alpha):                                #comparing the value of cosine scores with alpha for printing
                    print(eleme)
            yesno =  input('Do you want to continue? press y for yes, n for no\n')
            if yesno == 'n':
                break

if __name__=="__main__":
    main()
