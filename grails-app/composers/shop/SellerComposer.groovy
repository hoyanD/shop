package shop

import org.zkoss.zk.ui.Executions
import org.zkoss.zul.Listbox
import org.zkoss.zul.Window


class SellerComposer extends zk.grails.Composer {

    def Listbox goods
    def Window window
    def Window modalDialog

    def onClick_goods() {
        if (goods.getSelectedItem()) {
            if (modalDialog) { modalDialog.onClose() }
            Map arg = new HashMap();
            arg.put("val", goods.getSelectedItem().getValue())
            arg.put("goods", goods)
            arg.put("flag", 2)
            arg.put("buttons", true)
            modalDialog = (Window) Executions.createComponents("buy.zul", window, arg);
        }
}

    def setValues(){
        def list = Shopping.findAllByState("підтверджено")

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
    }

    def afterCompose = { window ->
       setValues()
    }
}
