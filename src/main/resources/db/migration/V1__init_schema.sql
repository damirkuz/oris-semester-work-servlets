CREATE EXTENSION IF NOT EXISTS pgcrypto;

create SCHEMA IF NOT EXISTS forum;

create table IF NOT EXISTS forum.users
(
    id            SERIAL PRIMARY KEY,
    login         VARCHAR                   NOT NULL UNIQUE,
    password_hash VARCHAR                   NOT NULL,
    name          VARCHAR                   NOT NULL,
    user_role     VARCHAR                   NOT NULL,
    created_at    TIMESTAMPTZ DEFAULT now() NOT NULL
);

create table IF NOT EXISTS forum.image
(
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    uploader_user_id INT              DEFAULT 0 REFERENCES forum.users (id) ON DELETE SET DEFAULT NOT NULL,
    path             VARCHAR                                                                      NOT NULL,
    created_at       TIMESTAMPTZ      DEFAULT now()                                               NOT NULL
);


ALTER TABLE forum.users
    ADD COLUMN IF NOT EXISTS profile_picture_id UUID,
    ADD CONSTRAINT fk_users_profile_picture
        FOREIGN KEY (profile_picture_id) REFERENCES forum.image (id) ON DELETE SET NULL;

INSERT INTO forum.users (id, name, login, password_hash, user_role)
values (0, 'deleted_user', 'deleted_user', 'incrediblepasswordhash', 'USER')
ON CONFLICT DO NOTHING;



create table IF NOT EXISTS forum.initiative
(
    id              SERIAL PRIMARY KEY,
    creator_user_id INT         DEFAULT 0 REFERENCES forum.users (id) ON DELETE SET DEFAULT NOT NULL,
    title           VARCHAR                                                                 NOT NULL,
    body            TEXT                                                                    NOT NULL,
    status          VARCHAR                                                                 NOT NULL,
    created_at      TIMESTAMPTZ DEFAULT now()                                               NOT NULL
);

create table IF NOT EXISTS forum.likes
(
    id            SERIAL PRIMARY KEY,
    user_id       INT REFERENCES forum.users (id) ON DELETE CASCADE      NOT NULL,
    initiative_id INT REFERENCES forum.initiative (id) ON DELETE CASCADE NOT NULL
);

ALTER TABLE forum.likes
    ADD CONSTRAINT uq_like_user_initiative
        UNIQUE (user_id, initiative_id);

CREATE TABLE IF NOT EXISTS forum.image_initiative
(
    image_id      UUID NOT NULL REFERENCES forum.image (id) ON DELETE CASCADE,
    initiative_id INT  NOT NULL REFERENCES forum.initiative (id) ON DELETE CASCADE,
    PRIMARY KEY (image_id, initiative_id)
);

create table IF NOT EXISTS forum.comment
(
    id             SERIAL PRIMARY KEY,
    author_user_id INT         DEFAULT 0 REFERENCES forum.users (id) ON DELETE SET DEFAULT NOT NULL,
    initiative_id  INT REFERENCES forum.initiative (id) ON DELETE CASCADE                  NOT NULL,
    body           TEXT                                                                    NOT NULL,
    created_at     TIMESTAMPTZ DEFAULT now()                                               NOT NULL
);


-- protection against deletion deleted_user
CREATE OR REPLACE FUNCTION forum.prevent_deleted_user_removal()
    RETURNS trigger AS
$$
BEGIN
    IF OLD.id = 0 THEN
        RAISE EXCEPTION 'Cannot delete the reserved deleted_user account.';
    END IF;
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER prevent_deleted_user_delete
    BEFORE DELETE
    ON forum.users
    FOR EACH ROW
EXECUTE FUNCTION forum.prevent_deleted_user_removal();

