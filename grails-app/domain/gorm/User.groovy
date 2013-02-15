package gorm

class User {

    String firstName
    String lastName
    String address
    Integer age
    Account account

    static constraints = {
        account(nullable: true)
    }
}
