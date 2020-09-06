import sys,os,math,re
from collections import defaultdict
from Tkinter import *

directory = './ShortStories/' #giving Shortstories path to the directory variable
extension = '.txt' #assigning .txt to the extension variable

# Selecting only files with the .txt extension from the directory given
txt_files = [i for i in os.listdir(directory)
             if os.path.splitext(i)[1] == extension]

documentsDictionary  = {} #an empty dictionary for storing (document ID: document path) as key:value pair

# Iterating over every file in the txt files
for file in txt_files:
    # Open the file one by one and assign it to the documentsDictionary
    with open(os.path.join(directory, file)) as file_object:
        documentsDictionary [file.strip('.txt')] = file_object.name #where key=Doc ID and value is file path
print "Total documents: "+ str(len(documentsDictionary))

# Iterating over the document dictionary to print the key/val pairs
def print_document_dictionary():
    for i in documentsDictionary :
        print i, documentsDictionary [i]

TotalDocsCount = len(documentsDictionary) #TotalDocsCount holds total count for number of documents in the corpus
dictionary = set() #the set dictionary contains all the unique words (terms) from the corpus
#for every term, it contains the document IDs along with their term frequencies such that {term:{DOC_ID:TF,DOC_ID:TF,...},term:{...},...}
postings = defaultdict(dict)
DF = defaultdict(int) #holds the DF for every term as term:DF (key:value)
length = defaultdict(float) #holds the length of every document

#This method declares and initializes the elemennts of GUI for VSM
def startGUI():
    global root,window,search,entry,count,text,docCount
    root = Tk()
    root.title("VSM")
    root.geometry("320x590+500+50")

    window = Frame(root)
    window.grid()

    instruction = Label(window, text="Enter query:")
    instruction.grid(row=0, column=1, columnspan=2, sticky=W,padx=20)

    entry = Entry(window)
    entry.grid(row=1, column=1, sticky=W,ipadx=60,padx=20)
    entry.focus()

    search = Button(window, text="Search", command=evaluate)
    search.grid(row=2, column=1, sticky=W,ipadx=100,padx=20)
    search.bind('<Return>',evaluate)
    root.bind('<Return>',evaluate)

    docCount = Label(window)
    docCount.grid(row=3, column=1, sticky=W,padx=20)

    count=Label(window,fg="dark green",font="Helvetica 9 bold")

    text = Text(width=30, height=28, wrap=WORD)
    text.place(x=20,y=90)
    text.config(state=DISABLED) #initially this is disabled when the gui is loaded

    printDict = Button(window, text="Show Dictionary", command=print_dict)
    printDict.grid(row=11, column=1, sticky=W,ipadx=76,padx=20, pady=10)
    printDict.bind('<Return>', print_dict) #on pressing

    scrollbar = Scrollbar(window, orient="vertical",command=text.yview) #creating a scrollbar widget
    scrollbar.grid(row=4,column=2,ipady=200) #placing the scrollbar on the window
    text.config(yscrollcommand=scrollbar.set) #to set the scrollbar for text widget

    root.bind("<Escape>", exit) #for when ESC is pressed, the program exits

#the function to print the dictionary in entry text widget on clicking show dictionary button
def print_dict(event=None):
    text.config(state=NORMAL)
    text.delete(0.0, END)
    text.insert(END, "Word Count | Term" + '\n')
    text.insert(END, "------------------------------" + '\n')
    i = 0
    for word in dictionary:
            i += 1
            if i>9999:
                text.insert(END, "word " + str(i) + " | " + word + '\n')
            elif i>999:
                text.insert(END, "word " + str(i) + "  | " + word + '\n')
            elif i>99:
                text.insert(END, "word " + str(i) + "   | " + word + '\n')
            elif i > 9:
                text.insert(END, "word "+str(i) + "    | " + word + '\n')
            else:
                text.insert(END, "word "+str(i) + "     | " + word + '\n')
    docCount["text"] = "Total word(s) retrieved: "
    count.place(x=150, y=66)
    count["text"] = str(i)

def evaluate(event=None):
    text.config(state=NORMAL)
    content=entry.get()
    docList=evaluateQuery(content)
    text.delete(0.0,END)
    if docList == []:
        text.insert(0.0, "No documents matched any query terms."+'\n'+"Try changing the value of alpha." + '\n')
        docCount["text"] = "Total document(s) retrieved: "
        count.place(x=175, y=66)
        count["text"] = 0
    else:
        text.insert(END, "Doc ID   |   Score"+'\n')
        text.insert(END, "------------------------------" + '\n')
        i=0
        for (id,score) in docList:
                i += 1
                if int(id)>9:
                    text.insert(END, str(id) + ".txt   |   " + str(score) + '\n')
                else:
                    text.insert(END, str(id)+".txt    |   "+str(score)+ '\n')
        docCount["text"]="Total document(s) retrieved: "
        count.place(x=175, y=66)
        count["text"]=str(i)


def createTermsAndPosting():
    global dictionary, postings
    for id in documentsDictionary: #iterating through every docID in documents dictionary
        f = open(documentsDictionary[id], 'r') #open each doc in the corpus one by one
        document = f.read() #the content of the current file is stored in the document
        f.close() #file is closed now
        tokens = tokenize(document) #the current document is tokenized and stored in tokens list
        terms = removeStopWords(tokens) #the stopwords are removed from the token list and saved in terms list
        unique_terms = set(terms) #the term list is converted to set so that duplicate terms are removed
        dictionary = dictionary.union(unique_terms) #these unique terms are now union-ed with dictionary set
        for term in unique_terms: #iterating for every term in unique terms set to store TF for that term
            postings[term][id] = terms.count(term) #storing the TF against the doc ID for that term in the postings

#this function removes the question mark that i encountered within the terms
def removeNonAscii(string):
    nonascii = bytearray(range(0x80, 0x100))
    return string.translate(None, nonascii)

#to tokenize document into individual tokens in lower case
def tokenize(document):
    tokens = re.split('\*|\?|\"|--- |,|-|--|\.|!| |\n',document.lower())
    return [removeNonAscii(term.strip("_.,!#$%^&*();:\n\t\\\"'?!{}[]<>")) for term in tokens]

#to read the Stopword-List.txt and store every stopword in stopwords list
def createStopwordsList():
    global stopwords
    with open('./Stopword-List.txt') as f:
        stopwords = f.read().splitlines()

#to return only those words in terms that are not in Stopwords list
def removeStopWords(terms):
    return [term for term in terms if term not in stopwords]

#to create the DF dictionary for every term in the dictionary
def computeDocumentFrequencies():
    global DF #DF is the document frequency dictionary
    for term in dictionary:
        #print term +" DF: " + str(len(postings[term]))
        DF[term] = len(postings[term]) #calculates the count of the doc IDs containing the current term

#calculates the length of all the documents in the corpus
def createDocumentLength():
    global length
    for id in documentsDictionary: #for every document in corpus
        l = 0 #initialize the len to zero for every document
        for term in dictionary: #for all the terms in dictionary
            l += TF_IDF(term, id) ** 2
        length[id] = math.sqrt(l) #stores the length against every Doc ID in length dictionary

#calculates the TF_IDF for every term document pair
def TF_IDF(term, id):
    if id in postings[term]:
        #for given term and doc ID, TF-IDF = TF for given (term,doc) x IDF for given term
        TF = postings[term][id]
        #print "term " +term+"DOC ID: "+str(id)+" TF:"+str(TF)+" TF-IDF: "+str(TF*IDF(term))
        return  TF * IDF(term)
    else:
        return 0.0 #return TF_IDF=0 when the given term is not the given document ID

#calculates the IDF for given term
def IDF(term):
    if term in dictionary:
        #using formula idf = log(TotalDocsCount/df[term])
        #print "term: "+term+"-IDF: "+ str(math.log(TotalDocsCount / DF[term], 2))
        return math.log(TotalDocsCount / DF[term], 2)
    else:
        return 0.0 #if term not found in dictionary then idf=0 is returned

#to evaluate the user entered query w.r.t to relevant documents to terms in query
def evaluateQuery(query):
        global alpha
        alpha = 0.005 #alpha is the cutoff value to show only documents with score greater than or equal to alpha
        query=tokenize(query) #split the query into tokens and store it in query list
        relevant_document_ids = union([set(postings[term].keys()) for term in query])  #to get all those documents containing the terms in query
        if not relevant_document_ids: #if no document contains any query terms
            print "No documents matched any query terms."
            return []
        else:
            #getting unsorted score list for the given query
            unsorted_score_list=[(id, cosineSimilarity(query, id))
            for id in relevant_document_ids if cosineSimilarity(query, id) > alpha]
            sorted_scores = sorted(unsorted_score_list,key=lambda x: x[1],reverse=True)

            print "Document ID - Score"
            for (id,score) in sorted_scores:
                    print str(id) +".txt - " + str(score)
            print ""
            print "Total doc(s) retrieved: " + str(len(sorted_scores))
            print ""
            return sorted_scores

#to union the document IDS for the given set of DOC IDs
def union(sets):
    return set.union(*[s for s in sets])

#computes the cosine similarity for given query and document ID
def cosineSimilarity(query, id):
    sim = 0.0 #initialize the numerator for cosine similarity
    for term in query:
        if term in dictionary:
            sim += IDF(term) * TF_IDF(term, id)
    sim = sim / length[id]
    return sim #cosine similarity returns zero if

#for printing all the terms in vocabulary set
def print_dictionary():
    i = 1
    for word in dictionary:
        print "word " + str(i) + ": " + word
        i += 1

if __name__ == "__main__":
    createStopwordsList()
    createTermsAndPosting()
    computeDocumentFrequencies()
    createDocumentLength()
    #print_dictionary()
    startGUI()
    root.mainloop()

    '''while True:
        query = (raw_input("Enter query (Enter q to exit): "))
        if query == "q":
            break
        evaluateQuery(query)'''

