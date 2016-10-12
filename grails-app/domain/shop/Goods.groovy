package shop

class Goods {

    String name
    String description
    int price
    int warehouse
    int soldOut
    static hasMany = [foto: Foto]
    Subcategory subCat

    static constraints = {
        name(nullable: false)
        description(nullable: false)
        subCat(nullable: false)
    }

    static mapping = {
        foto lazy: true
    }

    String toString(){
        name
}
}