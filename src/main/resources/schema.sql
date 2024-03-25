DROP TABLE IF EXISTS timetable;
DROP TABLE IF EXISTS groups_groups;
DROP TABLE IF EXISTS groups_time_constraints;
DROP TABLE IF EXISTS teachers_groups;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS teachers_time_constraints;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS lesson_times;
DROP TABLE IF EXISTS days;
DROP TABLE IF EXISTS classrooms;
DROP TABLE IF EXISTS subjects;
DROP TABLE IF EXISTS status;
DROP TYPE  IF EXISTS days;

/*CREATE TABLE days
(
    name          VARCHAR(20)          NOT NULL PRIMARY KEY
);*/

CREATE TYPE days AS ENUM ('Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница');

CREATE TABLE lesson_times
(
    id                   SERIAL        NOT NULL PRIMARY KEY,
    lesson_number        INTEGER       NOT NULL,
    in_day               days
);

CREATE TABLE subjects
(
    id            SERIAL               NOT NULL  PRIMARY KEY,
    name          VARCHAR(50)          NOT NULL
);

CREATE TABLE teachers
(
    id               SERIAL                  NOT NULL PRIMARY KEY,
    name             VARCHAR(50)             NOT NULL,
    working_hours    INTEGER      DEFAULT 0  NOT NULL CONSTRAINT positive CHECK (teachers.working_hours > 0)
);

CREATE TABLE groups
(
    id                  SERIAL                NOT NULL  PRIMARY KEY,
    name                VARCHAR(50)           NOT NULL
);

CREATE TABLE teachers_time_constraints
(
    id                    SERIAL            NOT NULL PRIMARY KEY,
    teacher_id            INTEGER,
    FOREIGN KEY       (teacher_id) REFERENCES teachers (id),
    time_constraints_id   INTEGER,
    FOREIGN KEY       (time_constraints_id) REFERENCES lesson_times (id),
    UNIQUE (teacher_id, time_constraints_id)
);

CREATE TABLE groups_time_constraints
(
    id                    SERIAL            NOT NULL PRIMARY KEY,
    group_id              INTEGER,
    FOREIGN KEY       (group_id) REFERENCES groups (id),
    time_constraints_id   INTEGER,
    FOREIGN KEY       (time_constraints_id) REFERENCES lesson_times (id),
    UNIQUE (group_id, time_constraints_id)
);

CREATE TABLE teachers_groups
(
    id                SERIAL            NOT NULL PRIMARY KEY,
    teacher_id        INTEGER,
    FOREIGN KEY       (teacher_id) REFERENCES teachers (id),
    group_id          INTEGER,
    FOREIGN KEY       (group_id) REFERENCES groups (id),
    count_per_week    INTEGER,
    UNIQUE (teacher_id, group_id)
);

CREATE TABLE groups_groups
(
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    group1_id   INTEGER,
    FOREIGN KEY (group1_id) REFERENCES groups (id),
    group2_id   INTEGER,
    FOREIGN KEY (group2_id) REFERENCES groups (id),
    UNIQUE (group1_id, group2_id)
);

CREATE TABLE classrooms
(
    number        INTEGER               NOT NULL PRIMARY KEY,
    capacity      INTEGER CONSTRAINT positive_capacity CHECK (capacity > 0),
    subject_id    INTEGER,
    FOREIGN KEY (subject_id) REFERENCES subjects (id)
);

CREATE TABLE status
(
    name          VARCHAR(20)           NOT NULL PRIMARY KEY
);

/*CREATE TABLE timetables
(
    id            SERIAL               NOT NULL PRIMARY KEY,
    status        VARCHAR(20),
    FOREIGN KEY   (status) REFERENCES status (name)
);*/

CREATE TABLE timetable
(
    id               SERIAL             NOT NULL PRIMARY KEY,
    teacher_group_id INTEGER REFERENCES teachers_groups(id) NOT NULL,
    timeslot_id      INTEGER REFERENCES lesson_times(id) NOT NULL,
    UNIQUE (teacher_group_id, timeslot_id)
);