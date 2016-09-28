package shop

import admin.Role
import admin.User
import admin.UserRole
import org.zkoss.zul.Button
import org.zkoss.zul.Datebox
import org.zkoss.zul.Intbox
import org.zkoss.zul.Label
import org.zkoss.zul.Textbox


class RegistrationComposer extends zk.grails.Composer {

    Textbox login
    Textbox password
    Textbox repPassword
    Textbox lastName
    Textbox firstName
    Textbox middleName
    Textbox email
    Textbox address
    Datebox date
    Intbox phone
    Label labPass
    Label labLog
    Button reg

    def onClick_reg(){
        labLog.setVisible(false)
        labPass.setVisible(false)

        if(User.findByUsername(login.getValue())){
            labLog.setVisible(true)
            return
        }

        if(!password.getValue().equalsIgnoreCase(repPassword.getValue())){
            labPass.setVisible(true)
            return
        }

        User user = new User()
        user.setUsername(login.getValue())
        user.setPassword(password.getValue())
        user.save(flush: true)

        UserRole.create(user, Role.findByAuthority("ROLE_USER"))

        Physician physician = new Physician()
        physician.setLastName(lastName.getValue())
        physician.setFirstName(firstName.getValue())
        physician.setMiddleName(middleName.getValue())
        physician.setEmail(email.getValue())
        physician.setAddress(address.getValue())
        physician.setDate(date.getValue())
        physician.setPhone(phone.getValue())
        physician.setUser(user)

        physician.save(flush: true)

        redirect(uri: "/login/auth")
    }

    def afterCompose = { window ->
        // initialize components here
    }
}
