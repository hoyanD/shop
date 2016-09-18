package shop

class Foto {

    String path
    Goods goods

    static constraints = {
        path(nullable: false)
        goods(nullable: false)
    }

    String toString(){
        goods
    }
}