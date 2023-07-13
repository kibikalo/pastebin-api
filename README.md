# Pastebin API

## This is my implementation of an application like Pastebin.

Here I wanted to try working with one of the cloud storage services.
I am using one of them, Amazon S3, to store the content of notes.

In the next versions I will be adding new functionality, improving previous, learning and working with technologies that are new to me.

---

### Initial Requirements:
* The user can create a block of text and upload it to the system.
* Can send a link to that block of text to another user, who will see the same block of text when they click on it.
* Text blocks and links are deactivated after a certain period of time. The user can specify when this happens.

### Tech-stack:
* ***Spring Boot***
* ***Amazon S3***
* ***PostgreSQL***

---

## v 1.0

### POST > http://localhost:8080/

**Request:**

[![85e9646da831d3fb9c65a9f7111d8f4f.md.png](https://imgtr.ee/images/2023/07/13/85e9646da831d3fb9c65a9f7111d8f4f.md.png)](https://imgtr.ee/image/JtfR5)

**Response:**

[![c8e170e10dba65f3996d30f20010859c.md.png](https://imgtr.ee/images/2023/07/13/c8e170e10dba65f3996d30f20010859c.md.png)](https://imgtr.ee/image/Jtxnh)


### GET > http://localhost:8080/hash

**Response:**

[![ac110c975dab8006edbf4975686799c2.md.png](https://imgtr.ee/images/2023/07/13/ac110c975dab8006edbf4975686799c2.md.png)](https://imgtr.ee/image/JtkUb)

