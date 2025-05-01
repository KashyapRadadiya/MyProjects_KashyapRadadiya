import json
import pandas as pd
from http.server import BaseHTTPRequestHandler, HTTPServer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import MultinomialNB

# Load and preprocess the dataset
data = pd.read_csv("D:/PROJECTS/PROJECT-8_SpamMailDetection_usingML/SpamData.csv")

# Standardize labels
data['Category'] = data['Category'].str.lower().map({'spam': 1, 'ham': 0})
X = data['Message']
y = data['Category']

# Handle missing labels
if y.isnull().sum() > 0:
    y.fillna(y.mode()[0], inplace=True)

# Split data
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# TF-IDF Vectorization
vectorizer = TfidfVectorizer()
X_train_tfidf = vectorizer.fit_transform(X_train)
X_test_tfidf = vectorizer.transform(X_test)

# Train Naive Bayes Model
model = MultinomialNB()
model.fit(X_train_tfidf, y_train)

# HTTP Server Handler
class SpamDetectionHandler(BaseHTTPRequestHandler):
    def _set_headers(self, content_type="application/json"):
        self.send_response(200)
        self.send_header('Content-type', content_type)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'POST, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type')
        self.end_headers()

    def do_OPTIONS(self):
        self._set_headers()

    def do_POST(self):
        self._set_headers()

        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        data = json.loads(post_data.decode('utf-8'))
        email_text = data.get('email')

        if not email_text:
            response = {'error': 'Email text is required'}
        else:
            input_data_tfidf = vectorizer.transform([email_text])
            prediction = model.predict(input_data_tfidf)
            result = "Spam Mail" if prediction[0] == 1 else "Genuine Mail"
            response = {'prediction': result}

        self.wfile.write(json.dumps(response).encode('utf-8'))

# Server runner
def run_server():
    port = 8080
    server_address = ('', port)
    httpd = HTTPServer(server_address, SpamDetectionHandler)
    print(f"🚀 Server is running at http://localhost:{port}")
    httpd.serve_forever()

if __name__ == '__main__':
    run_server()
