package shop

import admin.Role
import admin.User
import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.EventListener
import org.zkoss.zk.ui.event.Events
import org.zkoss.zul.Button
import org.zkoss.zul.Grid
import org.zkoss.zul.Image
import org.zkoss.zul.Label
import org.zkoss.zul.Menubar
import org.zkoss.zul.Menuitem
import org.zkoss.zul.Row
import org.zkoss.zul.Rows
import org.zkoss.zul.Window

class HomeComposer extends zk.grails.Composer {

    def Menubar menubar
    def Rows goods
    def Grid grid
    def Window window
    def Window modal
    def Component stPnl
    def springSecurityService


    def showGoods(String str){

        grid.removeChild(goods)

        goods = new Rows()

        grid.appendChild(goods)

        Row row = new Row()

        goods.appendChild(row)

        def list = Subcategory.findByName(str).getGoods()
        def counter = 0

        //def list = Goods.findAll()

        list.each { e->
            Grid newGrid = new Grid()
            newGrid.setWidth("225px")
            newGrid.setHeight("225px")
            newGrid.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                @Override
                void onEvent(Event event) throws Exception {
                    if(modal != null) { modal.onClose() }

                    def principal = springSecurityService.principal
                    User user = User.findByUsername(principal.username)
                    def authority;

                    if(user) { authority = user.getAuthorities() }

                    boolean flag = false

                    for (Role role : authority){
                        if(role.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
                            flag = true
                        }
                    }

                    if(flag){
                        HashMap arg = new HashMap()

                        arg.put("goods", e)

                        modal = (Window)Executions.createComponents("addgoods.zul", window, arg);
                    }else {
                        Map arg = new HashMap();
                        arg.put("val", e)
                        arg.put("yes", stPnl.getFellow('window').getFellow('yes'))
                        arg.put("timer", stPnl.getFellow('window').getFellow('timer'))
                        arg.put("count", stPnl.getFellow('window').getFellow('count'))
                        arg.put("img", stPnl.getFellow('window').getFellow('img'))
                        modal = (Window)Executions.createComponents("goods.zul", window, arg);
                    }
                }
            })

            Rows rows = new Rows()

            Row row1 = new Row()
            row1.setAlign("center")
            Label l1 = new Label(e.getName())
            row1.appendChild(l1)
            rows.appendChild(row1)

            Row row2 = new Row()
            row2.setAlign("center")
            Image image = new Image("http://localhost:8080/shop/images/" + e.getSubCat().getCategory().getPath() +"/" + e.getSubCat().getPath() + "/" + e.getFoto().find().getPath())
            image.setWidth("150px")
            image.setHeight("150px")
            row2.appendChild(image)
            rows.appendChild(row2)

            Row row3 = new Row()
            row3.setAlign("center")
            Label l2 = new Label(e.getPrice() + " грн.")
            row3.appendChild(l2)
            rows.appendChild(row3)

            newGrid.appendChild(rows)

            counter++

            if(counter > 3){
                row = new Row();
                goods.appendChild(row)
                counter = 1
            }

            row.appendChild(newGrid)
        }
    }

    def afterCompose = { window ->

        def Menuitem men

        menubar.append {
            Category.findAll().each { e->
                menu(label: e.getName()) {
                    menupopup(){
                        e.getSabCat().each { c ->
                            men = menuitem(label: c.getName())
                            men.addEventListener(Events.ON_CLICK, new EventListener<Event>() {
                                @Override
                                void onEvent(Event event) throws Exception {
                                    showGoods(c.getName())
                                }
                            })
                        }
                    }
                }
            }
        }
    }
}