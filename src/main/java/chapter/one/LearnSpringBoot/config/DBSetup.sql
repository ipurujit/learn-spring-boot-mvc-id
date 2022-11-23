CREATE USER uname WITH PASSWORD 'pword';

--- GRANT DB
GRANT ALL ON DATABASE dbname TO uname;

---
CREATE SCHEMA IF NOT EXISTS dbschema AUTHORIZATION uname;

---
drop table dbschema.user_roles;
drop table dbschema.users;
drop table dbschema.roles;

---
CREATE USER expotester WITH PASSWORD 'pword';

CREATE SCHEMA IF NOT EXISTS auth AUTHORIZATION expotester;
