package shop

class Shopping {

    Goods goods
    Physician physician
    int count
    Date date
    String state
    String address
    String person
    String phone
    int price

    static constraints = {
        goods(nullable: false)
        physician(nullable: false)
        state(nullable: false)
        date(nullable: false)
    }

    String toString(){
        goods.toString() + " " + physician.toString()
    }
}