use ficss;
insert into topic (content) values 
('Artificial Intelligence'), ('Mechine Learning'), ('Big Data Analytics'), ('Software Engineering'), ('Digital Signal/Image/Audio Processing'), ('Pattern Recognition');

insert into agenda (event, event_date, event_start_time, event_end_time, room, description) 
values ('Registration', '2020-02-25', '13:00', '13:20', 'Room B401', null),
 ('Opening Remarks', '2020-02-25', '13:20', '13:40', 'Room B401', 'xxxxxxxxxxxxx'), 
('Fist Half', '2020-02-25', '13:40', '14:40', null, null), ('Coffee Break', '2020-02-25', '14:40', '15:00', null, null),
 ('Second Half', '2020-02-25', '15:00', '16:00', null, null),  ('Reviewer Discussion', '2020-02-25', '16:00', '17:00', null, null);

SELECT * FROM agenda WHERE event_start_time >= '10:00' AND room = ''

ORDER BY event_start_time, event_end_time;



