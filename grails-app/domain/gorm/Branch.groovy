package gorm

class Branch {

    String name
    String address

    static hasMany = [accounts: Account]
    static constraints = {
    }
}
