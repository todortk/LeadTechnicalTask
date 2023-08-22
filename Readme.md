# LEAD Technical Interview Follow up task
The application uses port 5192 (for some reason both 80 and 8080 were used on my machine)

I have put Swagger which is attached to "/" and from there the applicatiobn entry points can be tested.
Use the following command to build and run the application :
**docker-compose up --build**

## The app entry points are as follows:

**GET /** - this leads to the swagger UI

**GET /teachers** - returns a list of all teachers

**POST /teachers** - requires JSon for teacher. Adds or changes the  specified teacher. For the groups and courses part of the JSON, only the "name" property is required and taken into account.

**DELETE /teachers** - deletes a teacher by given name. Returns 404 if none exists.


**GET /students** - returns a list of all students

**POST /students** - requires JSon for student. Adds or modifies the specified student. For the groups and courses part of the JSON, only the "name" property is required and taken into account.

**DELETE /students** - deletes a student by given name. Returns 404 if none exists.


**GET /groups** - returns a list of all groups

**POST /groups** - requires JSon for a group. Adds or modifies the specified group.

**DELETE /groups** - deletes a group by specified name. Returns 404 if none exists. Returns 422 if the group is referenced by a student or teacher.


**GET /courses** - returns a list of all courses

**POST /courses** - requires JSon for a course. Adds or changes the course in question

**DELETE /courses** - deletes a course by given name. Returns 404 if none exists. Returns 422 if the course is referenced by a student or teacher.


**GET /teachers/count** - returns the number of teachers

**GET /students/count** - returns the number of students

**GET /reports/studentsingroup** - returns the students in a given group

**GET /reports/studentsincourse** - returns the students in a given course

**GET /reports/participation** - returns all students and teachers enrolled for the specified course and group.

**GET /reports/age** - returns all students older than the specified age attending a given course

**GET /courses/secondary/count** - returns all Secondary courses

**GET /courses/main/count** - returns all Main courses


## Sample data
I added two zips containing an empty and a small sample database in the root folder. If there is valid postgres-data folder when starting the app, the postgres container will use the existing database. If the **postgres-data** folder does not exist, a new empty one will be created. The sample database Contains the following records :

### Groups:
{"name":"a", "description":"Group A"}

{"name":"b", "description":"Group B"}

{"name":"c", "description":"Group C"}

### Courses:
{"name":"OOP", "description":"Obektno Orientirano Programirane", "type":"MAIN"}

{"name":"LAAG", "description":"Lineina Algebra", "type":"MAIN"}

{"name":"SQL", "description":"SQL Bazi Danni", "type":"SECONDARY"}

{"name":"Eclipse", "description":"Eclipse plugins done wrong", "type":"SECONDARY"}

### Teachers:
{"name": "Dimitar Birov", "age": 50, "group": { "name": "a" }, "courses": [{"name": "OOP"}, { "name": "SQL"}]}

{"name": "Bachvarov", "age": 70, "group": {"name": "a"}, "courses": [{"name": "LAAG"},{ "name": "OOP"}]}

{"name": "Velikova", "age": 67, "group": {"name": "b"}, "courses": [{"name": "LAAG"},{ "name": "SQL"}]}

### Students:
{"name": "Todor", "age": 44, "group": {"name": "a"}, "courses": [{"name": "OOP"},{"name": "SQL"}]}

{"name": "Rumi", "age": 30, "group": {"name": "b"},"courses": [{"name": "LAAG"},{"name": "SQL"}]}

{"name": "Stenly", "age": 30, "group": { "name": "a"}, "courses": [{"name": "OOP"},{"name": "SQL"}]}

{"name": "Gosho", "age": 37, "group": { "name": "c" }, "courses": [{"name": "OOP"},{"name": "LAAG"}]}

{"name": "Ivan", "age": 42, "group": { "name": "a"}, "courses": [{"name": "OOP"},{"name": "Eclipse"}]}
