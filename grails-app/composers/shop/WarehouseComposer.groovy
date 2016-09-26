package shop

import org.zkoss.zk.ui.Executions
import org.zkoss.zul.Listbox
import org.zkoss.zul.Window


class WarehouseComposer extends zk.grails.Composer {

    def Listbox goods
    def Window window
    def Window modalDialog
    def Listbox history

    def onClick_goods() {
        if (goods.getSelectedItem()) {
            if (modalDialog) { modalDialog.onClose() }
            Map arg = new HashMap();
            arg.put("val", goods.getSelectedItem().getValue())
            arg.put("goods", goods)
            arg.put("history", history)
            arg.put("flag", 3)
            arg.put("buttons", true)
            modalDialog = (Window) Executions.createComponents("buy.zul", window, arg);
        }
    }

    def onClick_history() {
        if (history.getSelectedItem()) {
            if (modalDialog) { modalDialog.onClose() }
            Map arg = new HashMap();
            arg.put("val", history.getSelectedItem().getValue())
            arg.put("goods", goods)
            arg.put("history", history)
            arg.put("flag", 3)
            arg.put("buttons", false)
            modalDialog = (Window) Executions.createComponents("buy.zul", window, arg);
        }
    }

    def setValues(){
        def list =  Shopping.getAll()

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

    def afterCompose = { window ->
        setValues()
    }
}
