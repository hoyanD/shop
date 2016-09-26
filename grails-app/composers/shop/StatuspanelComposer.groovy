package shop

import admin.User
import grails.plugin.springsecurity.SpringSecurityUtils
import org.zkoss.zhtml.Link
import org.zkoss.zul.Button
import org.zkoss.zul.Image
import org.zkoss.zul.Label
import org.zkoss.zul.Textbox
import org.zkoss.zul.Timer


class StatuspanelComposer extends zk.grails.Composer {

    def User user = null
    def springSecurityService
    def Label lUser
    def Label lReg
    def lbLogout
    def Button log
    def Button conf
    def Button reg
    def Timer timer
    def Label yes
    def Label count
    def Image img

    def onTimer_timer(){
        yes.setVisible(false)
        timer.stop()
    }

    def setValues(){

        if(user) {
            Physician phys = Physician.findByUser(user)
            conf.setVisible(true)
            lbLogout.setVisible(true)
            lUser.setVisible(true)
            lUser.setValue(phys.shortName())
            int counter = 0
            def list = phys.getShopping()
            list.each { e->
                if(e.getState().equalsIgnoreCase("корзина")){
                    counter++
                }
            }
            count.setValue(counter.toString())
            if(counter > 0){
                count.setVisible(true)
                img.setVisible(true)
            }
        }
        else {
            log.setVisible(true)
            reg.setVisible(true)
            lReg.setVisible(true)
        }
    }

    def afterCompose = { window ->
        def principal = springSecurityService.principal
        user = User.findByUsername(principal.username)

        timer.stop()

        setValues()
    }
}