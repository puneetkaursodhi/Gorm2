import gorm.Account
import gorm.Branch
import gorm.Transaction
import gorm.User

class BootStrap {

    def init = { servletContext ->
        createBranches()
        createUsers()
        createAccounts()
        createTransactions()
    }

    void createBranches() {
        println "############Creating Branches############"
        5.times {
            Branch branch = new Branch(name: "Delhi", address: "Address ${it}, Delhi")
            saveObject(branch)
            branch = new Branch(name: "London", address: "Address ${it}, London")
            saveObject(branch)
            branch = new Branch(name: "Berlin", address: "Address ${it}, Berlin")
            saveObject(branch)
            branch = new Branch(name: "New York", address: "Address ${it}, New York")
            saveObject(branch)
            branch = new Branch(name: "Sydney", address: "Address ${it}, Sydney")
            saveObject(branch)
        }
    }

    void createUsers() {
        println "############Creating Users############"
        User user
        10.times {
            user = new User(firstName: "Test ${it}", address: "Address user ${it}", lastName: "last name${it}", age: it < 18 ? (it + 18) : ((it > 50) ? (it - 32) : it))
            saveObject(user)
        }
    }

    void createAccounts() {
        println "############Creating accounts############"
        User.list().each { User user ->
            Branch.list().each { Branch branch ->
                Account account = new Account(balance: 1000 * (user.id), user: user, branch: branch, dateCreated: (new Date() - user.id.toInteger()))
                saveObject(account)
                user.account = account
            }
        }
    }

    void createTransactions() {
        println "############Creating transactions############"
        Account.list().eachWithIndex { Account account, index ->
            5.times {
                if (index % 2) {
                    createTransaction(account, index * 1000, "Cr", (new Date() - it))
                } else {
                    createTransaction(account, index * 1000, "Dr", (new Date() - it))
                }
            }

        }
    }

    void createTransaction(Account account, Float amount, String type, Date date) {
        Transaction transaction = new Transaction(amount: amount, type: type, dateCreated: date)
        account.addToTransactions(transaction)
        saveObject(account)
    }

    void saveObject(Object object) {
        if (object.hasErrors() || !object.save(flush: true)) {
            object.errors.allErrors.each {
                println "Errror ${it}"
            }
        }
    }
}
