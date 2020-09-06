from flask import Flask, render_template, request
app = Flask(__name__)
import pickle



# open a file, where you stored the pickled data
file = open('model.pkl', 'rb')
clf = pickle.load(file)
file.close()


@app.route('/', methods=["GET", "POST"])
def hello_world():
    if request.method == "POST":
        myDict = request.form
        fever = int(myDict['fever'])
        age = int(myDict['age'])
        pain = int(myDict['pain'])
        runnyNose = int(myDict['runnyNose'])
        diffBreath = int(myDict['diffBreath'])

        #code for inference
        inputFeatures = [fever,pain,age,runnyNose,diffBreath]
        # inputFeatures = [102,1,83,1,1]

        infProb = clf.predict_proba([inputFeatures])[0][0]
        print(infProb)
        NoinfProb = clf.predict_proba([inputFeatures])[0][1]
        print(NoinfProb)
        return render_template('show.html', inf=infProb, noinf=NoinfProb)
        

        


    return render_template('index.html')


if __name__ == "__main__":
    app.run(debug=True)