package shop

import admin.User
import grails.plugin.springsecurity.SpringSecurityUtils
import org.zkoss.zhtml.Link
import org.zkoss.zul.Button
import org.zkoss.zul.Label


class StatuspanelComposer extends zk.grails.Composer {

    def User user = null
    def springSecurityService
    def Label lUser
    def Label lReg
    def lbLogout
    def Button log
    def Button conf
    def Button reg

    def setValues(){

        if(user) {
            conf.setVisible(true)
            lbLogout.setVisible(true)
            lUser.setVisible(true)
            lUser.setValue(Physician.findByUser(user).shortName())
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

        setValues()
    }
}
