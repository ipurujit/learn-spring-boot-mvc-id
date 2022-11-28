--CREATE USER IF NOT EXISTS exponenter WITH PASSWORD 'pword';

-- SCHEMA: auth

-- DROP SCHEMA IF EXISTS auth ;

CREATE SCHEMA IF NOT EXISTS auth
    AUTHORIZATION exponenter;

-- Table: auth.users

-- DROP TABLE IF EXISTS auth.users;

CREATE TABLE IF NOT EXISTS auth.users
(
    id bigint PRIMARY KEY,
    email character varying(254) COLLATE pg_catalog."default" UNIQUE NOT NULL,
    full_name character varying(300) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    phone_number character varying(20) COLLATE pg_catalog."default" NOT NULL,
    date_of_birth date NOT NULL,
    account_non_expired boolean NOT NULL,
    account_non_locked boolean NOT NULL,
    credentials_non_expired boolean NOT NULL,
    enabled boolean NOT NULL,
    created_at timestamp with time zone,
    updated_at timestamp with time zone
--    CONSTRAINT users_pkey PRIMARY KEY (id),
--    CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS auth.users
    OWNER to exponenter;

-- Table: auth.roles

-- DROP TABLE IF EXISTS auth.roles;

CREATE TABLE IF NOT EXISTS auth.roles
(
    id bigint PRIMARY KEY,
    role_name character varying(255) COLLATE pg_catalog."default" UNIQUE NOT NULL,
    created_at timestamp with time zone,
    updated_at timestamp with time zone
--    CONSTRAINT roles_pkey PRIMARY KEY (id),
--    CONSTRAINT uk_716hgxp60ym1lifrdgp67xt5k UNIQUE (role_name)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS auth.roles
    OWNER to exponenter;

-- Table: auth.user_roles

-- DROP TABLE IF EXISTS auth.user_roles;

CREATE TABLE IF NOT EXISTS auth.user_roles
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,
    CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
    CONSTRAINT user_roles_role_id_foreign_key FOREIGN KEY (role_id)
        REFERENCES auth.roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT user_roles_user_id_foreign_key FOREIGN KEY (user_id)
        REFERENCES auth.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS auth.user_roles
    OWNER to exponenter;

