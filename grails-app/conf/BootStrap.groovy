import shop.Popular

class BootStrap {

    def init = { servletContext ->
        Popular.deleteAll()
    }

    def destroy = {
    }
}
