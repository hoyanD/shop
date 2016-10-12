package shop

import org.zkoss.util.media.Media
import org.zkoss.zk.ui.event.UploadEvent
import org.zkoss.zul.Button
import org.zkoss.zul.Grid
import org.zkoss.zul.Image
import org.zkoss.zul.Intbox
import org.zkoss.zul.Listbox
import org.zkoss.zul.Row
import org.zkoss.zul.Rows
import org.zkoss.zul.Textbox

class AddgoodsComposer extends zk.grails.Composer {

    def Listbox category
    def Listbox subcategory
    def Button upload
    def Grid grid
    def Rows rows
    def Row row
    def servletContext
    def Textbox name
    def Intbox warehouse
    def Intbox price
    def Button save
    def Textbox description
    def Goods goods
    String path

    def onClick_save(){
        goods.setDescription(description.getValue())
        goods.setName(name.getValue())
        goods.setWarehouse(warehouse.getValue())
        goods.setPrice(price.getValue())
        goods.setSubCat((Subcategory)subcategory.getSelectedItem().getValue())

        goods.save(flush: true)

        upload.setDisabled(false)

        path = "images/" + goods.getSubCat().getCategory().getPath() + "/" + goods.getSubCat().getPath() + "/"
    }

    def updatePhotos(){

        int counter = 0

        grid.removeChild(rows)

        rows = new Rows()

        grid.appendChild(rows)

        row = new Row()

        rows.appendChild(row)

        goods.refresh()

        def list = Foto.findAllByGoods(goods)

        list.each { e ->

            if (counter > 4) {
                row = new Row()
                rows.appendChild(row)
                counter = 0
            }

            Grid grid1 = new Grid()
            grid1.setWidth("100px")
            grid1.setHeight("100px")

            Rows rows1 = new Rows()
            grid1.appendChild(rows1)

            Row row1 = new Row()
            Image img = new Image()

            img.setSrc(path + e.getPath())

            img.setWidth("90px")
            img.setHeight("90px")
            row1.appendChild(img)
            rows1.appendChild(row1)

            row.appendChild(grid1)

            counter++
        }

        double n = list.size()

        n = (n % 5) == 0 ? (n / 5) : (n / 5) + 1

        grid.setHeight((110 * n.intValue()).toString() + "px")

        System.out.println("end")
    }

    def onUpload_upload(UploadEvent event){
        Media media = event.getMedia()

        Foto foto = new Foto()
        foto.setPath(media.getName())

        goods.refresh()

        foto.setGoods(goods)
        foto.save(flush: true)

        FileOutputStream fos = new FileOutputStream(new File(servletContext.getRealPath("/") + path + media.getName()));

        fos.write(media.getByteData())
        fos.close()

        updatePhotos()
    }

    def onSelect_category(){
        int count = 0, rez = 0

        subcategory.getChildren().clear()

        def cat = (Category)category.getSelectedItem().getValue()

        cat.refresh()

        subcategory.append {
            cat.getSabCat().each { e ->
                listitem(value: e){
                    listcell(label: e.getName())
                }

                if(goods.getSubCat() != null && e.getId() == goods.getSubCat().getId()){
                    rez = count
                }

                count++
            }
        }

        subcategory.setSelectedIndex(rez)
    }

    def afterCompose = { window ->
        int count = 0, rez = 0

        goods = (Goods)arg.get("goods")

        if (goods == null) {
            goods = new Goods()
        } else {
            goods.refresh()

            name.setValue(goods.getName())
            price.setValue(goods.getPrice())
            description.setValue(goods.getDescription())
            warehouse.setValue(goods.getWarehouse())
            upload.setDisabled(false)

            path = "images/" + goods.getSubCat().getCategory().getPath() + "/" + goods.getSubCat().getPath() + "/"

            updatePhotos()
        }

        def list = Category.findAll()

        category.append {
            list.each { e ->
                listitem(value: e) {
                    listcell(label: e.getName())
                }
                if(goods.getSubCat() != null && e.getId() == goods.getSubCat().getCategory().getId()){
                    rez = count
                }

                count++
            }
        }

        category.setSelectedIndex(rez)

        onSelect_category()
    }
}