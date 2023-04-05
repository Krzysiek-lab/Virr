# Virr is an application that runs simulations of an epidemic after an user provides starting variables.
To run this application using docker you need to:
1. docker pull ignatiuk/virr:baza
2. docker pull ignatiuk/virr:apka
3. docker run -p 5438:5432 --name vir -e POSTGRES_PASSWORD=krzysiek1 -td ignatiuk/virr:baza (where "krzysiek1" is a password    for an user in the postgres database)
4. docker exec -it vir bash
5. psql -U postgres
6. CREATE DATABASE vir;  (steps 3-5 creating a database in ignatiuk/virr:baza container)
7. docker run -p 8888:8888 -d ignatiuk/virr:apka

