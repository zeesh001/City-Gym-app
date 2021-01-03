package org.wit.repository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.wit.db.Employees
import org.wit.utilities.mapToUserDTO
import org.wit.db.Users
import org.wit.domain.EmployeeDTO
import org.wit.domain.UserDTO
import org.wit.utilities.mapToEmployeeDTO

class EmployeeDAO {

    fun getAll(): ArrayList<EmployeeDTO> {
        val employeeList: ArrayList<EmployeeDTO> = arrayListOf()
        transaction {
            Employees.selectAll().map {
                employeeList.add(mapToEmployeeDTO(it)) }
        }
        return employeeList
    }

    fun findById(id: Int): EmployeeDTO?{
        return transaction {
            Employees.select() {
                Employees.id eq id}
                .map{mapToEmployeeDTO(it)}
                .firstOrNull()
        }
    }

    fun findEmployeeByDesignation(designation: String): ArrayList<EmployeeDTO>{
        val employeeList: ArrayList<EmployeeDTO> = arrayListOf()
        transaction {
            Employees.select() { Employees.designation eq designation}.map{ employeeList.add(mapToEmployeeDTO(it)) }
                .firstOrNull()
        }
        return employeeList
    }

    fun save(employeeDTO: EmployeeDTO){
        return transaction {
            Employees.insert {
                it[id] = employeeDTO.id
                it[e_name] = employeeDTO.e_name
                it[e_phone] =  employeeDTO.e_phone
                it[designation] = employeeDTO.designation
            }
        }
    }

    fun findByPhone(number: Int):EmployeeDTO? {
        return  transaction {
            Employees.select(){
                Employees.e_phone eq number }.map{ mapToEmployeeDTO(it) }.firstOrNull()
        }
    }
//
//    fun deleteByPhone(phone: Int): Int {
//        return transaction{ Employees.deleteWhere{
//            Employees.e_phone eq phone
//        }
//        }
//    }

    fun delete(id: Int): Int{
        return transaction{
            Employees.deleteWhere{ Employees.id eq id }
        }
    }

    fun update(id: Int, employeeDTO: EmployeeDTO):Int{
        return transaction {
            Employees.update ({
                Employees.id eq id}) {
                it[e_name] = employeeDTO.e_name
                it[e_phone] =  employeeDTO.e_phone
                it[designation] = employeeDTO.designation
            }
        }
    }


}

