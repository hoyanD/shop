package shop

import admin.User
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.event.Events
import org.zkoss.zk.ui.event.KeyEvent
import org.zkoss.zul.Button
import org.zkoss.zul.Grid
import org.zkoss.zul.Image
import org.zkoss.zul.Label
import org.zkoss.zul.Listbox
import org.zkoss.zul.Row
import org.zkoss.zul.Rows
import org.zkoss.zul.Textbox
import org.zkoss.zul.Timer
import org.zkoss.zul.Vbox
import org.zkoss.zul.Window

class GoodsComposer extends zk.grails.Composer {

    def Goods goods
    def Window modalDialog
    def Label l1
    def Label l2
    def Image image
    def Vbox next
    def Vbox prev
    def Textbox tb
    def Listbox list
    def Button buy
    def springSecurityService
    def Rows rows
    def Grid gridPhotos
    def counter = 0
    String path
    Foto foto
    HashMap arr

    def onClick_next(){
        Iterator<Foto> it = goods.getFoto().iterator()

        ((Grid)arr.get(foto)).setClass("no")

        while(it.hasNext()){
            if(it.next() == foto){
                if(it.hasNext()){
                    foto = it.next()
                    image.setSrc(path + foto.getPath())
                }
            }
        }

        ((Grid)arr.get(foto)).setClass("yes")
    }

    def onClick_prev(){
        def list = goods.getFoto()

        ((Grid)arr.get(foto)).setClass("no")

        for(int i = 0; i < list.size(); i++){
            if(foto == list.getAt(i) && i > 0){
                foto = list.getAt(i - 1)
                image.setSrc(path + foto.getPath())
            }
        }

        ((Grid)arr.get(foto)).setClass("yes")
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
    }

    def prepareWindow() {
        goods = (Goods)arg.get("val")

        double n = goods.getFoto().size()

        n = (n % 5) == 0 ? (n / 5) : (n / 5) + 1

        gridPhotos.setHeight((110 * n.intValue()).toString() + "px")

        path = "images/" + goods.getSubCat().getCategory().getPath() + "/" + goods.getSubCat().getPath() + "/"

        l1.setValue(goods.getName())
        l2.setValue(goods.getPrice() + " грн.")

        tb.setValue(goods.getDescription())

        image.setSrc(path + goods.getFoto().find().getPath())

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

        arr = new HashMap()

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

            arr.put(e, grid1)

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
                    ((Grid)arr.get(foto)).setClass("no")

                    foto = e

                    changeFoto()

                    ((Grid)arr.get(foto)).setClass("yes")
                }
            })

            counter++
        }

        foto = list.getAt(0)

        modalDialog.addEventListener(Events.ON_CTRL_KEY, new EventListener() {
            public void onEvent(Event event) throws Exception {
                if (((KeyEvent)event).getKeyCode() == 37){
                    onClick_prev()
                }else {
                    onClick_next()
                }
            }
        });
    }
}