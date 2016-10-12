package shop

class Foto {

    String path
    Goods goods

    static constraints = {
        path(nullable: false)
        goods(nullable: true)
    }

    String toString(){
        goods
    }
}