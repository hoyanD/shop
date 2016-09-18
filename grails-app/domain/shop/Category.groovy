package shop

class Category {

    String name
    String path
    static hasMany =[sabCat: Subcategory]

    static constraints = {
        name(nullable: false)
        path(nullable: false)
    }

    static mapping = {
        sabCat lazy: true
    }

    String toString(){
        name
    }
}