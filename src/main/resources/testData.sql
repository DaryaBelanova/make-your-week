DELETE FROM timetable;
ALTER SEQUENCE timetable_id_seq RESTART WITH 1;
DELETE FROM groups_groups;
/*DELETE FROM groups_time_constraints;*/
DELETE FROM teachers_groups;
ALTER SEQUENCE public.teachers_groups_id_seq RESTART WITH 1;
DELETE FROM groups;
ALTER SEQUENCE public.groups_groups_id_seq RESTART WITH 1;
/*DELETE FROM teachers_time_constraints;*/
DELETE FROM teachers;
DELETE FROM lesson_times;
ALTER SEQUENCE public.lesson_times_id_seq RESTART WITH 1;
/*DELETE FROM days;*/
/*DELETE FROM classrooms;*/
DELETE FROM subjects;
ALTER SEQUENCE subjects_id_seq RESTART WITH 1;
/*DELETE FROM status;*/

INSERT INTO lesson_times (lesson_number, in_day)
VALUES (1, 'Понедельник'), (2, 'Понедельник'), (3, 'Понедельник'), (4, 'Понедельник'), (5, 'Понедельник'), (6, 'Понедельник'), (7, 'Понедельник'),
       (1, 'Вторник'), (2, 'Вторник'), (3, 'Вторник'), (4, 'Вторник'), (5, 'Вторник'), (6, 'Вторник'), (7, 'Вторник'),
       (1, 'Среда'), (2, 'Среда'), (3, 'Среда'), (4, 'Среда'), (5, 'Среда'), (6, 'Среда'), (7, 'Среда'),
       (1, 'Четверг'), (2, 'Четверг'), (3, 'Четверг'), (4, 'Четверг'), (5, 'Четверг'), (6, 'Четверг'), (7, 'Четверг'),
       (1, 'Пятница'), (2, 'Пятница'), (3, 'Пятница'), (4, 'Пятница'), (5, 'Пятница'), (6, 'Пятница'), (7, 'Пятница')
;

/*INSERT INTO status (name)
VALUES ('CREATED'),
       ('APPROVED')
;*/

INSERT INTO subjects (name)
VALUES ('информатика'),
       ('математика'),
       ('русский язык')
;

INSERT INTO teachers (id, name, working_hours)
VALUES (1,  'Прим Олег Евгеньевич', 34),
       (2,  'Краскал Борис Ильич', 34),
       (3,  'Пушкина Мария Ивановна', 28)
;

INSERT INTO groups (id, name)
VALUES (1,  '5a'),     (2,  '5а инф1'),  (3,  '5а инф2'),
       (4,  '5б'),     (5,  '5б инф1'),  (6,  '5б инф2'),
       (7,  '6a'),     (8,  '6а инф1'),  (9,  '6а инф2'),
       (10, '6б'),     (11, '6б инф1'),  (12, '6б инф2')
;

INSERT INTO groups_groups (group1_id, group2_id)
VALUES (1,2), (1,3),
       (4,5), (4,6),
       (7,8), (7,9),
       (10,11), (10,12)
;

INSERT INTO teachers_groups (teacher_id, group_id, count_per_week)
VALUES (1,2,2), (1,5,2), (1,8,2), (1,11,2),
       (2,3,2), (2,6,2), (2,9,2), (2,12,2),
       (3,1,3), (3,4,3), (3,7,3), (3,10,3)
;