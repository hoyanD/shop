package shop

import org.zkoss.zul.Image
import org.zkoss.zul.Label
import org.zkoss.zul.Textbox
import org.zkoss.zul.Window

class GoodsComposer extends zk.grails.Composer {

    def Window modalDialog
    def Label l1
    def Label l2
    def Image image
    def Textbox tb

    def prepareWindow() {
        Goods goods = (Goods)arg.get("val")

        l1.setValue(goods.getName())
        l2.setValue(goods.getPrice() + " грн.")

        tb.setValue(goods.getDescription())

        image.setSrc("http://localhost:8080/shop/images/" + goods.getSubCat().getCategory().getPath() +"/" + goods.getSubCat().getPath() + "/" + goods.getFoto().find().getPath())
        image.setWidth("80%")
        image.setHeight("600px")

    }

    def afterCompose = { modalDialog ->
        prepareWindow()
    }
}