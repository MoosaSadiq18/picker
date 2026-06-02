import cv2
from deepface import DeepFace
from fastapi import FastAPI
import requests

app = FastAPI()

@app.post("/uploadPfpEmbeddings")
def uploadPfpEmbeddings():
    img = cv2.imread('/home/moosa/prod/picker/src/main/java/org/example/picker/facial_recognition/group.jpg')

    results = DeepFace.represent(img, model_name='Facenet', enforce_detection=True, detector_backend='retinaface')

    responses = []

    for face in results:
        payload = {
            "userId": 1,
            "embeddings": face['embedding']
        }

        req = requests.post("http://localhost:8080/getPfpEmbeddings", json=payload)
        responses.append({
            "userId":1, "status":req.status_code, "body":req.text
        })

    return responses
