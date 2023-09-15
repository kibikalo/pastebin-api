# Pastebin API

## This is my implementation of an application like Pastebin.

Here I wanted to try working with one of the cloud storage services.
I am using one of them, Amazon S3, to store the content of notes.

In the next versions I will be adding new functionality, improving previous, learning and working with technologies that are new to me.

---

### Initial Requirements:
* The user can create a block of text and upload it to the app.
* Can send a link to that block of text to another user, who will see the same block of text when they click on the link.
* Text blocks and links are deactivated after a certain period of time. The user can specify when this happens.

### Tech-stack:
* ***Spring Boot***
* ***Amazon S3***
* ***PostgreSQL***

---

## v 1.0

### POST > http://localhost:8080/

**Request:**

![POST Request Img](https://github.com/kibikalo/pastebin-api/blob/main/src/main/resources/post-reqest.png?raw=true)

**Response:**

![POST Response Img](https://github.com/kibikalo/pastebin-api/blob/main/src/main/resources/post-response.png?raw=true)


### GET > http://localhost:8080/hash

**Response:**

![GET Response Img](https://github.com/kibikalo/pastebin-api/blob/main/src/main/resources/get-response.png?raw=true)
