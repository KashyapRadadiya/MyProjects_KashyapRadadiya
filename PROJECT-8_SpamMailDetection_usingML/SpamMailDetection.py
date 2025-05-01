import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression


data = pd.read_csv("D:/PROJECTS/PROJECT-8_SpamMailDetection_usingML/SpamData.csv")

data.loc[data['Category' ] == 'spam', 'Category', ] = 1
data.loc[data['Category'] == 'ham', 'Category',] = 0
X = data['Message']
Y = data['Category']

X_train, X_test, Y_train, Y_test = train_test_split(X,Y, test_size=0.2, random_state=3)


feature_extraction = TfidfVectorizer(min_df=1, stop_words='english', lowercase=True, ngram_range=(1,2))

X_train_features = feature_extraction. fit_transform(X_train)
X_test_features = feature_extraction.transform(X_test)

Y_train = Y_train.astype('int')
Y_test = Y_test.astype('int')


model = LogisticRegression()
model.fit(X_train_features, Y_train)


prediction_on_training_data = model.predict(X_train_features)
print('Accuracy of training data : ', accuracy_on_training_data)


prediction_on_test_data = model.predict(X_test_features)
print('acc on test data : ', accuracy_on_test_data)

# input_your_mail = ["you've won a prize at your winzo account of $486 by click a link or provide personal information"]
input_your_mail = ["This is the 2nd time we have tried 2 contact u. U have won the £750 Pound prize. 2 claim is easy, call 087187272008 NOW1! Only 10p per minute. BT-national-rate."]

input_data_features = feature_extraction.transform(input_your_mail)
prediction = model.predict(input_data_features)

print('Prediction : ',prediction)
if prediction==1 :
    print('Spam Mail :(')
else:
    print('Genuine Mail :)')