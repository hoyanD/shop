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
    def Rows rows
    def Row row
    def servletContext
    def counter = 0
    def Textbox name
    def Intbox warehouse
    def Intbox price
    def Button save
    def Textbox description
    def Goods goods = new Goods()

    def onClick_save(){
        if(!goods) { goods = new Goods() }
        goods.setDescription(description.getValue())
        goods.setName(name.getValue())
        goods.setWarehouse(warehouse.getValue())
        goods.setPrice(price.getValue())
        goods.setSubCat((Subcategory)subcategory.getSelectedItem().getValue())
        goods.setSoldOut(0)

        goods.save(flush: true)

        upload.setDisabled(false)
    }

    def onUpload_upload(UploadEvent event){
        Media media = event.getMedia()

        String path = servletContext.getRealPath("/") + "images/" + ((Category)category.getSelectedItem().getValue()).getPath() + "/" + ((Subcategory)subcategory.getSelectedItem().getValue()).getPath() + "/" + media.getName()

        Foto foto = new Foto()
        foto.setPath(media.getName())
        foto.setGoods(goods)
        foto.save(flush: true)

        FileOutputStream fos = new FileOutputStream(new File(path));

        fos.write(media.getByteData());
        fos.close();

        if(counter > 5) {
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
        img.setContent(media)
        img.setWidth("90px")
        img.setHeight("90px")
        row1.appendChild(img)
        rows1.appendChild(row1)

        row.appendChild(grid1)

        counter++
    }

    def onSelect_category(){
        subcategory.getChildren().clear()

        def cat = (Category)category.getSelectedItem().getValue()

        cat.refresh()

        subcategory.append {
            cat.getSabCat().each { e ->
                listitem(value: e){
                    listcell(label: e.getName())
                }
            }
        }

        subcategory.setSelectedIndex(0)
    }

    def afterCompose = { window ->
        def list = Category.findAll()

        category.append {
            list.each { e ->
                listitem(value: e) {
                    listcell(label: e.getName())
                }
            }
        }

        category.setSelectedIndex(0)

        onSelect_category()

        row = new Row()

        rows.appendChild(row)
    }
}