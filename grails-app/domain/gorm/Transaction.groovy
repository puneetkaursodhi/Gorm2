package gorm

class Transaction {

    Float amount = 0.0F
    String type = "Cr"
    Date dateCreated
    static belongsTo = [account: Account]

    static constraints = {
        type(inList: ["Cr", "Dr"])
    }

    static mapping = {
        autoTimestamp false
    }

    static namedQueries = {
       debitAmount {Account account ->
           projections{
           sum('amount')
           }
           eq('account',account)
           eq('type','Dr')
       }
    }
}
