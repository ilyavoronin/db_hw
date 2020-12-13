import java.io.BufferedReader
import java.io.FileReader
import java.sql.Connection
import java.sql.DriverManager
import java.util.*


fun main(args: Array<String>) {
    val config = Properties()
    config.load(BufferedReader(FileReader("config.properties")))

    val dbUrl = config.getProperty("url")
    val user = config.getProperty("user")
    val password = config.getProperty("password")

    Class.forName("org.postgresql.Driver")

    val props = Properties()
    props.setProperty("user", user)
    props.setProperty("password", password)
    val connection: Connection = DriverManager.getConnection(dbUrl, props)

    val stat = connection.createStatement()
    when {
        (args[0] == "c") -> {
            val res = stat.executeQuery("select course_name from course order by course_name")
            while (res.next()) {
                println(res.getString(1))
            }
        }
        (args[0] == "s") -> {
            val res = stat.executeQuery("select first_name || ' ' || last_name from student")
            while (res.next()) {
                println(res.getString(1))
            }
        }
        (args[0] == "g" && args.size == 2) -> {
            val res = stat.executeQuery(
                    """
                    select first_name || ' ' || last_name || ': ' || grade from student_grade sg
                    join student s on sg.student_id = s.student_id
                    join course c on sg.course_id = c.course_id
                    where course_name = '${args[1]}'
                    """
            )
            while (res.next()) {
                println(res.getString(1))
            }
        }
        (args[0] == "g" && args.size == 3) -> {
            val res = stat.executeQuery(
                    """
                    select grade from student_grade sg
                    join student s on sg.student_id = s.student_id
                    join course c on sg.course_id = c.course_id
                    where course_name = '${args[1]}' and last_name = '${args[2]}'
                    """
            )
            while (res.next()) {
                println(res.getString(1))
            }
        }
        (args[0] == "u") -> {
            val course_name = args[1]
            val student_lastname = args[2]
            val grade = args[3]
            stat.execute(
                    """
                    insert into student_grade(student_id, course_id, grade)
                    values ((select student_id from student where last_name = '$student_lastname'), 
                            (select course_id from course where course_name = '$course_name'),
                            $grade
                           )
                    """.trimIndent()
            )
        }
    }
}