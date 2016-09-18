package admin

import grails.plugin.springsecurity.SpringSecurityUtils

class UserController {

    def scaffold = User

    def home = {
        String view
        if (SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')) {
            view = '/'
        }
        else if (SpringSecurityUtils.ifAllGranted('ROLE_USER')) {
            view = '/home.zul'
        }
        else {
            view = '/'
        }

        redirect(uri: view)
    }
}