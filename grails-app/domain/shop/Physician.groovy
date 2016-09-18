package shop

import admin.User

class Physician {

    User user
    String lastName
    String firstName
    String middleName
    int phone
    String email
    Date date
    String address
    static hasMany = [shopping: Shopping]

    static constraints = {
        user(nullable: false)
        lastName(nullable: false)
        firstName(nullable: false)
        middleName(nullable: false)
        email(nullable: false)
        address(nullable: false)
        date(nullable: false)
    }

    String toString(){
        lastName + " " + firstName + " " + middleName
    }


    String shortName(){
        lastName + " " + firstName.charAt(0) + ". " + middleName.charAt(0) + "."
    }

    static mapping = {
        shopping lazy: true
    }
}