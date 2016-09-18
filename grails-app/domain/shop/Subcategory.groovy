package shop

class Subcategory {

    String name
    String path
    Category category
    static hasMany = [goods: Goods]

    static constraints = {
        name(nullable: false)
        category(nullable: false)
        path(nullable: false)
    }

    static mapping = {
        goods lazy: true
    }

    String toString(){
        name
    }
}