# Picker

Private room based image sharing using facial recognition to avoid 
having to search for your that fine side posing picture in a bulk of pictures.

## How It Works

Creator makes a room, gets a room code.
Members join using that code.
Each member has a profile picture (pfp).
Creator uploads a batch of images to the room.
System runs face matching and each member only sees images where their face appears.

## Stack

- Spring Boot: REST API, room and user management
- Python and FastAPI: face matching service using DeepFace using its Facenet model
- MariaDB: rooms, members, image and pfp embeddings, their metadata, match results
- AWS S3: image storage and retrieval with presigned URLs to reduce load of server and ensure scalability

## Final words
- Its has not been completed yet. I am still working on it, trying to make it a production ready backend.