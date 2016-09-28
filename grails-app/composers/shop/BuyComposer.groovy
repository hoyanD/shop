package shop

import admin.User
import org.zkoss.zul.Button
import org.zkoss.zul.Datebox
import org.zkoss.zul.Filedownload
import org.zkoss.zul.Image
import org.zkoss.zul.Label
import org.zkoss.zul.Listbox
import org.zkoss.zul.Textbox
import org.zkoss.zul.Timer
import org.zkoss.zul.Window

class BuyComposer extends zk.grails.Composer {

    def Shopping shopping
    def Window modalDialog
    def Label name
    def Label price
    def Textbox userName
    def Image image
    def Textbox tb
    def Textbox address
    def Label count
    def Datebox date
    def Textbox phone
    def Button buy
    def Button report
    def springSecurityService
    def reportsService

    def onClick_report(){
        reportsService.serviceMethod(shopping)

        Filedownload.save("reports/printed-files/" + shopping.getGoods().getName() + ".pdf", null)
    }

    def onClick_buy() {
        shopping.setPhone(phone.getValue())
        shopping.setPerson(userName.getValue())
        shopping.setAddress(address.getValue())

        switch (shopping.getState()){
            case "корзина" : shopping.setState("підтверджено"); break
            case "підтверджено" :
            if(shopping.getGoods().getWarehouse() >= shopping.getCount()){
                shopping.setState("склад")
                System.out.println(shopping.getGoods().getWarehouse() - shopping.getCount())
                shopping.getGoods().setWarehouse(shopping.getGoods().getWarehouse() - shopping.getCount())
                shopping.getGoods().setSoldOut(shopping.getGoods().getSoldOut() + shopping.getCount())
                shopping.getGoods().save(flush: true)
            }
            break
            case "склад" : shopping.setState("відправлено")
        }

        shopping.save(flush: true)

        Listbox history = (Listbox)arg.get("history")
        Listbox goods = (Listbox)arg.get("goods")
        Label count = (Label) arg.get("count")
        Image img = (Image) arg.get("img")
        int flag = (Integer)arg.get("flag")

        if(flag == 1){
            def list =  shopping.getPhysician().getShopping()
            goods.getItems().clear()
            goods.append {
                list.each { e ->
                    if(e.getState().equalsIgnoreCase("корзина")) {
                        listitem(value: e) {
                            listcell(label: e.getGoods())
                            listcell(label: e.getState())
                            listcell(label: e.getCount())
                            listcell(label: e.getPrice())
                        }
                    }
                }
            }

            history.getItems().clear()
            history.append {
                list.each { e ->
                    if(!e.getState().equalsIgnoreCase("корзина")) {
                        listitem(value: e) {
                            listcell(label: e.getGoods())
                            listcell(label: e.getState())
                            listcell(label: e.getCount())
                            listcell(label: e.getPrice())
                        }
                    }
                }
            }
        } else if(flag == 2) {
            def list = Shopping.findAllByState("підтверджено")
            goods.getItems().clear()
            goods.append {
                list.each { e ->
                    listitem(value: e) {
                        listcell(label: e.getGoods())
                        listcell(label: e.getState())
                        listcell(label: e.getCount())
                        listcell(label: e.getPrice())
                    }
                }
            }
        } else if(flag == 3) {
            def list =  Shopping.getAll()
            goods.getItems().clear()
            goods.append {
                list.each { e ->
                    if(e.getState().equalsIgnoreCase("склад")) {
                        listitem(value: e) {
                            listcell(label: e.getGoods())
                            listcell(label: e.getState())
                            listcell(label: e.getCount())
                            listcell(label: e.getPrice())
                        }
                    }
                }
            }

            history.getItems().clear()
            history.append {
                list.each { e ->
                    if(e.getState().equalsIgnoreCase("відправлено")) {
                        listitem(value: e) {
                            listcell(label: e.getGoods())
                            listcell(label: e.getState())
                            listcell(label: e.getCount())
                            listcell(label: e.getPrice())
                        }
                    }
                }
            }
        }

        if(count && img) {
            int c = Integer.parseInt(count.getValue())
            c--
            count.setValue(c.toString())
            if (c > 0) {
                count.setVisible(true)
                img.setVisible(true)
            } else {
                count.setVisible(false)
                img.setVisible(false)
            }
        }

        modalDialog.onClose()
    }

    def prepareWindow() {
        shopping = (Shopping)arg.get("val")

        buy.setVisible((Boolean)arg.get("buttons"))

        shopping.refresh()

        Goods goods = shopping.getGoods()
        Physician phys = shopping.getPhysician()

        modalDialog.setTitle(goods.getName())
        name.setValue(goods.getName())
        price.setValue(shopping.getPrice() + " грн.")
        count.setValue(shopping.getCount().toString())

        tb.setValue(goods.getDescription())

        image.setSrc("http://localhost:8080/shop/images/" + goods.getSubCat().getCategory().getPath() +"/" + goods.getSubCat().getPath() + "/" + goods.getFoto().find().getPath())
        image.setWidth("80%")
        image.setHeight("600px")

        userName.setValue(phys.shortName())
        address.setValue(phys.getAddress())
        date.setValue(shopping.getDate())
        phone.setValue(phys.getPhone().toString())
    }

    def afterCompose = { modalDialog ->
        prepareWindow()
    }
}
