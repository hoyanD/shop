package shop

import admin.User
import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.Button
import org.zkoss.zul.Grid
import org.zkoss.zul.Image
import org.zkoss.zul.Label
import org.zkoss.zul.Listbox
import org.zkoss.zul.Row
import org.zkoss.zul.Rows
import org.zkoss.zul.Textbox
import org.zkoss.zul.Timer
import org.zkoss.zul.Window

class GoodsComposer extends zk.grails.Composer {

    def Goods goods
    def Window modalDialog
    def Label l1
    def Label l2
    def Image image
    def Image next
    def Image prev
    def Textbox tb
    def Listbox list
    def Button buy
    def springSecurityService
    def Rows rows
    def counter = 0
    String path
    Foto foto

    def onClick_next(){
        Iterator<Foto> it = goods.getFoto().iterator()

        while(it.hasNext()){
            if(it.next() == foto){
                if(it.hasNext()){
                    foto = it.next()
                    image.setSrc(path + foto.getPath())
                }
            }
        }
    }

    def onClick_prev(){
        def list = goods.getFoto()
        for(int i = 0; i < list.size(); i++){
            if(foto == list.getAt(i) && i > 0){
                foto = list.getAt(i - 1)
                image.setSrc(path + foto.getPath())
            }
        }
    }

    def onClick_buy() {
        def principal = springSecurityService.principal
        User user = User.findByUsername(principal.username)

        if (!user) {
            redirect(uri: "/login/auth")
        } else {
            Physician phys = Physician.findByUser(user)

            Shopping shopping = new Shopping()

            shopping.setDate(new Date())
            shopping.setAddress(phys.getAddress())
            shopping.setPrice(goods.getPrice() * (Integer) list.getSelectedItem().getValue())
            shopping.setCount((Integer) list.getSelectedItem().getValue())
            shopping.setGoods(goods)
            shopping.setPhysician(phys)
            shopping.setState("корзина")
            shopping.setPerson(phys.shortName())
            shopping.setPhone(phys.getPhone().toString())

            shopping.save(flush: true)

            phys.getShopping().add(shopping)
            phys.save(flush: true)

            Timer timer = (Timer)arg.get("timer")
            Label yes = (Label)arg.get("yes")
            Label count = (Label)arg.get("count")
            Image img = (Image)arg.get("img")

            int c = Integer.parseInt(count.getValue())
            c++

            count.setValue(c.toString())
            if (c == 1) {
                count.setVisible(true)
                img.setVisible(true)
            }

            timer.start()
            yes.setVisible(true)
        }

        modalDialog.onClose()
    }

    def prepareWindow() {
        goods = (Goods)arg.get("val")

        l1.setValue(goods.getName())
        l2.setValue(goods.getPrice() + " грн.")

        tb.setValue(goods.getDescription())

        image.setSrc("http://localhost:8080/shop/images/" + goods.getSubCat().getCategory().getPath() +"/" + goods.getSubCat().getPath() + "/" + goods.getFoto().find().getPath())
        image.setWidth("80%")
        image.setHeight("600px")

        list.append{
            for(int i = 1; i <= goods.getWarehouse(); i++){
                listitem(value: i){
                    listcell(label: i)
                }
            }
        }

        list.setSelectedIndex(0)
    }

    def changeFoto(){
        foto.refresh()

        image.setSrc(path + foto.getPath())
    }

    def afterCompose = { modalDialog ->
        prepareWindow()

        path = new String("http://localhost:8080/shop/images/" + goods.getSubCat().getCategory().getPath() + "/" + goods.getSubCat().getPath() + "/")


        def list = goods.getFoto()
        Row row = new Row()
        rows.appendChild(row)

        list.each { e->

            if (counter > 4) {
                row = new Row()
                rows.appendChild(row)
                counter = 0
            }


            Grid grid1 = new Grid()
            grid1.setWidth("100px")
            grid1.setHeight("100px")

            Rows rows1 = new Rows()
            grid1.appendChild(rows1)

            Row row1 = new Row()
            Image img = new Image()
            img.setSrc(path + e.getPath())
            img.setWidth("90px")
            img.setHeight("90px")
            row1.appendChild(img)
            rows1.appendChild(row1)

            row.appendChild(grid1)

            grid1.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                void onEvent(Event event) throws Exception {
                    foto = e
                    changeFoto()
                }
            })

            counter++
        }

        foto = list.getAt(0)
    }
}