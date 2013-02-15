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
        (1..10).each {
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
        User user
        (1..50).each {
            user = new User(firstName: "Test ${it}", address: "Address user ${it}", lastName: "last name${it}", age: it < 18 ? (it + 18) : ((it > 50) ? (it - 32) : it))
            saveObject(user)
        }
    }

    void createAccounts() {
        User.list().eachWithIndex {User user,index ->
            Branch branch = Branch.get(user.id)
            Account account = new Account(balance: 1000 * (user.id), user: user, dateCreated: (new Date() - index))
            branch.addToAccounts(account)
            saveObject(branch)
            user.account = account
        }
    }

    void createTransactions() {
        Account.list().eachWithIndex {Account account, index ->
            (1..10).each {
                if (index % 2) {
                    createTransaction(account, index * 1000, "Cr", (new Date() - it))
                }
                else {
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
