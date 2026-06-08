import cv2
from deepface import DeepFace
from fastapi import FastAPI
import requests
from pydantic import BaseModel
import numpy as np
import random

app = FastAPI()

class PfpRequest(BaseModel):
    pfpUrl: str
    userId: int

@app.post("/uploadPfpEmbeddings")
def uploadPfpEmbeddings(request: PfpRequest):

    print(request.pfpUrl)
    response = requests.get(request.pfpUrl)
    print("Status:", response.status_code)
    print("Content-Type:", response.headers.get("Content-Type"))
    print("Content Length:", len(response.content))

    img_array = np.asarray(bytearray(response.content), dtype=np.uint8)
    img = cv2.imdecode(img_array, cv2.IMREAD_COLOR)
    print("img is None:", img is None)

    if img is not None:
        print("shape:", img.shape)

    results = DeepFace.represent(img, model_name='Facenet', enforce_detection=True, detector_backend='retinaface')
    responses = []

    for face in results:
        payload = {
            "userId": request.userId,
            "embeddings": face['embedding']
        }

        print("Embeddings",face['embedding'])
        print("userId",request.userId)

        req = requests.post("http://localhost:8080/getPfpEmbeddings", json=payload)
        responses.append({
            "userId":request.userId, "status":req.status_code, "body":req.text
        })

    return responses



##Image embedder

class imageRequest(BaseModel):
    imageUrl: str
    userId: int
    roomId: int
    position: int

@app.post("/uploadImageEmbeddings")
def uploadImageEmbeddings(request: imageRequest):

    print(request.imageUrl)
    response = requests.get(request.imageUrl)
    print("Status:", response.status_code)
    print("Content-Type:", response.headers.get("Content-Type"))
    print("Content Length:", len(response.content))

    img_array = np.asarray(bytearray(response.content), dtype=np.uint8)
    img = cv2.imdecode(img_array, cv2.IMREAD_COLOR)
    print("img is None:", img is None)

    if img is not None:
        print("shape:", img.shape)

    results = DeepFace.represent(img, model_name='Facenet', enforce_detection=True, detector_backend='retinaface')
    responses = []
    i = request.position
    print("Position: ", i)

    imageGroupId = random.randint(0,10000)

    for face in results:
        payload = {
            "imageGroupId": imageGroupId,
            "userId": request.userId,
            "roomId": request.roomId,
            "embeddings": face['embedding'],
            "position": i
        }

        i = i+1

        print("Embeddings",face['embedding'])
        print("userId",request.userId)

        req = requests.post("http://localhost:8080/getImageEmbeddings", json=payload)
        responses.append({
            "userId":request.userId, "status":req.status_code, "body":req.text
        })

    return responses