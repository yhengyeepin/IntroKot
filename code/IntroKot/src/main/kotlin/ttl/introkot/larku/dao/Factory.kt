package ttl.introkot.larku.dao

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ttl.introkot.larku.dao.inmemory.InMemoryStudentDAO
import ttl.introkot.larku.domain.Student
import ttl.introkot.larku.service.StudentService

/**
 * @author whynot
 */

class DAOFactory {
    companion object {


        @JvmStatic
        fun studentDAO(): BaseDAO<Student> {
            return InMemoryStudentDAO()
        }

        @JvmStatic
        fun studentService(): StudentService {
            return StudentService(studentDAO());
        }
    }
}

val jMapper = jacksonObjectMapper().registerModule(JavaTimeModule()).enable(SerializationFeature.INDENT_OUTPUT);

