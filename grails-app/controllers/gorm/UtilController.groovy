package gorm

class UtilController {

    static defaultAction = "list"

    def list = {
        List<User> users = User.createCriteria().list() {
            ilike("firstName", "Test%")
//            le("age", 30)
//            between("age", 18, 60)
        }
        render "Result -> ${users.size()} ${users.firstName} ${users.age}"
    }

    def listPaginate = {
        List<User> users
        LogSql.execute {
            users = User.createCriteria().list(max: 10, offset: 10) {
                ilike("firstName", "Test 1%")
                le("age", 50)
                between("age", 18, 60)
            }
        }
        render "Result -> ${users.size()} ${users*.id} totalCount ${users.totalCount}"
    }

    def listDistinct = {
        List<User> users = User.createCriteria().listDistinct() {
            ilike("firstName", "Test 1%")
            le("age", 50)
            between("age", 18, 60)
            maxResults 10
            firstResult 0
            order("age", "desc")
        }
        render "Result ->${users.size()} ${users*.id}"
    }

    def count = {
        Integer userCount = User.createCriteria().count() {
            ilike("firstName", "Test 1%")
            le("age", 50)
            between("age", 18, 60)
        }
        render "Result -> ${userCount}"
    }

    def and = {
        List<Account> accounts = Account.createCriteria().list() {
            and {
                between("balance", 5000, 10000)
                'branch' {
                    eq("name", "London")
                }
            }
        }
        render "Result -> ${accounts*.balance} ${accounts*.branch*.name}"
    }

    def or = {
        List<Account> accounts = Account.createCriteria().list() {
            or {
                between("balance", 5000, 10000)
                'branch' {
                    eq("name", "London")
                }
            }
        }
        render "Result -> ${accounts*.balance} ${accounts*.branch*.name}"
    }

    def not = {
        List<Account> accounts = Account.createCriteria().list() {
            not {
                between("balance", 5000, 10000)

                'branch' {
                    eq("name", "London")
                }
            }
        }
        render "Result -> ${accounts*.balance} ${accounts*.branch*.name}"
    }

    def property = {
        def dates = User.createCriteria().list() {
            projections {
                property("age")
                'account' {
                    property("dateCreated")
                }
            }
            ilike("firstName", "Test%")
            le("age", 50)
            between("age", 18, 60)
        }
        render "Result -> ${dates}"
    }

    def distinct = {
        List<Integer> userAges = User.createCriteria().list() {
            projections {
                distinct("age")
            }
            ilike("firstName", "Test%")
            le("age", 50)
            between("age", 18, 60)
        }
        render "Result -> ${userAges}"
    }

    def projections = {
        Integer ageSum = User.createCriteria().get() {
            projections {
                sum("age")
            }
            ilike("firstName", "Test%")
            le("age", 50)
            between("age", 18, 60)
        }
        render "Result -> ${ageSum}"
    }

    def rowCount = {
        Integer userCount = User.createCriteria().get() {
            projections {
                rowCount()
            }
            ilike("firstName", "Test%")
            le("age", 50)
            between("age", 18, 60)
        }
        render "Result -> ${userCount}"
    }

    def groupProperty = {
        List result = Account.createCriteria().list() {
            projections {
                groupProperty("branch")
                sum("balance")
            }
        }
        render "Result -> ${result}"
    }

    def alias = {
        List result = Account.createCriteria().list() {
            projections {
                groupProperty("branch")
                sum("balance", 'totalBalance')
            }
            order("totalBalance", "desc")
        }
        render "Result -> ${result}"
    }

    def executeQuery = {
        Integer age = 19
        List usersInfo = User.executeQuery("select u.firstName, u.lastName from User as u where age >:age", [age: age])
        render "User Info -: ${usersInfo}"
    }

    def executeUpdate = {
        User user = User.get(1)
        String firstName = user.firstName
        User.executeUpdate("update User as u set u.firstName=:firstName where u.id=:id", [firstName: "Test User 59", id: 1.toLong()])
//        user.refresh()
        render "firstName before ${firstName} -: After updation ${user.firstName}"
//        User.executeUpdate("delete User where id=:id", [id: 1.toLong()])
//        render "Success"
    }


    def namedQuery = {
        Date date = new Date() + 20
//        List<Account> accounts = Account.recentCustomers(date).list()
        List<Account> accounts = Account.recentCustomers(date).list(max: 10, offset: 0)
//        List<Account> accounts = Account.recentCustomers(date).findAllByBalanceGreaterThan(5000)
        render "Success -> ${accounts.balance}"
        /*
          Difference between

          Account.get(2)
          Account.recentCustomers(date).get(2)
           */


    }
}
