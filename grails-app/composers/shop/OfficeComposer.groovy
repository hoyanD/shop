package shop

import admin.User
import org.zkoss.zk.ui.Component
import org.zkoss.zk.ui.Executions
import org.zkoss.zul.Button
import org.zkoss.zul.Datebox
import org.zkoss.zul.Listbox
import org.zkoss.zul.Row
import org.zkoss.zul.Tab
import org.zkoss.zul.Textbox
import org.zkoss.zul.Window

class OfficeComposer extends zk.grails.Composer {
    def Component stPnl
    def Textbox lastName
    def Textbox firstName
    def Textbox middleName
    def Textbox email
    def Textbox address
    def Textbox phone
    def Datebox date
    def Button save
    def Row ok
    def Tab conf
    def springSecurityService
    def Physician phys
    def Listbox goods
    def Listbox history
    def Window window
    def Window modal

    def onClick_goods() {
        if (goods.getSelectedItem()) {
            if (modal){ modal.onClose() }
            Map arg = new HashMap();
            arg.put("val", goods.getSelectedItem().getValue())
            arg.put("goods", goods)
            arg.put("history", history)
            arg.put("count", stPnl.getFellow('window').getFellow('count'))
            arg.put("img", stPnl.getFellow('window').getFellow('img'))
            arg.put("flag", 1)
            arg.put("buttons", true)
            modal = (Window) Executions.createComponents("buy.zul", window, arg);
        }
    }

    def onClick_history() {
        if (history.getSelectedItem()) {
            if (modal) { modal.onClose() }
            Map arg = new HashMap();
            arg.put("val", history.getSelectedItem().getValue())
            arg.put("goods", goods)
            arg.put("history", history)
            arg.put("count", stPnl.getFellow('window').getFellow('count'))
            arg.put("img", stPnl.getFellow('window').getFellow('img'))
            arg.put("flag", 1)
            arg.put("buttons", false)
            modal = (Window) Executions.createComponents("buy.zul", window, arg);
        }
    }

    def onClick_save(){
        phys.setLastName(lastName.getValue())
        phys.setFirstName(firstName.getValue())
        phys.setMiddleName(middleName.getValue())
        phys.setEmail(email.getValue())
        phys.setAddress(address.getValue())
        phys.setPhone(Integer.parseInt(phone.getValue()))
        phys.setDate(date.getValue())

        phys.save(flush: true)

        ok.setVisible(true)

        stPnl.getFellow('window').getFellow('lUser').setValue(phys.shortName())
    }

    def onClick_conf(){
        ok.setVisible(false)
        lastName.setValue(phys.getLastName())
        firstName.setValue(phys.getFirstName())
        middleName.setValue(phys.getMiddleName())
        email.setValue(phys.getEmail())
        address.setValue(phys.getAddress())
        phone.setValue(phys.getPhone().toString())
        date.setValue(phys.getDate())
    }

    def afterCompose = { window ->
        def principal = springSecurityService.principal
        User user = User.findByUsername(principal.username)
        phys = Physician.findByUser(user)

        def list = phys.getShopping()

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
    }
}
