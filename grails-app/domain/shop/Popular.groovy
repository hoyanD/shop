package shop

class Popular {

    static hasMany = [goods: Goods]
    String name

    static constraints = {
        name (nullable: false)
    }

    String toString(){
        goods
    }

    static mapping = {
        goods lazy: true
    }
}