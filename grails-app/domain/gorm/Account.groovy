package gorm

class Account {

    Integer balance = 0
    Date dateCreated

    static belongsTo = [branch: Branch, user: User]
    static hasMany = [transactions: Transaction]

    static constraints = {
        dateCreated bindable: true
    }

    static mapping = {
        autoTimestamp false
//        branch fetch: 'join'
//        user fetch: 'join'
        user lazy: true
    }

    static namedQueries = {
       recentCustomers {Date date ->
               gt 'dateCreated', date - 10
       }
    }
}
