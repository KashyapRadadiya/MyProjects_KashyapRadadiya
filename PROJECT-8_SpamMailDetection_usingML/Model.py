import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import MultinomialNB
from sklearn.pipeline import make_pipeline

# Sample dataset (replace this with your actual dataset)
data = pd.read_csv("D:/PROJECTS/PROJECT-8_SpamMailDetection_usingML/SpamData.csv")  # Ensure this file exists and has no NaN values

# Assuming the dataset has columns: 'text' (email content) and 'label' (spam/ham)
X = data['Message']
y = data['Category']

# Check for NaN values in the target variable
# print("Missing values in y:", y.isnull().sum())

# Handle NaN values in y
if y.isnull().sum() > 0:
    y.fillna(y.mode()[0], inplace=True)  # Fill NaNs with most frequent label

# Splitting dataset into training and test sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# TF-IDF Vectorization
vectorizer = TfidfVectorizer()
X_train_tfidf = vectorizer.fit_transform(X_train)
X_test_tfidf = vectorizer.transform(X_test)

# Ensure X_train_tfidf and y_train have matching indices
y_train = y_train.iloc[:X_train_tfidf.shape[0]]

# Train the model
model = MultinomialNB()
model.fit(X_train_tfidf, y_train)

# Evaluate on the test set
accuracy = model.score(X_test_tfidf, y_test)
print(f"Model Accuracy: {accuracy:.2f}")

# Predict custom input
custom_messages = [
    "Click here to meet now"
]

custom_tfidf = vectorizer.transform(custom_messages)
predictions = model.predict(custom_tfidf)
print('Prediction : ',predictions)

